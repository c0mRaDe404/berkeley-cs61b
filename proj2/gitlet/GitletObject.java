package gitlet;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static gitlet.GitletRepository.CWD;
import static gitlet.GitletRepository.GITLET_DIR;
import static gitlet.Utils.join;
import static gitlet.Utils.readContentsAsString;

public class GitletObject {

    private static final File OBJ_DIR = join(GITLET_DIR, "objects");
    private static final String HASH_ALGO = "SHA-256";
    private static final int DIGEST_LENGTH = getDigestLength();

    public static String hashFileObject(String file) {
        File fileObj = join(CWD, file);
        //checkFileExists(file);
        return hashObject(readContentsAsString(fileObj));
    }

    public static String getHashAlgo() {
        return HASH_ALGO;
    }

    public static String hashObject(Object... contents) {
        return Utils.sha(contents);
    }

    public static int getDigestLength() {
        try {
            return MessageDigest.getInstance(HASH_ALGO).getDigestLength();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public static File getObjDir(String type) {
        return join(OBJ_DIR, type);
    }

    public static String getObjPathComplete(String type, String commitId) {

        if (commitId.length() == DIGEST_LENGTH) {
            return commitId;
        }

        String objParent = commitId.substring(0, 2);
        int commitIdLength = commitId.length();
        String objFile = commitId.substring(2, commitIdLength);

        File parent = join(OBJ_DIR, type, objParent);

        for (String file : parent.list()) {
            if (file.substring(0, commitIdLength - 2).equals(objFile)) {
                return objParent + file;
            }
        }
        return null;

    }

    public static File getObjectPath(String type, String commitId) {
        String objParent = commitId.substring(0, 2);
        String objFile = commitId.substring(2);
        return join(OBJ_DIR, type, objParent, objFile);
    }


    public static File createObjectFile(String type, String commitId) {
        File targetDir = join(OBJ_DIR, type, commitId.substring(0, 2));
        File targetFile = join(targetDir, commitId.substring(2));
        try {
            if (!targetDir.exists()) {
                GitletRepository.createDirectory(targetDir);
            }
            if (!targetFile.exists()) {
                GitletRepository.createFile(targetFile);
            }
        } catch (GitletException e) {
            e.printStackTrace();
        }
        return targetFile;
    }
}
