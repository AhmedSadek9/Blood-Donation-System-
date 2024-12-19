package com.project.ds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.border.AbstractBorder;

public class BloodInventorySearchGUI {
    private JFrame frame;
    private BackgroundPanel mainPanel; // Custom panel for background image
    private JTextField bloodGroupField;
    private JButton searchButton;
    private JTextArea resultArea;

    public BloodInventorySearchGUI() {
        frame = new JFrame("Blood Inventory Search");
        mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackgroundImage(new ImageIcon(getClass().getResource("/images/qqqq.jpg")).getImage());
        initializeUI();
    }

    private void initializeUI() {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setOpaque(false); // Make the panel transparent

        bloodGroupField = createStyledTextField(20);
        formPanel.add(new JLabel("Blood Group:")).setFont(new Font("SansSerif", Font.BOLD, 18));
        formPanel.add(bloodGroupField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        searchButton = new JButton("Search");
        buttonPanel.add(searchButton);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        resultArea.setOpaque(false);
        resultArea.setBorder(new RoundedBorder(20));
        resultArea.setFont(new Font("SansSerif", Font.BOLD, 18)); // Larger font for the text area

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchBloodGroup();
            }
        });

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(resultArea), BorderLayout.SOUTH);
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setOpaque(false);
        textField.setFont(new Font("SansSerif", Font.BOLD, 18));
        textField.setPreferredSize(new Dimension(200, 50)); // Larger size for the text fields
        textField.setBorder(new RoundedBorder(20));
        return textField;
    }

    private void searchBloodGroup() {
        String bloodGroup = bloodGroupField.getText();
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM blood_inventory WHERE blood_group = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, bloodGroup);
            ResultSet rs = pst.executeQuery();

            StringBuilder sb = new StringBuilder();
            boolean found = false;
            while (rs.next()) {
                found = true;
                sb.append("Blood Group: ").append(rs.getString("blood_group")).append("\n");
                sb.append("Sample Count: ").append(rs.getInt("sample_count")).append("\n");
                sb.append("-----------------\n");
            }

            if (found) {
                resultArea.setText(sb.toString());
            } else {
                resultArea.setText("Blood group not found in inventory.");
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error accessing database: " + e.getMessage());
        }
    }

    class RoundedBorder extends AbstractBorder {
        private int radius;
        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
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

    public JPanel getBloodInventoryPanel() {
        return mainPanel;
    }
}
