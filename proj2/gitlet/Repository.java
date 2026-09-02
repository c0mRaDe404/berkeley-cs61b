package gitlet;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import static gitlet.Utils.*;
import static gitlet.GitletBranch.createBranch;
import static gitlet.GitletBranch.getCurrentBranch;
import static gitlet.Commit.createCommitObject;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Bhuvanesh
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));

    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    private static final File[] DIRS = {
            join(GITLET_DIR,"objects"),
            join(GITLET_DIR,"refs"),
            join(GITLET_DIR, "branches"),
            join(GITLET_DIR,"refs", "heads"),
            join(GITLET_DIR,"refs", "tags")
    }; // order should be preserved

    private static final File[] FILES = {
            join(GITLET_DIR,"HEAD"),
    }; // order doesnt matter

    private static boolean repoExists() {
       return GITLET_DIR.exists();
    }

     static void createDirectory(File dir) {
        if (!dir.mkdir()) {
            System.out.println("Can't setup the" + dir.getPath() + " directory!");
            System.exit(0);
        }
    }

    static void createFile(File file) {
        try {
            if (!file.createNewFile()) {
                System.out.println("Can't setup the" + file.getPath() + " file!");
                System.exit(0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean makeGitletRepo() {
       return GITLET_DIR.mkdir();
    }

    private static String getRepoPath() {
        return GITLET_DIR.getPath();
    }

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

    public static void initRepo() {
        Repository.createRepository();
        Commit initialCommit = createCommitObject("initial commit", Instant.EPOCH.toString());
        createBranch(getCurrentBranch(), initialCommit.createCommit());
        updateHead(getCurrentBranch());
    }


    public static void updateHead(String ref) {
       File head =  join(GITLET_DIR,"HEAD");
       Utils.writeContents(head, "refs/heads/" + ref);
    }

}
