public class Warrior extends GameCharacter {

    public Warrior(String name) {
        super(name, 145);
    }

    @Override
    public String attack() {
        return getName() + " swings a mighty sword!";
    }

    @Override
    public int getAttackDamage() {
        return 20;
    }

    @Override
    public String specialMove() {
        return getName() + " uses Shield Bash!";
    }

    @Override
    public int getSpecialDamage() {
        return 34;
    }
}
