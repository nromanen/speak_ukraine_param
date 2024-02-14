package dao;

import model.Category;
import model.Child;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CategoryDB {
    private Connection conn;

    public CategoryDB(Connection conn) {
        this.conn = conn;
    }

    public CategoryDB()  {
        try {
            conn = DBUtil.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Category add(Category category) throws SQLException {
        String query = "insert into categories (avatar, title) values (?,?);";
        PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, category.avatar());
        pst.setString(2, category.title());
        pst.executeUpdate();
        ResultSet rs = pst.getGeneratedKeys();
        rs.next();
        Long id = rs.getLong(1);
        return new Category(id, category.avatar(), category.title());
    }

    public boolean update(Category category) throws SQLException {
        String query = "update categories set avatar = ?, title = ? where id = ?;";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, category.avatar());
        pst.setString(2, category.title());
        pst.setLong(3, category.id());
        int affectedRows = pst.executeUpdate();
        return affectedRows == 1;
    }


    public boolean delete(Long id) throws SQLException {
        String query = "delete from categories where id = ?;";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setLong(1, id);
            int affectedRows = pst.executeUpdate();
            return affectedRows == 1;
        }
    }

    public List<Category> titlePart(String titlePart) throws SQLException {
        String query = "SELECT * FROM categories where lower(title) like ?";
        List<Category> categories = new ArrayList<Category>();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, "%" + titlePart.toLowerCase() + "%");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Long id = rs.getLong("id");
            String avatar = rs.getString("avatar");
            String title = rs.getString("title");
            Category category = new Category(id, avatar, title);

            categories.add(category);
        }
        return categories;
    }

    public List<Child> allChildren(Long categoryID) throws SQLException {
        String query = "SELECT * FROM child where category_id = ?";
        List<Child> children = new ArrayList<Child>();
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setLong(1, categoryID);
        ResultSet rs = statement.executeQuery();
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
        //System.out.println(new CategoryDB().update(new Category(7l, "abc1", "def1")));
        //System.out.println(new CategoryDB().titlePart("1"));
        //new DBUtil().executeFile("init.sql");
    }
}
