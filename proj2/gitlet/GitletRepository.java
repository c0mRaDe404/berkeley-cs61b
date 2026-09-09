package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.GitletBranch.*;
import static gitlet.GitletCommit.*;
import static gitlet.GitletCommitObj.createCommitObject;
import static gitlet.GitletIndex.getIndexInstance;
import static gitlet.Utils.*;

import gitlet.GitletCommitObj;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author Bhuvanesh
 */
public class GitletRepository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));

    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /**
     * the index file
     *
     */
    public static final File INDEX_FILE = join(GITLET_DIR, "index");

    /**
     * the HEAD file
     *
     */
    public static final File HEAD = join(GITLET_DIR, "HEAD");

    private static final File[] DIRS = {
            join(GITLET_DIR, "objects"),
            join(GITLET_DIR, "refs"),
            join(GITLET_DIR, "branches"),
            join(GITLET_DIR, "refs", "heads"),
            join(GITLET_DIR, "refs", "tags")
    }; // order should be preserved

    private static final File[] FILES = {
            HEAD
    }; // order doesnt matter

    private static boolean repoExists() {
        return GITLET_DIR.exists();
    }

    /**
     * creates a new directory
     *
     * @param dir
     */
    static void createDirectory(File dir) {
        if (!dir.mkdirs()) {
            throw new GitletException("Can't setup the" + dir.getPath() + " directory");
        }
    }

    /**
     * creates a file
     *
     * @param file
     */
    static void createFile(File file) {
        try {
            if (!file.createNewFile()) {
                throw new GitletException("Can't setup the" + file.getPath() + " file!");
            }
        } catch (IOException | GitletException e) {
            throw new GitletException(e.getMessage());
        }
    }


    static boolean deleteFile(File file) {
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * creates the .gitlet directory
     *
     * @return true if it succeeds making the dir
     */
    private static boolean makeGitletRepo() {
        return GITLET_DIR.mkdir();
    }

    /**
     * gives the path of the .gitlet directory
     *
     * @return
     */
    private static String getRepoPath() {
        return GITLET_DIR.getPath();
    }

    /**
     * creates the .gitlet file in the current working tree
     *
     */
    private static void createRepository() {
        if (repoExists()) {
            System.err.println("A Gitlet version-control system already exists in the current directory.");
            System.exit(0);
        }

        if (!makeGitletRepo()) {
            System.out.println("Can't setup the" + getRepoPath() + " directory!");
            System.exit(0);
        }

        for (File dir : DIRS) {
            createDirectory(dir);
        }

        for (File file : FILES) {
            createFile(file);
        }
    }

    /**
     * initializes the repository and sets up everything
     *
     */
    public static void initRepo() {
        GitletRepository.createRepository(); // initialize the .gitlet repo
        updateHead(getDefaultBranch()); // initializing head with default branch


        GitletCommitObj initialCommit = createCommitObject("initial commit",
                getFormattedTime(new Date(0))); // creating the initial commit

        createBranch(getDefaultBranch(), createCommit(initialCommit)); // create the default branch and add the commit id

    }

    private static Map<String, List<String>> getIndexStatus(GitletIndex index) {
        Map<String, List<String>> status = new HashMap<>();
        List<String> added = new ArrayList<>();
        List<String> deleted = new ArrayList<>();
        for (Map.Entry<String, String> pair : index.getIndexPair().entrySet()) {
            if (pair.getValue() == null) {
                deleted.add(pair.getKey());
            } else {
                added.add(pair.getKey());
            }
        }
        status.put("added", added);
        status.put("deleted", deleted);
        return status;
    }

    private static Map<String, List<String>> getSnapshotStatus(GitletIndex index, GitletCommitObj currentCommit) {


        Map<String, List<String>> status = new HashMap<>();
        List<String> modified = new ArrayList<>();
        List<String> deleted = new ArrayList<>();
        for (String file : getCommitSnapshot(index).getIndexPair().keySet()) {
            if (index.isRemoved(currentCommit, file)) {
                deleted.add(file);
            } else if (index.isModified(currentCommit, file)) {
                modified.add(file);
            }
        }
        status.put("modified", modified);
        status.put("deleted", deleted);
        return status;
    }

    public static List<String> getUntrackedStatus(GitletIndex index, GitletCommitObj currentCommit) {
        List<String> untracked = new ArrayList<>();
        for (String file : Utils.plainFilenamesIn(CWD)) {
            if (!index.isTracked(currentCommit, file)) {
                untracked.add(file);
            }
        }
        return untracked;
    }

    public static void showBranchStatus() {
        System.out.println("=== Branches ===");
        String curBranch = getCurrentBranch();
        for (String branch : getBranches()) {
            if (curBranch.equals(branch)) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
        System.out.println();
    }

    public static void showUntrackedStatus() {
        System.out.println("=== Untracked Files ===");
        for (String file : getUntrackedStatus(getIndexInstance(), getCurrentCommit())) {
            System.out.println(file);
        }
        System.out.println();
    }

    public static void showIndexStatus() {
        GitletIndex index = getIndexInstance();
        System.out.println("=== Staged Files ===");
        for (String file : getIndexStatus(index).get("added")) {
            System.out.println(file);
        }

        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String file : getIndexStatus(index).get("deleted")) {
            System.out.println(file);
        }
        System.out.println();
    }

    public static void showSnapshotStatus() {
        GitletIndex index = getIndexInstance();
        GitletCommitObj currentCommit = getCurrentCommit();
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String file : getSnapshotStatus(index, currentCommit).get("modified")) {
            System.out.println(file + " (modified)");
        }
        for (String file : getSnapshotStatus(index, currentCommit).get("deleted")) {
            System.out.println(file + " (deleted)");
        }
        System.out.println();
    }

    public static void getRepoStatus() {

        showBranchStatus();
        showIndexStatus();
        showSnapshotStatus();
        showUntrackedStatus();
    }


}




