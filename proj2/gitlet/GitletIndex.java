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
import static gitlet.GitletRepository.*;
import static gitlet.Utils.join;


public class GitletIndex implements Serializable {

    HashMap<String, String> INDEX;


    private GitletIndex() {
        INDEX = new HashMap<>();
    }

    private GitletIndex(HashMap<String, String> map) {
        INDEX = map;
    }

    public String getIndexEntry(String key) {
        return INDEX.get(key);
    }


    public boolean isTracked(String file) {
        return hasEntry(file) || getCurrentCommit().getSnapshot().hasEntry(file);
    }

    public boolean isModified(String file) {
        GitletCommitObj currentCommit = getCurrentCommit();
        String fileId = currentCommit.getSnapshot().getIndexEntry(file);
        if (hashFileObject(file).equals(fileId)) {
            return false;
        }
        return true;
    }


    public boolean hasEntry(String file) {
        return INDEX.containsKey(file);
    }

    public static void stageFile(String file) {
        GitletIndex index = getIndexInstance();
        if (!index.isModified(file)) {
            if (index.hasEntry(file)) {
                System.out.print("i am here");
                index.removeFromIndex(file);
            }
        } else {
            index.addToIndex(file);
        }

    }

    public static void removeFile(String file) {

        GitletIndex index = getIndexInstance();

        if (index.isTracked(file)) { // if a file is tracked
            if (index.hasEntry(file)) { // if it's in index
                index.removeFromIndex(file);
            }

            if (getCurrentCommit().getSnapshot().hasEntry(file)) { // if it's in the current commit
                index.updateIndex(file, null);
            }

            File targetFile = join(CWD, file);
            deleteFile(targetFile);
        }

    }

    public static GitletIndex getIndexInstance() {
        GitletIndex index = new GitletIndex();
        index.readFromIndex();
        return index;
    }

    public static GitletIndex getIndexInstance(HashMap<String, String> index) {
        return new GitletIndex(index);
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
        updateIndex(file, hash);
    }

    private void updateIndex(String key, String value) {
        readFromIndex();
        INDEX.put(key, value);
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
        GitletIndex index = commitObj.getSnapshot();
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
