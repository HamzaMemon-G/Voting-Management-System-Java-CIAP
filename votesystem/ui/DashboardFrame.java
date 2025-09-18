package com.votesystem.ui;

import com.votesystem.models.User;
import com.votesystem.models.Poll;
import com.votesystem.services.DataService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DashboardFrame extends JFrame {
    private User currentUser;
    private JTable pollsTable;
    private DefaultTableModel tableModel;
    private JButton voteButton;
    private JButton createPollButton;
    private JButton viewResultsButton;
    private JButton refreshButton;
    private JButton logoutButton;
    
    public DashboardFrame(User user) {
        this.currentUser = user;
        
        setTitle("Vote Management System - Dashboard (" + user.getUsername() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
        addListeners();
        refreshPollsTable();
    }
    
    private void initComponents() {
        String[] columnNames = {"ID", "Title", "Description", "Options", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pollsTable = new JTable(tableModel);
        pollsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pollsTable.getTableHeader().setBackground(new Color(70, 130, 180));
        pollsTable.getTableHeader().setForeground(Color.WHITE);
        
        voteButton = new JButton("Vote");
        createPollButton = new JButton("Create Poll");
        viewResultsButton = new JButton("View Results");
        refreshButton = new JButton("Refresh");
        logoutButton = new JButton("Logout");
        
        voteButton.setBackground(new Color(34, 139, 34));
        voteButton.setForeground(Color.WHITE);
        
        createPollButton.setBackground(new Color(70, 130, 180));
        createPollButton.setForeground(Color.WHITE);
        
        viewResultsButton.setBackground(new Color(255, 140, 0));
        viewResultsButton.setForeground(Color.WHITE);
        
        refreshButton.setBackground(new Color(128, 128, 128));
        refreshButton.setForeground(Color.WHITE);
        
        logoutButton.setBackground(new Color(220, 20, 60));
        logoutButton.setForeground(Color.WHITE);
        
        if (!currentUser.isAdmin()) {
            createPollButton.setVisible(false);
        }
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(45, 45, 45));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getUsername() + 
                (currentUser.isAdmin() ? " (Admin)" : " (User)"));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutButton, BorderLayout.EAST);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Active Polls"));
        
        JScrollPane scrollPane = new JScrollPane(pollsTable);
        scrollPane.setPreferredSize(new Dimension(750, 400));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        bottomPanel.add(voteButton);
        bottomPanel.add(createPollButton);
        bottomPanel.add(viewResultsButton);
        bottomPanel.add(refreshButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void addListeners() {
        voteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleVote();
            }
        });
        
        createPollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreatePollFrame(currentUser, DashboardFrame.this).setVisible(true);
            }
        });
        
        viewResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleViewResults();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshPollsTable();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame().setVisible(true);
            }
        });
        
        pollsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    handleVote();
                }
            }
        });
    }
    
    private void handleVote() {
        int selectedRow = pollsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a poll to vote on.", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int pollId = (Integer) tableModel.getValueAt(selectedRow, 0);
        Poll poll = DataService.findPollById(pollId);
        
        if (poll == null || !poll.isActive()) {
            JOptionPane.showMessageDialog(this, "This poll is not active.", 
                    "Poll Inactive", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (DataService.hasUserVoted(currentUser.getId(), pollId)) {
            JOptionPane.showMessageDialog(this, "You have already voted on this poll.", 
                    "Already Voted", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        new VotingFrame(currentUser, poll, this).setVisible(true);
    }
    
    private void handleViewResults() {
        int selectedRow = pollsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a poll to view results.", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int pollId = (Integer) tableModel.getValueAt(selectedRow, 0);
        Poll poll = DataService.findPollById(pollId);
        
        if (poll == null) {
            JOptionPane.showMessageDialog(this, "Poll not found.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        new ResultsFrame(poll).setVisible(true);
    }
    
    public void refreshPollsTable() {
        tableModel.setRowCount(0);
        List<Poll> activePolls = DataService.getActivePolls();
        
        for (Poll poll : activePolls) {
            Object[] rowData = {
                poll.getId(),
                poll.getTitle(),
                poll.getDescription(),
                poll.getOptions().size() + " options",
                poll.isActive() ? "Active" : "Inactive"
            };
            tableModel.addRow(rowData);
        }
        
        if (activePolls.isEmpty()) {
            Object[] emptyRow = {"No polls available", "", "", "", ""};
            tableModel.addRow(emptyRow);
        }
    }
}