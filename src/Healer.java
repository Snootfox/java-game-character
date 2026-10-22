public class Healer extends GameCharacter {

    public Healer(String name) {
        super(name);
    }

    @Override
    public String attack() {
        return getName() + " launches a radiant bolt!";
    }

    @Override
    public String specialMove() {
        return getName() + " casts Healing Wave to restore HP of all allies!";
    }
}
