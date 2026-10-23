import java.util.LinkedList;
import java.util.List;
import java.util.Random;

// Controls the rules of the battle separately from the Swing interface.
// The heroes work as a team against one monster.
public class BattleManager {

    private final CharacterManager characterManager;
    private Monster monster;
    private boolean battleStarted;
    private boolean battleOver;
    private boolean victory;

    public BattleManager(CharacterManager characterManager) {
        this.characterManager = characterManager;
        monster = new Monster();
        battleStarted = false;
        battleOver = false;
        victory = false;
    }

    public Monster getMonster() {
        return monster;
    }

    public boolean isBattleStarted() {
        return battleStarted;
    }

    public boolean isBattleOver() {
        return battleOver;
    }

    public boolean isVictory() {
        return victory;
    }

    // Begins a new battle and restores each hero's health and points.
    public List<String> startBattle() {
        List<String> messages = new LinkedList<>();

        if (characterManager.isEmpty()) {
            messages.add("Add at least one hero before starting the battle.");
            return messages;
        }

        monster = new Monster();
        for (GameCharacter hero : characterManager.getAllCharacters()) {
            hero.resetForBattle();
        }

        battleStarted = true;
        battleOver = false;
        victory = false;

        messages.add("");
        messages.add("------------------------------------------------------------");
        messages.add("                    BATTLE BEGINS");
        messages.add("------------------------------------------------------------");
        messages.add(monster.getName() + " appears with " + monster.getHealth() + " HP.");
        messages.add("");
        messages.add("HOW TO PLAY");
        messages.add("- Team Attack: each living hero attacks once, then the monster retaliates.");
        messages.add("- Special Move: select a living hero and use their special ability.");
        messages.add("------------------------------------------------------------");
        return messages;
    }

    // Every living hero attacks once, then the monster takes its turn.
    public List<String> teamAttack() {
        List<String> messages = new LinkedList<>();
        if (!canTakeTurn(messages)) {
            return messages;
        }

        messages.add("");
        messages.add("-------------------- TEAM ATTACK --------------------");
        for (GameCharacter hero : characterManager.getAllCharacters()) {
            if (!hero.isAlive()) {
                messages.add(hero.getName() + " is knocked out and cannot attack.");
                continue;
            }

            int damage = monster.takeDamage(hero.getAttackDamage());
            hero.addPoints(damage);
            messages.add(hero.attack() + " Damage: " + damage
                    + ". Points gained: " + damage + ".");

            if (!monster.isAlive()) {
                finishBattle(messages, true);
                return messages;
            }
        }

        monsterTakesTurn(messages);
        return messages;
    }

    // A selected hero uses its special move, then the monster retaliates.
    public List<String> useSpecialMove(GameCharacter hero) {
        List<String> messages = new LinkedList<>();
        if (!canTakeTurn(messages)) {
            return messages;
        }

        if (hero == null || !characterManager.getAllCharacters().contains(hero)) {
            messages.add("Select a hero from the team first.");
            return messages;
        }

        if (!hero.isAlive()) {
            messages.add(hero.getName() + " is knocked out and cannot use a special move.");
            return messages;
        }

        messages.add("");
        messages.add("-------------------- SPECIAL MOVE --------------------");
        messages.add("Hero: " + hero.getName());

        // A Healer restores health to every living teammate instead of damaging the monster.
        if (hero.getHealingPower() > 0) {
            messages.add(hero.specialMove());
            int totalHealed = 0;

            for (GameCharacter teammate : characterManager.getAllCharacters()) {
                if (teammate.isAlive()) {
                    int healed = teammate.heal(hero.getHealingPower());
                    if (healed > 0) {
                        totalHealed += healed;
                        messages.add(teammate.getName() + " recovered " + healed + " HP.");
                    }
                }
            }

            hero.addPoints(totalHealed);
            if (totalHealed > 0) {
                messages.add(hero.getName() + " gained " + totalHealed
                        + " support points for healing the team.");
            } else {
                messages.add("No health was restored because all living teammates already have full health.");
            }
        } else {
            int damage = monster.takeDamage(hero.getSpecialDamage());
            hero.addPoints(damage);
            messages.add(hero.specialMove() + " Damage: " + damage
                    + ". Points gained: " + damage + ".");

            if (!monster.isAlive()) {
                finishBattle(messages, true);
                return messages;
            }
        }

        monsterTakesTurn(messages);
        return messages;
    }

    // Resets all game state so the user can build a different team. 
    public void resetGame() {
        monster = new Monster();
        battleStarted = false;
        battleOver = false;
        victory = false;
    }

    public int getTeamScore() {
        int total = 0;
        for (GameCharacter hero : characterManager.getAllCharacters()) {
            total += hero.getPoints();
        }
        return total;
    }

    public int getLivingHeroCount() {
        int total = 0;
        for (GameCharacter hero : characterManager.getAllCharacters()) {
            if (hero.isAlive()) {
                total++;
            }
        }
        return total;
    }

    public int getTeamCurrentHealth() {
        int total = 0;
        for (GameCharacter hero : characterManager.getAllCharacters()) {
            total += hero.getHealth();
        }
        return total;
    }

    public int getTeamMaximumHealth() {
        int total = 0;
        for (GameCharacter hero : characterManager.getAllCharacters()) {
            total += hero.getMaxHealth();
        }
        return total;
    }

    private boolean canTakeTurn(List<String> messages) {
        if (!battleStarted) {
            messages.add("Start the battle before choosing an action.");
            return false;
        }
        if (battleOver) {
            messages.add("The match has ended. Choose New Game to play again.");
            return false;
        }
        return true;
    }

    private void monsterTakesTurn(List<String> messages) {
        if (battleOver || !monster.isAlive()) {
            if (!battleOver && !monster.isAlive()) {
                finishBattle(messages, true);
            }
            return;
        }

        GameCharacter target = getRandomLivingHero();
        if (target == null) {
            finishBattle(messages, false);
            return;
        }

        messages.add("");
        messages.add("-------------------- MONSTER'S TURN --------------------");
        int damage = monster.attackDamage();
        int actualDamage = target.takeDamage(damage);
        messages.add(monster.getName() + " attacks " + target.getName()
                + " for " + actualDamage + " damage.");
        messages.add(target.getName() + " HP: " + target.getHealth()
                + "/" + target.getMaxHealth() + ".");

        if (getLivingHeroCount() == 0) {
            finishBattle(messages, false);
        }
    }

    // The monster randomly targets an alive hero.
    private GameCharacter getRandomLivingHero() {
        List<GameCharacter> livingHeroes = new LinkedList<>();
        for (GameCharacter hero : characterManager.getAllCharacters()) {
            if (hero.isAlive()) {
                livingHeroes.add(hero);
            }
        }
        
        if (livingHeroes.isEmpty()) {
            return null;
        }
        
        Random random = new Random();
        return livingHeroes.get(random.nextInt(livingHeroes.size()));
    }

    private void finishBattle(List<String> messages, boolean teamWon) {
        battleOver = true;
        victory = teamWon;

        messages.add("");
        messages.add("============================================================");
        messages.add("                     MATCH RESULT");
        messages.add("============================================================");
        if (teamWon) {
            messages.add("VICTORY! The team defeated " + monster.getName() + ".");
        } else {
            messages.add("DEFEAT! All heroes have been knocked out.");
        }

        messages.add("Final team score: " + getTeamScore() + " points.");
        messages.add("The match is over. Choose New Game to play again.");
        messages.add("============================================================");
    }
}