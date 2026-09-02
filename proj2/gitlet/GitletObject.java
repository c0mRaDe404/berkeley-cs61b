package gitlet;

import java.io.File;

import static gitlet.Repository.CWD;
import static gitlet.Repository.GITLET_DIR;
import static gitlet.Utils.join;
import static gitlet.Utils.readContentsAsString;

public class GitletObject {

    public static String hashFileObject(String file) {
       File fileObj = join(CWD, file);
       return hashObject(readContentsAsString(fileObj));
    }

    public static String hashObject(Object... contents) {
       return Utils.sha1(contents);
    }

    public static File createObjectFile(String commitHash) {
        File targetDir = join(GITLET_DIR,"objects", commitHash.substring(0, 2));
        File targetFile = join(targetDir, commitHash.substring(2));
        Repository.createDirectory(targetDir);
        Repository.createFile(targetFile);
        return targetFile;
    }
}
