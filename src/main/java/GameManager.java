import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * A simple game where the player and computer take turns naming cities that start with the last letter of the previous city.
 */
public class GameManager {
    private static final List<String> cities = new ArrayList<>();
    private static final Scanner sc = new Scanner(System.in);
    private static final Random random = new Random();

    private static char lastChar;
    private static String lastComputerCity;


    /**
     * The main game loop.
     */

    public void startGame() {
        System.out.println("Hello! Choose your option \nName of city for start the game:\nExit - for close  ");
        fillCities();
        gameLoop();
    }


    private static void gameLoop() {
        while (true) {
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Thanks for the game! Now you know the cities of Ukraine better");
                System.out.println("Exiting...");
                System.exit(0);
            }

            if (input.trim().isEmpty()) {
                System.err.println("You haven't entered a city name. Please enter the name of a city.");
                continue;
            }

            char firstInputChar = input.charAt(0);

            if (lastComputerCity != null && !isInputValid(firstInputChar, lastComputerCity)) {
                System.err.println("You've entered an incorrect city.");
                continue;
            }


            if (!isCityExists(input)) {
                System.err.println("That city does not exist. Please type another city.");
                continue;
            }

            lastChar = getLastChar(input);
            cities.removeIf(c -> c.equalsIgnoreCase(input));

            List<String> availableCities = findCitiesStartingWith(lastChar);
            if (!availableCities.isEmpty()) {
                String city = getRandomCity(availableCities);
                System.out.println("Computer's city: " + city);
                lastComputerCity = city;

                cities.removeIf(c -> c.equalsIgnoreCase(city));
                lastChar = getLastChar(city);
            } else {
                System.out.println("There are no more cities available for this letter.");
                System.out.println("Do you want to play again? (y/n)");
                String restartChoice = sc.nextLine();
                if (restartChoice.equalsIgnoreCase("y")) {
                    restartGame();
                } else {
                    System.out.println("Thanks for playing!");
                    System.exit(0);
                }
            }


            if (cities.isEmpty()) {
                System.out.println("All available cities were used");
                offerRestart();
            } else {
                System.out.println("Enter a city starting with the letter '" + lastChar + "':");
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
    private static boolean isInputValid(char firstInputChar, String lastComputerCity) {
        char lastChar = getLastChar(lastComputerCity);
        char firstInputLower = Character.toLowerCase(firstInputChar);
        return Character.toLowerCase(lastChar) == firstInputLower;
    }

    /**
     * Checks if a city exists in the list of cities.
     *
     * @param city The city name to be checked.
     * @return {@code true} if the city exists, {@code false} otherwise.
     */

    private static boolean isCityExists(String city) {
        return cities.stream().anyMatch(c -> c.equalsIgnoreCase(city));
    }

    /**
     * Gets the last character of a city name, considering specific endings.
     *
     * @param city The city name.
     * @return The last character of the city name.
     */
    private static char getLastChar(String city) {
        for (int i = city.length() - 1; i >= 0; i--) {
            char currentChar = city.charAt(i);
            if (Character.isLetter(currentChar)) {
                switch (currentChar) {
                    case 'ь':
                    case 'й':
                    case 'и':
                    case 'ц':
                        continue;
                    default:
                        return currentChar;
                }
            }
        }

        return ' ';
    }


    /**
     * Finds cities in the list that start with a specific character.
     *
     * @param character The character to search for at the beginning of city names.
     * @return A list of cities starting with the specified character.
     */
//    private static List<String> findCitiesStartingWith(char character) {
//        return cities.stream()
//                .filter(city -> Character.toLowerCase(city.charAt(0)) == Character.toLowerCase(character))
//                .collect(Collectors.toList());
//    }

    private static List<String> findCitiesStartingWith(char character) {
        return cities.stream()
                .filter(city -> Character.toLowerCase(city.charAt(0)) == Character.toLowerCase(character))
                .collect(Collectors.toList());
    }




    /**
     * Gets a random city from a list of cities.
     *
     * @param citiesList The list of cities to choose from.
     * @return A randomly selected city.
     */
    private static String getRandomCity(List<String> citiesList) {
        int randomIndex = random.nextInt(citiesList.size());
        String city = citiesList.get(randomIndex);
        cities.removeIf(c -> c.equalsIgnoreCase(city));
        return city;
    }

    /**
     * Methods for restarting the game.
     */
    private static void restartGame() {
        cities.clear();
        fillCities();
        lastComputerCity = null;
        lastChar = ' ';
        System.out.println("Let's start a new game! Enter a city to begin:");
    }

    private static void offerRestart() {
        System.out.println("No more available cities starting with the letter '" + lastChar + "'.");
        System.out.println("Do you want to play again? (y/n)");
        String restartChoice = sc.nextLine();
        if (restartChoice.equalsIgnoreCase("y")) {
            restartGame();
        } else {
            System.out.println("Thanks for the game! Now you know the cities of Ukraine better");
            System.exit(0);
        }
    }


    /**
     * Fills the list of cities with predefined city names.
     */
    private static void fillCities() {
        try {
            InputStream inputStream = GameManager.class.getResourceAsStream("/cities.txt");
            assert inputStream != null;
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String city = line.trim().replace("\"", "").replace(",", "");
                if (!city.isEmpty()) {
                    cities.add(city);
                }
            }

            scanner.close();
        } catch (Exception e) {
            System.err.println("Ошибка при чтении файла 'cities.txt': " + e.getMessage());
            System.exit(1);
        }
    }

}
