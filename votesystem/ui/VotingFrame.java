package com.votesystem.ui;

import com.votesystem.models.User;
import com.votesystem.models.Poll;
import com.votesystem.models.Vote;
import com.votesystem.services.DataService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VotingFrame extends JFrame {
    private User currentUser;
    private Poll poll;
    private DashboardFrame parentFrame;

    public VotingFrame(User user, Poll poll, DashboardFrame parent) {
        this.currentUser = user;
        this.poll = poll;
        this.parentFrame = parent;

        setTitle("Vote on Poll: " + poll.getTitle());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel(poll.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea descriptionArea = new JTextArea(poll.getDescription());
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(getBackground());
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Choose an option:"));

        ButtonGroup optionGroup = new ButtonGroup();
        for (String option : poll.getOptions()) {
            JRadioButton radioButton = new JRadioButton(option);
            radioButton.setFont(new Font("Arial", Font.PLAIN, 14));
            radioButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            optionGroup.add(radioButton);
            optionsPanel.add(radioButton);
        }

        JButton submitButton = new JButton("Submit Vote");
        JButton cancelButton = new JButton("Cancel");

        submitButton.setBackground(new Color(34, 139, 34));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));

        cancelButton.setBackground(new Color(128, 128, 128));
        cancelButton.setForeground(Color.WHITE);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Determine selected option
                ButtonModel selectedModel = optionGroup.getSelection();
                if (selectedModel == null) {
                    JOptionPane.showMessageDialog(VotingFrame.this, "Please select an option before submitting your vote.",
                            "No Option Selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String selectedOption = null;
                for (Component component : optionsPanel.getComponents()) {
                    if (component instanceof JRadioButton) {
                        JRadioButton rb = (JRadioButton) component;
                        if (rb.isSelected()) {
                            selectedOption = rb.getText();
                            break;
                        }
                    }
                }

                if (selectedOption == null) {
                    JOptionPane.showMessageDialog(VotingFrame.this, "Error determining selected option.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (DataService.hasUserVoted(currentUser.getId(), poll.getId())) {
                    JOptionPane.showMessageDialog(VotingFrame.this, "You have already voted on this poll.",
                            "Already Voted", JOptionPane.WARNING_MESSAGE);
                    dispose();
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(VotingFrame.this,
                        "Are you sure you want to vote for: \"" + selectedOption + "\"?\n\n" +
                                "Note: You cannot change your vote after submitting.",
                        "Confirm Vote",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    Vote vote = new Vote(currentUser.getId(), poll.getId(), selectedOption);
                    DataService.addVote(vote);

                    JOptionPane.showMessageDialog(VotingFrame.this,
                            "Your vote has been recorded successfully!\n\n" +
                                    "You voted for: " + selectedOption,
                            "Vote Submitted",
                            JOptionPane.INFORMATION_MESSAGE);

                    parentFrame.refreshPollsTable();
                    dispose();
                }
            }
        });

        cancelButton.addActionListener(e -> dispose());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBorder(BorderFactory.createTitledBorder("Poll Description"));
        descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        descPanel.setPreferredSize(new Dimension(400, 80));

        JScrollPane optionsScrollPane = new JScrollPane(optionsPanel);
        optionsScrollPane.setPreferredSize(new Dimension(400, 180));
        optionsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(descPanel, BorderLayout.NORTH);
        mainPanel.add(optionsScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
