package com.votesystem.models;

import java.io.Serializable;
import java.util.Date;

public class Vote implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int userId;
    private int pollId;
    private String selectedOption;
    private Date votedAt;
    
    public Vote() {
        this.votedAt = new Date();
    }
    
    public Vote(int userId, int pollId, String selectedOption) {
        this.userId = userId;
        this.pollId = pollId;
        this.selectedOption = selectedOption;
        this.votedAt = new Date();
    }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getPollId() { return pollId; }
    public void setPollId(int pollId) { this.pollId = pollId; }
    
    public String getSelectedOption() { return selectedOption; }
    public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }
    
    public Date getVotedAt() { return votedAt; }
    public void setVotedAt(Date votedAt) { this.votedAt = votedAt; }
    
    @Override
    public String toString() {
        return "Vote{" +
                "userId=" + userId +
                ", pollId=" + pollId +
                ", selectedOption='" + selectedOption + '\'' +
                ", votedAt=" + votedAt +
                '}';
    }
}