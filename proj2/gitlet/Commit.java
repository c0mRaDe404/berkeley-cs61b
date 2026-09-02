package gitlet;
import jdk.jshell.execution.Util;

import java.io.File;
import java.io.Serializable;

import java.util.Date; // TODO: You'll likely use this in this class

import static gitlet.GitletObject.createObjectFile;
import static gitlet.Repository.GITLET_DIR;
import static gitlet.Utils.join;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private final String message;
    private final String time;

    private Commit(String message, String time) {
        this.message = message;
        this.time = time;
    }

    /** creates a commit object with given msg and time
     *
     * @param msg
     * @param time
     * @return a commit object
     */
    public static Commit createCommitObject(String msg, String time) {
        return new Commit(msg, time);
    }

    /** creates an empty commit with the given message and time
     *
     * @return sha1 message digest
     */
    public String createCommit() {
        String commitHash;
        commitHash = Utils.sha1(message, time);
        createCommit(commitHash);
        return commitHash;
    }

    /** uses commit hash to create a commit object file
     *
     * @param commitHash
     */
    private void createCommit(String commitHash) {
        File targetFile = createObjectFile(commitHash);
        Utils.writeObject(targetFile, this);
    }

}
