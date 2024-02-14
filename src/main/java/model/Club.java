package model;

import dao.DBUtil;

import java.sql.SQLException;
import java.util.List;


public class Club {

    private Long id;
    private String title;
    private Category category;
    private String description;
    private String imageUrl;
    private List<Child> children;

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }




    public static void main(String[] args) throws SQLException {
        var cat = new model.Category("ksks", "ksksk");
        //executeStatement("create table categories (id bigserial primary key, avatar varchar not null, title varchar not null)");
//        DBUtil.executeStatement("""
//        create table club (
//        id bigserial primary key,
//        title varchar not null,
//        description varchar not null,
//        image_url varchar,
//        category_id int8 references categories(id))
//        """
//        );
//        DBUtil.executeStatement("""
//        create table child (
//        id bigserial primary key,
//        first_name varchar not null,
//        last_name varchar not null,
//        birth_date date)
//        """
//        );

//        DBUtil.executeStatement("""
//        create table club_child(
//        club_id int8 references club(id),
//        child_id int8 references child(id))
//        """
//        );
    }
}
