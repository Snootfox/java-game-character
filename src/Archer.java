public class Archer extends GameCharacter {

    public Archer(String name) {
        super(name);
    }

    @Override
    public String attack() {
        return getName() + " fires a poisonous arrow at the target!";
    }

    @Override
    public String specialMove() {
        return getName() + " unleashes a Rain of Fire Arrows!";
    }
}
