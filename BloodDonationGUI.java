package com.project.ds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class BloodDonationGUI {
    private JFrame frame;
    private BackgroundPanel mainPanel; // Custom panel for background image
    private JTextField idField, donorIdField, recipientIdField, donationDateField;
    private JButton addButton, updateButton, deleteButton, viewButton;
    private SimpleDateFormat dateFormat;

    public BloodDonationGUI() {
        frame = new JFrame("Blood Donation Management");
        mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackgroundImage(new ImageIcon(getClass().getResource("/images/qqqq.jpg")).getImage());
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        initializeUI();
    }

    private void initializeUI() {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setOpaque(false); // Make the panel transparent

        Color backgroundColor = new Color(255, 255, 255, 50); // Semi-transparent white

        class RoundedBorder extends AbstractBorder {
            private int radius;
            RoundedBorder(int radius) {
                this.radius = radius;
            }

            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            }
        }

        idField = createStyledTextField(20, backgroundColor, new RoundedBorder(20));
        formPanel.add(new JLabel("Donation ID (for Update/Delete):")).setFont(new Font("SansSerif", Font.BOLD, 18));
        formPanel.add(idField);

        donorIdField = createStyledTextField(20, backgroundColor, new RoundedBorder(20));
        formPanel.add(new JLabel("Donor ID:")).setFont(new Font("SansSerif", Font.BOLD, 18));
        formPanel.add(donorIdField);

        recipientIdField = createStyledTextField(20, backgroundColor, new RoundedBorder(20));
        formPanel.add(new JLabel("Recipient ID:")).setFont(new Font("SansSerif", Font.BOLD, 18));
        formPanel.add(recipientIdField);

        donationDateField = createStyledTextField(20, backgroundColor, new RoundedBorder(20));
        formPanel.add(new JLabel("Donation Date (YYYY-MM-DD):")).setFont(new Font("SansSerif", Font.BOLD, 18));
        formPanel.add(donationDateField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        addButton = new JButton("Add Donation");
        buttonPanel.add(addButton);
        updateButton = new JButton("Update Donation");
        buttonPanel.add(updateButton);
        deleteButton = new JButton("Delete Donation");
        buttonPanel.add(deleteButton);
        viewButton = new JButton("View All Donations");
        buttonPanel.add(viewButton);

        addButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        addBloodDonation();
    }
});

updateButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        updateBloodDonation();
    }
});

deleteButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        deleteBloodDonation();
    }
});

viewButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        viewAllBloodDonations();
    }
});

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JTextField createStyledTextField(int columns, Color backgroundColor, Border border) {
        JTextField textField = new JTextField(columns);
        textField.setOpaque(false);
        textField.setFont(new Font("SansSerif", Font.BOLD, 18));
        textField.setPreferredSize(new Dimension(200, 50));
        textField.setBackground(backgroundColor);
        textField.setBorder(border);
        return textField;
    }
private void addBloodDonation() {
    Connection conn = null;
    PreparedStatement pst = null;
    try {
        conn = DBConnection.getConnection();
        String sql = "INSERT INTO blood_donations (donor_id, recipient_id, donation_date) VALUES (?, ?, ?)";
        pst = conn.prepareStatement(sql);

        String donorId = donorIdField.getText().trim();
        String recipientId = recipientIdField.getText().trim();

        pst.setObject(1, donorId.isEmpty() ? null : Integer.parseInt(donorId));
        pst.setObject(2, recipientId.isEmpty() ? null : Integer.parseInt(recipientId));

        String donationDateString = donationDateField.getText().trim();
        Date donationDate = donationDateString.isEmpty() ? null : dateFormat.parse(donationDateString);

        pst.setObject(3, donationDate == null ? null : new java.sql.Date(donationDate.getTime()));

        int result = pst.executeUpdate();
        if (result > 0) {
            JOptionPane.showMessageDialog(frame, "Donation added successfully!");
            clearFields(); // Clear fields after successful operation
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to add donation.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
    } finally {
        try {
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

private void updateBloodDonation() {
    Connection conn = null;
    PreparedStatement pst = null;
    try {
        conn = DBConnection.getConnection();
        String sql = "UPDATE blood_donations SET donor_id = ?, recipient_id = ?, donation_date = ? WHERE id = ?";
        pst = conn.prepareStatement(sql);

        String donorId = donorIdField.getText().trim();
        String recipientId = recipientIdField.getText().trim();
        String id = idField.getText().trim();

        pst.setObject(1, donorId.isEmpty() ? null : Integer.parseInt(donorId));
        pst.setObject(2, recipientId.isEmpty() ? null : Integer.parseInt(recipientId));

        String donationDateString = donationDateField.getText().trim();
        Date donationDate = donationDateString.isEmpty() ? null : dateFormat.parse(donationDateString);

        pst.setObject(3, donationDate == null ? null : new java.sql.Date(donationDate.getTime()));
        pst.setObject(4, id.isEmpty() ? null : Integer.parseInt(id));

        int result = pst.executeUpdate();
        if (result > 0) {
            JOptionPane.showMessageDialog(frame, "Donation updated successfully!");
            clearFields(); // Clear fields after successful operation
        } else {
            JOptionPane.showMessageDialog(frame, "No donation found with ID: " + id);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
    } finally {
        try {
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

private void deleteBloodDonation() {
    Connection conn = null;
    PreparedStatement pst = null;
    try {
        conn = DBConnection.getConnection();
        String sql = "DELETE FROM blood_donations WHERE id = ?";
        pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(idField.getText().trim()));

        int result = pst.executeUpdate();
        if (result > 0) {
            JOptionPane.showMessageDialog(frame, "Donation deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(frame, "No donation found with ID: " + idField.getText());
        }
        clearFields(); // Clear fields whether or not the deletion was successful
    } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(frame, "Invalid ID: " + idField.getText());
        clearFields(); // Clear fields if input was invalid
    } catch (Exception e) {
        JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
    } finally {
        try {
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

 private void viewAllBloodDonations() {
        JFrame viewFrame = new JFrame("View All Blood Donations");
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewFrame.setLayout(new BorderLayout());

        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        viewFrame.add(scrollPane, BorderLayout.CENTER);

         JButton printButton = new JButton("Print Blood Donations");
    printButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                boolean complete = table.print(JTable.PrintMode.FIT_WIDTH, new MessageFormat("Blood Donation List"), new MessageFormat("Page {0}"));
                if (complete) {
                    JOptionPane.showMessageDialog(viewFrame, "Print complete");
                } else {
                    JOptionPane.showMessageDialog(viewFrame, "Printing cancelled");
                }
            } catch (PrinterException pe) {
                JOptionPane.showMessageDialog(viewFrame, "Failed to print: " + pe.getMessage());
            }
        }
    });

    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    topPanel.add(printButton);
    viewFrame.add(topPanel, BorderLayout.NORTH);
        
        
        viewFrame.setSize(800, 600);
        viewFrame.setLocationRelativeTo(null);
        viewFrame.setVisible(true);

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM blood_donations");
            ResultSet rs = pst.executeQuery();

            int columnCount = rs.getMetaData().getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(rs.getMetaData().getColumnName(i));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }

            rs.close();
            pst.close();
            conn.close();

            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            table.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error accessing database: " + e.getMessage());
        }
    }
private void clearFields() {
    idField.setText("");
    donorIdField.setText("");
    recipientIdField.setText("");
    donationDateField.setText("");
}

    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public void setBackgroundImage(Image image) {
            backgroundImage = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
