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

    /** returns the hash digest of a given file
     *
     * @param file
     * @return a hash digest if the given file exists, otherwise null
     */
    public static String hashFileObject(String file) {
        File fileObj;
        fileObj = join(CWD, file);
        if (!fileObj.exists()) {
            return null;
        }
        return hashObject(readContentsAsString(fileObj));
    }

    /** returns the hashing algorithm used in gitlet
     *
     * @return the hash algorithm string
     */
    public static String getHashAlgo() {
        return HASH_ALGO;
    }

    /** returns the hash digest of objects
     *
     * @param contents
     * @return a hash digest
     */
    public static String hashObject(Object... contents) {
        return Utils.sha(contents);
    }

    /** gives digest length
     *
     * @return the hash value string's length
     */
    public static int getDigestLength() {
        try {
            return MessageDigest.getInstance(HASH_ALGO).getDigestLength();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /** gets the directory where objects are stored
     *
     * @param type
     * @return the object directory inside the gitlet repo
     */
    public static File getObjDir(String type) {
        return join(OBJ_DIR, type);
    }

    /** given a partial commit id, it constructs the full commit id
     *
     * @param type
     * @param commitId
     * @return the path of an object, returns null if it does not exist
     */
    public static String getObjPathComplete(String type, String commitId) {

        if (commitId.length() == DIGEST_LENGTH) {
            return commitId;
        }

        String objParent, objFile;
        int commitIdLength;

        objParent = commitId.substring(0, 2);
        commitIdLength = commitId.length();
        objFile = commitId.substring(2, commitIdLength);

        File parent = join(OBJ_DIR, type, objParent);

        if (parent.exists()) {
            for (String file : parent.list()) {
                if (file.substring(0, commitIdLength - 2).equals(objFile)) {
                    return objParent + file;
                }
            }
        }

        return null;

    }

    /** requires full commit id, nothing else about it
     *
     * @param type
     * @param commitId
     * @return the object path, null if it does not exist
     */
    public static File getObjectPath(String type, String commitId) {
        String objParent, objFile;
        if (commitId == null) {
            return null;
        }

        objParent = commitId.substring(0, 2);
        objFile = commitId.substring(2);
        return join(OBJ_DIR, type, objParent, objFile);
    }


    /** creates object files
     *
     * @param type
     * @param commitId
     * @return the created file
     */
    public static File createObjectFile(String type, String commitId) {
        File targetDir, targetFile;

        targetDir = join(OBJ_DIR, type, commitId.substring(0, 2));
        targetFile = join(targetDir, commitId.substring(2));

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
