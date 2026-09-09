package gitlet;

import static gitlet.GitletBranch.*;
import static gitlet.GitletCommit.*;
import static gitlet.GitletErrorMsg.*;
import static gitlet.GitletIndex.*;
import static gitlet.GitletObject.getObjPathComplete;
import static gitlet.GitletStatus.getRepoStatus;

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
                // check if a user is in a gitlet ininitialized directory, otherwise exit
                // handle the `add [filename]` command
                // check not only argument count, but also appropriateness

                checkRepoDoesNotExist();
                checkFileExists(args[1]);
                stageFile(getCurrentCommit(), args[1]);
                break;
            case "rm":
                checkRepoDoesNotExist();
                removeFile(getCurrentCommit(), args[1]);
                break;
            case "branch":
                checkRepoDoesNotExist();
                if (args.length < 2) {
                    listBranches();
                    System.exit(0);
                }
                createBranch(args[1]);
                break;
            case "rm-branch":
                checkRepoDoesNotExist();
                removeBranch(args[1]);
                break;
            case "merge":
                checkRepoDoesNotExist();
                mergeBranch(args[1]);
                break;
            case "checkout":
                checkRepoDoesNotExist();
                if (args.length == 2) {
                    checkoutBranch(args[1]);
                } else if (args.length == 3) {
                    checkOperands(args[1], "--");
                    checkoutFile(getCurrentCommit(), args[2]);
                } else if (args.length == 4) {
                    checkOperands(args[2], "--");
                    String objPath = getObjPathComplete("commit", args[1]);
                    checkCommitExists(objPath);
                    checkoutFile(getCommit(objPath), args[3]);
                } else {
                    System.err.println("Not supported.");
                }
                break;
            case "log":

                checkRepoDoesNotExist();
                printLog(getBranchId(getCurrentBranch()));
                break;
            case "global-log":

                checkRepoDoesNotExist();
                printGlobalLog();
                break;
            case "find":

                checkRepoDoesNotExist();
                findCommit(args[1]);
                break;
            case "status":

                checkRepoDoesNotExist();
                getRepoStatus();
                break;
            case "reset":

                checkRepoDoesNotExist();
                resetBranch(args[1]);
                break;
            case "commit":

                checkRepoDoesNotExist();
                if (args.length < 2 || args[1].isEmpty()) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                makeCommit(args[1]);
                break;
            case "ls-files":

                checkRepoDoesNotExist();
                //listIndex();
                System.out.println(getIndexInstance().getIndexPair());
                break;
            case "show":

                checkRepoDoesNotExist();
                if (args.length < 2) {
                    showLatestCommit();
                } else {
                    String commitId = getObjPathComplete("commit", args[1]);
                    showCommit(commitId, getCommit(commitId));
                }
                break;
            case "hash-object":
                checkRepoDoesNotExist();
                System.out.println(GitletObject.hashFileObject(args[1]));
                break;
            default:
                printError("No command with that name exists.");
                break;
        }
    }
}
