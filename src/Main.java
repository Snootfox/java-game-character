import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class Main extends JFrame {

    private static final Color BACKGROUND = new Color(24, 29, 42);
    private static final Color PANEL = new Color(37, 44, 61);
    private static final Color ACCENT = new Color(108, 145, 255);
    private static final Color TEXT = new Color(238, 241, 248);
    private static final Color MUTED = new Color(174, 183, 202);

    private final CharacterManager manager;
    private final DefaultListModel<GameCharacter> listModel;
    private final JList<GameCharacter> characterList;
    private final JTextField nameField;
    private final JComboBox<String> typeBox;
    private final JTextArea outputArea;
    private final JLabel countLabel;

    public Main() {
        manager = new CharacterManager();
        listModel = new DefaultListModel<>();
        characterList = new JList<>(listModel);
        nameField = new JTextField();
        typeBox = new JComboBox<>(new String[]{"Warrior", "Mage", "Archer", "Healer"});
        outputArea = new JTextArea();
        countLabel = new JLabel("0 characters");

        buildInterface();
    }

    private void buildInterface() {
        setTitle("Game Character Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(780, 540));
        setSize(900, 620);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBackground(BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        setContentPane(root);

        // Header
        JPanel header = new JPanel(new BorderLayout(4, 4));
        header.setOpaque(false);

        JLabel title = new JLabel("GAME CHARACTER MANAGER");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Create characters, manage your roster, and test their abilities.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);

        // Left side: character roster
        JPanel rosterPanel = new JPanel(new BorderLayout(8, 8));
        rosterPanel.setBackground(PANEL);
        rosterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(63, 73, 96)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        rosterPanel.setPreferredSize(new Dimension(280, 100));

        JLabel rosterTitle = makeLabel("CHARACTER ROSTER", true);
        characterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        characterList.setBackground(new Color(29, 35, 49));
        characterList.setForeground(TEXT);
        characterList.setSelectionBackground(ACCENT);
        characterList.setSelectionForeground(Color.WHITE);
        characterList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        characterList.setFixedCellHeight(34);
        characterList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane listScroll = new JScrollPane(characterList);
        listScroll.setBorder(BorderFactory.createLineBorder(new Color(63, 73, 96)));

        countLabel.setForeground(MUTED);
        countLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));

        rosterPanel.add(rosterTitle, BorderLayout.NORTH);
        rosterPanel.add(listScroll, BorderLayout.CENTER);
        rosterPanel.add(countLabel, BorderLayout.SOUTH);
        root.add(rosterPanel, BorderLayout.WEST);

        // Right side: input fields, buttons, and action log
        JPanel rightPanel = new JPanel(new BorderLayout(12, 12));
        rightPanel.setOpaque(false);

        JPanel controlsPanel = new JPanel(new BorderLayout(8, 10));
        controlsPanel.setOpaque(false);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        formPanel.setBackground(PANEL);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(63, 73, 96)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        formPanel.add(makeLabel("Character name", false));
        styleTextField(nameField);
        formPanel.add(nameField);

        formPanel.add(makeLabel("Character type", false));
        styleComboBox(typeBox);
        formPanel.add(typeBox);

        controlsPanel.add(formPanel, BorderLayout.NORTH);

        JPanel buttonArea = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonArea.setOpaque(false);

        JButton addButton = makeButton("Add Character", ACCENT);
        JButton searchButton = makeButton("Search", new Color(75, 112, 155));
        JButton updateButton = makeButton("Update Selected", new Color(75, 112, 155));
        JButton removeButton = makeButton("Remove Selected", new Color(164, 75, 87));

        buttonArea.add(addButton);
        buttonArea.add(searchButton);
        buttonArea.add(updateButton);
        buttonArea.add(removeButton);
        controlsPanel.add(buttonArea, BorderLayout.CENTER);

        JPanel combatPanel = new JPanel(new GridLayout(1, 2, 8, 8));
        combatPanel.setOpaque(false);

        JButton attackButton = makeButton("Attack", new Color(52, 132, 108));
        JButton specialButton = makeButton("Special Move", new Color(129, 92, 174));
        combatPanel.add(attackButton);
        combatPanel.add(specialButton);
        controlsPanel.add(combatPanel, BorderLayout.SOUTH);

        rightPanel.add(controlsPanel, BorderLayout.NORTH);

        JPanel logPanel = new JPanel(new BorderLayout(6, 6));
        logPanel.setBackground(PANEL);
        logPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(63, 73, 96)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel logTitle = makeLabel("ACTION LOG", true);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setForeground(TEXT);
        outputArea.setBackground(new Color(29, 35, 49));
        outputArea.setMargin(new Insets(8, 8, 8, 8));
        outputArea.setText("Welcome! Add a Warrior, Mage, Archer, or Healer to begin.\n");

        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createLineBorder(new Color(63, 73, 96)));

        logPanel.add(logTitle, BorderLayout.NORTH);
        logPanel.add(outputScroll, BorderLayout.CENTER);
        rightPanel.add(logPanel, BorderLayout.CENTER);

        root.add(rightPanel, BorderLayout.CENTER);

        // Selecting a character loads its details into the form.
        characterList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                GameCharacter selected = characterList.getSelectedValue();
                if (selected != null) {
                    nameField.setText(selected.getName());
                    typeBox.setSelectedItem(selected.getClass().getSimpleName());
                }
            }
        });

        addButton.addActionListener(event -> addCharacter());
        searchButton.addActionListener(event -> searchCharacter());
        updateButton.addActionListener(event -> updateCharacter());
        removeButton.addActionListener(event -> removeCharacter());
        attackButton.addActionListener(event -> performAction(false));
        specialButton.addActionListener(event -> performAction(true));

        nameField.addActionListener(event -> searchCharacter());
    }

    private JLabel makeLabel(String text, boolean heading) {
        JLabel label = new JLabel(text);
        label.setForeground(heading ? TEXT : MUTED);
        label.setFont(new Font("SansSerif", heading ? Font.BOLD : Font.PLAIN,
                heading ? 13 : 12));
        return label;
    }

    private JButton makeButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setMargin(new Insets(9, 8, 9, 8));
        return button;
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(29, 35, 49));
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(76, 88, 115)),
                BorderFactory.createEmptyBorder(6, 7, 6, 7)
        ));
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(new Color(29, 35, 49));
        comboBox.setForeground(TEXT);
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    // Creates the correct character object based on the chosen type.
    private GameCharacter createCharacter(String name) {
        String type = (String) typeBox.getSelectedItem();

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

        throw new IllegalArgumentException("Unknown character type: " + type);
    }

    private boolean validateName(String name) {
        if (name.isEmpty()) {
            showMessage("Character name cannot be empty or spaces only.");
            return false;
        }

        return true;
    }

    private void addCharacter() {
        String name = nameField.getText().trim();
        if (!validateName(name)) {
            return;
        }

        GameCharacter character = createCharacter(name);
        if (manager.addCharacter(character)) {
            refreshList(character);
            log("Added " + character + ".");
        } else {
            showMessage("A character with that name already exists.");
        }
    }

    private void searchCharacter() {
        String name = nameField.getText().trim();
        if (!validateName(name)) {
            return;
        }

        GameCharacter found = manager.findByName(name);
        if (found == null) {
            showMessage("No character named '" + name + "' was found.");
            return;
        }

        characterList.setSelectedValue(found, true);
        characterList.ensureIndexIsVisible(characterList.getSelectedIndex());
        log("Found " + found + ".");
    }

    private void updateCharacter() {
        GameCharacter selected = characterList.getSelectedValue();
        if (selected == null) {
            showMessage("Select a character from the roster before updating.");
            return;
        }

        String newName = nameField.getText().trim();
        if (!validateName(newName)) {
            return;
        }

        String newType = (String) typeBox.getSelectedItem();
        if (manager.updateCharacter(selected, newName, newType)) {
            GameCharacter updated = manager.findByName(newName);
            refreshList(updated);
            log("Updated character to " + updated + ".");
        } else {
            showMessage("Update failed. Another character may already use that name.");
        }
    }

    private void removeCharacter() {
        GameCharacter selected = characterList.getSelectedValue();
        if (selected == null) {
            showMessage("Select a character from the roster before removing.");
            return;
        }

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Remove " + selected.getName() + " from the roster?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (answer == JOptionPane.YES_OPTION) {
            String removedName = selected.getName();
            manager.removeCharacter(selected);
            refreshList(null);
            log("Removed " + removedName + ".");
        }
    }

    private void performAction(boolean useSpecialMove) {
        GameCharacter selected = characterList.getSelectedValue();
        if (selected == null) {
            showMessage("Select a character before choosing a combat action.");
            return;
        }

        String result;
        if (useSpecialMove) {
            result = selected.specialMove();
        } else {
            result = selected.attack();
        }

        log(result);
    }

    private void refreshList(GameCharacter characterToSelect) {
        listModel.clear();

        for (GameCharacter character : manager.getAllCharacters()) {
            listModel.addElement(character);
        }

        int count = listModel.getSize();
        countLabel.setText(count + (count == 1 ? " character" : " characters"));

        if (characterToSelect != null) {
            characterList.setSelectedValue(characterToSelect, true);
        } else {
            characterList.clearSelection();
            nameField.setText("");
            typeBox.setSelectedItem("Warrior");
        }
    }

    private void log(String message) {
        outputArea.append(message + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Game Character Manager",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
