import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class BudgetPlannerApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame().setVisible(true);
        });
    }
}

// Custom Colors Class
class AppColors {
    public static final Color PRIMARY = new Color(0, 121, 107); // Teal
    public static final Color ACCENT = new Color(255, 214, 0); // Yellow
    public static final Color BACKGROUND = new Color(248, 249, 250);
    public static final Color CARD_BACKGROUND = Color.WHITE;
    public static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    public static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    public static final Color SUCCESS = new Color(40, 167, 69);
    public static final Color DANGER = new Color(220, 53, 69);
    public static final Color SIDEBAR = new Color(52, 58, 64);
}

// Custom Fonts Class
class AppFonts {
    public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBTITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font SMALL = new Font("Segoe UI", Font.PLAIN, 12);
}

// Custom Button Component
class ModernButton extends JButton {
    private Color backgroundColor;
    private Color hoverColor;
    private boolean isHovered = false;

    public ModernButton(String text, Color bgColor) {
        super(text);
        this.backgroundColor = bgColor;
        this.hoverColor = bgColor.darker();
        setupButton();
    }

    private void setupButton() {
        setFont(AppFonts.BODY);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color currentColor = isHovered ? hoverColor : backgroundColor;
        g2.setColor(currentColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        
        super.paintComponent(g);
        g2.dispose();
    }
}

// Custom Panel with Rounded Corners
class RoundedPanel extends JPanel {
    private int cornerRadius;
    private Color backgroundColor;

    public RoundedPanel(int radius, Color bgColor) {
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        
        super.paintComponent(g);
        g2.dispose();
    }
}

// Login Frame
class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Budget Planner - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(AppColors.BACKGROUND);
    }

    private void createComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Main container
        RoundedPanel mainPanel = new RoundedPanel(15, Color.WHITE);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(700, 400));
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Left side - Logo and tagline
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(AppColors.PRIMARY);
        leftPanel.setBorder(new EmptyBorder(50, 40, 50, 40));

        JLabel appNameLabel = new JLabel("Budget Planner");
        appNameLabel.setFont(AppFonts.TITLE);
        appNameLabel.setForeground(Color.WHITE);
        appNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel taglineLabel = new JLabel("<html><center>Plan your money,<br>secure your future</center></html>");
        taglineLabel.setFont(AppFonts.BODY);
        taglineLabel.setForeground(Color.WHITE);
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(appNameLabel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(taglineLabel);
        leftPanel.add(Box.createVerticalGlue());

        // Right side - Login form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(60, 40, 60, 40));

        JLabel loginTitle = new JLabel("Welcome Back");
        loginTitle.setFont(AppFonts.TITLE);
        loginTitle.setForeground(AppColors.TEXT_PRIMARY);
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(AppFonts.BODY);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        usernameField = new JTextField();
        usernameField.setFont(AppFonts.BODY);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.TEXT_SECONDARY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Password field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(AppFonts.BODY);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = new JPasswordField();
        passwordField.setFont(AppFonts.BODY);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.TEXT_SECONDARY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        // Buttons
        ModernButton loginBtn = new ModernButton("Login", AppColors.SUCCESS);
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginBtn.addActionListener(e -> handleLogin());

        JLabel signupLabel = new JLabel("<html><a href='#'>Don't have an account? Sign up</a></html>");
        signupLabel.setFont(AppFonts.SMALL);
        signupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openSignupFrame();
            }
        });

        rightPanel.add(loginTitle);
        rightPanel.add(Box.createVerticalStrut(30));
        rightPanel.add(userLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(usernameField);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(passLabel);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(passwordField);
        rightPanel.add(Box.createVerticalStrut(25));
        rightPanel.add(loginBtn);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(signupLabel);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // Add shadow effect
        JPanel shadowPanel = new JPanel();
        shadowPanel.setBackground(new Color(0, 0, 0, 50));
        shadowPanel.setLayout(new BorderLayout());
        shadowPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        shadowPanel.add(mainPanel);

        add(shadowPanel, gbc);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Simple validation (in real app, check against database)
        if (username.length() >= 3) {
            dispose();
            SwingUtilities.invokeLater(() -> new MainFrame(username).setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSignupFrame() {
        dispose();
        SwingUtilities.invokeLater(() -> new SignupFrame().setVisible(true));
    }
}

// Signup Frame
class SignupFrame extends JFrame {
    private JTextField usernameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;

    public SignupFrame() {
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("Budget Planner - Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(AppColors.BACKGROUND);
    }

    private void createComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        RoundedPanel mainPanel = new RoundedPanel(15, Color.WHITE);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(700, 450));

        // Left side
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(AppColors.PRIMARY);
        leftPanel.setBorder(new EmptyBorder(50, 40, 50, 40));

        JLabel appNameLabel = new JLabel("Join Budget Planner");
        appNameLabel.setFont(AppFonts.TITLE);
        appNameLabel.setForeground(Color.WHITE);
        appNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(appNameLabel);
        leftPanel.add(Box.createVerticalGlue());

        // Right side - Signup form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel signupTitle = new JLabel("Create Account");
        signupTitle.setFont(AppFonts.TITLE);
        signupTitle.setForeground(AppColors.TEXT_PRIMARY);
        signupTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form fields
        usernameField = createFormField("Username");
        emailField = createFormField("Email");
        passwordField = createPasswordField("Password");
        confirmPasswordField = createPasswordField("Confirm Password");

        ModernButton signupBtn = new ModernButton("Sign Up", AppColors.PRIMARY);
        signupBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        signupBtn.addActionListener(e -> handleSignup());

        JLabel loginLabel = new JLabel("<html><a href='#'>Already have an account? Login</a></html>");
        loginLabel.setFont(AppFonts.SMALL);
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
            }
        });

        rightPanel.add(signupTitle);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(createFieldWithLabel("Username", usernameField));
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(createFieldWithLabel("Email", emailField));
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(createFieldWithLabel("Password", passwordField));
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(createFieldWithLabel("Confirm Password", confirmPasswordField));
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(signupBtn);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(loginLabel);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel, gbc);
    }

    private JTextField createFormField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(AppFonts.BODY);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.TEXT_SECONDARY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setFont(AppFonts.BODY);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.TEXT_SECONDARY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private JPanel createFieldWithLabel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(AppFonts.BODY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);

        return panel;
    }

    private void handleSignup() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords don't match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

