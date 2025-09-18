package com.votesystem.ui;

import com.votesystem.models.Poll;
import com.votesystem.models.Vote;
import com.votesystem.services.DataService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsFrame extends JFrame {
    private Poll poll;
    private JLabel titleLabel;
    private JTextArea descriptionArea;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JPanel chartPanel;
    private JLabel totalVotesLabel;
    private JButton closeButton;
    
    public ResultsFrame(Poll poll) {
        this.poll = poll;
        
        setTitle("Poll Results - " + poll.getTitle());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        
        initComponents();
        layoutComponents();
        loadResults();
        addListeners();
    }
    
    private void initComponents() {
        titleLabel = new JLabel(poll.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        descriptionArea = new JTextArea(poll.getDescription());
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(getBackground());
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        String[] columnNames = {"Option", "Votes", "Percentage"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        resultsTable = new JTable(tableModel);
        resultsTable.getTableHeader().setBackground(new Color(70, 130, 180));
        resultsTable.getTableHeader().setForeground(Color.WHITE);
        resultsTable.setRowHeight(25);
        
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart(g);
            }
        };
        chartPanel.setPreferredSize(new Dimension(300, 200));
        chartPanel.setBorder(BorderFactory.createTitledBorder("Visual Results"));
        chartPanel.setBackground(Color.WHITE);
        
        totalVotesLabel = new JLabel();
        totalVotesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalVotesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        closeButton = new JButton("Close");
        closeButton.setBackground(new Color(128, 128, 128));
        closeButton.setForeground(Color.WHITE);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBorder(BorderFactory.createTitledBorder("Poll Description"));
        descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        descPanel.setPreferredSize(new Dimension(580, 60));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("Detailed Results"));
        JScrollPane tableScrollPane = new JScrollPane(resultsTable);
        tableScrollPane.setPreferredSize(new Dimension(280, 200));
        resultsPanel.add(tableScrollPane, BorderLayout.CENTER);
        resultsPanel.add(totalVotesLabel, BorderLayout.SOUTH);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, resultsPanel, chartPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(closeButton);
        
        add(topPanel, BorderLayout.NORTH);
        add(descPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        add(descPanel, BorderLayout.NORTH);
        remove(descPanel);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(descPanel, BorderLayout.NORTH);
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void addListeners() {
        closeButton.addActionListener(e -> dispose());
    }
    
    private void loadResults() {
        List<Vote> pollVotes = DataService.getVotesForPoll(poll.getId());
        
        Map<String, Integer> voteCounts = new HashMap<>();
        for (String option : poll.getOptions()) {
            voteCounts.put(option, 0);
        }
        
        for (Vote vote : pollVotes) {
            String option = vote.getSelectedOption();
            voteCounts.put(option, voteCounts.getOrDefault(option, 0) + 1);
        }
        
        int totalVotes = pollVotes.size();
        
        tableModel.setRowCount(0);
        for (String option : poll.getOptions()) {
            int votes = voteCounts.get(option);
            double percentage = totalVotes > 0 ? (votes * 100.0 / totalVotes) : 0.0;
            
            Object[] rowData = {
                option,
                votes,
                String.format("%.1f%%", percentage)
            };
            tableModel.addRow(rowData);
        }
        
        totalVotesLabel.setText("Total Votes: " + totalVotes);
        
        chartPanel.repaint();
    }
    
    private void drawChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        List<Vote> pollVotes = DataService.getVotesForPoll(poll.getId());
        
        if (pollVotes.isEmpty()) {
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            g2d.setColor(Color.GRAY);
            String noVotesText = "No votes yet";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (chartPanel.getWidth() - fm.stringWidth(noVotesText)) / 2;
            int y = chartPanel.getHeight() / 2;
            g2d.drawString(noVotesText, x, y);
            return;
        }
        
        Map<String, Integer> voteCounts = new HashMap<>();
        for (String option : poll.getOptions()) {
            voteCounts.put(option, 0);
        }
        
        for (Vote vote : pollVotes) {
            String option = vote.getSelectedOption();
            voteCounts.put(option, voteCounts.getOrDefault(option, 0) + 1);
        }
        
        int totalVotes = pollVotes.size();
        
        int chartWidth = chartPanel.getWidth() - 40;
        int chartHeight = chartPanel.getHeight() - 80;
        int startX = 20;
        int startY = 30;
        
        int maxVotes = voteCounts.values().stream().mapToInt(Integer::intValue).max().orElse(1);
        int barWidth = Math.max(30, chartWidth / poll.getOptions().size() - 10);
        
        Color[] colors = {
            new Color(255, 99, 132),
            new Color(54, 162, 235),
            new Color(255, 205, 86),
            new Color(75, 192, 192),
            new Color(153, 102, 255),
            new Color(255, 159, 64)
        };
        
        int colorIndex = 0;
        int x = startX;
        
        for (String option : poll.getOptions()) {
            int votes = voteCounts.get(option);
            int barHeight = maxVotes > 0 ? (votes * chartHeight / maxVotes) : 0;
            
            g2d.setColor(colors[colorIndex % colors.length]);
            g2d.fillRect(x, startY + chartHeight - barHeight, barWidth, barHeight);
            
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, startY + chartHeight - barHeight, barWidth, barHeight);
            
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            String voteText = String.valueOf(votes);
            FontMetrics fm = g2d.getFontMetrics();
            int textX = x + (barWidth - fm.stringWidth(voteText)) / 2;
            int textY = startY + chartHeight - barHeight - 5;
            g2d.drawString(voteText, textX, Math.max(textY, 15));
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            String optionText = option.length() > 8 ? option.substring(0, 8) + "..." : option;
            fm = g2d.getFontMetrics();
            textX = x + (barWidth - fm.stringWidth(optionText)) / 2;
            textY = startY + chartHeight + 15;
            g2d.drawString(optionText, textX, textY);
            
            x += barWidth + 10;
            colorIndex++;
        }
    }
}