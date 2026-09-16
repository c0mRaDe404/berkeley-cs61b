package gitlet;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static gitlet.GitletBranch.*;
import static gitlet.GitletCommit.*;
import static gitlet.GitletIndex.*;
import static gitlet.GitletObject.getObjectPath;
import static gitlet.GitletRepository.CWD;
import static gitlet.GitletRepository.deleteFile;
import static gitlet.Utils.*;

public class GitletMerge {

    /**
     * holds nodes-depth mapping and nodes-parents mapping
     *
     */
    public static class CommitGraph {
        private final HashMap<String, Integer> depth;
        private final HashMap<String, List<String>> parentCache;

        CommitGraph() {
            depth = new HashMap<>();
            parentCache = new HashMap<>();
        }

        public HashMap<String, Integer> getDepth() {
            return depth;
        }

        public HashMap<String, List<String>> getParentCache() {
            return parentCache;
        }

        /**
         * print parentCache nicely
         *
         */
        void printParentsFormatted() {
            parentCache.forEach((node, parent) -> {
                System.out.print(node.substring(0, 6) + ":" + parent.stream()
                        .map(s -> s.substring(0, 6)).collect(Collectors.toList()));
                System.out.println();
            });
        }

        /**
         * print depth map nicely
         *
         */
        void printDepthFormatted() {
            depth.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .forEach((n) -> {
                        System.out.println(n.getKey().substring(0, 6) + ":" + n.getValue());
                    });
        }
    }


    /* make it private after testing bruhhhhh */

    /**
     * just a helper class to store commits and their depth
     *
     */
    public static class CommitNode implements Comparable<CommitNode> {
        String commitId;
        Integer depth;

        CommitNode(String commitId, Integer depth) {
            this.commitId = commitId;
            this.depth = depth;
        }

        public String getCommitId() {
            return commitId;
        }

        public Integer getDepth() {
            return depth;
        }

        @Override
        public int compareTo(CommitNode o) {
            return Integer.compare(depth, o.depth);
        }

        @Override
        public String toString() {
            return "[" + commitId.substring(0, 6) + "," + depth + "]";
        }
    }


    /**
     * performs a depth first search to find the depth from the root
     *
     * @param currentBranchId
     * @param targetBranchId
     * @return mapping for nodes with their depth from the root
     */
    public static CommitGraph depthFind(String currentBranchId,
                                        String targetBranchId) {

        Stack<String> visit;

        visit = new Stack<>(); // currently visiting
        CommitGraph cg = new CommitGraph(); // collects depth and caches graph

        /* adding branches to be visited */
        visit.push(currentBranchId);
        visit.push(targetBranchId);

        while (!visit.isEmpty()) {
            /* current vertex being visited */
            String curVertex = visit.peek();

            /* fetch the top branch */
            GitletCommitObj currentCommit = getCommit(curVertex);

            /* parent list of the current commit */
            List<String> parentList = currentCommit.getParents();
            /* store its parents in the cache */
            cg.parentCache.put(curVertex, parentList);

            /* if root */
            if (parentList.isEmpty()) {
                cg.depth.put(curVertex, 0);
                visit.pop();
            } else {
                for (String parent : parentList) {
                    if (!cg.depth.containsKey(parent)) {
                        visit.push(parent);
                    }
                }

                /* after visiting all the parents of the current vertex */
                if (curVertex.equals(visit.peek())) {
                    /* loop through the parent list */
                    /* find the minimum depth parent */
                    /* update the depth of the current vertex */
                    cg.depth.put(curVertex, parentList.stream()
                            .mapToInt(cg.depth::get)
                            .min()
                            .orElseThrow(() -> new GitletException("ParentList is empty.")) - 1);
                    visit.pop();
                }
            }
        }

        return cg;
    }


    /**
     * given a commitId and a commitGraph, returns the path from that commit to the root
     *
     * @param commitId
     * @param commitGraph
     * @return path from the commitId to the root (initial commit)
     */
    public static List<CommitNode> getCommitGraph(
            String commitId, /* usually a branch */
            CommitGraph commitGraph /* parent cache and depth of all nodes */) {

        /* collect commit nodes */
        List<CommitNode> commitNodes = new ArrayList<>();

        /* to visit paths in order (minimum) */
        PriorityQueue<CommitNode> heap = new PriorityQueue<>();

        /* starts from the given node */
        heap.add(new CommitNode(commitId, commitGraph.depth.get(commitId)));


        /* visited set */
        Set<String> seen = new HashSet<>();

        /* basically doing a heap sort to get elements in order (ascending) */
        while (!heap.isEmpty()) {
            CommitNode curNode = heap.remove();

            /* if it's not seen yet, add it to the set */
            if (!seen.contains(curNode.commitId)) {

                seen.add(curNode.commitId); /* added to the seen list */
                commitNodes.add(curNode); /* collecting the nodes */

                /* add the parents of the current node */
                for (String parent : commitGraph.parentCache.get(curNode.commitId)) {
                    CommitNode parentNode = new CommitNode(parent, commitGraph.depth.get(parent));
                    heap.add(parentNode);
                }
            }

        }
        return commitNodes;
    }

