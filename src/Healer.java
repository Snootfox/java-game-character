public class Healer extends GameCharacter {

    public Healer(String name) {
        super(name, 120);
    }

    @Override
    public String attack() {
        return getName() + " launches a radiant bolt!";
    }

    @Override
    public int getAttackDamage() {
        return 13;
    }

    @Override
    public String specialMove() {
        return getName() + " casts Healing Wave to restore HP of all allies!";
    }

    @Override
    public int getSpecialDamage() {
        return 0;
    }

    @Override
    public int getHealingPower() {
        return 32;
    }
}
