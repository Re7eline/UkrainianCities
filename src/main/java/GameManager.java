import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GameManager {
    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);
    private static final Scanner sc = new Scanner(System.in);
    private final CityValidator cityValidator;
    private final CityLoader cityLoader;
    static String lastComputerCity;
    static char lastChar;
    static List<String> cities = new ArrayList<>();

    /**
     * Constructs a new GameManager instance.
     * Initializes the game by creating instances of CityValidator and CityLoader,
     * and loads the list of cities from a file using the CityLoader.
     */
    public GameManager() {
        cityValidator = new CityValidator(cities);
        cityLoader = new CityLoader();
        cities = cityLoader.loadCities();
    }

    /**
     * Initializes the game by filling the list of cities and starting the game loop.
     */
    public void startGame() {
        logger.info("""
                ↘ Hello! Choose your option ↙
                Name of city for start the game ⌨
                I - CHEATING \uD83D\uDC7F
                Exit ⬅ \s""");
        gameLoop();
    }

    /**
     * The main game loop where players take turns entering city names.
     */
    private void gameLoop() {
        while (true) {
            String input = sc.nextLine();

            switch (input.toLowerCase()) {
                case "exit" -> exitGame();
                case "i" -> showAvailableCities();
                default -> processUserInput(input);
            }
        }
    }

    /**
     * Handles the case where the user's input is empty.
     */
    private void handleEmptyInput() {
        logger.error("You haven't entered a city name. Please enter the name of a city.");
    }

    /**
     * Handles the case where the user's input is incorrect based on the game rules.
     */
    private void handleIncorrectInput() {
        logger.error("You've entered an incorrect city.");
    }

    /**
     * Handles the case where the city entered by the user does not exist.
     */
    private void handleCityNotFound() {
        logger.error("That city does not exist. Please type another city.");
    }

    /**
     * Processes the user's valid input and continues the game.
     *
     * @param input The valid city name entered by the user.
     */
    private void processValidInput(String input) {
        lastChar = cityValidator.getLastChar(input);
        cities.removeIf(c -> c.equalsIgnoreCase(input));
        List<String> availableCities = cityValidator.findCitiesStartingWith(lastChar);

        if (availableCities.isEmpty()) {
            offerRestart();
        } else {
            lastComputerCity = cityValidator.getRandomCity(availableCities);
            lastChar = cityValidator.getLastChar(lastComputerCity);
            logger.info("Enter a city that starts with the letter: '" + Character.toUpperCase(lastChar) + "'");
        }
        if (availableCities.isEmpty()) {
            offerRestart();
        }
    }


    /**
     * Processes the user's input and handles different cases.
     *
     * @param input The user's input.
     */
    private void processUserInput(String input) {
        if (input.trim().isEmpty()) {
            handleEmptyInput();
        } else {
            char firstInputChar = input.charAt(0);

            if (lastComputerCity != null && !isInputValid(firstInputChar, lastComputerCity)) {
                handleIncorrectInput();
            } else if (!cityValidator.isCityExists(input)) {
                handleCityNotFound();
            } else {
                processValidInput(input);
            }
        }
    }

    /**
     * Checks if the input city name starts with the last letter of the computer's city name.
     *
     * @param firstInputChar   The first character of the input city name.
     * @param lastComputerCity The last city chosen by the computer.
     * @return {@code true} if the input city name is valid, {@code false} otherwise.
     */
    private boolean isInputValid(char firstInputChar, String lastComputerCity) {
        char lastChar = cityValidator.getLastChar(lastComputerCity);
        char firstInputLower = Character.toLowerCase(firstInputChar);
        return Character.toLowerCase(lastChar) == firstInputLower;
    }

    /**
     * Exits the game and displays a farewell message.
     */
    private void exitGame() {
        logger.info("Thanks for the game! Now you know the cities of Ukraine better");
        logger.info("Exiting...");
        System.exit(0);
    }

    /**
     * Restarts the game by clearing the cities list and starting anew.
     */
    private void restartGame() {
        cities.clear();
        cities = cityLoader.loadCities();
        lastComputerCity = null;
        lastChar = ' ';
        logger.info("Let's start a new game! Enter a city to begin:");
    }

    /**
     * Offers the player a chance to restart the game or exit.
     */
    private void offerRestart() {
        logger.info("No more available cities starting with the letter '" + lastChar + "'.");
        logger.info("Do you want to play again? (y/n)");
        String restartChoice = sc.nextLine();
        if (restartChoice.equalsIgnoreCase("y")) {
            restartGame();
        } else {
            exitGame();
        }
    }

    /**
     * Displays a list of available cities that start with the last letter chosen by the computer.
     */
    private void showAvailableCities() {
        if (lastChar == ' '|| !Character.isLetter(lastChar)) {
            logger.info("No cities available yet. Enter a city to start the game.");
            return;
        }
        List<String> availableCities = cityValidator.findCitiesStartingWith(lastChar);
        System.out.println("Available cities for the letter '" + Character.toUpperCase(lastChar) + "':");
        availableCities.forEach(System.out::println);
    }

}
