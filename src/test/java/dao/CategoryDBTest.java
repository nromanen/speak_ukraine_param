package dao;

import model.Category;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.DBUtil;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDBTest {

    private CategoryDB db = new CategoryDB();

    @BeforeAll
    public static void beforeAll() throws SQLException, IOException {
        new DBUtil().executeFile("init.sql");
    }

    @Test
    void incrementCategoryCountWhenAddNewCategory() throws SQLException {
        int oldCount = DBUtil.totalCount("categories");
        var newCategory = new Category("avatar", "title");
        db.add(newCategory);
        int newCount = DBUtil.totalCount("categories");
        assertEquals(newCount, oldCount+1, "the difference between two numbers");
    }

    @Test
    void unchangedCategoryCountWhenAddCategoryWithExistingTitle() throws SQLException {
        int oldCount = DBUtil.totalCount("categories");
        var newCategory = new Category("avatar", "Old Category");
        db.add(newCategory);
        int newCount = DBUtil.totalCount("categories");
        assertEquals(newCount, oldCount, "The count categories should not be change");
    }

    @Test
    void update() throws SQLException {
        var firstCategory = new Category(1l, "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAIAQMAAAD+wSzIAAAABlBMVEX///+/v7+jQ3Y5AAAADklEQVQI12P4AIX8EAgALgAD/aNpbtEAAAAASUVORK5CYII", "New Old Category");
        boolean actual = db.update(firstCategory);
        assertTrue(actual, "Should returns true if it was updated just one record");
    }

    @Test
    void delete() throws SQLException {
        boolean actual = db.delete(2L);
        assertTrue(actual, "Should returns true if it was delete just one record");
    }

    @Test
    void titlePart() throws SQLException {
        String searchedPart = "uniquE";
        int expected = 2;
        int actual = db.titlePart(searchedPart).size();
        assertEquals(expected, actual, String.format("Database should contain %d records with %s word ", expected, searchedPart));
    }
}