// Budget Record Class
class BudgetRecord {
    private String date;
    private double income;
    private double food;
    private double rent;
    private double travel;
    private double study;
    private double entertainment;
    private double totalExpenses;
    private double savings;

    public BudgetRecord(double income, double food, double rent, double travel, double study, double entertainment) {
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.income = income;
        this.food = food;
        this.rent = rent;
        this.travel = travel;
        this.study = study;
        this.entertainment = entertainment;
        this.totalExpenses = food + rent + travel + study + entertainment;
        this.savings = income - totalExpenses;
    }

    // Constructor for file import
    public BudgetRecord(String date, double income, double food, double rent, double travel, double study, double entertainment) {
        this.date = date;
        this.income = income;
        this.food = food;
        this.rent = rent;
        this.travel = travel;
        this.study = study;
        this.entertainment = entertainment;
        this.totalExpenses = food + rent + travel + study + entertainment;
        this.savings = income - totalExpenses;
    }

    // Getters
    public String getDate() { return date; }
    public double getIncome() { return income; }
    public double getFood() { return food; }
    public double getRent() { return rent; }
    public double getTravel() { return travel; }
    public double getStudy() { return study; }
    public double getEntertainment() { return entertainment; }
    public double getTotalExpenses() { return totalExpenses; }
    public double getSavings() { return savings; }
}

// Main Application Frame
class MainFrame extends JFrame {
    private int userId; // New field to store the logged-in user's ID
    private String username;
    // ... other fields ...

