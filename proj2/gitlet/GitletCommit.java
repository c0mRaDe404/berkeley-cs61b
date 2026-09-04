package gitlet;

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

import static gitlet.GitletBranch.*;
import static gitlet.GitletIndex.clearIndex;
import static gitlet.GitletIndex.getIndex;

/**
 * Represents a gitlet commit object.
 *
 * @author Bhuvanesh
 */

class GitletCommitObj extends GitletObject implements Serializable {

    final String message; // commit message
    final String time; // commit timestamp
    final List<String> parents; // parents of the commit
    final HashMap<String, String> currentWorkingTree; // copy of the index file

    private GitletCommitObj(String message, String time) {
        this.message = message;
        this.time = time;
        this.currentWorkingTree = getIndex();
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
        return new GitletCommitObj(msg, time);
    }

    /**
     * check any files are in the staging area
     *
     * @return true if staging area is not empty otherwise false
     */
    boolean hasStagedFiles() {
        return !currentWorkingTree.isEmpty();
    }

    /**
     * add the commitId as a parent of the commit object
     *
     * @param commitId
     */
    void addParent(String commitId) {
        parents.add(commitId);
    }
}


public class GitletCommit extends GitletObject implements Serializable {


    /**
     * sort the index key-value pairs and concatenates them to one long string
     *
     * @return concatenated key-value pairs
     */
    private static String indexToString(GitletCommitObj commitObj) {
        List<String> keys = new ArrayList<>(commitObj.currentWorkingTree.keySet());
        Collections.sort(keys);

        StringBuilder concatenatedKeyValuePairs = new StringBuilder();
        for (String key : keys) {
            concatenatedKeyValuePairs.append(key);
            concatenatedKeyValuePairs.append(commitObj.currentWorkingTree.get(key));
        }
        return concatenatedKeyValuePairs.toString();
    }


    /**
     * creates an empty commit with the given message and time
     *
     * @param commitObj
     * @return sha1 message digest
     */
    public static String createCommit(GitletCommitObj commitObj) {
        String commitHash;
        commitHash = Utils.sha1(commitObj.message, commitObj.time, indexToString(commitObj));
        createCommit(commitHash, commitObj);
        return commitHash;
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
        GitletCommitObj commitObj = GitletCommitObj.createCommitObject(commitMsg, Instant.now().toString());
        if (!commitObj.hasStagedFiles()) {
            System.exit(0);
        }
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
        return Utils.readObject(getObjectPath(commitId), GitletCommitObj.class);
    }

    /**
     * gets a commit by its commitId
     *
     * @param commitId
     * @return a commit object
     */
    public static GitletCommitObj getCommit(String commitId) {
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
        System.out.println("message: " + commitObj.message);
        System.out.println("parent: " + commitObj.parents);
        System.out.println("timestamp: " + commitObj.time);
        System.out.println("tree: " + commitObj.currentWorkingTree.entrySet());
    }

    /**
     * prints out what HEAD points to
     *
     */
    public static void showLatestCommit() {
        String commitId = getBranchId(getCurrentBranch());
        showCommit(commitId, getCurrentCommit());
    }
}
