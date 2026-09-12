package gitlet;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static gitlet.GitletCommit.getCommit;
import static gitlet.GitletCommit.getCurrentCommit;
import static gitlet.GitletErrorMsg.*;
import static gitlet.GitletIndex.clearIndex;
import static gitlet.GitletObject.getObjPathComplete;
import static gitlet.GitletObject.getObjectPath;
import static gitlet.GitletRepository.*;
import static gitlet.Utils.join;
import static gitlet.Utils.readContentsAsString;


public class GitletBranch {
    // i should only manipulate HEAD

    private static String currentBranch = getDefaultBranch();
    private static File REF_DIR = join(GITLET_DIR, "refs", "heads");



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
        Utils.writeContents(HEAD, ref);
    }


    /**
     * creates a new branch with the given branchName and makes it point to the given commitId
     *
     * @param branchName
     * @param commitId
     */
    public static void createBranch(String branchName, String commitId) {
        File branch = getBranchFile(branchName);
        if (branch.exists()) {
            System.err.println("A branch with that name already exists.");
            System.exit(0);
        }
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
                if (file.getName().equals(getCurrentBranch())) {
                    System.out.println("*" + file.getName());
                } else {
                    System.out.println(file.getName());
                }
            }
        }
    }

    /**
     * just removes the given branch
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
        if (!branch.exists()) {
            return null;
        }
        return readContentsAsString(branch);
    }


    public static boolean untrackedExists() {
        GitletIndex index = GitletIndex.getIndexInstance();
        for (String file : Utils.plainFilenamesIn(CWD)) {
            if (!index.isTracked(getCurrentCommit(), file)) {
                return true;
            }
        }
        return false;
    }




    public static void resetBranch(String commitId) {
        checkUntracked();
        commitId = getObjPathComplete("commit", commitId);

        checkCommitExists(commitId);


        GitletCommitObj commitObj = getCommit(commitId);
        checkoutCommit(commitObj);
        updateBranch(getCurrentBranch(), commitId);
        clearIndex();
    }

    public static void checkoutBranch(String branchName) {

        String branchId;
        checkCurrentBranchCheckout(branchName);
        checkUntracked();
        branchId = getBranchId(branchName);
        checkBranchValidity(branchId);

        /**
         getCommit always get a valid branchId
         cuz of checkBranchValidity
         **/
        checkoutCommit(getCommit(branchId));
        updateHead(branchName);
        clearIndex();
    }

    public static void checkoutCommit(GitletCommitObj commitObj) {

        checkCommitValidity(commitObj);

        GitletIndex snapshot = commitObj.getSnapshot();
        Set<String> files = snapshot.getIndexPair().keySet();

        for (String file : Utils.plainFilenamesIn(CWD)) {
            if (!files.contains(file)) {
                Utils.restrictedDelete(join(CWD, file)); // delete cwd
            }
        }

        for (String file : snapshot.getIndexPair().keySet()) {
            checkoutFile(commitObj, file); // restore files from the commit
        }

    }


    public static void checkoutFile(GitletCommitObj commitObj, String file) {
        File newFile = join(CWD, file);
        checkoutFile(commitObj, newFile);
    }

    public static void checkoutFile(GitletCommitObj commitObj, File file) {
        GitletIndex snapshot = commitObj.getSnapshot();
        String fileNameString = file.getName();
        checkFileExistsInCommit(snapshot, fileNameString);

        if (!file.exists()) {
            createFile(file);
        }

        File objPath =  getObjectPath("blob", snapshot.getIndexEntry(fileNameString));
        String contents = readContentsAsString(objPath);
        Utils.writeContents(file, contents);
    }


}