    public MainFrame(int userId, String username) { // Modified constructor
        this.userId = userId;
        this.username = username;
        this.budgetRecords = new ArrayList<>(); // You'll replace this with DB data
        initializeFrame();
        createComponents();
        showDashboard();
    }
    // ...
}
    private void initializeFrame() {
        setTitle("Budget Planner - " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    private void createComponents() {
        setLayout(new BorderLayout());

        // Create content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(AppColors.BACKGROUND);

        add(createSidebar(), BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Add all panels
        contentPanel.add(createDashboardPanel(), "dashboard");
        contentPanel.add(createAddBudgetPanel(), "addBudget");
        contentPanel.add(createRecordsPanel(), "records");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppColors.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        // App logo and name
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.X_AXIS));
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel appName = new JLabel("Budget Planner");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        appName.setForeground(Color.WHITE);

        logoPanel.add(appName);

        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(30));

        // Navigation buttons
        dashboardBtn = createSidebarButton("Dashboard");
        addBudgetBtn = createSidebarButton("Add Budget");
        recordsBtn = createSidebarButton("Records");

        dashboardBtn.addActionListener(e -> showDashboard());
        addBudgetBtn.addActionListener(e -> showAddBudget());
        recordsBtn.addActionListener(e -> showRecords());

        sidebar.add(dashboardBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(addBudgetBtn);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(recordsBtn);

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(AppFonts.BODY);
        btn.setForeground(Color.WHITE);
        btn.setBackground(AppColors.SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(AppColors.PRIMARY);
                btn.setContentAreaFilled(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setContentAreaFilled(false);
            }
        });

        return btn;
    }

    // Dashboard Panel
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppColors.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel welcomeLabel = new JLabel("Welcome back, " + username + "!");
        welcomeLabel.setFont(AppFonts.TITLE);
        welcomeLabel.setForeground(AppColors.TEXT_PRIMARY);

        topBar.add(welcomeLabel, BorderLayout.WEST);

        // Main content
        JPanel mainContent = new JPanel(new GridLayout(1, 3, 20, 20));
        mainContent.setBackground(AppColors.BACKGROUND);

        // Quick Summary Card
        RoundedPanel summaryCard = createDashboardCard("Quick Summary", 
            getSummaryText(), AppColors.PRIMARY);

        // Quick Actions
        RoundedPanel actionsCard = new RoundedPanel(15, Color.WHITE);
        actionsCard.setLayout(new BoxLayout(actionsCard, BoxLayout.Y_AXIS));
        actionsCard.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel actionsTitle = new JLabel("Quick Actions");
        actionsTitle.setFont(AppFonts.SUBTITLE);
        actionsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        ModernButton addBudgetQuickBtn = new ModernButton("Add Budget", AppColors.SUCCESS);
        ModernButton viewRecordsQuickBtn = new ModernButton("View Records", AppColors.PRIMARY);
        ModernButton importFileBtn = new ModernButton("Import from File", AppColors.ACCENT);

        addBudgetQuickBtn.addActionListener(e -> showAddBudget());
        viewRecordsQuickBtn.addActionListener(e -> showRecords());
        importFileBtn.addActionListener(e -> importFromFile());

        actionsCard.add(actionsTitle);
        actionsCard.add(Box.createVerticalStrut(20));
        actionsCard.add(addBudgetQuickBtn);
        actionsCard.add(Box.createVerticalStrut(10));
        actionsCard.add(viewRecordsQuickBtn);
        actionsCard.add(Box.createVerticalStrut(10));
        actionsCard.add(importFileBtn);

        // Recent Activity
        RoundedPanel activityCard = createDashboardCard("Recent Activity", 
            getRecentActivityText(), AppColors.TEXT_SECONDARY);

        mainContent.add(summaryCard);
        mainContent.add(actionsCard);
        mainContent.add(activityCard);

        panel.add(topBar, BorderLayout.NORTH);
        panel.add(mainContent, BorderLayout.CENTER);

        return panel;
    }

    private void importFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Budget File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }
            
            @Override
            public String getDescription() {
                return "Text files (*.txt)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                importBudgetFromFile(selectedFile);
                JOptionPane.showMessageDialog(this, 
                    "Budget data imported successfully!", 
                    "Import Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
                refreshTableData();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error importing file: " + e.getMessage(), 
                    "Import Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importBudgetFromFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    try {
                        String date = parts[0].trim();
                        double income = Double.parseDouble(parts[1].trim());
                        double food = Double.parseDouble(parts[2].trim());
                        double rent = Double.parseDouble(parts[3].trim());
                        double travel = Double.parseDouble(parts[4].trim());
                        double study = Double.parseDouble(parts[5].trim());
                        double entertainment = Double.parseDouble(parts[6].trim());
                        
                        BudgetRecord record = new BudgetRecord(date, income, food, rent, travel, study, entertainment);
                        budgetRecords.add(record);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid line: " + line);
                    }
                }
            }
        }
    }

    private RoundedPanel createDashboardCard(String title, String content, Color accentColor) {
        RoundedPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppFonts.SUBTITLE);
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel contentLabel = new JLabel("<html><center>" + content + "</center></html>");
        contentLabel.setFont(AppFonts.BODY);
        contentLabel.setForeground(AppColors.TEXT_PRIMARY);
        contentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(15));
        card.add(contentLabel);

        return card;
    }

    private String getSummaryText() {
        if (budgetRecords.isEmpty()) {
            return "No budget records yet.<br>Start by adding your first budget!";
        }

        BudgetRecord latest = budgetRecords.get(budgetRecords.size() - 1);
        return String.format("Latest Savings: $%.2f<br>Total Expenses: $%.2f<br>Income: $%.2f", 
            latest.getSavings(), latest.getTotalExpenses(), latest.getIncome());
    }

    private String getRecentActivityText() {
        if (budgetRecords.isEmpty()) {
            return "No recent activity";
        }
        return "Last budget added: " + budgetRecords.get(budgetRecords.size() - 1).getDate() +
               "<br>Total records: " + budgetRecords.size();
    }

    // Add Budget Panel
    private JPanel createAddBudgetPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppColors.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Add New Budget");
        titleLabel.setFont(AppFonts.TITLE);
        titleLabel.setForeground(AppColors.TEXT_PRIMARY);

        // Form panel
        RoundedPanel formPanel = new RoundedPanel(15, Color.WHITE);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input fields
        JTextField incomeField = createInputField("Income");
        JTextField foodField = createInputField("Food");
        JTextField rentField = createInputField("Rent");
        JTextField travelField = createInputField("Travel");
        JTextField studyField = createInputField("Study");
        JTextField entertainmentField = createInputField("Entertainment");

        // Add fields to form
        addFormField(formPanel, gbc, "Income:", incomeField, 0);
        addFormField(formPanel, gbc, "Food:", foodField, 1);
        addFormField(formPanel, gbc, "Rent:", rentField, 2);
        addFormField(formPanel, gbc, "Travel:", travelField, 3);
        addFormField(formPanel, gbc, "Study:", studyField, 4);
        addFormField(formPanel, gbc, "Entertainment:", entertainmentField, 5);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setOpaque(false);

        ModernButton calculateBtn = new ModernButton("Calculate", AppColors.SUCCESS);
        ModernButton saveBtn = new ModernButton("Save Record", AppColors.PRIMARY);
        ModernButton backBtn = new ModernButton("Back", AppColors.TEXT_SECONDARY);

        // Result panel
        RoundedPanel resultPanel = new RoundedPanel(15, AppColors.BACKGROUND);
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        resultPanel.setVisible(false);

        JLabel resultTitle = new JLabel("Calculation Results");
        resultTitle.setFont(AppFonts.SUBTITLE);
        resultTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel expensesLabel = new JLabel();
        expensesLabel.setFont(AppFonts.BODY);
        expensesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel savingsLabel = new JLabel();
        savingsLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        savingsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel messageLabel = new JLabel();
        messageLabel.setFont(AppFonts.BODY);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        resultPanel.add(resultTitle);
        resultPanel.add(Box.createVerticalStrut(15));
        resultPanel.add(expensesLabel);
        resultPanel.add(Box.createVerticalStrut(10));
        resultPanel.add(savingsLabel);
        resultPanel.add(Box.createVerticalStrut(10));
        resultPanel.add(messageLabel);

        calculateBtn.addActionListener(e -> {
            try {
                double income = parseDouble(incomeField.getText());
                double food = parseDouble(foodField.getText());
                double rent = parseDouble(rentField.getText());
                double travel = parseDouble(travelField.getText());
                double study = parseDouble(studyField.getText());
                double entertainment = parseDouble(entertainmentField.getText());

                double totalExpenses = food + rent + travel + study + entertainment;
                double savings = income - totalExpenses;

                expensesLabel.setText("Total Expenses: $" + String.format("%.2f", totalExpenses));
                savingsLabel.setText("Savings: $" + String.format("%.2f", savings));
                
                if (savings > 0) {
                    savingsLabel.setForeground(AppColors.SUCCESS);
                    messageLabel.setText("Great job! You're saving money!");
                    messageLabel.setForeground(AppColors.SUCCESS);
                } else {
                    savingsLabel.setForeground(AppColors.DANGER);
                    messageLabel.setText("You're overspending! Consider reducing expenses.");
                    messageLabel.setForeground(AppColors.DANGER);
                }

                resultPanel.setVisible(true);
                panel.revalidate();
                panel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Please enter valid numbers", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        saveBtn.addActionListener(e -> {
            try {
                double income = parseDouble(incomeField.getText());
                double food = parseDouble(foodField.getText());
                double rent = parseDouble(rentField.getText());
                double travel = parseDouble(travelField.getText());
                double study = parseDouble(studyField.getText());
                double entertainment = parseDouble(entertainmentField.getText());

                BudgetRecord record = new BudgetRecord(income, food, rent, travel, study, entertainment);
                budgetRecords.add(record);

                JOptionPane.showMessageDialog(panel, "Budget record saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear fields
                incomeField.setText("");
                foodField.setText("");
                rentField.setText("");
                travelField.setText("");
                studyField.setText("");
                entertainmentField.setText("");
                resultPanel.setVisible(false);
                panel.revalidate();
                panel.repaint();
                
                // Refresh table if it exists
                refreshTableData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Please calculate first", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> showDashboard());

        buttonsPanel.add(calculateBtn);
        buttonsPanel.add(saveBtn);
        buttonsPanel.add(backBtn);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(buttonsPanel, gbc);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(resultPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JTextField createInputField(String placeholder) {
        JTextField field = new JTextField(15);
        field.setFont(AppFonts.BODY);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppColors.TEXT_SECONDARY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JTextField field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;

        JLabel label = new JLabel(labelText);
        label.setFont(AppFonts.BODY);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private double parseDouble(String text) throws NumberFormatException {
        if (text.isEmpty()) return 0.0;
        return Double.parseDouble(text);
    }

    // Records Panel
    private JPanel createRecordsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppColors.BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Budget Records");
        titleLabel.setFont(AppFonts.TITLE);
        titleLabel.setForeground(AppColors.TEXT_PRIMARY);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(new EmptyBorder(10, 20, 10, 20));

        ModernButton exportBtn = new ModernButton("Export CSV", AppColors.SUCCESS);
        ModernButton refreshBtn = new ModernButton("Refresh", AppColors.PRIMARY);

        exportBtn.addActionListener(e -> exportToCSV());
        refreshBtn.addActionListener(e -> refreshTableData());

        toolbar.add(refreshBtn);
        toolbar.add(exportBtn);

        // Table
        String[] columns = {"Date", "Income", "Food", "Rent", "Travel", "Study", "Entertainment", "Total Expenses", "Savings"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        recordsTable = new JTable(tableModel);
        recordsTable.setFont(AppFonts.BODY);
        recordsTable.setRowHeight(30);
        recordsTable.setGridColor(AppColors.TEXT_SECONDARY);
        recordsTable.setSelectionBackground(AppColors.PRIMARY);
        recordsTable.setSelectionForeground(Color.WHITE);

        // Populate table
        refreshTableData();

        JScrollPane scrollPane = new JScrollPane(recordsTable);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // File format info panel
        RoundedPanel infoPanel = new RoundedPanel(15, Color.WHITE);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        infoPanel.setPreferredSize(new Dimension(0, 150));

        JLabel infoTitle = new JLabel("File Import Format", SwingConstants.CENTER);
        infoTitle.setFont(AppFonts.SUBTITLE);
        infoTitle.setForeground(AppColors.TEXT_PRIMARY);

        JLabel formatInfo = new JLabel("<html><center>For file import, use this format:<br><br>" +
            "<b>date,income,food,rent,travel,study,entertainment</b><br><br>" +
            "Example: 2024-01-15,3000.00,500.00,1200.00,200.00,300.00,400.00<br>" +
            "Lines starting with # are treated as comments</center></html>");
        formatInfo.setFont(AppFonts.SMALL);
        formatInfo.setForeground(AppColors.TEXT_SECONDARY);

        infoPanel.add(infoTitle);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(formatInfo);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(toolbar, BorderLayout.AFTER_LINE_ENDS);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshTableData() {
        if (tableModel != null) {
            tableModel.setRowCount(0);
            for (BudgetRecord record : budgetRecords) {
                Object[] row = {
                    record.getDate(),
                    String.format("$%.2f", record.getIncome()),
                    String.format("$%.2f", record.getFood()),
                    String.format("$%.2f", record.getRent()),
                    String.format("$%.2f", record.getTravel()),
                    String.format("$%.2f", record.getStudy()),
                    String.format("$%.2f", record.getEntertainment()),
                    String.format("$%.2f", record.getTotalExpenses()),
                    String.format("$%.2f", record.getSavings())
                };
                tableModel.addRow(row);
            }
        }
    }

    private void exportToCSV() {
        if (budgetRecords.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No records to export", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Budget Data");
        fileChooser.setSelectedFile(new File("budget_data.csv"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                // Write header
                writer.println("Date,Income,Food,Rent,Travel,Study,Entertainment,Total Expenses,Savings");
                
                // Write data
                for (BudgetRecord record : budgetRecords) {
                    writer.printf("%s,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f,%.2f%n",
                        record.getDate(),
                        record.getIncome(),
                        record.getFood(),
                        record.getRent(),
                        record.getTravel(),
                        record.getStudy(),
                        record.getEntertainment(),
                        record.getTotalExpenses(),
                        record.getSavings()
                    );
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Data exported successfully to " + file.getName(), 
                    "Export Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error exporting data: " + e.getMessage(), 
                    "Export Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Navigation methods
    private void showDashboard() {
        cardLayout.show(contentPanel, "dashboard");
        highlightSidebarButton(dashboardBtn);
    }

    private void showAddBudget() {
        cardLayout.show(contentPanel, "addBudget");
        highlightSidebarButton(addBudgetBtn);
    }

    private void showRecords() {
        cardLayout.show(contentPanel, "records");
        highlightSidebarButton(recordsBtn);
        refreshTableData(); // Refresh when showing records
    }

    private void highlightSidebarButton(JButton activeButton) {
        JButton[] buttons = {dashboardBtn, addBudgetBtn, recordsBtn};
        
        for (JButton btn : buttons) {
            btn.setBackground(AppColors.SIDEBAR);
            btn.setContentAreaFilled(false);
        }
        
        activeButton.setBackground(AppColors.PRIMARY);
        activeButton.setContentAreaFilled(true);
    }
// Inside the MainFrame class...

// FINAL, CORRECTED DBConnection CLASS

public class DBConnection {
    // Correct URL for Named Instance (SQLEXPRESS)
    private static final String URL = "jdbc:sqlserver://LAPTOP-4JFN9RFR;instanceName=SQLEXPRESS;databaseName=BudgetPlannerDB;user=budgetUser;password=StrongPassword123;encrypt=false;trustServerCertificate=true;";
    private static final String USER = "budgetUser"; 
    private static final String PASSWORD = "StrongPassword123"; 

    public static Connection getConnection() {
        try {
            // Step 1: Load the JDBC Driver Class (essential for some environments)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Step 2: Establish the connection
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to SQL Server!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("❌ SQL Server JDBC Driver not found. Check your classpath.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.err.println("❌ Connection failed! Check URL, user/pass, and SQL Server Browser service.");
            e.printStackTrace();
            return null;
        }
    }
}
public void addTransaction(int userId, String category, double amount, String type) {
    String sql = "INSERT INTO Transactions(user_id, category, amount, type) VALUES (?, ?, ?, ?)";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, userId);
        stmt.setString(2, category);
        stmt.setDouble(3, amount);
        stmt.setString(4, type);
        stmt.executeUpdate();
        
        System.out.println("✅ Transaction added successfully!");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


public void saveBudgetToDatabase(int userId, double income, double food, double rent, double travel, double study, double entertainment) {
    // Note: The SQL is for a single transaction, we'll execute it multiple times as a batch
    String sql = "INSERT INTO Transactions (user_id, category, amount, type, transaction_date) VALUES (?, ?, ?, ?, GETDATE())";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        // 1. Add Income to Batch
        stmt.setInt(1, userId);
        stmt.setString(2, "Income");
        stmt.setDouble(3, income);
        stmt.setString(4, "income");
        stmt.addBatch(); // <-- Add to the batch

        // 2. Add Expenses to Batch
        String[] categories = {"Food", "Rent", "Travel", "Study", "Entertainment"};
        double[] values = {food, rent, travel, study, entertainment};

        for (int i = 0; i < categories.length; i++) {
            // Reset parameters for the next record
            stmt.setInt(1, userId);
            stmt.setString(2, categories[i]);
            stmt.setDouble(3, values[i]);
            stmt.setString(4, "expense");
            stmt.addBatch(); // <-- Add to the batch
        }

        // 3. Execute all statements in the batch
        stmt.executeBatch(); 
        
        System.out.println("✅ Budget and transactions saved successfully in a batch!");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
