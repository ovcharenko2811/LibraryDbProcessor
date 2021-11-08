import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        LibraryDB libraryDB = new LibraryDB();
        Connection connection = libraryDB.createConnection();
        libraryDB.createLibraryTable(connection);
        libraryDB.batchInsertRecordsIntoTable(connection);
        libraryDB.selectRecordsFromLibrary(connection);
        libraryDB.updateRecordLibrary(connection);

        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Hmm... Could not close connection.");
        }
    }
}
