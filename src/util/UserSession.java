package util;

import model.User;

public class UserSession {
    private static volatile UserSession instance;
    private User currentUser;

    // Private constructor to prevent instantiation
    private UserSession() {}

    // Get the singleton instance
    public static UserSession getInstance() {
        if(instance == null) {
            synchronized (UserSession.class) {
                if(instance == null) {
                	instance =  new UserSession();
                }
            }
        }
        return instance;
    }

    // Set the current logged-in user
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Get the current logged-in user
    public User getCurrentUser() {
        return this.currentUser;
    }

    // Clear the session (logout)
    public void clearSession() {
        this.currentUser = null;
    }

    // Check if a user is logged in
    public boolean isUserLoggedIn() {
        return this.currentUser != null;
    }
}

