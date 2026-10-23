/** An additional hero who specializes in ranged attacks. */
public class Archer extends GameCharacter {

    public Archer(String name) {
        super(name, 110);
    }

    @Override
    public String attack() {
        return getName() + " fires a precise arrow at the target!";
    }

    @Override
    public int getAttackDamage() {
        return 23;
    }

    @Override
    public String specialMove() {
        return getName() + " unleashes a Rain of Arrows!";
    }

    @Override
    public int getSpecialDamage() {
        return 39;
    }
}
