package com.votesystem.services;

import com.votesystem.models.User;
import com.votesystem.models.Poll;
import com.votesystem.models.Vote;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataService {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.dat";
    private static final String POLLS_FILE = DATA_DIR + "/polls.dat";
    private static final String VOTES_FILE = DATA_DIR + "/votes.dat";
    
    private static List<User> users = new ArrayList<>();
    private static List<Poll> polls = new ArrayList<>();
    private static List<Vote> votes = new ArrayList<>();
    
    static {
        loadAllData();
        if (users.isEmpty()) {
            users.add(new User(1, "admin", "admin", "admin@college.edu", true));
            saveUsers();
        }
    }
    
    public static List<User> getUsers() {
        return new ArrayList<>(users);
    }
    
    public static void addUser(User user) {
        user.setId(getNextUserId());
        users.add(user);
        saveUsers();
    }
    
    public static User findUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
    
    private static int getNextUserId() {
        return users.stream().mapToInt(User::getId).max().orElse(0) + 1;
    }
    
    public static List<Poll> getPolls() {
        return new ArrayList<>(polls);
    }
    
    public static List<Poll> getActivePolls() {
        return polls.stream()
                .filter(Poll::isActive)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    public static void addPoll(Poll poll) {
        poll.setId(getNextPollId());
        polls.add(poll);
        savePolls();
    }
    
    public static Poll findPollById(int id) {
        return polls.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    private static int getNextPollId() {
        return polls.stream().mapToInt(Poll::getId).max().orElse(0) + 1;
    }
    
    public static List<Vote> getVotes() {
        return new ArrayList<>(votes);
    }
    
    public static void addVote(Vote vote) {
        votes.add(vote);
        saveVotes();
    }
    
    public static boolean hasUserVoted(int userId, int pollId) {
        return votes.stream()
                .anyMatch(v -> v.getUserId() == userId && v.getPollId() == pollId);
    }
    
    public static List<Vote> getVotesForPoll(int pollId) {
        return votes.stream()
                .filter(v -> v.getPollId() == pollId)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
    
    private static void loadAllData() {
        loadUsers();
        loadPolls();
        loadVotes();
    }
    
    @SuppressWarnings("unchecked")
    private static void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            users = (List<User>) ois.readObject();
        } catch (Exception e) {
            users = new ArrayList<>();
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void loadPolls() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(POLLS_FILE))) {
            polls = (List<Poll>) ois.readObject();
        } catch (Exception e) {
            polls = new ArrayList<>();
        }
    }
    
    @SuppressWarnings("unchecked")
    private static void loadVotes() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(VOTES_FILE))) {
            votes = (List<Vote>) ois.readObject();
        } catch (Exception e) {
            votes = new ArrayList<>();
        }
    }
    
    private static void saveUsers() {
        saveToFile(users, USERS_FILE);
    }
    
    private static void savePolls() {
        saveToFile(polls, POLLS_FILE);
    }
    
    private static void saveVotes() {
        saveToFile(votes, VOTES_FILE);
    }
    
    private static void saveToFile(Object data, String filename) {
        try {
            File dataDir = new File(DATA_DIR);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                oos.writeObject(data);
            }
        } catch (IOException e) {
            System.err.println("Error saving to file: " + filename);
            e.printStackTrace();
        }
    }
}