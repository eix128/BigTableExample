import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by kadir.basol on 24.2.2016.
 */
public class Connection {
    public final void connect() throws SQLException {
        try{
            Class.forName("org.mariadb.jdbc.Driver");

            final java.sql.Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/project", "root", "");
            Statement statement = connection.createStatement();
            connection.prepareStatement("");
        } catch (Exception epx) {

        }
    }
}