    /**
     * helper to commits by generations (depth)
     *
     * @param commitId
     * @param commitGraph
     * @return map of grouped commits by their depth
     */
    public static Map<Integer, Set<String>> generations(
            String commitId,
            CommitGraph commitGraph) {

        /* grouping commits by their depth */
        return getCommitGraph(commitId, commitGraph).stream()
                .collect(Collectors.groupingBy(
                        node -> node.depth,
                        LinkedHashMap::new,
                        Collectors.mapping(node -> node.commitId, Collectors.toSet())
                ));
    }

    /**
     * given two commits, findMergeBase returns the latest common ancestor
     *
     * @param currentCommitId
     * @param targetCommitId
     * @param commitGraph
     * @return the latest common ancestor
     */
    public static String findMergeBase(
            String currentCommitId, // current branch
            String targetCommitId, // target branch
            CommitGraph commitGraph) {

        var current = generations(currentCommitId, commitGraph);
        var target = generations(targetCommitId, commitGraph);


        var iterCurrent = current.entrySet().iterator();
        var iterTarget = target.entrySet().iterator();

        var currentDepth = iterCurrent.next();
        var targetDepth = iterTarget.next();

        while (iterTarget.hasNext() && iterCurrent.hasNext()) {
            if (currentDepth.getKey().equals(targetDepth.getKey())) {
                for (String commit : currentDepth.getValue()) {
                    if (targetDepth.getValue().contains(commit)) {
                        return commit;
                    }
                }
                /* walk down paths at the same time */
                currentDepth = iterCurrent.next();
                targetDepth = iterTarget.next();
            } else if (currentDepth.getKey() > targetDepth.getKey()) {
                targetDepth = iterTarget.next();
            } else {
                currentDepth = iterCurrent.next();
            }
        }

        return null;
    }


    private static boolean handleMerge(
            GitletCommitObj mergeBaseCommitObj,
            GitletCommitObj currentCommitObj,
            GitletCommitObj targetCommitObj) {


        boolean conflict = false;

        conflict |= handleMergeFromBase(
                mergeBaseCommitObj,
                currentCommitObj,
                targetCommitObj);
        conflict |= handleMergeFromTarget(
                mergeBaseCommitObj,
                currentCommitObj,
                targetCommitObj
        );

        return conflict;
    }

    private static boolean handleMergeFromBase(
            GitletCommitObj mergeBaseCommitObj,
            GitletCommitObj currentCommitObj,
            GitletCommitObj targetCommitObj) {


        GitletIndex sFiles = mergeBaseCommitObj.getSnapshot();
        GitletIndex cFiles = currentCommitObj.getSnapshot();
        GitletIndex tFiles = targetCommitObj.getSnapshot();

        boolean conflict = false;

        for (String file : sFiles.getIndexPair().keySet()) {

            String sFile = sFiles.getIndexEntry(file);
            String cFile = cFiles.getIndexEntry(file);
            String tFile = tFiles.getIndexEntry(file);

            if (sFile.equals(cFile)) {
                /* not modified in the current branch */
                if (!sFile.equals(tFile)) {

                    if (tFile == null) {
                        deleteFile(join(CWD, file));
                        removeFile(currentCommitObj, file);
                    } else {
                        /* modified in the target branch */
                        checkoutFile(targetCommitObj, file);
                        stageFile(currentCommitObj, file);
                    }
                }
            } else {
                /* modified in the current branch */
                File curObjPath = getObjectPath(
                        "blob",
                        cFiles
                                .getIndexEntry(file));

                File targetObjPath = getObjectPath(
                        "blob",
                        tFiles
                                .getIndexEntry(file));

                if (!sFile.equals(tFile)) {
                    /* modified in the target branch as well */
                    if (cFile == null) {
                        if (tFile != null) {
                            /* conflict */
                            conflict = true;
                            mergeFiles(join(CWD, file), curObjPath, targetObjPath);
                            stageFile(currentCommitObj, file);

                        }
                    } else {
                        if (tFile == null) {
                            /* conflict */
                            conflict = true;
                            mergeFiles(join(CWD, file), curObjPath, targetObjPath);
                            stageFile(currentCommitObj, file);
                        } else {
                            /* conflict */
                            conflict = true;
                            mergeFiles(join(CWD, file), curObjPath, targetObjPath);
                            stageFile(currentCommitObj, file);
                        }
                    }
                }
            }
        }

        return conflict;
    }


