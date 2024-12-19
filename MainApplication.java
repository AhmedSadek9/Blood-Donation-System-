package com.project.ds;
import javax.swing.*;
import java.awt.*;

public class MainApplication {
    private JFrame frame;
    private JTabbedPane tabbedPane;


    public MainApplication() {
        initializeUI();
       
        }

    private void initializeUI() {
        frame = new JFrame("Blood Donation Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();
 

        JPanel donorPanel = new DonorGUI().getDonorPanel();
        JPanel recipientPanel = new RecipientGUI().getRecipientPanel();
        JPanel bloodDonationGUI = new BloodDonationGUI().getMainPanel();
        JPanel bloodInventoryPanel = new BloodInventorySearchGUI().getBloodInventoryPanel();
     
        tabbedPane.addTab("Donor Management", donorPanel);
        tabbedPane.addTab("Recipient Management", recipientPanel);
        tabbedPane.addTab("Blood Inventory Search", bloodInventoryPanel);
        tabbedPane.addTab("BloodDonation Management", bloodDonationGUI);
        tabbedPane.setOpaque(false);

        frame.add(tabbedPane, BorderLayout.CENTER);

       frame.setSize(800, 400); 
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
 
 


 
}

