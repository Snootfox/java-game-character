# Java Game Character Manager

A Java Swing application that improves the original Game Character project. 

## Features

- **Create:** Add a Warrior, Mage, Archer, or Healer.
- **View:** Display all characters in a roster list.
- **Search:** Find a character by name, ignoring uppercase and lowercase differences.
- **Update:** Change a selected character's name or type.
- **Delete:** Remove a selected character after confirming the action.
- **Distinct abilities:** Run an attack or special move unique to each hero type.
- **Input validation:** Reject blank names and duplicate names, and prompt users when a character selection is required.

## Character types and abilities

| Hero type | Normal attack           | Special move |
|---|-------------------------|---|
| Warrior | Swings a mighty sword   | Shield Bash |
| Mage | Casts a fireball        | Lightning Storm |
| Archer | Fires a poisonous arrow | Rain of Arrows |
| Healer | Launches a radiant bolt | Healing Wave |

## Project structure

```text
java-game-character/
├── src/
│   ├── GameCharacter.java
│   ├── Warrior.java
│   ├── Mage.java
│   ├── Archer.java
│   ├── Healer.java
│   ├── CharacterManager.java
│   └── Main.java
├── README.md
└── .gitignore
```

## Requirements

- JDK 8 or later
- IntelliJ IDEA, Eclipse, or another Java IDE

No external libraries or database are required.

## Run in any Java IDE

1. Open the project in your preferred IDE.
2. Ensure all seven Java files are in the same `src` folder and do not have a `package` declaration.
3. Open `Main.java` and click **Run** next to `main()`.

## Run from a terminal

Run these commands from the project root:

```bash
javac -d out src/*.java
java -cp out Main
```

On Windows, these commands work in PowerShell when a JDK is installed and `javac` and `java` are on `PATH`.

## Current limitation

Characters are stored in memory only. Closing the application clears the roster. Saving and loading the roster from a file could be a future improvement.
