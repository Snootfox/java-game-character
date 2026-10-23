# Java Game Character

A small Java Swing game. You build a team of heroes and fight **The Shadow Beast**. It is turn-based, so you click the buttons to make the heroes act.

## What it does

- Add up to 4 heroes: Warrior, Mage, Archer, and Healer.
- Each hero has their own HP, attack damage, and special move.
- **Team Attack** makes every living hero attack once. Then the monster attacks back.
- Select a hero and use **Selected Hero: Special** to use their special move.
- The Healer's special restores HP to living teammates instead of damaging the monster.
- Heroes get points for damage dealt. The Healer gets points for HP actually restored.
- The match ends when the monster is defeated or all heroes are knocked out.
- Click **New Game** to reset and play again.
- Blank or duplicate names are not allowed.

## Heroes

| Hero | HP | Attack | Special |
|---|---:|---:|---|
| Warrior | 145 | 20 damage | Shield Bash, 34 damage |
| Mage | 95 | 27 damage | Lightning Storm, 43 damage |
| Archer | 110 | 23 damage | Rain of Arrows, 39 damage |
| Healer | 120 | 13 damage | Healing Wave, heals up to 32 HP per living teammate |

The Shadow Beast has **380 HP** and hits for a random **18-27 damage**. It attacks the living hero with the lowest current HP. Damage and healing are capped so HP does not go past its limits.

## Running it

You need a JDK and an IDE like IntelliJ IDEA or Eclipse. There are no extra libraries to install.

In IntelliJ, open the project, make sure `src` is marked as the source folder, then run `Main.java`.

Or compile from the project folder in a terminal:

```bash
mkdir out
javac -d out src/*.java
java -cp out Main
```

Add your heroes, click **Start Battle**, then use **Team Attack** or pick a hero and use their special. The Action Log shows what happened.

## Files

```text
src/
├── GameCharacter.java
├── Warrior.java
├── Mage.java
├── Archer.java
├── Healer.java
├── CharacterManager.java
├── Monster.java
├── BattleManager.java
└── Main.java
```
