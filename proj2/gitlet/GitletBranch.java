package gitlet;

import java.io.File;
import java.util.List;
import java.util.Objects;

import static gitlet.GitletRepository.*;
import static gitlet.Utils.join;
import static gitlet.Utils.readContentsAsString;


public class GitletBranch {
    // i should only manipulate HEAD

    private static String currentBranch = getDefaultBranch();
    private static File REF_DIR =  join(GITLET_DIR, "refs", "heads");

    /**
     * gives the HEAD file
     *
     * @return headFile pointer
     */
    public static File getHead() {
        return HEAD;
    }

    public static List<String> getBranches() {
       return Utils.plainFilenamesIn(REF_DIR);
    }

    /**
     * gives the default branch name
     *
     * @return default branch
     */
    public static String getDefaultBranch() {
        return "master";
    }

    /**
     * fetch the current branch from the HEAD file
     *
     * @return current branch
     */
    public static String getCurrentBranch() {
        if (HEAD.length() == 0) {
            System.err.println("no reference found in HEAD.");
            System.exit(0);
        }
        File ref = new File(Utils.readContentsAsString(HEAD));
        return ref.getName();
    }

    /**
     * replace the HEAD file with the given reference
     *
     * @param ref
     */
    public static void updateHead(String ref) {
        Utils.writeContents(HEAD, "refs/heads/" + ref);
    }


    /**
     * creates a new branch with the given branchName and makes it point to the given commitId
     *
     * @param branchName
     * @param commitId
     */
    public static void createBranch(String branchName, String commitId) {
        File branch = getBranchFile(branchName);
        createFile(branch);
        Utils.writeContents(branch, commitId);
    }

    public static void createBranch(String branchName) {
        createBranch(branchName, getBranchId(getCurrentBranch()));
    }


    public static void listBranches() {
        // what if ../refs/heads got deleted?
        for (File file : Objects.requireNonNull(REF_DIR.listFiles())) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
    }

    /** just removes the given branch
     *
      * @param branchName
     */
    public static void removeBranch(String branchName) {
        File branch = getBranchFile(branchName);
        if (!branch.exists()) {
           System.err.println("A branch with that name does not exist.");
           System.exit(0);
        }

        if (getCurrentBranch().equals(branchName)) {
           System.err.println("Cannot remove the current branch.");
           System.exit(0);
        }
        deleteFile(branch);
    }

    /**
     * updates the specified branch with the given commitId
     *
     * @param branchName
     * @param commitId
     */
    public static void updateBranch(String branchName, String commitId) {
        File branch = getBranchFile(branchName);
        Utils.writeContents(branch, commitId);
    }

    /**
     * gives the branch file
     *
     * @param branchName
     * @return branch file
     */
    private static File getBranchFile(String branchName) {
        return join(GITLET_DIR, "refs", "heads", branchName);
    }

    /**
     * gives the branchId for the given branch
     *
     * @param branchName
     * @return branchId
     */
    public static String getBranchId(String branchName) {
        File branch = getBranchFile(branchName);
        if (branch.exists()) {
            return readContentsAsString(branch);
        }
        return null;
    }

    public static void switchBranch(String branchName) {
        // what if branch doesnt exist? handle that
        if (!getBranchFile(branchName).exists()) {
           System.err.println("No such branch exists.");
           System.exit(0);
        }

        if (getCurrentBranch().equals(branchName)) {
           System.err.println();
        }
        updateHead(branchName);
        System.out.println("Switched to branch" + "'" + branchName + "'");
    }
}
