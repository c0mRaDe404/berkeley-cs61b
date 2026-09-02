package gitlet;

import static gitlet.GitletIndex.addToIndex;
import static gitlet.GitletIndex.listFilesFromIndex;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
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
    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args.length < 1) {
          printError("Please enter a command.");
        }

        String firstArg = args[0];

        switch(firstArg) {
            case "init":
                Repository.initRepo();

                break;
            case "add":
                // TODO: check if a user is in a gitlet ininitialized directory, otherwise exit
                // TODO: handle the `add [filename]` command
                // TODO: check not only argument count, but also appropriateness
                addToIndex(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "ls-files":
                listFilesFromIndex();
                break;
            case "hash-object":
               System.out.println(GitletObject.hashFileObject(args[1]));
               break;
            default:
                printError("No command with that name exists.");
                break;
        }
    }
}
