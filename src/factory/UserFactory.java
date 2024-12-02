package factory;

import java.util.ArrayList;

import model.User;

public class UserFactory {
	private static int userIdCounter = 1;

	private static ArrayList<User> userList = new ArrayList<>();
	public static void createUser(String username, String password, String email, String confirmPassword) {
		User user = new User(userIdCounter++, username, password, email, true);
		userList.add(user);
	}
	public static ArrayList<User> getUserList() {
        return userList;
    }
}

