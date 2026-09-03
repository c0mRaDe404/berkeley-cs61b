package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.GitletRepository.CWD;
import static gitlet.GitletRepository.GITLET_DIR;
import static gitlet.Utils.join;
import static gitlet.Utils.readContentsAsString;

public class GitletObject {

   private static final File OBJ_DIR = join(GITLET_DIR,"objects");

    public static String hashFileObject(String file) {
       File fileObj = join(CWD, file);
       if (!fileObj.exists()) {
          System.err.println("File does not exist.");
          System.exit(0);
       }
       return hashObject(readContentsAsString(fileObj));
    }

    public static String hashObject(Object... contents) {
       return Utils.sha1(contents);
    }

    public static File getObjectPath(String commitId) {
       String objParent = commitId.substring(0, 2);
       String objFile = commitId.substring(2);
       return join(OBJ_DIR, objParent, objFile);
    }

    public static File createObjectFile(String commitId) {
        File targetDir = join(OBJ_DIR, commitId.substring(0, 2));
        File targetFile = join(targetDir, commitId.substring(2));
        if (!targetDir.exists()) {
            GitletRepository.createDirectory(targetDir);
        }
        if (!targetFile.exists()) {
            GitletRepository.createFile(targetFile);
        }
        return targetFile;
    }
}
