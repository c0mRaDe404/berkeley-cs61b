package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
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

    /* TODO: fill in the rest of this class. */
    private static final File[] DIRS = {join(GITLET_DIR,"objects"), join(GITLET_DIR,"refs"),
            join(GITLET_DIR, "branches")};
    private static final File[] FILES = {join(GITLET_DIR,"HEAD"), join(GITLET_DIR,"index")};

    public static void createRepository() {
        if (GITLET_DIR.exists()) {
            System.err.println(".gitlet has already been initialized.");
            System.exit(0);
        }

        if (!GITLET_DIR.mkdir()) {
            System.out.println("Can't setup the" + GITLET_DIR.getPath() + " directory!");
            System.exit(0);
        }

        for (File dir : DIRS) {
            if (!dir.mkdir()) {
                System.out.println("Can't setup the" + dir.getPath() + " directory!");
                System.exit(0);
            }
        }
        for (File file : FILES) {
            try {
                if (!file.createNewFile()) {
                    System.out.println("Can't setup the" + file.getPath() + " file!");
                    System.exit(0);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
