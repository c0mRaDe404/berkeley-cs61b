package gitlet;

import java.io.File;

import static gitlet.GitletBranch.getCurrentBranch;
import static gitlet.GitletBranch.untrackedExists;
import static gitlet.GitletCommit.getCurrentCommit;
import static gitlet.GitletRepository.CWD;
import static gitlet.GitletRepository.repoExists;
import static gitlet.Utils.join;

public class GitletErrorMsg {

    public static void checkRepoExists() {
        if (repoExists()) {
            System.err.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
            System.exit(0);
        }
    }

    public static void checkRepoDoesNotExist() {
        if (!repoExists()) {
            System.err.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    public static void checkOperands(String given, String target) {
        if (!given.equals(target)) {
            System.err.println("Incorrect operands.");
            System.exit(0);
        }
    }

    public static void checkCurrentBranchCheckout(String branchName) {
        if (branchName.equals(getCurrentBranch())) {
            System.err.println("No need to checkout the current branch.");
            System.exit(0);
        }
    }

    public static void checkUntracked() {
        if (untrackedExists()) {
            System.err.println("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            System.exit(0);
        }
    }

    public static void checkBranchValidity(String branchId) {
        if (branchId == null) {
            System.err.println("No such branch exists.");
            System.exit(0);
        }
    }

    public static void checkFileIsTracked(GitletIndex index, String file) {
        if (!index.isTracked(getCurrentCommit(), file)) {
            System.err.println("File is untracked.");
            System.exit(0);
        }
    }

    public static void checkCommitExists(String commitId) {
        if (commitId == null) {
            System.err.println("No commit with that id exists.");
            System.exit(0);
        }
    }

    public static void checkFileExistsInCommit(GitletIndex snapshot, String file) {
        if (!snapshot.hasEntry(file)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }

    public static void checkCommitValidity(GitletCommitObj commit) {
        if (commit == null) {
            System.err.println("No commit with that id exists.");
            System.exit(0);
        }
    }

    /**
     * Checks if the given file exists
     *
     * @param file
     */
    public static void checkFileExists(String file) {
        if (!join(CWD, file).exists()) {
            System.err.println("File does not exist.");
            System.exit(0);
        }
    }

}
