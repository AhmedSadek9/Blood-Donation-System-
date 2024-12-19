package com.project.ds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class DonorGUI {
    private JFrame frame; 
    private BackgroundPanel mainPanel; // Custom panel for background image
    private JTextField idField, nameField, ageField, bloodGroupField, genderField, donationReasonField;
    private JButton addButton, updateButton, deleteButton, viewButton;

    public DonorGUI() {
        frame = new JFrame("Donor Management"); 
        mainPanel = new BackgroundPanel(); // Initialize your custom background panel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackgroundImage(new ImageIcon(getClass().getResource("/images/qqqq.jpg")).getImage()); // Set the background image
        initializeUI();
    }

    private void initializeUI() {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setOpaque(false); // Make the panel transparent
        // Custom styling for JTextField
        Color backgroundColor = new Color(255, 255, 255, 50);
        // Custom border class for rounded corners
        class RoundedBorder extends AbstractBorder {
            private int radius;
            RoundedBorder(int radius) {
                this.radius = radius;
            }

        
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            }
        }

        // Apply styles to each JTextField
        idField = new JTextField(20);
        styleTextField(idField, backgroundColor, new RoundedBorder(20));
        formPanel.add(new JLabel("ID (for Update/Delete):")).setFont(new Font("SansSerif", Font.BOLD, 18));
        formPanel.add(idField);

        nameField = new JTextField(20);
        styleTextField(nameField, backgroundColor, new RoundedBorder(20));
        formPanel.add(new JLabel("Name:")).setFont(new Font("SansSerif", Font.BOLD, 18));
        formPanel.add(nameField);

        ageField = new JTextField(20);
        styleTextField(ageField, backgroundColor, new RoundedBorder(20));
        formPanel.add(new JLabel("Age:")).setFont(new Font("SansSerif", Font.BOLD, 18));
        formPanel.add(ageField);

        bloodGroupField = new JTextField(20);
        styleTextField(bloodGroupField, backgroundColor, new RoundedBorder(20));
        formPanel.add(new JLabel("Blood Group:")).setFont(new Font("SansSerif", Font.BOLD, 18));
        formPanel.add(bloodGroupField);

        genderField = new JTextField(20);
        styleTextField(genderField, backgroundColor, new RoundedBorder(20));
        formPanel.add(new JLabel("Gender:")).setFont(new Font("SansSerif", Font.BOLD, 18));
        formPanel.add(genderField);

        donationReasonField = new JTextField(20);
        styleTextField(donationReasonField, backgroundColor, new RoundedBorder(20));
        formPanel.add(new JLabel("Donation Reason:")).setFont(new Font("SansSerif", Font.BOLD, 18));
        formPanel.add(donationReasonField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the panel transparent
        addButton = new JButton("Add Donor");
        buttonPanel.add(addButton);
        updateButton = new JButton("Update Details");
        buttonPanel.add(updateButton);
        deleteButton = new JButton("Delete Donor");
        buttonPanel.add(deleteButton);
        viewButton = new JButton("View All Donors");
        buttonPanel.add(viewButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addDonor();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateDonor();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteDonor();
                } catch (SQLException ex) {
                    Logger.getLogger(DonorGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewAllDonors();
            }
        });
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
///  Create Bacj
    private void styleTextField(JTextField textField, Color backgroundColor, Border border) {
        textField.setOpaque(false);
        textField.setBackground(backgroundColor);
        textField.setFont(new Font("SansSerif", Font.BOLD, 18));
        textField.setPreferredSize(new Dimension(200, 50));
        textField.setBorder(border);
    }

    private void addDonor() {
        try {
            String name = nameField.getText();
            String gender = genderField.getText();
            String donationReason = donationReasonField.getText();
            int age = Integer.parseInt(ageField.getText());
            String bloodGroup = bloodGroupField.getText();

            Connection conn = DBConnection.getConnection();
            String addDonorSQL = "INSERT INTO donors (name, age, blood_group, gender, donation_reason) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement addDonorPst = conn.prepareStatement(addDonorSQL, Statement.RETURN_GENERATED_KEYS);
            addDonorPst.setString(1, name);
            addDonorPst.setInt(2, age);
            addDonorPst.setString(3, bloodGroup);
            addDonorPst.setString(4, gender);
            addDonorPst.setString(5, donationReason);

            int result = addDonorPst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(frame, "Donor added successfully!");

                // Update blood inventory
                String updateBloodInventorySQL = "INSERT INTO blood_inventory (blood_group, sample_count) VALUES (?, 1) ON DUPLICATE KEY UPDATE sample_count = sample_count + 1";
                PreparedStatement updateBloodInventoryPst = conn.prepareStatement(updateBloodInventorySQL);
                updateBloodInventoryPst.setString(1, bloodGroup);
                updateBloodInventoryPst.executeUpdate();
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to add donor.");
            }

            addDonorPst.close();
            conn.close();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid numerical values.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error accessing database: " + e.getMessage());
        }

        clearFields();
    }

    private void updateDonor() {
        try {
            String id = idField.getText();
            String name = nameField.getText();
            String gender = genderField.getText();
            String donationReason = donationReasonField.getText();
            int age = Integer.parseInt(ageField.getText());
            String bloodGroup = bloodGroupField.getText();

            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE donors SET name = ?, age = ?, blood_group = ?, gender = ?, donation_reason = ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            pst.setInt(2, age);
            pst.setString(3, bloodGroup);
            pst.setString(4, gender);
            pst.setString(5, donationReason);
            pst.setString(6, id);
            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(frame, "Donor updated successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "No donor found with ID: " + id);
            }
            pst.close();
            conn.close();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numerical values.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error updating donor: " + e.getMessage());
        }
        clearFields();
    }

    private void deleteDonor() throws SQLException {
        String id = idField.getText();
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "DELETE FROM donors WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(id));
            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(frame, "Donor deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "No donor found with ID: " + id);
            }
            pst.close();
            conn.close();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid ID.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error deleting donor: " + e.getMessage());
        }
        clearFields();
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        bloodGroupField.setText("");
        genderField.setText("");
        donationReasonField.setText("");
    }

    private void viewAllDonors() {
        // Create a new JFrame for viewing all donors
        JFrame viewFrame = new JFrame("View All Donors");
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewFrame.setLayout(new BorderLayout());

        // Create a table to display the donors' details
        JTable table = new JTable();

        // Create a scroll pane to hold the table
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the frame
        viewFrame.add(scrollPane, BorderLayout.CENTER);

        JButton printButton = new JButton("Print Donors");
    printButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                boolean complete = table.print(JTable.PrintMode.FIT_WIDTH, new MessageFormat("Donor List"), new MessageFormat("Page {0}"));
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
        
        
        
        // Set the size of the frame
        viewFrame.setSize(800, 600);
        viewFrame.setLocationRelativeTo(null);
        viewFrame.setVisible(true);

        // Fetch the donors' details from the database and populate the table
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM donors");
            ResultSet rs = pst.executeQuery();

            // Get metadata for column names
            int columnCount = rs.getMetaData().getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(rs.getMetaData().getColumnName(i));
            }

            // Get data for the table
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }

            // Close resources
            rs.close();
            pst.close();
            conn.close();

            // Set the table model with data and column names
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            table.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error accessing database: " + e.getMessage());
        }
    }

    public JPanel getDonorPanel() {
        return mainPanel;
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
}
