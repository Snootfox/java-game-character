public class Mage extends GameCharacter {

    public Mage(String name) {
        super(name, 95);
    }

    @Override
    public String attack() {
        return getName() + " casts a fireball!";
    }

    @Override
    public int getAttackDamage() {
        return 27;
    }

    @Override
    public String specialMove() {
        return getName() + " uses Lightning Storm!";
    }

    @Override
    public int getSpecialDamage() {
        return 43;
    }
}
