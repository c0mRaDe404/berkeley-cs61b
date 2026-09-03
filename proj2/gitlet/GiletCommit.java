package gitlet;

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

import static gitlet.GitletBranch.*;
import static gitlet.GitletIndex.getIndex;

/**
 * Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author Bhuvanesh
 */
public class GiletCommit extends GitletObject implements Serializable {

    private final String message; // commit message
    private final String time; // commit timestamp
    private final List<String> parents; // parents of the commit
    private final HashMap<String, String> currentWorkingTree; // copy of the index file

    private GiletCommit(String message, String time) {
        this.message = message;
        this.time = time;
        this.currentWorkingTree = getIndex();
        this.parents = new ArrayList<>();
    }

    /** sort the index key-value pairs and concatenates them to one long string
     *
     * @return
     */
    private String indexToString() {
        List<String> keys = new ArrayList<>(currentWorkingTree.keySet());
        Collections.sort(keys);

        StringBuilder concatenatedKeyValuePairs = new StringBuilder();
        for (String key : keys) {
            concatenatedKeyValuePairs.append(key);
            concatenatedKeyValuePairs.append(currentWorkingTree.get(key));
        }
        return concatenatedKeyValuePairs.toString();
    }


    /**
     * creates a commit object with given msg and time
     *
     * @param msg
     * @param time
     * @return a commit object
     */
    public static GiletCommit createCommitObject(String msg, String time) {
        return new GiletCommit(msg, time);
    }

    /**
     * creates an empty commit with the given message and time
     *
     * @return sha1 message digest
     */
    public String createCommit() {
        String commitHash;
        commitHash = Utils.sha1(message, time, indexToString());
        createCommit(commitHash);
        return commitHash;
    }


    /**
     * uses commit hash to create a commit object file
     *
     * @param commitHash
     */
    private void createCommit(String commitHash) {
        File targetFile = createObjectFile(commitHash);
        Utils.writeObject(targetFile, this);
    }

    /**
     * add the commitId as a parent of the commit object
     *
     * @param commitId
     */
    private void addParent(String commitId) {
        parents.add(commitId);
    }

    /**
     * makes a commit with all the metadata extracted from the commit object
     *
     * @param commitMsg
     */
    public static void makeCommit(String commitMsg) {
        GiletCommit commitObj = createCommitObject(commitMsg, Instant.now().toString());
        commitObj.addParent(getBranchId(getCurrentBranch()));
        updateBranch(getCurrentBranch(), commitObj.createCommit());
    }

    /** deserialize the commit object
     *
     * @param commitId
     * @return gitlet commit object
     */
    public static GiletCommit readCommitObject(String commitId) {
        return Utils.readObject(getObjectPath(commitId), GiletCommit.class);
    }

    /** prints out what is in commit
     *
     * @param commitId
     */
    public static void showCommit(String commitId) {
        GiletCommit commitObj = readCommitObject(commitId);
        System.out.println("message: " + commitObj.message);
        System.out.println("parent: " + commitObj.parents);
        System.out.println("timestamp: " + commitObj.time);
        System.out.println("tree: " + commitObj.currentWorkingTree.entrySet());
    }

    /** prints out what HEAD points to
     *
     */
    public static void showLatestCommit() {
        String commitId = getBranchId(getCurrentBranch());
        showCommit(commitId);
    }


}
