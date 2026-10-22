public class Warrior extends GameCharacter {

    public Warrior(String name) {
        super(name);
    }

    @Override
    public String attack() {
        return getName() + " swings a mighty sword!";
    }

    @Override
    public String specialMove() {
        return getName() + " uses Shield Bash!";
    }
}
