package capers;

import java.io.File;
import java.io.IOException;

import static capers.Utils.*;

/** A repository for Capers 
 * @author Bhuvanesh C
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 */

public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(CWD, ".capers");


    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */

    static class Story {
       static File story = Utils.join(CAPERS_FOLDER, "story");

       static void create() {
           if (!story.exists()) {
               try {
                   if (!story.createNewFile()) {
                       System.err.println("Can't setup the" + story.getPath() + "story");
                   }
               } catch (IOException e) {
                   System.err.println("IOException:" + e.getMessage());
               }
           }
       }

       static File getStory() {
           return story;
       }

       static String getContents() {
           return Utils.readContentsAsString(story);
       }

       static boolean exists() {
           return story.exists();
       }

       static void write(File f, Object...args) {
           Utils.writeContents(f, args);
       }
    }

    public static void setupPersistence() {
        if(!Dog.DOG_FOLDER.exists() && !Dog.DOG_FOLDER.mkdirs()) {
           System.out.println("Can't setup the" + Dog.DOG_FOLDER.getPath() + " directory!");
        }
        Story.create();
    }



    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) {
        File story = Story.getStory();

        if (!CAPERS_FOLDER.exists()) {
            setupPersistence();
        }

        if (!Story.exists()) {
           Story.create();
        }
        String contents = Story.getContents();
        text = contents + text + '\n';
        Story.write(story, text);
        System.out.print(text);

    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
       Dog dog = new Dog(name, breed, age);
       dog.saveDog();
       System.out.println(dog.toString());
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        Dog dog = Dog.fromFile(name);
        dog.haveBirthday();
        dog.saveDog();
    }
}
