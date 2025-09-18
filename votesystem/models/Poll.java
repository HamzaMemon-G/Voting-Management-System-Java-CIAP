package com.votesystem.models;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Poll implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String title;
    private String description;
    private List<String> options;
    private int creatorId;
    private boolean isActive;
    
    public Poll() {
        this.options = new ArrayList<>();
        this.isActive = true;
    }
    
    public Poll(int id, String title, String description, int creatorId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creatorId = creatorId;
        this.options = new ArrayList<>();
        this.isActive = true;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
    
    public void addOption(String option) {
        this.options.add(option);
    }
    
    public int getCreatorId() { return creatorId; }
    public void setCreatorId(int creatorId) { this.creatorId = creatorId; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    @Override
    public String toString() {
        return "Poll{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", options=" + options +
                ", isActive=" + isActive +
                '}';
    }
}