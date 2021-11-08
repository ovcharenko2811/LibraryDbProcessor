
import java.sql.*;

import static java.lang.Class.*;

public class LibraryDB {
    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:~/Documents/db/test";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    //Method to establish connection
    public Connection createConnection() {
        System.out.println("------- H2 " + "JDBC Connection Testing -------");
        try {
            forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("H2 DB Driver not found" + "Include in your library path!");
            throw new RuntimeException(e);
        }

        System.out.println("H2 JDBC Driver registered!");
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check console output");
            throw new RuntimeException(e);
        }

        if (connection != null) {
            System.out.println("Connection established. Please start working with DB.");
        } else {
            System.out.println("Failed to establish connection");
        }
        return connection;
    }

    //Method to create table

    public void createLibraryTable(Connection connection) {
        Statement statement = null;
        String createTableSQL = "CREATE TABLE LIBRARY_TABLE("
                + "RECORD_ID INT PRIMARY KEY, "
                + "BOOK_NAME VARCHAR(255), "
                + "AUTHOR VARCHAR(255), "
                + "RELEASE_YEAR INT, "
                + "SHELF_NUMBER INT)";
        try {
            statement = connection.createStatement();
            System.out.println(createTableSQL);
            statement.execute(createTableSQL);
            System.out.println("Table LIBRARY_TABLE has been created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void batchInsertRecordsIntoTable(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = null;
        StringBuffer insertSQLBuffer = new StringBuffer();
        insertSQLBuffer.append("INSERT INTO LIBRARY_TABLE")
                .append("(RECORD_ID, BOOK_NAME, AUTHOR, RELEASE_YEAR, SHELF_NUMBER) " + "VALUES(?,?,?,?,?)");
        try {
            preparedStatement = connection.prepareStatement(insertSQLBuffer.toString());
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, 101);
            preparedStatement.setString(2, "Voyna & Mir");
            preparedStatement.setString(3, "Tolstoy");
            preparedStatement.setInt(4, 1867);
            preparedStatement.setInt(5, 5);
            preparedStatement.addBatch();

            preparedStatement.setInt(1, 102);
            preparedStatement.setString(2, "Gore ot uma");
            preparedStatement.setString(3, "Griboedov");
            preparedStatement.setInt(4, 1833);
            preparedStatement.setInt(5, 6);
            preparedStatement.addBatch();

            preparedStatement.setInt(1, 103);
            preparedStatement.setString(2, "Onegin");
            preparedStatement.setString(3, "Pushkin");
            preparedStatement.setInt(4, 1833);
            preparedStatement.setInt(5, 1);
            preparedStatement.addBatch();

            preparedStatement.setInt(1, 104);
            preparedStatement.setString(2, "Groza");
            preparedStatement.setString(3, "Ostrovsky");
            preparedStatement.setInt(4, 1860);
            preparedStatement.setInt(5, 5);
            preparedStatement.addBatch();

            preparedStatement.executeBatch();

            connection.commit();

            System.out.println("Books have been added into the Library!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            connection.rollback();
        } 

    }

    public void selectRecordsFromLibrary(Connection connection) {
        Statement statement = null;
        String selectTableSQL = "SELECT * FROM LIBRARY_TABLE";
        try {
            statement = connection.createStatement();
            System.out.println(selectTableSQL);
            ResultSet rs = statement.executeQuery(selectTableSQL);
            while (rs.next()) {
                int id = rs.getInt("RECORD_ID");
                String bookName = rs.getString("BOOK_NAME");
                String author = rs.getString("AUTHOR");
                int releaseYear = rs.getInt("RELEASE_YEAR");
                int shelfNumber = rs.getInt("SHELF_NUMBER");
                System.out.println("Record ID : " + id);
                System.out.println("Book name : " + bookName);
                System.out.println("Author : " + author);
                System.out.println("Year of release : " + releaseYear);
                System.out.println("Shelf number : " + shelfNumber);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void updateRecordLibrary(Connection connection) {
        Statement statement = null;
        String updateTableSQL = "UPDATE LIBRARY_TABLE"
                + " SET BOOK_NAME = 'Mir i Voina' "
                + " WHERE RECORD_ID = 1";
        try {
            statement = connection.createStatement();
            System.out.println(updateTableSQL);
            statement.execute(updateTableSQL);
            System.out.println("Book data has been updated in Library!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
