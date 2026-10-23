import java.util.Random;

// The enemy fought by the hero team.
public class Monster {

    private final String name;
    private final int maxHealth;
    private int health;
    private final int minimumDamage;
    private final int maximumDamage;
    private final Random random;

    public Monster() {
        name = "The Shadow Beast";
        maxHealth = 380;
        health = maxHealth;
        minimumDamage = 18;
        maximumDamage = 27;
        random = new Random();
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isAlive() {
        return health > 0;
    }

    // Removes health and returns the damage actually dealt.
    public int takeDamage(int damage) {
        if (damage <= 0 || !isAlive()) {
            return 0;
        }

        int actualDamage = Math.min(damage, health);
        health -= actualDamage;
        return actualDamage;
    }

    // Gives the monster a little attack variation: 18 to 27 damage.
    public int attackDamage() {
        return minimumDamage + random.nextInt(maximumDamage - minimumDamage + 1);
    }

    public void reset() {
        health = maxHealth;
    }
}
