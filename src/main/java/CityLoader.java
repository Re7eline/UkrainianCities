import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class CityLoader {
    private static final Logger logger = LoggerFactory.getLogger(CityLoader.class);

    /**
     * Fills the list of cities with predefined city names from a cities.txt file.
     */
    public List<String> loadCities() {
        try {
            InputStream inputStream = CityLoader.class.getResourceAsStream("/cities.txt");
            assert inputStream != null;
            Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String city = line.trim().replace("\"", "").replace(",", "");
                if (!city.isEmpty()) {
                    GameManager.cities.add(city);
                }
            }
            scanner.close();
        } catch (Exception e) {
            logger.error("Error while reading file 'cities.txt': " + e.getMessage());
            System.exit(1);
        }
        return GameManager.cities;
    }
}
