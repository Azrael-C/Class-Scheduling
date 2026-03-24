package Database;

public class SetupTest {
    public static void main(String[] args) {
        DBConnection.getConnection(); // triggers setup
        System.out.println("✅ Setup complete. Database ready to use!");
        DBConnection.closeConnection();
    }
}