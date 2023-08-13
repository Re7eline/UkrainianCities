/**
 * A simple game where the player and computer take turns naming cities that start with the last letter of the previous city.
 */

public class CityGameApp {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.startGame();
    }
}