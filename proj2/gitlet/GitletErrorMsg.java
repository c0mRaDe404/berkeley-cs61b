package gitlet;

import java.io.File;

import static gitlet.GitletCommit.getCurrentCommit;
import static gitlet.GitletRepository.CWD;
import static gitlet.Utils.join;

public class GitletErrorMsg {


    public static void checkFileIsTracked(GitletIndex index, String file) {
        if (!index.isTracked(getCurrentCommit(), file)) {
            System.err.println("File is untracked.");
            System.exit(0);
        }
    }

    /** Checks if the given file exists
     *
     * @param file
     */
    public static void checkFileExists(String file) {
        if (!join(CWD, file).exists()) {
            System.err.println("File does not exist.");
            System.exit(0);
        }
    }

    /** Checks if the given file exists
     *
     * @param file
     */
    public static void checkFileExists(File file) {
        if (!file.exists()) {
            System.err.println("File does not exist.");
            System.exit(0);
        }
    }

}
