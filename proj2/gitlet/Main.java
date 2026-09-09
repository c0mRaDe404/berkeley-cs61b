package gitlet;

import java.io.IOException;

import static gitlet.GitletBranch.*;
import static gitlet.GitletCommit.*;
import static gitlet.GitletErrorMsg.checkRepoExists;
import static gitlet.GitletIndex.*;
import static gitlet.GitletObject.getObjPathComplete;
import static gitlet.GitletRepository.getRepoStatus;
import static gitlet.GitletRepository.showBranchStatus;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author TODO
 */
public class Main {

    private static void checkFlagsCount(int given, int needed) {
        if (given > needed) {
            printError("Incorrect operands.");
        }
    }

    private static void printError(String msg) {
        System.err.println(msg);
        System.exit(0);
    }

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length < 1) {
            printError("Please enter a command.");
        }

        String firstArg = args[0];

        switch (firstArg) {
            case "init":
                GitletRepository.initRepo();
                break;
            case "add":
                // TODO: check if a user is in a gitlet ininitialized directory, otherwise exit
                // TODO: handle the `add [filename]` command
                // TODO: check not only argument count, but also appropriateness

                checkRepoExists();
                stageFile(getCurrentCommit(), args[1]);
                break;
            case "rm":
                checkRepoExists();
                removeFile(getCurrentCommit(), args[1]);
                break;
            case "branch":
                checkRepoExists();
                if (args.length < 2) {
                    listBranches();
                    System.exit(0);
                }
                createBranch(args[1]);
                break;
            case "rm-branch":

                checkRepoExists();
                removeBranch(args[1]);
                break;
            case "checkout":
                checkRepoExists();
                if (args.length == 2) {
                    checkoutBranch(args[1]);
                } else if (args.length == 3) {
                    assert args[1].equals("--");
                    checkoutFile(getCurrentCommit(), args[2]);
                } else if (args.length == 4) {
                    assert args[2].equals("--");
                    checkoutFile(getCommit(getObjPathComplete("commit", args[1])), args[3]);
                } else {
                    System.err.println("Not supported.");
                }
                break;
            case "log":

                checkRepoExists();
                printLog(getBranchId(getCurrentBranch()));
                break;
            case "global-log":

                checkRepoExists();
                printGlobalLog();
                break;
            case "find":

                checkRepoExists();
                findCommit(args[1]);
                break;
            case "status":

                checkRepoExists();
                getRepoStatus();
                break;
            case "reset":

                checkRepoExists();
                resetBranch(args[1]);
                break;
            case "commit":

                checkRepoExists();
                if (args.length < 2) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                makeCommit(args[1]);
                break;
            case "ls-files":

                checkRepoExists();
                //listIndex();
                System.out.println(getIndexInstance().getIndexPair());
                break;
            case "show":

                checkRepoExists();
                if (args.length < 2) {
                    showLatestCommit();
                } else {
                    String commitId = getObjPathComplete("commit", args[1]);
                    showCommit(commitId, getCommit(commitId));
                }
                break;
            case "hash-object":
                checkRepoExists();
                System.out.println(GitletObject.hashFileObject(args[1]));
                break;
            default:
                printError("No command with that name exists.");
                break;
        }
    }
}
