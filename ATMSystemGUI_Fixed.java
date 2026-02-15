import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import java.io.*;

// ═══════════════════════════════════════════════════════════════════════════
// BACKGROUND PANEL - Draws the ATM Image
// ═══════════════════════════════════════════════════════════════════════════
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel() {
        try {
            URL url = new URL("https://img.freepik.com/free-vector/atm-screen-automated-teller-machine-monitor-illustration_1441-3923.jpg");
            backgroundImage = ImageIO.read(url);
        } catch (IOException e) {
            System.err.println("Could not load background image.");
        }
        setLayout(null); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// LOGIC CLASSES
// ═══════════════════════════════════════════════════════════════════════════
class Transaction {
    private String transactionType;
    private double amount;
    private double balanceAfter;
    private String description;
    private LocalDateTime timestamp;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");

    public Transaction(String transactionType, double amount, double balanceAfter, String description) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public String getFormattedTimestamp() { return timestamp.format(formatter); }
    public String getTransactionType() { return transactionType; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
}

class Account {
    private String userId;
    private String userPin;
    private String accountHolderName;
    private double balance;
    private List<Transaction> transactionHistory;

    public Account(String userId, String userPin, String accountHolderName, double initialBalance) {
        this.userId = userId;
        this.userPin = userPin;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        transactionHistory.add(new Transaction("OPEN", initialBalance, balance, "Account Opened"));
    }

    public String getUserId() { return userId; }
    public String getAccountHolderName() { return accountHolderName; }
    public double getBalance() { return balance; }
    public List<Transaction> getTransactionHistory() { return transactionHistory; }
    public boolean validatePin(String pin) { return this.userPin.equals(pin); }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add(new Transaction("DEPOSIT", amount, balance, "Deposit"));
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactionHistory.add(new Transaction("WITHDRAW", -amount, balance, "Withdrawal"));
            return true;
        }
        return false;
    }

    public boolean transfer(Account recipient, double amount) {
        if (balance >= amount) {
            balance -= amount;
            recipient.deposit(amount);
            transactionHistory.add(new Transaction("TRANSFER", -amount, balance, "To " + recipient.getUserId()));
            return true;
        }
        return false;
    }
}

class Bank {
    private Map<String, Account> accounts = new HashMap<>();
    private String bankName;

    public Bank(String bankName) {
        this.bankName = bankName;
        initializeAccounts();
    }

    private void initializeAccounts() {
        accounts.put("user1", new Account("user1", "1234", "Shivani Chauhan", 5000.00));
        accounts.put("user2", new Account("user2", "5678", "Aditya Jadhav", 7500.00));
        accounts.put("user3", new Account("user3", "9012", "Pranav Bahir", 3200.00));
        accounts.put("admin", new Account("admin", "0000", "Administrator", 10000.00));
    }

    public Account authenticate(String u, String p) {
        Account acc = accounts.get(u);
        if (acc != null && acc.validatePin(p)) return acc;
        return null;
    }
    public Account getAccountById(String id) { return accounts.get(id); }
    public String getBankName() { return bankName; }
}

// ═══════════════════════════════════════════════════════════════════════════
// GUI IMPLEMENTATION
// ═══════════════════════════════════════════════════════════════════════════
class ATMInterface extends JFrame {
    private Bank bank;
    private Account currentAccount;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    // UI Colors
    private final Color SCREEN_BG = new Color(220, 240, 255); 
    private final Color TEXT_COLOR = new Color(0, 50, 100);
    
    // Labels
    private JLabel nameLbl, idLbl, balLbl;

    public ATMInterface(Bank bank) {
        this.bank = bank;
        
        setTitle(bank.getBankName() + " - ATM System");
        setSize(1000, 750); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel bg = new BackgroundPanel();
        
        // --- SCREEN POSITION ---
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(SCREEN_BG);
        cardPanel.setBounds(169, 220, 410, 270); 
        cardPanel.setBorder(null);

        cardPanel.add(createLoginScreen(), "login");
        cardPanel.add(createMainMenuScreen(), "menu");

        bg.add(cardPanel);
        setContentPane(bg);
        setVisible(true);
    }

    // --- SCREEN 1: LOGIN ---
    private JPanel createLoginScreen() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(SCREEN_BG);
        p.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

