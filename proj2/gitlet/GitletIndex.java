package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static gitlet.GitletCommit.getCurrentCommit;
import static gitlet.GitletObject.createObjectFile;
import static gitlet.GitletObject.hashFileObject;
import static gitlet.GitletRepository.CWD;
import static gitlet.Utils.join;
import static gitlet.GitletRepository.INDEX_FILE;



public class GitletIndex implements Serializable {

    HashMap<String, String> INDEX;


    private GitletIndex() {
        INDEX = new HashMap<>();
    }

    public String getIndexEntry(String key) {
       return INDEX.get(key);
    }



    public boolean isModified(String file) {
        GitletCommitObj currentCommit = getCurrentCommit();
        String fileId = currentCommit.currentIndex.getIndexEntry(file);
        if (hashFileObject(file).equals(fileId)) {
            return false;
        }
        return true;
    }


    public boolean isStaged(String file) {
       return INDEX.containsKey(file);
    }

    public static void stageFile(String file) {
        GitletIndex index = getIndexInstance();
        if (!index.isModified(file)) {
            if (index.isStaged(file)) {
                System.out.print("i am here");
                index.removeFromIndex(file);
            }
        } else {
            index.addToIndex(file);
        }

    }

    public static GitletIndex getIndexInstance() {
        GitletIndex index = new GitletIndex();
        index.readFromIndex();
        return index;
    }

    public static void listIndex() {
        GitletIndex index = new GitletIndex();
        index.listFilesFromIndex();
    }

    public static void clearIndex() {
        GitletIndex index = new GitletIndex();
        index.clear();
    }


    private void createIndexFile() {
        if (!indexExists()) {
            GitletRepository.createFile(INDEX_FILE);
        }
    }

    /**
     * clears the index file
     *
     */
    private void clear() {
        INDEX.clear();
        writeToIndex();
    }

    /**
     * adds a file to the index file
     *
     * @param file
     */
    public void addToIndex(String file) {
        File sourceFile = join(CWD, file);
        if (!sourceFile.exists()) {
           System.err.println("File does not exist.");
           System.exit(0);
        }
        String hash = hashFileObject(file);
        File targetFile = createObjectFile(hash);
        Utils.writeContents(targetFile, Utils.readContentsAsString(sourceFile));
        readFromIndex();
        INDEX.put(file, hash);
        writeToIndex();
    }


    private boolean indexExists() {
        return INDEX_FILE.exists();
    }

    /**
     * helper for serializing the index object
     *
     */
    private void writeToIndex() {
        createIndexFile();
        Utils.writeObject(INDEX_FILE, INDEX);
    }

    /**
     * helper for deserializing index file
     *
     */
    private void readFromIndex() {
        if (indexExists()) {
            INDEX = (HashMap<String, String>) Utils.readObject(INDEX_FILE, HashMap.class);
        }
    }

    /**
     * unstages a file
     *
     * @param file
     */
    public void removeFromIndex(String file) {
        INDEX.remove(file);
        writeToIndex();
    }

    /**
     * list contents in the index file (for debugging purposes)
     *
     */
    private void listFilesFromIndex() {
        readFromIndex();
        for (Object file : INDEX.keySet()) {
            System.out.println(file);
        }
    }


    /**
     * sort the index key-value pairs and concatenates them to one long string
     *
     * @return concatenated key-value pairs
     */
    public String indexToString(GitletCommitObj commitObj) {
        GitletIndex index = commitObj.currentIndex;
        List<String> keys = new ArrayList<>(index.INDEX.keySet());
        Collections.sort(keys);

        StringBuilder concatenatedKeyValuePairs = new StringBuilder();
        for (String key : keys) {
            concatenatedKeyValuePairs.append(key);
            concatenatedKeyValuePairs.append(index.INDEX.get(key));
        }
        return concatenatedKeyValuePairs.toString();
    }


    /**
     * check any files are in the staging area
     *
     * @return true if staging area is not empty otherwise false
     */
    boolean hasStagedFiles() {
        return !INDEX.isEmpty();
    }

    HashMap<String, String> getIndexPair() {
       return INDEX;
    }
}
