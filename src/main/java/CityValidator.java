import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CityValidator {
    private static final Logger logger = LoggerFactory.getLogger(CityValidator.class);
    private final Random random = new Random();
    private List<String> cities;

    public CityValidator(List<String> cities) {
        this.cities = cities;
    }

    /**
     * Checks if a city exists in the list of cities.
     *
     * @param city The city name to be checked.
     * @return {@code true} if the city exists, {@code false} otherwise.
     */
    boolean isCityExists(String city) {
        return cities.stream().anyMatch(c -> c.equalsIgnoreCase(city));
    }

    /**
     * Gets the last character of a city name, considering specific endings.
     *
     * @param city The city name.
     * @return The last character of the city name.
     */
    char getLastChar(String city) {
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
     * Finds cities that start with a specific character.
     *
     * @param character The character to search for.
     * @return A list of cities starting with the specified character.
     */
    public List<String> findCitiesStartingWith(char character) {
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
    String getRandomCity(List<String> citiesList) {
        int randomIndex = random.nextInt(citiesList.size());
        String city = citiesList.get(randomIndex);

        citiesList.removeIf(c -> c.equalsIgnoreCase(city));
        cities.removeIf(c -> c.equalsIgnoreCase(city));
        logger.info("Computer's city: " + city);
        GameManager.lastComputerCity = city;
        GameManager.lastChar = getLastChar(city);

        return city;
    }
}
