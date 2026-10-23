import java.util.LinkedList;
import java.util.List;

public class CharacterManager {

    private final List<GameCharacter> heroes;

    public CharacterManager() {
        heroes = new LinkedList<>();
    }

    /** Adds a hero unless the name is blank or already used. */
    public boolean addCharacter(GameCharacter character) {
        if (character == null || character.getName().trim().isEmpty()) {
            return false;
        }

        if (findByName(character.getName()) != null) {
            return false;
        }

        heroes.add(character);
        return true;
    }

    /** Returns a copy of the team list. */
    public List<GameCharacter> getAllCharacters() {
        return new LinkedList<>(heroes);
    }

    public int getSize() {
        return heroes.size();
    }

    public boolean isEmpty() {
        return heroes.isEmpty();
    }

    /** Searches by name without considering letter case. */
    public GameCharacter findByName(String name) {
        if (name == null) {
            return null;
        }

        String searchName = name.trim();
        for (GameCharacter hero : heroes) {
            if (hero.getName().equalsIgnoreCase(searchName)) {
                return hero;
            }
        }
        return null;
    }

    /** Clears the team when the player chooses New Descent. */
    public void clearCharacters() {
        heroes.clear();
    }
}
