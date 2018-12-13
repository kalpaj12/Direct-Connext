import java.sql.*;

public class Database_layer {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/dc";

    static final String USER = "root";
    static final String PASS = "kalpaj@123";

    Database_layer() {

    }

    public boolean FoundinServer(String Filename) {
        boolean found = false;
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String sql;
            stmt = conn.createStatement();

            sql = "SELECT * FROM filenames";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String first = rs.getString("FileName");
                if (first.compareToIgnoreCase(Filename) == 0)
                    found = true;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return found;
    }

    public Database_layer(String FileName_ToAdd) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            // System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            // System.out.println("Inserting records into the table...");
            stmt = conn.createStatement();

            sql = "INSERT INTO filenames (FileName) " + "VALUES ('" + FileName_ToAdd + "')";
            stmt.executeUpdate(sql);
            sql = "SELECT * FROM filenames";
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("-----Files in Database-----");
            while (rs.next()) {
                String first = rs.getString("FileName");
                System.out.println(first);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("-----End of Database-----");
    }
}