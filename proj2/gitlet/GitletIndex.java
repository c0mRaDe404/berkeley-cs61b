package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static gitlet.GitletErrorMsg.checkFileExists;
import static gitlet.GitletErrorMsg.checkFileIsTracked;
import static gitlet.GitletObject.createObjectFile;
import static gitlet.GitletObject.hashFileObject;
import static gitlet.GitletRepository.*;
import static gitlet.Utils.join;


public class GitletIndex implements Serializable {

    private HashMap<String, String> INDEX;


    private GitletIndex() {
        INDEX = new HashMap<>();
    }

    private GitletIndex(HashMap<String, String> map) {
        INDEX = map;
    }

    public String getIndexEntry(String key) {
        return INDEX.get(key);
    }


    public static GitletIndex getIndexInstance() {
        GitletIndex index = new GitletIndex();
        index.readFromIndex();
        return index;
    }

    public static GitletIndex getIndexInstance(HashMap<String, String> index) {
        return new GitletIndex(index);
    }



    public boolean isTracked(GitletCommitObj currentCommit, String file) {
        return hasEntry(file) || currentCommit.getSnapshot().hasEntry(file);
    }



    public boolean isRemoved(GitletCommitObj currentCommit, String file) {
        // deleted after the recent commit (unstaged deletion)
        // removed after staging (unstaged deletion)


        boolean fileExists = join(CWD, file).exists();
        return (currentCommit.getSnapshot().hasEntry(file) && !fileExists) ||
                (getIndexInstance().hasEntry(file) && !fileExists);
    }

    public boolean isModified(GitletCommitObj currentCommit, String file) {
        // changed since the recent commit (unstaged modification)
        // changed after staging (unstaged modification)

        checkFileExists(file);
        checkFileIsTracked(this, file);


        GitletIndex currentIndex = getIndexInstance();

        String fileIdCommit = currentCommit.getSnapshot().getIndexEntry(file); // version from the commit
        String fileIdIndex = currentIndex.getIndexEntry(file); // version from the index
        String fileIdWorkingTree = hashFileObject(file); // version from the working tree



        if (fileIdCommit == null) {
           return !fileIdWorkingTree.equals(fileIdIndex);
        } else {
            return !fileIdWorkingTree.equals(fileIdCommit);
        }
    }


    public boolean hasEntry(String file) {
        return INDEX.containsKey(file);
    }

    public static void stageFile(GitletCommitObj currentCommit, String file) {
        //checkFileExists(file);
        GitletIndex index = getIndexInstance();

        if (index.isTracked(currentCommit, file)) { // if it's tracked
            if (index.isRemoved(currentCommit, file)) {
              removeFile(currentCommit, file);
            }
            if (index.isModified(currentCommit, file)) { // and also modified
                index.addToIndex(file); // then add it
            } else if (index.hasEntry(file)) { // not modified? but already staged?
                index.removeFromIndex(file);  // remove it since the file is intact
            }
        } else {
            index.addToIndex(file); // untracked, then add it.
        }

    }

    public static void removeFile(GitletCommitObj currentCommit, String file) {

        GitletIndex index = getIndexInstance();
        if (index.isTracked(currentCommit, file)) { // if a file is tracked
            if (index.hasEntry(file)) { // if it's in index
                index.removeFromIndex(file);
            }

            if (currentCommit.getSnapshot().hasEntry(file)) { // if it's in the current commit
                index.updateIndex(file, null);
                File targetFile = join(CWD, file);
                deleteFile(targetFile);
            }
        } else {
            System.err.println("No reason to remove the file.");
            System.exit(0);
        }

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
    private void addToIndex(String file) {
        File sourceFile = join(CWD, file);
        String hash = hashFileObject(file);
        File targetFile = createObjectFile("blob", hash);
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
    public boolean hasStagedFiles() {
        return !INDEX.isEmpty();
    }


    public HashMap<String, String> getIndexPair() {
        return (HashMap<String, String>) INDEX.clone();
    }
}
