package com.votesystem.ui;

import com.votesystem.models.User;
import com.votesystem.models.Poll;
import com.votesystem.services.DataService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CreatePollFrame extends JFrame {
    private User currentUser;
    private DashboardFrame parentFrame;
    
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JPanel optionsPanel;
    private List<JTextField> optionFields;
    private JButton addOptionButton;
    private JButton removeOptionButton;
    private JButton createButton;
    private JButton cancelButton;
    
    public CreatePollFrame(User user, DashboardFrame parent) {
        this.currentUser = user;
        this.parentFrame = parent;
        this.optionFields = new ArrayList<>();
        
        setTitle("Create New Poll");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        initComponents();
        layoutComponents();
        addListeners();
        
        addOptionField();
        addOptionField();
    }
    
    private void initComponents() {
        titleField = new JTextField(30);
        descriptionArea = new JTextArea(3, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        
        addOptionButton = new JButton("Add Option");
        removeOptionButton = new JButton("Remove Last");
        createButton = new JButton("Create Poll");
        cancelButton = new JButton("Cancel");
        
        addOptionButton.setBackground(new Color(34, 139, 34));
        addOptionButton.setForeground(Color.WHITE);
        
        removeOptionButton.setBackground(new Color(255, 140, 0));
        removeOptionButton.setForeground(Color.WHITE);
        
        createButton.setBackground(new Color(70, 130, 180));
        createButton.setForeground(Color.WHITE);
        
        cancelButton.setBackground(new Color(128, 128, 128));
        cancelButton.setForeground(Color.WHITE);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Poll Title:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(titleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setPreferredSize(new Dimension(300, 60));
        mainPanel.add(descScrollPane, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Options:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        JScrollPane optionsScrollPane = new JScrollPane(optionsPanel);
        optionsScrollPane.setPreferredSize(new Dimension(300, 150));
        optionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(optionsScrollPane, gbc);
        
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0.0;
        JPanel optionButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionButtonPanel.add(addOptionButton);
        optionButtonPanel.add(removeOptionButton);
        mainPanel.add(optionButtonPanel, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void addListeners() {
        addOptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addOptionField();
            }
        });
        
        removeOptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeLastOptionField();
            }
        });
        
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCreatePoll();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    
    private void addOptionField() {
        if (optionFields.size() >= 10) {
            JOptionPane.showMessageDialog(this, "Maximum 10 options allowed.", 
                    "Limit Reached", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField optionField = new JTextField(25);
        optionField.setPreferredSize(new Dimension(250, 25));
        
        optionPanel.add(new JLabel("Option " + (optionFields.size() + 1) + ":"));
        optionPanel.add(optionField);
        
        optionFields.add(optionField);
        optionsPanel.add(optionPanel);
        
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }
    
    private void removeLastOptionField() {
        if (optionFields.size() <= 2) {
            JOptionPane.showMessageDialog(this, "At least 2 options are required.", 
                    "Minimum Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int lastIndex = optionFields.size() - 1;
        optionFields.remove(lastIndex);
        optionsPanel.remove(lastIndex);
        
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }
    
    private void handleCreatePoll() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a poll title.", 
                    "Title Required", JOptionPane.WARNING_MESSAGE);
            titleField.requestFocus();
            return;
        }
        
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a poll description.", 
                    "Description Required", JOptionPane.WARNING_MESSAGE);
            descriptionArea.requestFocus();
            return;
        }
        
        List<String> options = new ArrayList<>();
        for (JTextField optionField : optionFields) {
            String option = optionField.getText().trim();
            if (!option.isEmpty()) {
                if (options.contains(option)) {
                    JOptionPane.showMessageDialog(this, "Duplicate option: " + option, 
                            "Duplicate Option", JOptionPane.WARNING_MESSAGE);
                    optionField.requestFocus();
                    return;
                }
                options.add(option);
            }
        }
        
        if (options.size() < 2) {
            JOptionPane.showMessageDialog(this, "Please provide at least 2 non-empty options.", 
                    "Insufficient Options", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Poll newPoll = new Poll(0, title, description, currentUser.getId());
        newPoll.setOptions(options);
        
        DataService.addPoll(newPoll);
        
        JOptionPane.showMessageDialog(this, "Poll created successfully!", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        
        parentFrame.refreshPollsTable();
        dispose();
    }
}