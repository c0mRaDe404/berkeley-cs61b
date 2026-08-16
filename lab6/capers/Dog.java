package capers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/** Represents a dog that can be serialized.
 * @author Bhuvanesh C
*/
public class Dog implements Serializable {

    /** Folder that dogs live in. */
    static final File DOG_FOLDER = Utils.join(CapersRepository.CAPERS_FOLDER, "dogs");

    /** Age of dog. */
    private int age;
    /** Breed of dog. */
    private String breed;
    /** Name of dog. */
    private String name;

    /**
     * Creates a dog object with the specified parameters.
     * @param name Name of dog
     * @param breed Breed of dog
     * @param age Age of dog
     */
    public Dog(String name, String breed, int age) {
        this.age = age;
        this.breed = breed;
        this.name = name;
    }

    /**
     * Reads in and deserializes a dog from a file with name NAME in DOG_FOLDER.
     *
     * @param name Name of dog to load
     * @return Dog read from file
     */
    public static Dog fromFile(String name) {
        File file = Utils.join(DOG_FOLDER, name);
        return Utils.readObject(file, Dog.class);
    }

    /**
     * Saves a dog to a file for future use.
     */

    private static void createDogFile(File file) {

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.err.println("Can't setup the" + file.getPath() + "story");
                }
            } catch (IOException e) {
                System.err.println("IOException:" + e.getMessage());
            }
        }
    }
    public void saveDog() {
       File dog =  Utils.join(DOG_FOLDER, name);
       Dog.createDogFile(dog);
       Utils.writeObject(dog, this);
    }


    /**
     * Increases a dog's age and celebrates!
     */
    public void haveBirthday() {
        age += 1;
        System.out.println(toString());
        System.out.println("Happy birthday! Woof! Woof!");
    }

    @Override
    public String toString() {
        return String.format(
            "Woof! My name is %s and I am a %s! I am %d years old! Woof!",
            name, breed, age);
    }

}
