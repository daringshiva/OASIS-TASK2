import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class ATMSystem {
    private static final Map<Integer, User> users = new HashMap<>();
    private static final Map<Integer, java.util.List<Transaction>> transactionHistory = new HashMap<>();
    private JFrame frame;
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private static final Color SECONDARY_COLOR = new Color(241, 245, 249);
    private static final Color ACCENT_COLOR = new Color(41, 128, 185);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
       
        users.put(1001, new User(1001, 1234, 1000.00));
        transactionHistory.put(1001, new ArrayList<>());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new ATMSystem().createAndShowGUI());
    }

    public void createAndShowGUI() {
        frame = new JFrame("Modern ATM Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(SECONDARY_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Custom header panel with gradient
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_COLOR, getWidth(), getHeight(), ACCENT_COLOR);
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                g2d.dispose();
            }
        };
        headerPanel.setPreferredSize(new Dimension(900, 120));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Modern ATM System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Main content panel with buttons
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(SECONDARY_COLOR);
        
        JButton registerButton = createStyledButton("Register Account");
        JButton loginButton = createStyledButton("Login to Account");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(registerButton, gbc);
        
        gbc.gridx = 1;
        contentPanel.add(loginButton, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Add shadow border to main panel
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1, true)
        ));

        frame.add(mainPanel);
        frame.setVisible(true);

        registerButton.addActionListener(e -> showRegistrationPage());
        loginButton.addActionListener(e -> showLoginPage());
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, PRIMARY_COLOR, getWidth(), getHeight(), ACCENT_COLOR);
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        button.setPreferredSize(new Dimension(250, 60));
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 2, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        textField.setPreferredSize(new Dimension(200, 40));
        return textField;
    }

    private void showLoginPage() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(SECONDARY_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);

        JLabel titleLabel = new JLabel("Login to Your Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);

        JTextField userIdField = createStyledTextField();
        JPasswordField pinField = new JPasswordField(15);
        pinField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 2, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        pinField.setPreferredSize(new Dimension(200, 40));

        JButton loginButton = createStyledButton("Login");
        JButton backButton = createStyledButton("Back to Main Menu");

        // Layout components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(titleLabel, gbc);

        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setFont(LABEL_FONT);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(userIdLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(userIdField, gbc);

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        loginPanel.add(pinLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(pinField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 4;
        loginPanel.add(backButton, gbc);

        updateFrameContent(loginPanel);

        loginButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                int pin = Integer.parseInt(new String(pinField.getPassword()));

                if (authenticateUser(userId, pin)) {
                    showMainMenu(userId);
                } else {
                    showError("Invalid User ID or PIN.");
                }
            } catch (NumberFormatException ex) {
                showError("Please enter valid numeric values.");
            }
        });

        backButton.addActionListener(e -> createAndShowGUI());
    }

    private void showRegistrationPage() {
        JPanel registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(SECONDARY_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);

        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);

        JTextField userIdField = createStyledTextField();
        JPasswordField pinField = new JPasswordField(15);
        pinField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 2, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        pinField.setPreferredSize(new Dimension(200, 40));

        JTextField initialDepositField = createStyledTextField();

        JButton registerButton = createStyledButton("Register");
        JButton backButton = createStyledButton("Back to Main Menu");

        // Layout components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerPanel.add(titleLabel, gbc);

        JLabel userIdLabel = new JLabel("Choose User ID:");
        userIdLabel.setFont(LABEL_FONT);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        registerPanel.add(userIdLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(userIdField, gbc);

        JLabel pinLabel = new JLabel("Choose PIN:");
        pinLabel.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        registerPanel.add(pinLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(pinField, gbc);

        JLabel depositLabel = new JLabel("Initial Deposit ($):");
        depositLabel.setFont(LABEL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        registerPanel.add(depositLabel, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(initialDepositField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerPanel.add(registerButton, gbc);

        gbc.gridy = 5;
        registerPanel.add(backButton, gbc);

        updateFrameContent(registerPanel);

        registerButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText());
                int pin = Integer.parseInt(new String(pinField.getPassword()));
                double initialDeposit = Double.parseDouble(initialDepositField.getText());

                if (initialDeposit < 0) {
                    showError("Initial deposit cannot be negative.");
                    return;
                }

                if (registerUser(userId, pin, initialDeposit)) {
                    JOptionPane.showMessageDialog(frame, 
                        "Registration Successful!\nYour User ID: " + userId, 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                    createAndShowGUI();
                } else {
                    showError("User ID already exists. Please choose a different one.");
                }
            } catch (NumberFormatException ex) {
                showError("Please enter valid numeric values.");
            }
        });

        backButton.addActionListener(e -> createAndShowGUI());
    }

    private void showMainMenu(int userId) {
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBackground(SECONDARY_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        User user = users.get(userId);
        JLabel welcomeLabel = new JLabel("Welcome, User " + userId);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(PRIMARY_COLOR);

        JLabel balanceLabel = new JLabel(String.format("Current Balance: $%.2f", user.getBalance()));
        balanceLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        balanceLabel.setForeground(new Color(46, 204, 113));

        JButton[] buttons = {
            createStyledButton("View Transaction History"),
            createStyledButton("Withdraw Funds"),
            createStyledButton("Deposit Funds"),
            createStyledButton("Transfer Money"),
            createStyledButton("Logout")
        };

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        menuPanel.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        menuPanel.add(balanceLabel, gbc);

        for (int i = 0; i < buttons.length; i++) {
            gbc.gridy = i + 2;
            menuPanel.add(buttons[i], gbc);
        }

        updateFrameContent(menuPanel);

        // Transaction History
        buttons[0].addActionListener(e -> showTransactionHistory(userId));

        // Withdraw
        buttons[1].addActionListener(e -> {
            String amount = JOptionPane.showInputDialog(frame, 
                "Enter amount to withdraw ($):", 
                "Withdraw Funds", 
                JOptionPane.PLAIN_MESSAGE);
            try {
                double withdrawAmount = Double.parseDouble(amount);
                if (withdrawAmount <= 0) {
                    showError("Please enter a positive amount.");
                    return;
                }
                if (withdrawAmount > user.getBalance()) {
                    showError("Insufficient funds.");
                    return;
                }
                performTransaction(userId, "Withdrawal", -withdrawAmount);
                showMainMenu(userId);
            } catch (NumberFormatException ex) {
                showError("Please enter a valid amount.");
            }
        });

        // Deposit
        buttons[2].addActionListener(e -> {
            String amount = JOptionPane.showInputDialog(frame, 
                "Enter amount to deposit ($):", 
                "Deposit Funds", 
                JOptionPane.PLAIN_MESSAGE);
            try {
                double depositAmount = Double.parseDouble(amount);
                if (depositAmount <= 0) {
                    showError("Please enter a positive amount.");
                    return;
                }
                performTransaction(userId, "Deposit", depositAmount);
                showMainMenu(userId);
            } catch (NumberFormatException ex) {
                showError("Please enter a valid amount.");
            }
        });

        // Transfer
        buttons[3].addActionListener(e -> {
            String recipientId = JOptionPane.showInputDialog(frame, 
                "Enter recipient's User ID:", 
                "Transfer Money", 
                JOptionPane.PLAIN_MESSAGE);
            try {
                int recipient = Integer.parseInt(recipientId);
                if (!users.containsKey(recipient)) {
                    showError("Recipient not found.");
                    return;
                }
                if (recipient == userId) {
                    showError("Cannot transfer to yourself.");
                    return;
                }
                String amount = JOptionPane.showInputDialog(frame, 
                    "Enter amount to transfer ($):", 
                    "Transfer Money", 
                    JOptionPane.PLAIN_MESSAGE);
                double transferAmount = Double.parseDouble(amount);
                if (transferAmount <= 0) {
                    showError("Please enter a positive amount.");
                    return;
                }
                if (transferAmount > user.getBalance()) {
                    showError("Insufficient funds.");
                    return;
                }
                performTransaction(userId, "Transfer to " + recipient, -transferAmount);
                performTransaction(recipient, "Transfer from " + userId, transferAmount);
                showMainMenu(userId);
            } catch (NumberFormatException ex) {
                showError("Please enter valid values.");
            }
        });

        // Logout
        buttons[4].addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(frame, 
                "Are you sure you want to logout?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                createAndShowGUI();
            }
        });
    }

    private void showTransactionHistory(int userId) {
        java.util.List<Transaction> transactions = transactionHistory.get(userId);
        
        JPanel historyPanel = new JPanel(new BorderLayout(0, 20));
        historyPanel.setBackground(SECONDARY_COLOR);
        historyPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Transaction History", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);

        // Create table model with custom renderer
        String[] columnNames = {"Date", "Type", "Amount"};
        Object[][] data = new Object[transactions.size()][3];
        
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            data[i][0] = DATE_FORMAT.format(t.getDate());
            data[i][1] = t.getType();
            data[i][2] = String.format("$%.2f", t.getAmount());
        }

        JTable table = new JTable(data, columnNames) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                return c;
            }
        };
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Custom renderer for amount column
        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String amount = (String) value;
                if (amount.startsWith("$-")) {
                    setForeground(new Color(231, 76, 60));
                } else {
                    setForeground(new Color(46, 204, 113));
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        JButton backButton = createStyledButton("Back to Main Menu");
        backButton.addActionListener(e -> showMainMenu(userId));

        historyPanel.add(titleLabel, BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        historyPanel.add(backButton, BorderLayout.SOUTH);

        updateFrameContent(historyPanel);
    }

    private void performTransaction(int userId, String type, double amount) {
        User user = users.get(userId);
        users.put(userId, new User(userId, user.getPin(), user.getBalance() + amount));
        transactionHistory.get(userId).add(new Transaction(type, amount));
    }

    private void updateFrameContent(JPanel panel) {
        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static boolean authenticateUser(int userId, int pin) {
        User user = users.get(userId);
        return user != null && user.getPin() == pin;
    }

    private static boolean registerUser(int userId, int pin, double initialDeposit) {
        if (users.containsKey(userId)) {
            return false;
        }
        users.put(userId, new User(userId, pin, initialDeposit));
        transactionHistory.put(userId, new ArrayList<>());
        return true;
    }

    static class User {
        private final int userId;
        private final int pin;
        private final double balance;

        public User(int userId, int pin, double balance) {
            this.userId = userId;
            this.pin = pin;
            this.balance = balance;
        }

        public int getUserId() { return userId; }
        public int getPin() { return pin; }
        public double getBalance() { return balance; }
    }

    static class Transaction {
        private final String type;
        private final double amount;
        private final Date date;

        public Transaction(String type, double amount) {
            this.type = type;
            this.amount = amount;
            this.date = new Date();
        }

        public String getType() { return type; }
        public double getAmount() { return amount; }
        public Date getDate() { return date; }
    }
}