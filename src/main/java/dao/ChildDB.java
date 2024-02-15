package dao;

import model.Category;
import model.Child;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ChildDB {
    private Connection conn;

    public ChildDB(Connection conn) {
        this.conn = conn;
    }

    public ChildDB()  {
        try {
            conn = DBUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Child add(Child child) throws SQLException {
        String query = "insert into child (first_name, last_name, birth_date) values (?,?,?);";
        PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, child.firstName());
        pst.setString(2, child.lastName());
        java.sql.Date date = null;
        if (child.birthDate() != null){
            date = java.sql.Date.valueOf(child.birthDate());
        }
        pst.setDate(3, date);
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        rs.next();
        Long id = rs.getLong(1);
        return new Child(id, child.firstName(), child.lastName(), child.birthDate());
    }

    public boolean update(Child child) throws SQLException {
        String query = "update child set first_name = ?, last_name = ?, birth_date = ? where id = ?;";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, child.firstName());
        pst.setString(2, child.lastName());
        pst.setDate(3, java.sql.Date.valueOf(child.birthDate()));
        pst.setLong(4, child.id());
        int affectedRows = pst.executeUpdate();
        return affectedRows == 1;
    }


    public boolean delete(Long id) throws SQLException {
        String query = "delete from child where id = ?;";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setLong(1, id);
            int affectedRows = pst.executeUpdate();
            return affectedRows == 1;
        }
    }

    public List<Child> allAtLeastAge(int age) throws SQLException {
        String query = "select * from child where extract(year from age(birth_date)) >= ?;";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, age);
        ResultSet rs = statement.executeQuery();
        return fromResultSetToChild(rs);
    }

    public List<Child> allWithoutBirthDate(int age) throws SQLException {
        String query = "select * from child where birth_date is null;";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        return fromResultSetToChild(rs);
    }

    private static List<Child> fromResultSetToChild(ResultSet rs) throws SQLException {
        List<Child> children = new ArrayList<Child>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");
            LocalDate date = LocalDate.parse(rs.getString("birth_date"));
            var child = new Child(id, firstName, lastName, date);

            children.add(child);
        }
        return children;
    }


    public static void main(String[] args) throws SQLException, IOException {
        System.out.println(new ChildDB().update(new Child(1L, "FN1", "LN", LocalDate.now())));
        //System.out.println(new CategoryDB().titlePart("1"));
        //new DBUtil().executeFile("init.sql");
    }
}
