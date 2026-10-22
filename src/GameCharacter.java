public abstract class GameCharacter {

    private String name;

    public GameCharacter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Each character type must provide its own attack and special move.
    public abstract String attack();
    public abstract String specialMove();

    // Displays a description in the GUI character list.
    @Override
    public String toString() {
        return getClass().getSimpleName() + " - " + name;
    }
}
