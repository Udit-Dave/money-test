import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

// Mock implementation of external dependencies
class MockDatabase {
    private Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public User getUser(String username) {
        return users.get(username);
    }

    public void updateUser(User user) {
        users.put(user.getUsername(), user);
    }
}

class MockEmailService {
    public void sendEmail(String to, String subject, String body) {
        // Simulating email sending
        System.out.println("Email sent to: " + to + ", Subject: " + subject + ", Body: " + body);
    }
}

// Class under test
class UserService {
    private MockDatabase database;
    private MockEmailService emailService;

    public UserService(MockDatabase database, MockEmailService emailService) {
        this.database = database;
        this.emailService = emailService;
    }

    public void registerUser(String username, String email, String password) {
        if (database.getUser(username) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        User newUser = new User(username, email, password);
        database.addUser(newUser);
        emailService.sendEmail(email, "Welcome!", "Welcome to our service!");
    }

    public void updateUserEmail(String username, String newEmail) {
        User user = database.getUser(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        user.setEmail(newEmail);
        database.updateUser(user);
        emailService.sendEmail(newEmail, "Email Updated", "Your email has been updated successfully.");
    }
}

// User class
class User {
    private String username;
    private String email;
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
}

// Test class
public class UserServiceTest {
    private UserService userService;
    private MockDatabase mockDatabase;
    private MockEmailService mockEmailService;

    @BeforeEach
    void setUp() {
        mockDatabase = new MockDatabase();
        mockEmailService = new MockEmailService();
        userService = new UserService(mockDatabase, mockEmailService);
    }

    @Test
    void testRegisterUser_Success() {
        userService.registerUser("john", "john@example.com", "password123");
        User registeredUser = mockDatabase.getUser("john");
        assertNotNull(registeredUser);
        assertEquals("john@example.com", registeredUser.getEmail());
    }

    @Test
    void testRegisterUser_UsernameAlreadyExists() {
        userService.registerUser("jane", "jane@example.com", "password456");
        assertThrows(IllegalArgumentException.class, () -> 
            userService.registerUser("jane", "jane2@example.com", "newpassword")
        );
    }

    @Test
    void testUpdateUserEmail_Success() {
        userService.registerUser("alice", "alice@example.com", "alicepass");
        userService.updateUserEmail("alice", "newalice@example.com");
        User updatedUser = mockDatabase.getUser("alice");
        assertEquals("newalice@example.com", updatedUser.getEmail());
    }

    @Test
    void testUpdateUserEmail_UserNotFound() {
        assertThrows(IllegalArgumentException.class, () -> 
            userService.updateUserEmail("nonexistent", "new@example.com")
        );
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("UserServiceTest");
    }
}