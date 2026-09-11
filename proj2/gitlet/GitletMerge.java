package gitlet;

import java.util.*;
import java.util.stream.Collectors;

import static gitlet.GitletCommit.getCommit;

public class GitletMerge {

    /* make it private after testing bruh, remember ABSTRACTIONNNNN */
    public static class CommitGraph {
        HashMap<String, Integer> depth;
        HashMap<String, List<String>> parentCache;

        CommitGraph() {
            depth = new HashMap<>();
            parentCache = new HashMap<>();
        }

        void printParentsFormatted() {
            parentCache.forEach((node, parent) -> {
                System.out.print(node.substring(0, 6) + ":" + parent.stream()
                        .map(s -> s.substring(0, 6)).collect(Collectors.toList()));
                System.out.println();
            });
        }

        void printDepthFormatted() {
            depth.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .forEach((n) -> {
                        System.out.println(n.getKey().substring(0, 6) + ":" + n.getValue());
                    });
        }
    }


    /* make it private after testing bruhhhhh */
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


    public static Map<Integer, Set<String>> generations(String commitId, CommitGraph commitGraph) {
        /* grouping commits by their depth */
        return getCommitGraph(commitId, commitGraph).stream()
                .collect(Collectors.groupingBy(
                        node -> node.depth,
                        LinkedHashMap::new,
                        Collectors.mapping(node -> node.commitId, Collectors.toSet())
                ));
    }



   public static String findMergeBase(
           String currentCommitId,
           String targetCommitId,
           CommitGraph commitGraph) {

       var current = generations(currentCommitId, commitGraph);
       var target = generations(targetCommitId, commitGraph);

       var iterCurrent = current.entrySet().iterator();
       var iterTarget = target.entrySet().iterator();

       while (iterTarget.hasNext() && iterCurrent.hasNext()) {
             var currentDepth = iterCurrent.next();
           var targetDepth = iterTarget.next();
            if (currentDepth.getKey().equals(targetDepth.getKey())) {
                    for (String commit: currentDepth.getValue()) {
                       if (targetDepth.getValue().contains(commit)) {
                           return commit;
                       }
                    }
            } else if (currentDepth.getKey() > targetDepth.getKey()) {
                    targetDepth = iterTarget.next();
            } else {
                currentDepth = iterCurrent.next();
            }
       }

       return null;
   }


}
