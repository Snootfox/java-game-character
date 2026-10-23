public abstract class GameCharacter {
    private String name;
    private final int maxHealth;
    private int health;
    private int points;

    public GameCharacter(String name, int maxHealth) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Hero name cannot be empty.");
        }
        if (maxHealth <= 0) {
            throw new IllegalArgumentException("Maximum health must be positive.");
        }

        this.name = name.trim();
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.points = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Hero name cannot be empty.");
        }
        this.name = name.trim();
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getPoints() {
        return points;
    }

    public boolean isAlive() {
        return health > 0;
    }

    /** Removes health and returns the amount of damage actually received. */
    public int takeDamage(int damage) {
        if (damage <= 0 || !isAlive()) {
            return 0;
        }

        int actualDamage = Math.min(damage, health);
        health -= actualDamage;
        return actualDamage;
    }

    /** Restores health without exceeding maximum health; returns HP restored. */
    public int heal(int amount) {
        if (amount <= 0 || !isAlive()) {
            return 0;
        }

        int actualHealing = Math.min(amount, maxHealth - health);
        health += actualHealing;
        return actualHealing;
    }

    public void addPoints(int amount) {
        if (amount > 0) {
            points += amount;
        }
    }

    /** Restores a hero's health and points before a new battle. */
    public void resetForBattle() {
        health = maxHealth;
        points = 0;
    }

    // Every hero defines the text and damage for a normal attack.
    public abstract String attack();
    public abstract int getAttackDamage();

    // Every hero defines the text and damage for a special move.
    public abstract String specialMove();
    public abstract int getSpecialDamage();

    // Only support heroes need to override this method.
    public int getHealingPower() {
        return 0;
    }

    @Override
    public String toString() {
        String status = isAlive() ? "Alive" : "KO";
        return getClass().getSimpleName() + " - " + name
                + " | HP " + health + "/" + maxHealth
                + " | Points " + points + " | " + status;
    }
}
