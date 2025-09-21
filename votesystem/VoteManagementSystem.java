package votesystem;

import com.votesystem.ui.LoginFrame;

import javax.swing.*;

public class VoteManagementSystem {
    
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new LoginFrame().setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, 
                            "Error starting application: " + e.getMessage(), 
                            "Application Error", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}