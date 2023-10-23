package fittrack.storage;

import fittrack.MealList;
import fittrack.UserProfile;
import fittrack.WorkoutList;
import fittrack.data.Meal;
import fittrack.data.Workout;
import fittrack.parser.IllegalValueException;
import java.io.FileNotFoundException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Storage {

    private static final String FILE_DIRECTORY = "data";
    private static final String PROFILE_FILE_PATH = "./data/Profile.txt";
    private static final String MEAL_LIST_FILE_PATH = "./data/mealList.txt";
    private static final String WORKOUT_LIST_FILE_PATH = "./data/workoutList.txt";
    private final File profileFile;
    private final File mealFile;
    private final File workoutFile;
    private Path profilePath;
    private Path mealListPath;
    private Path workoutListPath;


    /**
     * Constructs storage. Creates new file
     * in a directory called data if none exist.
     */
    public Storage() {
        this.profileFile = new File(PROFILE_FILE_PATH);
        this.mealFile = new File(MEAL_LIST_FILE_PATH);
        this.workoutFile = new File(WORKOUT_LIST_FILE_PATH);

        assert profileFile != null;
        assert mealFile != null;
        assert workoutFile != null;

        try {
            File f = new File(FILE_DIRECTORY);

            if (f.mkdir()) {
                System.out.println("Directory created: " + f.getName());
            } else {
                System.out.println("Directory already exists.\n");
            }

            if (!this.profileFile.exists()) {
                profileFile.createNewFile();
            }
            if (!this.mealFile.exists()) {
                mealFile.createNewFile();
            }
            if (!this.workoutFile.exists()) {
                workoutFile.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Failed to create directory and file.");
        }
    }

    /**
     * Saves user profile data into storage
     *
     * @throws IOException error
     */
    public void saveProfile(UserProfile userProfile) throws IOException {
        //TODO write data to file
        FileWriter file = new FileWriter(PROFILE_FILE_PATH);
        file.write(userProfile.toString() + "\n");
        file.close();
    }

    /**
     * Saves meal list into storage
     *
     * @throws IOException error
     */
    public void saveMeals(MealList mealList) throws IOException {
        ArrayList<Meal> mealArr = mealList.getMealList();
        FileWriter file = new FileWriter(MEAL_LIST_FILE_PATH);
        for (Meal m : mealArr) {
            file.write(m.formatToFile() + "\n");
        }
        file.close();
    }

    /**
     * Saves workout list into storage
     *
     * @throws IOException error
     */
    public void saveWorkouts(WorkoutList workoutList) throws IOException {
        ArrayList<Workout> workoutArr = workoutList.getWorkoutList();
        FileWriter file = new FileWriter(WORKOUT_LIST_FILE_PATH);
        for (Workout w : workoutArr) {
            file.write(w.formatToFile() + "\n");
        }
        file.close();
    }

    /**
     * Save all data when exiting
     *
     * @param userProfile profile of user containing personal data
     * @param mealList list of meals
     * @param workoutList list of workouts
     * @throws IOException error
     */
    public void save(UserProfile userProfile, MealList mealList, WorkoutList workoutList)
            throws IOException {
        saveProfile(userProfile);
        saveMeals(mealList);
        saveWorkouts(workoutList);
    }

    /**
     * Loads the {@code UserProfile} data from this profile storage file, and then returns it.
     * Returns an empty {@code UserProfile} if the file does not exist, or is not a regular file.
     *
     * @throws StorageOperationException if there were errors reading and/or converting data from file.
     */
    public UserProfile profileLoad() throws StorageOperationException {
        profilePath = Paths.get(PROFILE_FILE_PATH);
        if (!Files.exists(profilePath) || !Files.isRegularFile(profilePath)) {
            return new UserProfile();
        }

        try {
            return UserProfileDecoder.decodeUserProfile(Files.readAllLines(profilePath));
        } catch (FileNotFoundException fnfe) {
            throw new AssertionError("A non-existent file scenario is already handled earlier.");
        } catch (IOException ioe) {
            throw new StorageOperationException("Error writing to file: " + profilePath);
        } catch (IllegalValueException ive) {
            throw new StorageOperationException("File contains illegal data values; data type constraints not met");
        }
    }

    /**
     * Loads the {@code MealList} data from this meal list storage file, and then returns it.
     * Returns an empty {@code MealList} if the file does not exist, or is not a regular file.
     *
     * @throws StorageOperationException if there were errors reading and/or converting data from file.
     */
    public MealList mealLoad() throws StorageOperationException {
        mealListPath = Paths.get(MEAL_LIST_FILE_PATH);
        if (!Files.exists(mealListPath) || !Files.isRegularFile(mealListPath)) {
            return new MealList();
        }

        try {
            return MealListDecoder.decodeMealList(Files.readAllLines(mealListPath));
        } catch (FileNotFoundException fnfe) {
            throw new AssertionError("A non-existent file scenario is already handled earlier.");
        } catch (IOException ioe) {
            throw new StorageOperationException("Error writing to file: " + mealListPath);
        } catch (IllegalValueException ive) {
            throw new StorageOperationException("File contains illegal data values; data type constraints not met");
        }
    }

    /**
     * Loads the {@code WorkoutList} data from this workout list storage file, and then returns it.
     * Returns an empty {@code WorkoutList} if the file does not exist, or is not a regular file.
     *
     * @throws StorageOperationException if there were errors reading and/or converting data from file.
     */
    public WorkoutList workoutLoad() throws StorageOperationException {
        workoutListPath = Paths.get(WORKOUT_LIST_FILE_PATH);
        if (!Files.exists(workoutListPath) || !Files.isRegularFile(workoutListPath)) {
            return new WorkoutList();
        }

        try {
            return WorkoutListDecoder.decodeWorkoutList(Files.readAllLines(workoutListPath));
        } catch (FileNotFoundException fnfe) {
            throw new AssertionError("A non-existent file scenario is already handled earlier.");
        } catch (IOException ioe) {
            throw new StorageOperationException("Error writing to file: " + workoutListPath);
        } catch (IllegalValueException ive) {
            throw new StorageOperationException("File contains illegal data values; data type constraints not met");
        }
    }

    /**
     * Checks if the profile file contains any profile settings if
     * the file exists. If no data, program will prompt user to input
     * profile data.
     *
     * @return true if file is empty and false otherwise
     */
    public boolean isProfileFileEmpty() {
        try (FileReader reader = new FileReader(profileFile)) {
            int data = reader.read();
            return data == -1; // Returns true if the file is empty
        } catch (IOException e) {
            System.out.println(e.getMessage());;
            return false; // Consider it non-empty if there's an exception
        }
    }

    /**
     * Signals that some error has occured while trying to convert and read/write
     * data between the application and the storage file.
     */
    public static class StorageOperationException extends Exception {
        public StorageOperationException(String message) {
            super(message);
        }
    }
}
