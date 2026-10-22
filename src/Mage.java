public class Mage extends GameCharacter {

    public Mage(String name) {
        super(name);
    }

    @Override
    public String attack() {
        return getName() + " casts a fireball!";
    }

    @Override
    public String specialMove() {
        return getName() + " uses Lightning Storm!";
    }
}
