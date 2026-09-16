package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.GitletBranch.*;
import static gitlet.GitletIndex.clearIndex;
import static gitlet.GitletIndex.getIndexInstance;
import static gitlet.GitletObject.*;
import static gitlet.Utils.getFormattedTime;


public class GitletCommit {

    /**
     * creates a commit with the given message and time
     *
     * @param commitObj
     * @return sha1 message digest
     */
    public static String createCommit(GitletCommitObj commitObj) {
        String commitHash;
        commitHash = Utils.sha(commitObj.getMsg(), commitObj.getTimestamp(),
                commitObj.getSnapshot().indexToString(commitObj));
        createCommit(commitHash, commitObj);
        return commitHash;
    }


    public static GitletIndex getCommitSnapshot(GitletIndex index) {
        GitletCommitObj currentCommitObj = getCurrentCommit();

        if (currentCommitObj == null) {
            return getIndexInstance();
        } else {
            HashMap<String, String> newSnapshot;
            newSnapshot = new HashMap<>(currentCommitObj.getSnapshot().getIndexPair());

            for (Map.Entry<String, String> pair : index.getIndexPair().entrySet()) {
                if (pair.getValue() == null) {
                    newSnapshot.remove(pair.getKey());
                    continue;
                }
                newSnapshot.put(pair.getKey(), pair.getValue());
            }

            return getIndexInstance(newSnapshot);
        }
    }

    /**
     * uses commit hash to create a commit object file
     *
     * @param commitHash
     * @param commitObj
     */
    private static void createCommit(String commitHash, GitletCommitObj commitObj) {
        File targetFile = createObjectFile("commit", commitHash);
        Utils.writeObject(targetFile, commitObj);
    }


    /**
     * makes a commit with all the metadata extracted from the commit object
     *
     * @param commitMsg
     */
    public static void makeCommit(String commitMsg) {

        GitletIndex currentIndex = getIndexInstance(); // fetch the current index object

        // is there some way to prevent multiple index file reads? nvm
        if (!currentIndex.hasStagedFiles()) {
            System.err.println("No changes added to the commit.");
            System.exit(0);
        }

        GitletCommitObj commitObj = GitletCommitObj.createCommitObject(commitMsg,
                getFormattedTime(new Date()),
                currentIndex);

        commitObj.addParent(getBranchId(getCurrentBranch()));
        updateBranch(getCurrentBranch(), createCommit(commitObj));
        clearIndex();
    }


    /**
     * deserialize the commit object
     *
     * @param commitId
     * @return gitlet commit object
     */
    private static GitletCommitObj readCommitObject(String commitId) {
        File commitObjPath = getObjectPath("commit", commitId);
        if (commitObjPath == null) {
            System.err.println("No commit with that id exists.");
            System.exit(0);
        }
        return Utils.readObject(commitObjPath, GitletCommitObj.class);
    }

    /**
     * gets a commit by its commitId
     *
     * @param commitId
     * @return a commit object
     */
    public static GitletCommitObj getCommit(String commitId) {
        /* it doesn't error when the given
          commit is invalid
         */
        if (commitId == null) {
            return null;
        }
        return readCommitObject(commitId);
    }

    /**
     * gets the current commit
     *
     * @return the current commit object
     */
    public static GitletCommitObj getCurrentCommit() {
        String commitId = getBranchId(getCurrentBranch());
        return getCommit(commitId);
    }

    /**
     * prints out what is in commit
     *
     * @param commitId
     * @param commitObj
     */
    public static void showCommit(String commitId, GitletCommitObj commitObj) {
        if (commitObj == null) {
            System.err.println("No commit with that id exists.");
            System.exit(0);
        }

        System.out.println("===");
        System.out.println("commit " + commitId);
        List<String> parents = commitObj.getParents();
        StringBuilder parentString = new StringBuilder();
        if (parents.size() >= 2) {
            for (String parent : parents) {
                parentString.append(parent, 0, 7).append(" ");
            }
            System.out.println("Merge: " + parentString);
        }

        System.out.println("Date: " + commitObj.getTimestamp());
        System.out.println(commitObj.getMsg());
        System.out.println();

    }

    /**
     * prints out what HEAD points to
     *
     */
    public static void showLatestCommit() {
        String commitId = getBranchId(getCurrentBranch());
        showCommit(commitId, getCurrentCommit());
    }


    public static void printLog(String id) {

        String currentId = id;
        GitletCommitObj currentObj = getCommit(id);
        List<String> parent = currentObj.getParents();

        while (!parent.isEmpty()) {
            showCommit(currentId, currentObj);
            currentId = currentObj.getParents().get(0);
            currentObj = getCommit(currentId);
            parent = currentObj.getParents();
        }
        showCommit(currentId, currentObj);
    }

    public static void printGlobalLog() {
        for (File dir : Objects.requireNonNull(getObjDir("commit").listFiles())) {
            if (dir.isDirectory()) {
                for (String file : Objects.requireNonNull(Utils.plainFilenamesIn(dir))) {
                    String commitId = dir.getName() + file;
                    showCommit(commitId, getCommit(commitId));
                }
            }
        }
    }

    public static void findCommit(String msg) {
        boolean found = false; // set to true at least one commit matches
        for (File dir : Objects.requireNonNull(getObjDir("commit").listFiles())) {
            if (dir.isDirectory()) {
                for (String file : Objects.requireNonNull(Utils.plainFilenamesIn(dir))) {
                    String commitId = dir.getName() + file;
                    if (getCommit(commitId).getMsg().equals(msg)) {
                        found = true;
                        System.out.println(commitId);
                    }
                }
            }
        }
        if (!found) {
            System.err.println("Found no commit with that message.");
            System.exit(0);
        }
    }

}





