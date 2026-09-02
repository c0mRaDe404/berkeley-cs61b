package gitlet;

import java.io.File;
import static gitlet.Utils.join;
import static gitlet.Repository.GITLET_DIR;
import static gitlet.Repository.createFile;

public class GitletBranch {
    private static String currentBranch = "master";

    public static String getCurrentBranch() {
       return currentBranch;
    }

    public static void createBranch(String branchName, String commitId) {
        File branch = join(GITLET_DIR,"refs", "heads", branchName);
        createFile(branch);
        Utils.writeContents(branch, commitId);
    }


}
