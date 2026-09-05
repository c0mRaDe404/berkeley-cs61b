package gitlet;

import java.io.File;

import static gitlet.Utils.join;
import static gitlet.GitletRepository.createFile;
import static gitlet.Utils.readContentsAsString;
import static gitlet.GitletRepository.GITLET_DIR;
import static gitlet.GitletRepository.HEAD;


public class GitletBranch {
    // i should only manipulate HEAD

    private static String currentBranch = getDefaultBranch();


    /**
     * gives the HEAD file
     *
     * @return headFile pointer
     */
    public static File getHead() {
        return HEAD;
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
        String ref = Utils.readContentsAsString(HEAD);
        String[] branch = ref.split("/");
        return branch[branch.length - 1];
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

}
