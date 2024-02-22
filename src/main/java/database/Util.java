package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Util {

    public static Connection getConnection() throws SQLException {
        return DriverManager
                .getConnection("jdbc:postgresql://localhost:5432/example", "myuser", "mypass");
    }

    public static int executeStatement(String query) throws SQLException {
        Statement st = getConnection().createStatement();
        return st.executeUpdate(query);
    }

    public static boolean executeQueryOneBoolean(String query) throws SQLException {
        Statement st = getConnection().createStatement();
        ResultSet rs = st.executeQuery(query);
        rs.next();
        return rs.getBoolean(1);
    }

    public static String executeQueryOneString(String query) throws SQLException {
        Statement st = getConnection().createStatement();
        ResultSet rs = st.executeQuery(query);
        rs.next();
        return rs.getString(1);
    }

    public static List<String> executeQueryListString(String query) throws SQLException {
        Statement st = getConnection().createStatement();
        ResultSet rs = st.executeQuery(query);
        List<String> result = new ArrayList<>();
        while (rs.next()){
            result.add(rs.getString(1));
        }
        return result;
    }

    public void executeFile(String path) throws IOException, SQLException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path);


        assert inputStream != null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

             Statement statement = getConnection().createStatement()) {

            StringBuilder builder = new StringBuilder();

            String line;
            int lineNumber = 0;
            int count = 0;

            while ((line = bufferedReader.readLine()) != null) {
                lineNumber += 1;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("--"))
                    continue;

                builder.append(line);
                if (line.endsWith(";"))
                    try {
                        statement.execute(builder.toString());
                        System.err.println(
                                ++count
                                        + " Command successfully executed : "
                                        + builder.substring(
                                        0,
                                        Math.min(builder.length(), 15))
                                        + "...");
                        builder.setLength(0);
                    } catch (SQLException e) {
                        System.err.println(
                                "At line " + lineNumber + " : "
                                        + e.getMessage() + "\n");
                        return;
                    }
            }

        }
    }
}
