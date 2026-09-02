package gitlet;

import java.io.File;
import java.util.HashMap;

import static gitlet.GitletObject.createObjectFile;
import static gitlet.GitletObject.hashFileObject;
import static gitlet.Repository.CWD;
import static gitlet.Repository.GITLET_DIR;
import static gitlet.Utils.join;

// gitlet add [filename]
// get file(s)
// hash it
// new directory
// new file object
// add name and hash to the index.


public class GitletIndex {

    private static HashMap<String, String> index = new HashMap<>();
    private static final File INDEX_FILE = join(GITLET_DIR, "index");

    public static void addToIndex(String file) {
        String hash = hashFileObject(file);
        File targetFile = createObjectFile(hash);
        File sourceFile = join(CWD, file);
        Utils.writeContents(targetFile, Utils.readContentsAsString(sourceFile));
        readFromIndex();
        index.put(file, hash);
        writeToIndex();
    }
    private static void writeToIndex() {
        if (!INDEX_FILE.exists()) {
            Repository.createFile(INDEX_FILE);
        }
       Utils.writeObject(INDEX_FILE, index);
    }

    private static void readFromIndex() {
        if (!INDEX_FILE.exists()) {
            return;
        }
        index = Utils.readObject(INDEX_FILE, HashMap.class);
    }

    public static void removeFromIndex(String file){
        readFromIndex();
        index.remove(file, hashFileObject(file));
        writeToIndex();
    }

    public static void listFilesFromIndex() {
        readFromIndex();
        for (Object file: index.keySet()) {
            System.out.println(file);
        }
    }
}
