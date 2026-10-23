import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class Main extends JFrame {

    private static final int MAX_HEROES = 4;

    private static final Color BACKGROUND = new Color(20, 22, 36);
    private static final Color PANEL = new Color(31, 35, 56);
    private static final Color ACCENT = new Color(115, 126, 230);
    private static final Color TEXT = new Color(241, 243, 255);
    private static final Color MUTED = new Color(170, 177, 200);
    private static final Color LOG_GREEN = new Color(130, 230, 150);
    private static final Color SHELL_IVORY = new Color(228, 231, 255);
    private static final Color BLIGHT_ORANGE = new Color(220, 95, 112);

    private final CharacterManager characterManager;
    private final BattleManager battleManager;

    private final DefaultListModel<GameCharacter> listModel;
    private final JList<GameCharacter> characterList;
    private final JTextField nameField;
    private final JComboBox<String> typeBox;
    private final JTextArea outputArea;
    private final JLabel countLabel;
    private final JLabel teamHealthLabel;
    private final JLabel scoreLabel;
    private final JLabel monsterTitleLabel;
    private final JLabel statusLabel;
    private final JProgressBar monsterHealthBar;

    private JButton addHeroButton;
    private JButton startBattleButton;
    private JButton teamAttackButton;
    private JButton specialMoveButton;
    private JButton newGameButton;

    public Main() {
        characterManager = new CharacterManager();
        battleManager = new BattleManager(characterManager);

        listModel = new DefaultListModel<>();
        characterList = new JList<>(listModel);
        nameField = new JTextField();
        typeBox = new JComboBox<>(new String[]{
                "Warrior", "Mage", "Archer", "Healer"
        });
        outputArea = new JTextArea();
        countLabel = new JLabel("Heroes: 0 / " + MAX_HEROES);
        teamHealthLabel = new JLabel("Team HP: 0 / 0");
        scoreLabel = new JLabel("Points: 0");
        monsterTitleLabel = new JLabel();
        statusLabel = new JLabel();
        monsterHealthBar = new JProgressBar();

        buildInterface();
        updateGameDisplay();
        updateButtonStates();
        log("------------------------------------------------------------");
        log("                    TEAM SETUP");
        log("------------------------------------------------------------");
        log("Welcome! Add a Warrior, Mage, Archer, or Healer to begin.");
        log("Build your team, then fight the Shadow Beast to earn points.");
    }

    private void buildInterface() {
        setTitle("Hero Team Battle - Shadow Beast");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 650));
        setSize(1080, 760);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBackground(BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setContentPane(root);

        // Header
        JPanel header = new JPanel(new BorderLayout(4, 4));
        header.setOpaque(false);

        JLabel title = new JLabel("H E R O   T E A M   B A T T L E");
        title.setFont(new Font("Serif", Font.BOLD, 27));
        title.setForeground(SHELL_IVORY);

        JLabel subtitle = new JLabel(
                "Build your team of heroes and defeat the Shadow Beast."
        );
        subtitle.setFont(new Font("Serif", Font.ITALIC, 14));
        subtitle.setForeground(MUTED);

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(14, 14));
        body.setOpaque(false);
        root.add(body, BorderLayout.CENTER);

        buildRosterPanel(body);
        buildRightPanel(body);

        characterList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                GameCharacter selected = characterList.getSelectedValue();
                if (selected != null) {
                    nameField.setText(selected.getName());
                    typeBox.setSelectedItem(selected.getClass().getSimpleName());
                }
            }
        });

        addHeroButton.addActionListener(event -> addHero());
        startBattleButton.addActionListener(event -> startBattle());
        teamAttackButton.addActionListener(event -> teamAttack());
        specialMoveButton.addActionListener(event -> useSpecialMove());
        newGameButton.addActionListener(event -> startNewGame());
        nameField.addActionListener(event -> addHero());
    }

    private void buildRosterPanel(JPanel body) {
        JPanel rosterPanel = new JPanel(new BorderLayout(8, 8));
        rosterPanel.setBackground(PANEL);
        rosterPanel.setBorder(panelBorder());
        rosterPanel.setPreferredSize(new Dimension(330, 100));

        JLabel rosterTitle = makeLabel("HERO TEAM", true);

        characterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        characterList.setBackground(new Color(17, 21, 30));
        characterList.setForeground(TEXT);
        characterList.setSelectionBackground(SHELL_IVORY);
        characterList.setSelectionForeground(new Color(21, 25, 31));
        characterList.setFont(new Font("SansSerif", Font.PLAIN, 13));
        characterList.setFixedCellHeight(58);
        characterList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane listScroll = new JScrollPane(characterList);
        listScroll.setBorder(BorderFactory.createLineBorder(new Color(58, 66, 73)));

        JPanel teamSummary = new JPanel(new GridLayout(3, 1, 4, 4));
        teamSummary.setOpaque(false);
        countLabel.setForeground(MUTED);
        teamHealthLabel.setForeground(MUTED);
        scoreLabel.setForeground(new Color(211, 190, 130));
        teamSummary.add(countLabel);
        teamSummary.add(teamHealthLabel);
        teamSummary.add(scoreLabel);

        rosterPanel.add(rosterTitle, BorderLayout.NORTH);
        rosterPanel.add(listScroll, BorderLayout.CENTER);
        rosterPanel.add(teamSummary, BorderLayout.SOUTH);
        body.add(rosterPanel, BorderLayout.WEST);
    }

    private void buildRightPanel(JPanel body) {
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setOpaque(false);
        body.add(rightPanel, BorderLayout.CENTER);

        JPanel topControls = new JPanel();
        topControls.setLayout(new BoxLayout(topControls, BoxLayout.Y_AXIS));
        topControls.setOpaque(false);

        JPanel encounterPanel = buildMonsterPanel();
        encounterPanel.setPreferredSize(new Dimension(500, 142));
        encounterPanel.setMinimumSize(new Dimension(300, 142));
        encounterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 142));
        topControls.add(encounterPanel);
        topControls.add(Box.createVerticalStrut(8));

        JPanel setupPanel = buildTeamSetupPanel();
        setupPanel.setPreferredSize(new Dimension(500, 96));
        setupPanel.setMinimumSize(new Dimension(300, 96));
        setupPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
        topControls.add(setupPanel);
        topControls.add(Box.createVerticalStrut(8));

        JPanel actionsPanel = buildBattleButtonsPanel();
        actionsPanel.setPreferredSize(new Dimension(500, 42));
        actionsPanel.setMinimumSize(new Dimension(300, 42));
        actionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        topControls.add(actionsPanel);
        rightPanel.add(topControls, BorderLayout.NORTH);

        JPanel logPanel = new JPanel(new BorderLayout(6, 6));
        logPanel.setBackground(PANEL);
        logPanel.setBorder(panelBorder());

        JLabel logTitle = makeLabel("BATTLE LOG", true);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setForeground(LOG_GREEN);
        outputArea.setBackground(new Color(17, 21, 30));
        outputArea.setMargin(new Insets(8, 8, 8, 8));

        JScrollPane logScroll = new JScrollPane(outputArea);
        logScroll.setBorder(BorderFactory.createLineBorder(new Color(58, 66, 73)));

        logPanel.add(logTitle, BorderLayout.NORTH);
        logPanel.add(logScroll, BorderLayout.CENTER);
        rightPanel.add(logPanel, BorderLayout.CENTER);
    }

    private JPanel buildMonsterPanel() {
        JPanel monsterPanel = new JPanel(new BorderLayout(8, 8));
        monsterPanel.setBackground(PANEL);
        monsterPanel.setLayout(new BorderLayout(8, 8));
        monsterPanel.setBorder(panelBorder());

        monsterTitleLabel.setForeground(SHELL_IVORY);
        monsterTitleLabel.setFont(new Font("Serif", Font.BOLD, 16));

        monsterHealthBar.setStringPainted(true);
        monsterHealthBar.setForeground(BLIGHT_ORANGE);
        monsterHealthBar.setBackground(new Color(14, 17, 25));
        monsterHealthBar.setBorderPainted(false);
        monsterHealthBar.setFont(new Font("SansSerif", Font.BOLD, 11));
        monsterHealthBar.setPreferredSize(new Dimension(400, 19));
        monsterHealthBar.setMinimumSize(new Dimension(150, 19));

        statusLabel.setForeground(MUTED);
        statusLabel.setFont(new Font("Serif", Font.ITALIC, 12));

        JPanel labels = new JPanel(new GridLayout(2, 1, 3, 3));
        labels.setOpaque(false);
        labels.add(monsterTitleLabel);
        labels.add(statusLabel);

        monsterPanel.add(labels, BorderLayout.NORTH);
        monsterPanel.add(monsterHealthBar, BorderLayout.SOUTH);
        return monsterPanel;
    }

    private JPanel buildTeamSetupPanel() {
        JPanel setupPanel = new JPanel(new GridLayout(2, 1, 6, 6));
        setupPanel.setBackground(PANEL);
        setupPanel.setBorder(panelBorder());

        styleTextField(nameField);
        nameField.setColumns(22);
        nameField.setMinimumSize(new Dimension(190, 32));
        nameField.setToolTipText("Enter a name for your hero.");

        styleComboBox(typeBox);

        addHeroButton = makeButton("Add Hero", new Color(76, 89, 170));
        startBattleButton = makeButton("Start Battle", new Color(70, 113, 162));
        Dimension actionButtonSize = new Dimension(132, 34);
        addHeroButton.setPreferredSize(actionButtonSize);
        startBattleButton.setPreferredSize(actionButtonSize);

        JPanel nameRow = new JPanel(new BorderLayout(10, 0));
        nameRow.setOpaque(false);
        JLabel nameLabel = makeLabel("Name", false);
        nameLabel.setPreferredSize(new Dimension(78, 28));
        nameRow.add(nameLabel, BorderLayout.WEST);
        nameRow.add(nameField, BorderLayout.CENTER);
        nameRow.add(addHeroButton, BorderLayout.EAST);

        JPanel typeRow = new JPanel(new BorderLayout(10, 0));
        typeRow.setOpaque(false);
        JLabel typeLabel = makeLabel("Hero Type", false);
        typeLabel.setPreferredSize(new Dimension(78, 28));
        typeRow.add(typeLabel, BorderLayout.WEST);
        typeRow.add(typeBox, BorderLayout.CENTER);
        typeRow.add(startBattleButton, BorderLayout.EAST);

        setupPanel.add(nameRow);
        setupPanel.add(typeRow);
        return setupPanel;
    }

    private JPanel buildBattleButtonsPanel() {
        JPanel battleButtons = new JPanel(new GridLayout(1, 3, 8, 8));
        battleButtons.setOpaque(false);

        teamAttackButton = makeButton("Team Attack", new Color(87, 103, 150));
        teamAttackButton.setToolTipText("Each living hero attacks once, then the Shadow Beast retaliates.");

        specialMoveButton = makeButton("Selected Hero: Special", new Color(104, 83, 157));
        specialMoveButton.setToolTipText("Use the selected hero's special move. The Healer restores team HP.");

        newGameButton = makeButton("New Game", new Color(151, 83, 96));
        newGameButton.setToolTipText("Reset the match and build a new hero team.");

        battleButtons.add(teamAttackButton);
        battleButtons.add(specialMoveButton);
        battleButtons.add(newGameButton);
        return battleButtons;
    }

    private javax.swing.border.Border panelBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(67, 75, 82)),
                BorderFactory.createEmptyBorder(11, 11, 11, 11)
        );
    }

    private JLabel makeLabel(String text, boolean heading) {
        JLabel label = new JLabel(text);
        label.setForeground(heading ? TEXT : MUTED);
        label.setFont(new Font(heading ? "Serif" : "SansSerif",
                heading ? Font.BOLD : Font.PLAIN, heading ? 14 : 12));
        return label;
    }

    private JButton makeButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(new Color(246, 244, 234));
        button.setBackground(color);
        button.setFont(new Font("Serif", Font.BOLD, 13));
        button.setMargin(new Insets(9, 8, 9, 8));
        return button;
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(15, 19, 27));
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(77, 88, 89)),
                BorderFactory.createEmptyBorder(6, 7, 6, 7)
        ));
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(new Color(15, 19, 27));
        comboBox.setForeground(TEXT);
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    // Reads user input and adds a hero to the team before the battle begins.
    private void addHero() {
        if (battleManager.isBattleStarted()) {
            return;
        }

        if (characterManager.getSize() >= MAX_HEROES) {
            showMessage("The team is full. You can add up to four heroes.");
            return;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showMessage("Enter a name before adding a hero.");
            nameField.requestFocusInWindow();
            return;
        }

        GameCharacter hero = createHero(name, (String) typeBox.getSelectedItem());
        if (!characterManager.addCharacter(hero)) {
            showMessage("A hero with that name already exists. Choose a different name.");
            return;
        }

        log(hero.getName() + " joins the team as a "
                + hero.getClass().getSimpleName() + ".");
        refreshRoster(null, true);
        updateGameDisplay();
        updateButtonStates();
    }

    private GameCharacter createHero(String name, String type) {
        if ("Warrior".equals(type)) {
            return new Warrior(name);
        }
        if ("Mage".equals(type)) {
            return new Mage(name);
        }
        if ("Archer".equals(type)) {
            return new Archer(name);
        }
        if ("Healer".equals(type)) {
            return new Healer(name);
        }

        throw new IllegalArgumentException("Unknown hero type: " + type);
    }

    private void startBattle() {
        if (characterManager.isEmpty()) {
            showMessage("Add at least one hero before starting the battle.");
            return;
        }

        appendMessages(battleManager.startBattle());
        refreshRoster(null, false);
        updateGameDisplay();
        updateButtonStates();
    }

    private void teamAttack() {
        appendMessages(battleManager.teamAttack());
        refreshRoster(characterList.getSelectedValue(), false);
        updateGameDisplay();
        updateButtonStates();
    }

    private void useSpecialMove() {
        GameCharacter selectedHero = characterList.getSelectedValue();
        if (selectedHero == null) {
            showMessage("Select a hero from the team before using a special move.");
            return;
        }

        appendMessages(battleManager.useSpecialMove(selectedHero));
        refreshRoster(selectedHero, false);
        updateGameDisplay();
        updateButtonStates();
    }

    private void startNewGame() {
        if (battleManager.isBattleStarted() && !battleManager.isBattleOver()) {
            showMessage("Finish the current match before starting a new game.");
            return;
        }

        characterManager.clearCharacters();
        battleManager.resetGame();
        listModel.clear();
        nameField.setText("");
        typeBox.setSelectedItem("Warrior");
        outputArea.setText("");
        log("------------------------------------------------------------");
        log("                    NEW GAME");
        log("------------------------------------------------------------");
        log("Add up to four heroes to your team.");
        log("Tip: the Healer's Healing Wave restores health to living teammates.");

        updateGameDisplay();
        updateButtonStates();
    }

    private void refreshRoster(GameCharacter heroToSelect, boolean clearInput) {
        listModel.clear();
        for (GameCharacter hero : characterManager.getAllCharacters()) {
            listModel.addElement(hero);
        }

        countLabel.setText("Heroes: " + listModel.getSize() + " / " + MAX_HEROES);

        if (heroToSelect != null) {
            characterList.setSelectedValue(heroToSelect, true);
        } else {
            characterList.clearSelection();
        }

        if (clearInput) {
            nameField.setText("");
            typeBox.setSelectedItem("Warrior");
        }
    }

    private void updateGameDisplay() {
        Monster monster = battleManager.getMonster();
        monsterTitleLabel.setText("MONSTER: " + monster.getName());
        monsterHealthBar.setMaximum(monster.getMaxHealth());
        monsterHealthBar.setValue(monster.getHealth());
        monsterHealthBar.setString("HP: " + monster.getHealth() + " / " + monster.getMaxHealth());

        teamHealthLabel.setText("Team HP: " + battleManager.getTeamCurrentHealth()
                + " / " + battleManager.getTeamMaximumHealth()
                + " | Standing: " + battleManager.getLivingHeroCount());
        scoreLabel.setText("TOTAL POINTS: " + battleManager.getTeamScore());
        countLabel.setText("Heroes: " + characterManager.getSize() + " / " + MAX_HEROES);

        if (battleManager.isBattleOver()) {
            if (battleManager.isVictory()) {
                statusLabel.setText("VICTORY - The Shadow Beast has been defeated.");
                statusLabel.setForeground(LOG_GREEN);
            } else {
                statusLabel.setText("DEFEAT - All heroes have been knocked out.");
                statusLabel.setForeground(new Color(255, 120, 120));
            }
        } else if (battleManager.isBattleStarted()) {
            statusLabel.setText("BATTLE IN PROGRESS - Protect your team.");
            statusLabel.setForeground(new Color(218, 188, 119));
        } else {
            statusLabel.setText("READY - Add heroes, then start the battle.");
            statusLabel.setForeground(MUTED);
        }
    }

    private void updateButtonStates() {
        boolean setupMode = !battleManager.isBattleStarted();
        boolean battleActive = battleManager.isBattleStarted() && !battleManager.isBattleOver();

        boolean roomForHeroes = characterManager.getSize() < MAX_HEROES;
        addHeroButton.setEnabled(setupMode && roomForHeroes);
        nameField.setEnabled(setupMode && roomForHeroes);
        typeBox.setEnabled(setupMode && roomForHeroes);
        startBattleButton.setEnabled(setupMode && !characterManager.isEmpty());
        teamAttackButton.setEnabled(battleActive);
        specialMoveButton.setEnabled(battleActive);
        newGameButton.setEnabled(setupMode || battleManager.isBattleOver());
    }

    private void appendMessages(List<String> messages) {
        for (String message : messages) {
            log(message);
        }
    }

    private void log(String message) {
        outputArea.append(message + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Hero Team Battle", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
