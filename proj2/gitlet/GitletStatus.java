package gitlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gitlet.GitletBranch.getBranches;
import static gitlet.GitletBranch.getCurrentBranch;
import static gitlet.GitletCommit.getCommitSnapshot;
import static gitlet.GitletCommit.getCurrentCommit;
import static gitlet.GitletIndex.getIndexInstance;
import static gitlet.GitletRepository.CWD;

public class GitletStatus {

    private static Map<String, List<String>> getIndexStatus(GitletIndex index) {
        Map<String, List<String>> status = new HashMap<>();
        List<String> added = new ArrayList<>();
        List<String> deleted = new ArrayList<>();
        for (Map.Entry<String, String> pair : index.getIndexPair().entrySet()) {
            if (pair.getValue() == null) {
                deleted.add(pair.getKey());
            } else {
                added.add(pair.getKey());
            }
        }
        status.put("added", added);
        status.put("deleted", deleted);
        return status;
    }

    private static Map<String, List<String>> getSnapshotStatus(GitletIndex index,
                                                               GitletCommitObj currentCommit) {


        Map<String, List<String>> status = new HashMap<>();
        List<String> modified = new ArrayList<>();
        List<String> deleted = new ArrayList<>();
        for (String file : getCommitSnapshot(index).getIndexPair().keySet()) {
            if (index.isRemoved(currentCommit, file)) {
                deleted.add(file);
            } else if (index.isModified(currentCommit, file)) {
                modified.add(file);
            }
        }
        status.put("modified", modified);
        status.put("deleted", deleted);
        return status;
    }

    public static List<String> getUntrackedStatus(GitletIndex index,
                                                  GitletCommitObj currentCommit) {
        List<String> untracked = new ArrayList<>();
        for (String file : Utils.plainFilenamesIn(CWD)) {
            if (!index.isTracked(currentCommit, file)) {
                untracked.add(file);
            }
        }
        return untracked;
    }

    public static void showBranchStatus() {
        System.out.println("=== Branches ===");
        String curBranch = getCurrentBranch();
        for (String branch : getBranches()) {
            if (curBranch.equals(branch)) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
        System.out.println();
    }

    public static void showUntrackedStatus() {
        System.out.println("=== Untracked Files ===");
        for (String file : getUntrackedStatus(getIndexInstance(), getCurrentCommit())) {
            System.out.println(file);
        }
        System.out.println();
    }

    public static void showIndexStatus() {
        GitletIndex index = getIndexInstance();
        System.out.println("=== Staged Files ===");
        for (String file : getIndexStatus(index).get("added")) {
            System.out.println(file);
        }

        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String file : getIndexStatus(index).get("deleted")) {
            System.out.println(file);
        }
        System.out.println();
    }

    public static void showSnapshotStatus() {
        GitletIndex index = getIndexInstance();
        GitletCommitObj currentCommit = getCurrentCommit();
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String file : getSnapshotStatus(index, currentCommit).get("modified")) {
            System.out.println(file + " (modified)");
        }
        for (String file : getSnapshotStatus(index, currentCommit).get("deleted")) {
            System.out.println(file + " (deleted)");
        }
        System.out.println();
    }

    public static void getRepoStatus() {

        showBranchStatus();
        showIndexStatus();
        showSnapshotStatus();
        showUntrackedStatus();
    }
}
