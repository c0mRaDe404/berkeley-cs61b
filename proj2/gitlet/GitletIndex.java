package gitlet;

import java.io.File;
import java.util.HashMap;

import static gitlet.GitletObject.createObjectFile;
import static gitlet.GitletObject.hashFileObject;
import static gitlet.GitletRepository.CWD;
import static gitlet.GitletRepository.GITLET_DIR;
import static gitlet.Utils.join;

// gitlet add [filename]
// get file(s)
// hash it
// new directory
// new file object
// add name and hash to the index.


public class GitletIndex {

    private static HashMap<String, String> index = new HashMap<>();
    public static final File INDEX_FILE = join(GITLET_DIR, "index");

    /**
     * get the index file
     *
     * @return deserialized index map
     */
    public static HashMap<String, String> getIndex() {
        readFromIndex();
        return index;
    }


    /**
     * clears the index file
     *
     */
    public static void clearIndex() {
        index.clear();
        writeToIndex();
    }

    /**
     * adds a file to the index file
     *
     * @param file
     */
    public static void addToIndex(String file) {
        String hash = hashFileObject(file);
        File targetFile = createObjectFile(hash);
        File sourceFile = join(CWD, file);
        Utils.writeContents(targetFile, Utils.readContentsAsString(sourceFile));
        readFromIndex();
        index.put(file, hash);
        writeToIndex();
    }

    /**
     * helper for serializing the index object
     *
     */
    private static void writeToIndex() {
        if (!INDEX_FILE.exists()) {
            GitletRepository.createFile(INDEX_FILE);
        }
        Utils.writeObject(INDEX_FILE, index);
    }

    /**
     * helper for deserializing index file
     *
     */
    private static void readFromIndex() {
        if (!INDEX_FILE.exists()) {
            return;
        }
        index = Utils.readObject(INDEX_FILE, HashMap.class);
    }

    /**
     * unstages a file
     *
     * @param file
     */
    public static void removeFromIndex(String file) {
        readFromIndex();
        index.remove(file, hashFileObject(file));
        writeToIndex();
    }

    /**
     * list contents in the index file (for debugging purposes)
     *
     */
    public static void listFilesFromIndex() {
        readFromIndex();
        for (Object file : index.keySet()) {
            System.out.println(file);
        }
    }
}
