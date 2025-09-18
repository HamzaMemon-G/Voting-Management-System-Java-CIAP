# Vote Management System

A simple Java Swing application for managing polls and votes, designed as a college project.

## Features

- **User Management**: Registration, login, and role-based access (Admin/User)
- **Poll Creation**: Admins can create polls with multiple options
- **Voting System**: Users can vote once per poll
- **Results Display**: View vote counts and simple bar charts
- **File Storage**: Data persisted using Java serialization (no database required)

## Project Structure

```
VoteManagementSystem/
├── src/com/votesystem/
│   ├── VoteManagementSystem.java    # Main application class
│   ├── models/                      # Data models
│   │   ├── User.java
│   │   ├── Poll.java
│   │   └── Vote.java
│   ├── services/                    # Business logic
│   │   └── DataService.java
│   └── ui/                          # User interface
│       ├── LoginFrame.java
│       ├── DashboardFrame.java
│       ├── CreatePollFrame.java
│       ├── VotingFrame.java
│       └── ResultsFrame.java
└── data/                            # Data files (auto-created)
    ├── users.dat
    ├── polls.dat
    └── votes.dat
```

## How to Run

### Method 1: Batch File
1. Open the `run.bat` file in the project root (Windows only)
2. This will compile and run the application automatically

### Method 2: Command Line
1. Open terminal/command prompt
2. Navigate to the project directory
3. Compile: `javac -d bin src/com/votesystem/**/*.java`
4. Run: `java -cp bin com.votesystem.VoteManagementSystem`

### Method 3: IDE
1. Import the project into your Java IDE (Eclipse, IntelliJ, VS Code)
2. Run the `VoteManagementSystem.java` main class

## Default Login

- **Username**: admin
- **Password**: admin
- **Role**: Administrator

## User Guide

### For Regular Users:
1. **Login**: Use your credentials or register a new account
2. **View Polls**: See all active polls on the dashboard
3. **Vote**: Double-click a poll or select it and click "Vote"
4. **View Results**: Select a poll and click "View Results"

### For Administrators:
1. **Create Polls**: Click "Create Poll" to add new polls
2. **Manage Polls**: View all polls and their status
3. **Monitor Results**: Check voting progress and results

## System Requirements

- Java 8 or higher
- No additional dependencies required (uses only built-in Java libraries)

## Technical Details

- **UI Framework**: Java Swing (built into Java)
- **Data Storage**: Java Serialization to .dat files
- **Architecture**: Simple MVC pattern
- **Security**: Basic password validation (suitable for college project)

## Features Implemented

✅ User registration and login  
✅ Admin and regular user roles  
✅ Poll creation with multiple options  
✅ Voting system with duplicate prevention  
✅ Results display with charts  
✅ File-based data persistence  
✅ Clean, user-friendly interface  

## Sample Usage

1. Start the application
2. Login with `admin/admin`
3. Create a new poll (e.g., "Favorite Programming Language")
4. Add options (Java, Python, C++, JavaScript)
5. Logout and register as a regular user
6. Login as the new user and vote
7. View results to see the vote count and chart

## Notes for College Submission

- **Pure Java**: Uses only standard Java libraries (no external dependencies)
- **Simple Design**: Easy to understand and modify
- **File-based**: No database setup required
- **Portable**: Runs on any system with Java installed
- **Educational**: Demonstrates basic Java concepts like OOP, Swing, and file I/O

## Possible Enhancements

- Add poll expiration dates
- Implement password encryption
- Add export functionality for results
- Multiple choice voting
- User profile management
- Poll categories

---

**Author**: [Hamza Memon]("https://hamzamemon.me")  
**Language**: Java  
**Framework**: Swing  
**Year**: 2025