    private static boolean handleMergeFromTarget(
            GitletCommitObj mergeBaseCommitObj,
            GitletCommitObj currentCommitObj,
            GitletCommitObj targetCommitObj) {
            /* checkout the file that are not in the split point
             and not in the current commit.
             */


        GitletIndex sFiles = mergeBaseCommitObj.getSnapshot();
        GitletIndex cFiles = currentCommitObj.getSnapshot();
        GitletIndex tFiles = targetCommitObj.getSnapshot();

        boolean conflict = false;

        Set<String> newFilesTargetBranch = targetCommitObj
                .getSnapshot()
                .getIndexPair()
                .keySet()
                .stream()
                .filter(key -> !mergeBaseCommitObj.getSnapshot().hasEntry(key))
                .collect(Collectors.toSet());

        for (String file : newFilesTargetBranch) {

            File curObjPath = getObjectPath(
                    "blob",
                    cFiles
                            .getIndexEntry(file));

            File targetObjPath = getObjectPath(
                    "blob",
                    tFiles
                            .getIndexEntry(file));

            if (cFiles.hasEntry(file)) {
                if (!cFiles.getIndexEntry(file).equals(tFiles.getIndexEntry(file))) {
                    conflict = true;
                    mergeFiles(join(CWD, file), curObjPath, targetObjPath);
                    stageFile(currentCommitObj, file);
                }
            } else {
                checkoutFile(targetCommitObj, file);
                stageFile(currentCommitObj, file);
            }
        }
        return conflict;
    }

    /**
     * merge two branches
     *
     * @param cBranch
     * @param tBranch
     * @param commitGraph
     */
    public static void mergeBranches(
            String cBranch,
            String tBranch,
            CommitGraph commitGraph) {

        String cbId = getBranchId(cBranch); /* current branch id */
        String tbId = getBranchId(tBranch); /* given branch id */

        boolean conflict = false;

        String mergeBaseId = findMergeBase(cbId, tbId, commitGraph);

        GitletCommitObj mergeBaseCommitObj = getCommit(mergeBaseId);
        GitletCommitObj currentCommitObj = getCommit(cbId);
        GitletCommitObj targetCommitObj = getCommit(tbId);

        if (mergeBaseId.equals(tbId)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        } else if (mergeBaseId.equals(cbId)) {
            checkoutCommit(getCommit(tbId));
            updateBranch(cBranch, tbId);
            clearIndex();
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        } else {

            conflict = handleMerge(
                    mergeBaseCommitObj,
                    currentCommitObj,
                    targetCommitObj);

            makeMergeCommit(cBranch, tBranch, cbId, tbId);
        }

        if (conflict) {
            System.err.println("Encountered a merge conflict.");
            System.exit(0);
        }

    }


    private static void makeMergeCommit(
            String cBranch,
            String tBranch,
            String cbId,
            String tbId) {

        GitletIndex currentIndex = getIndexInstance();

        if (!currentIndex.hasStagedFiles()) {
            System.err.println("No changes added to the commit.");
            System.exit(0);
        }

        GitletCommitObj commitObj = GitletCommitObj.createCommitObject("Merged "
                        + tBranch
                        + " into "
                        + cBranch + ".",
                getFormattedTime(new Date()),
                currentIndex);


        commitObj.addParent(cbId);
        commitObj.addParent(tbId);
        updateBranch(cBranch, createCommit(commitObj));
        clearIndex();
    }


    private static void mergeFiles(
            File file,
            File curObjPath,
            File targetObjPath) {

        String currentContents = "";
        String targetContents = "";

        if (curObjPath != null) {
            currentContents = readContentsAsString(curObjPath);
        }

        if (targetObjPath != null) {
            targetContents = readContentsAsString(targetObjPath);
        }

        StringBuilder merged = new StringBuilder();
        merged.append("<<<<<<< HEAD\n");
        merged.append(currentContents);
        merged.append("=======\n");
        merged.append(targetContents);
        merged.append(">>>>>>>\n");
        writeContents(file, merged.toString());
    }

}
