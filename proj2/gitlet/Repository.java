package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.*;

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
            join(GITLET_DIR,"index")
    }; // order doesnt matter

    private static boolean repoExists() {
       return GITLET_DIR.exists();
    }

    private static void createDirectory(File dir) {
        if (!dir.mkdir()) {
            System.out.println("Can't setup the" + dir.getPath() + " directory!");
            System.exit(0);
        }
    }

    private static void createFile(File file) {
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

    public static void createRepository() {
        if (repoExists()) {
            System.err.println(".gitlet has already been initialized.");
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

}
