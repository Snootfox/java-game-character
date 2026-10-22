import java.util.ArrayList;

public class CharacterManager {

    private final ArrayList<GameCharacter> characters;

    public CharacterManager() {
        characters = new ArrayList<>();
    }

    // CREATE: Add a character unless its name is already being used.
    public boolean addCharacter(GameCharacter character) {
        if (character == null || findByName(character.getName()) != null) {
            return false;
        }

        characters.add(character);
        return true;
    }

    // READ: Return a copy so callers cannot directly change our list.
    public ArrayList<GameCharacter> getAllCharacters() {
        return new ArrayList<>(characters);
    }

    // SEARCH: Names are matched without considering letter case.
    public GameCharacter findByName(String name) {
        if (name == null) {
            return null;
        }

        String searchName = name.trim();

        for (GameCharacter character : characters) {
            if (character.getName().equalsIgnoreCase(searchName)) {
                return character;
            }
        }

        return null;
    }

    // UPDATE: Replace a character with the chosen name and type.
    public boolean updateCharacter(
            GameCharacter oldCharacter,
            String newName,
            String newType) {

        if (oldCharacter == null || newName == null || newType == null) {
            return false;
        }

        int index = characters.indexOf(oldCharacter);
        if (index == -1) {
            return false;
        }

        String cleanedName = newName.trim();
        if (cleanedName.isEmpty()) {
            return false;
        }

        GameCharacter characterWithName = findByName(cleanedName);

        if (characterWithName != null && characterWithName != oldCharacter) {
            return false;
        }

        GameCharacter updatedCharacter = createCharacter(cleanedName, newType);
        if (updatedCharacter == null) {
            return false;
        }

        characters.set(index, updatedCharacter);
        return true;
    }

    // DELETE: Remove the specified character from the list.
    public boolean removeCharacter(GameCharacter character) {
        return characters.remove(character);
    }

    // Creates the correct subclass based on the selected type.
    private GameCharacter createCharacter(String name, String type) {
        if ("Warrior".equalsIgnoreCase(type)) {
            return new Warrior(name);
        }

        if ("Mage".equalsIgnoreCase(type)) {
            return new Mage(name);
        }

        if ("Archer".equalsIgnoreCase(type)) {
            return new Archer(name);
        }

        if ("Healer".equalsIgnoreCase(type)) {
            return new Healer(name);
        }

        return null;
    }
}
