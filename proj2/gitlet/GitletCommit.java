package gitlet;

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

import static gitlet.GitletBranch.*;
import static gitlet.GitletIndex.clearIndex;
import static gitlet.GitletIndex.getIndexInstance;
import static gitlet.GitletObject.createObjectFile;
import static gitlet.GitletObject.getObjectPath;

/**
 * Represents a gitlet commit object.
 *
 * @author Bhuvanesh
 */

class GitletCommitObj extends GitletObject implements Serializable {

    private String message; // commit message
    private String time; // commit timestamp
    private List<String> parents; // parents of the commit
    private GitletIndex snapshot; // copy of the index file

    private GitletCommitObj(String message, String time, GitletIndex index) {
        this.message = message;
        this.time = time;
        //get commit snapshot
        this.snapshot = GitletCommit.getCommitSnapshot(index);
        this.parents = new ArrayList<>();
    }

    /**
     * creates a commit object with given msg and time
     *
     * @param msg
     * @param time
     * @return a commit object
     */

    public static GitletCommitObj createCommitObject(String msg, String time) {
        return new GitletCommitObj(msg, time, getIndexInstance());
    }

    public static GitletCommitObj createCommitObject(String msg, String time, GitletIndex index) {
        return new GitletCommitObj(msg, time, index);
    }

    /**
     * add the commitId as a parent of the commit object
     *
     * @param commitId
     */
    void addParent(String commitId) {
        parents.add(commitId);
    }


    String getMsg() {
        return message;
    }

    String getTimestamp() {
        return time;
    }

    List<String> getParents() {
        return parents;
    }

    GitletIndex getSnapshot() {
        return snapshot;
    }

}


public class GitletCommit {

    /**
     * creates an empty commit with the given message and time
     *
     * @param commitObj
     * @return sha1 message digest
     */
    public static String createCommit(GitletCommitObj commitObj) {
        String commitHash;
        commitHash = Utils.sha1(commitObj.getMsg(), commitObj.getTimestamp(),
                commitObj.getSnapshot().indexToString(commitObj));
        createCommit(commitHash, commitObj);
        return commitHash;
    }


    public static GitletIndex getCommitSnapshot(GitletIndex index) {
        GitletCommitObj currentCommitObj = getCurrentCommit();

        if (currentCommitObj == null) {
            return getIndexInstance();
        } else {
            HashMap<String, String> newSnapshot = new HashMap<>(currentCommitObj.getSnapshot().getIndexPair());

            for (Map.Entry<String, String> pair : index.getIndexPair().entrySet()) {
                if (pair.getValue() == null) {
                    newSnapshot.remove(pair.getKey());
                    continue;
                }
                newSnapshot.put(pair.getKey(), pair.getValue());
            }

            return getIndexInstance(newSnapshot);
        }
    }

    /**
     * uses commit hash to create a commit object file
     *
     * @param commitHash
     * @param commitObj
     */
    private static void createCommit(String commitHash, GitletCommitObj commitObj) {
        File targetFile = createObjectFile(commitHash);
        Utils.writeObject(targetFile, commitObj);
    }


    /**
     * makes a commit with all the metadata extracted from the commit object
     *
     * @param commitMsg
     */
    public static void makeCommit(String commitMsg) {

        GitletIndex currentIndex = getIndexInstance(); // fetch the current index object

        if (!currentIndex.hasStagedFiles()) { // is there some way to prevent multiple index file reads? nvm
            System.err.println("No changes added to the commit.");
            System.exit(0);
        }

        GitletCommitObj commitObj = GitletCommitObj.createCommitObject(commitMsg, Instant.now().toString(), currentIndex);
        commitObj.addParent(getBranchId(getCurrentBranch()));
        updateBranch(getCurrentBranch(), createCommit(commitObj));
        clearIndex();
    }

    /**
     * deserialize the commit object
     *
     * @param commitId
     * @return gitlet commit object
     */
    private static GitletCommitObj readCommitObject(String commitId) {
        File commitObjPath = getObjectPath(commitId);
        if (!commitObjPath.exists()) {
            return null;
        }
        return Utils.readObject(commitObjPath, GitletCommitObj.class);
    }

    /**
     * gets a commit by its commitId
     *
     * @param commitId
     * @return a commit object
     */
    public static GitletCommitObj getCommit(String commitId) {
        if (commitId == null) {
            return null;
        }
        return readCommitObject(commitId);
    }

    /**
     * gets the current commit
     *
     * @return the current commit object
     */
    public static GitletCommitObj getCurrentCommit() {
        String commitId = getBranchId(getCurrentBranch());
        return getCommit(commitId);
    }

    /**
     * prints out what is in commit
     *
     * @param commitId
     * @param commitObj
     */
    public static void showCommit(String commitId, GitletCommitObj commitObj) {
        System.out.println("commit " + commitId);
        System.out.println("message: " + commitObj.getMsg());
        System.out.println("parent: " + commitObj.getParents());
        System.out.println("timestamp: " + commitObj.getTimestamp());
        System.out.println("tree: " + commitObj.getSnapshot().getIndexPair());
    }

    /**
     * prints out what HEAD points to
     *
     */
    public static void showLatestCommit() {
        String commitId = getBranchId(getCurrentBranch());
        showCommit(commitId, getCurrentCommit());
    }


    public static void printLog(GitletCommitObj current) {
        if (current.getParents().isEmpty()) {
            return;
        }
       showCommit("summa", current);
       printLog(current.getParents().get(0));
    }
}