        // Header
        JLabel title = new JLabel("<html><center><b>WELCOME TO<br>ATM BANKING</b></center></html>", SwingConstants.CENTER);
        title.setForeground(TEXT_COLOR);
        title.setFont(new Font("Arial", Font.PLAIN, 16));
        p.add(title, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(SCREEN_BG);
        form.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel uLbl = new JLabel("User ID:");
        uLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextField userTxt = new JTextField();
        userTxt.setMaximumSize(new Dimension(300, 30));
        
        JLabel pLbl = new JLabel("PIN:");
        pLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPasswordField passTxt = new JPasswordField();
        passTxt.setMaximumSize(new Dimension(300, 30));

        // --- BUTTON FIX ---
        JButton loginBtn = new JButton("LOGIN");
        
        // Force Blue Color
        loginBtn.setBackground(new Color(0, 122, 255)); // Bright Blue
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 12));
        
        // These settings are critical for Mac/Windows to show color
        loginBtn.setOpaque(true); 
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Size: 150 width (Half of input fields), 35 height
        loginBtn.setMaximumSize(new Dimension(150, 35)); 
        loginBtn.setPreferredSize(new Dimension(150, 35));

        JLabel msg = new JLabel(" ");
        msg.setForeground(Color.RED);
        msg.setFont(new Font("Arial", Font.PLAIN, 11));
        msg.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // --- ADDED HINT LABEL ---
        JLabel hint = new JLabel("<html><center>Demo: user1 (1234) | user2 (5678) | user3 (9012)</center></html>", SwingConstants.CENTER);
        hint.setFont(new Font("Arial", Font.PLAIN, 10));
        hint.setForeground(Color.GRAY);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add components
        form.add(uLbl);
        form.add(userTxt);
        form.add(Box.createVerticalStrut(5));
        form.add(pLbl);
        form.add(passTxt);
        form.add(Box.createVerticalStrut(15));
        form.add(loginBtn);
        form.add(Box.createVerticalStrut(5));
        form.add(msg);
        form.add(Box.createVerticalStrut(5));
        form.add(hint);

        p.add(form, BorderLayout.CENTER);

        // Action
        loginBtn.addActionListener(e -> {
            String u = userTxt.getText();
            String pin = new String(passTxt.getPassword());
            currentAccount = bank.authenticate(u, pin);
            
            if (currentAccount != null) {
                userTxt.setText(""); passTxt.setText(""); msg.setText(" ");
                updateMenuInfo();
                cardLayout.show(cardPanel, "menu");
            } else {
                msg.setText("Invalid Login");
            }
        });

        return p;
    }

    // --- SCREEN 2: MAIN MENU ---
    private JPanel createMainMenuScreen() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(SCREEN_BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(SCREEN_BG);
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        
        nameLbl = new JLabel("Name");
        nameLbl.setFont(new Font("Arial", Font.BOLD, 14));
        nameLbl.setForeground(TEXT_COLOR);
        
        balLbl = new JLabel("Rs. 0.00");
        balLbl.setFont(new Font("Arial", Font.BOLD, 15));
        balLbl.setForeground(new Color(0, 100, 0));

        header.add(nameLbl, BorderLayout.WEST);
        header.add(balLbl, BorderLayout.EAST);
        p.add(header, BorderLayout.NORTH);

        // Grid Menu
        JPanel grid = new JPanel(new GridLayout(3, 2, 8, 8));
        grid.setBackground(SCREEN_BG);
        grid.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        grid.add(createBtn("History", e -> showHistory()));
        grid.add(createBtn("Withdraw", e -> doTxn("Withdraw")));
        grid.add(createBtn("Deposit", e -> doTxn("Deposit")));
        grid.add(createBtn("Transfer", e -> doTransfer()));
        grid.add(createBtn("Balance", e -> JOptionPane.showMessageDialog(this, "Balance: " + currentAccount.getBalance())));
        
        JButton logout = createBtn("Logout", e -> {
            currentAccount = null;
            cardLayout.show(cardPanel, "login");
        });
        logout.setBackground(new Color(200, 50, 50)); 
        logout.setForeground(Color.WHITE);
        logout.setOpaque(true);
        logout.setBorderPainted(false);
        grid.add(logout);

        p.add(grid, BorderLayout.CENTER);
        return p;
    }

    private JButton createBtn(String txt, ActionListener act) {
        JButton b = new JButton(txt);
        b.setFont(new Font("Arial", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBackground(Color.WHITE);
        b.addActionListener(act);
        return b;
    }

    private void updateMenuInfo() {
        if(currentAccount != null) {
            nameLbl.setText(currentAccount.getAccountHolderName());
            balLbl.setText("Rs. " + String.format("%,.2f", currentAccount.getBalance()));
        }
    }

    // --- DIALOGS ---
    private void doTxn(String type) {
        String s = JOptionPane.showInputDialog(this, "Enter Amount to " + type + ":");
        if(s == null || s.isEmpty()) return;
        try {
            double amt = Double.parseDouble(s);
            if(type.equals("Withdraw")) {
                if(!currentAccount.withdraw(amt)) {
                    JOptionPane.showMessageDialog(this, "Insufficient Funds");
                    return;
                }
            } else {
                currentAccount.deposit(amt);
            }
            updateMenuInfo();
            JOptionPane.showMessageDialog(this, "Success!");
        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Amount");
        }
    }

    private void doTransfer() {
        String id = JOptionPane.showInputDialog(this, "Enter Recipient User ID:");
        if(id == null) return;
        Account rec = bank.getAccountById(id);
        if(rec == null) { JOptionPane.showMessageDialog(this, "User not found"); return; }
        
        String s = JOptionPane.showInputDialog(this, "Enter Amount:");
        if(s == null) return;
        try {
            double amt = Double.parseDouble(s);
            if(currentAccount.transfer(rec, amt)) {
                updateMenuInfo();
                JOptionPane.showMessageDialog(this, "Transfer Complete");
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient Funds");
            }
        } catch(Exception e) { JOptionPane.showMessageDialog(this, "Invalid Amount"); }
    }

    private void showHistory() {
        JDialog d = new JDialog(this, "Mini Statement", true);
        d.setSize(400, 300);
        d.setLocationRelativeTo(this);
        String[] cols = {"Time", "Type", "Amt", "Bal"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        for(Transaction t : currentAccount.getTransactionHistory()) {
            m.addRow(new Object[]{t.getFormattedTimestamp(), t.getTransactionType(), t.getAmount(), t.getBalanceAfter()});
        }
        d.add(new JScrollPane(new JTable(m)));
        d.setVisible(true);
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// MAIN CLASS
// ═══════════════════════════════════════════════════════════════════════════
public class ATMSystemGUI_Fixed {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e){}
            new ATMInterface(new Bank("National Bank of India"));
        });
    }
}