package gitlet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static gitlet.GitletIndex.getIndexInstance; /**
 * Represents a gitlet commit object.
 *
 * @author Bhuvanesh
 */

public class GitletCommitObj extends GitletObject implements Serializable {

    private String message; // commit message
    private String time; // commit timestamp
    private List<String> parents; // parents of the commit
    private GitletIndex snapshot; // copy of the index file

    private GitletCommitObj(String message, String time, GitletIndex snapshot) {
        this.message = message;
        this.time = time;
        this.snapshot = snapshot;
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
        return new GitletCommitObj(msg, time, GitletCommit.getCommitSnapshot(getIndexInstance()));
    }

    public static GitletCommitObj createCommitObject(String msg, String time, GitletIndex index) {
        return new GitletCommitObj(msg, time, GitletCommit.getCommitSnapshot(index));
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
