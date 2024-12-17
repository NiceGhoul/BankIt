package util;

import model.User;

public class UserSession {
    private static volatile UserSession instance;
    private User currentUser;

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

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void clearSession() {
        this.currentUser = null;
    }

    public boolean isUserLoggedIn() {
        return this.currentUser != null;
    }
}

