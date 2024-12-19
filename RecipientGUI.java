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
import java.text.MessageFormat;
import java.util.Vector;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class RecipientGUI {
    private JFrame frame; 
    private BackgroundPanel mainPanel; // Custom panel for background image
    private JTextField idField, nameField, bloodGroupField, genderField, ageField, recipientCaseField; 
    private JButton addButton, updateButton, deleteButton,viewButton;

    public RecipientGUI() {
        frame = new JFrame("Recipient Management");
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
        
        recipientCaseField = new JTextField(20);
        styleTextField(recipientCaseField, backgroundColor, new RoundedBorder(20));
        formPanel.add(new JLabel("Recipient Case:")).setFont(new Font("SansSerif", Font.BOLD, 18));
        formPanel.add(recipientCaseField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the panel transparent
        addButton = new JButton("Add Recipient");
        buttonPanel.add(addButton);
        updateButton = new JButton("Update Details");
        buttonPanel.add(updateButton);
        deleteButton = new JButton("Delete Recipient");
        buttonPanel.add(deleteButton);
        viewButton = new JButton("View All Recipients");
        buttonPanel.add(viewButton);
        

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRecipient();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateRecipient();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteRecipient();
            }
        });
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewAllRecipients();
            }
        });
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleTextField(JTextField textField, Color backgroundColor, Border border) {
        textField.setOpaque(false);
        textField.setBackground(backgroundColor);
        textField.setFont(new Font("SansSerif", Font.BOLD, 18));
        textField.setPreferredSize(new Dimension(200, 50));
        textField.setBorder(border);
    }

    private void addRecipient() {
        String name = nameField.getText();
        String bloodGroup = bloodGroupField.getText();
        String gender = genderField.getText();
        String recipientCase = recipientCaseField.getText(); 
        try {
            int age = Integer.parseInt(ageField.getText());
            Connection conn = DBConnection.getConnection();
            
            // Check if blood group exists in blood_inventory
            PreparedStatement checkBloodGroupPst = conn.prepareStatement("SELECT sample_count FROM blood_inventory WHERE blood_group = ?");
            checkBloodGroupPst.setString(1, bloodGroup);
            ResultSet rs = checkBloodGroupPst.executeQuery();
            
            int sampleCount = 0;
            if (rs.next()) {
                sampleCount = rs.getInt("sample_count");
            }
            
            // If blood group exists and sample count > 0, decrement the sample count
            if (sampleCount > 0) {
                PreparedStatement decrementBloodCountPst = conn.prepareStatement("UPDATE blood_inventory SET sample_count = sample_count - 1 WHERE blood_group = ?");
                decrementBloodCountPst.setString(1, bloodGroup);
                decrementBloodCountPst.executeUpdate();
                
                // Insert recipient details
                String sql = "INSERT INTO recipients (name, blood_group, gender, age, recipient_case) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, name);
                pst.setString(2, bloodGroup);
                pst.setString(3, gender);
                pst.setInt(4, age);
                pst.setString(5, recipientCase); 
                int result = pst.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(frame, "Recipient added successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add recipient.");
                }
                pst.close();
            } else {
                JOptionPane.showMessageDialog(frame, "Blood group not available in inventory.");
            }
            
            conn.close();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid age.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error accessing database: " + e.getMessage());
        }
        clearFields();
    }


    private void updateRecipient() {
        String id = idField.getText();
        String name = nameField.getText();
        String bloodGroup = bloodGroupField.getText();
        String gender = genderField.getText();
        String recipientCase = recipientCaseField.getText(); 
        try {
            int age = Integer.parseInt(ageField.getText());
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE recipients SET name = ?, blood_group = ?, gender = ?, age = ?, recipient_case = ? WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, bloodGroup);
            pst.setString(3, gender);
            pst.setInt(4, age);
            pst.setString(5, recipientCase); 
            pst.setInt(6, Integer.parseInt(id));
            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(frame, "Recipient updated successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "No recipient found with ID: " + id);
            }
            pst.close();
            conn.close();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numerical values.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error updating recipient: " + e.getMessage());
        }
         clearFields();
    }

    private void deleteRecipient() {
        String id = idField.getText();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM recipients WHERE id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(id));
            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(frame, "Recipient deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "No recipient found with ID: " + id);
            }
            pst.close();
            conn.close();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid ID.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error deleting recipient: " + e.getMessage());
        }
         clearFields();
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        bloodGroupField.setText("");
        genderField.setText("");
        recipientCaseField.setText("");
    }

    private void viewAllRecipients() {
        // Create a new JFrame for viewing all recipients
        JFrame viewFrame = new JFrame("View All Recipients");
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewFrame.setLayout(new BorderLayout());

        // Create a table to display the recipients' details
        JTable table = new JTable();

        // Create a scroll pane to hold the table
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the scroll pane to the frame
        viewFrame.add(scrollPane, BorderLayout.CENTER);

        
         JButton printButton = new JButton("Print Recipients");
    printButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                boolean complete = table.print(JTable.PrintMode.FIT_WIDTH, new MessageFormat("Recipient List"), new MessageFormat("Page {0}"));
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

        // Fetch the recipients' details from the database and populate the table
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM recipients");
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

    public JPanel getRecipientPanel() {
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
