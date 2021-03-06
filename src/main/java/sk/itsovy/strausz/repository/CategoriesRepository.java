package sk.itsovy.strausz.repository;



import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import sk.itsovy.strausz.Database;
import sk.itsovy.strausz.model.Categories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@CrossOrigin(origins = "*")
public class CategoriesRepository {

    public List<Categories> getAllCategories() {
        Database database = new Database();
        Categories categorie;

        List<Categories> categoriesList = new ArrayList<>();

        try{
            PreparedStatement statement = database.getConnection().prepareStatement("select * from categories");
            ResultSet rs = statement.executeQuery();


            while (rs.next()){
                categorie= new Categories();
                categorie.setId(rs.getInt("id"));
                categorie.setTitle(rs.getString("title"));



                categoriesList.add(categorie);



            }
            database.getConnection().close();
        }catch (SQLException e) {
            e.printStackTrace();
        }


        return categoriesList;
    }

    public Categories getCategoriesById(int id) {
        Database database = new Database();
        Categories categorie = new Categories();

        try {

            PreparedStatement statement = database.getConnection().prepareStatement("select * from categories where id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                categorie.setId(rs.getInt("id"));
                categorie.setTitle(rs.getString("title"));



            }
            database.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorie;
    }
}
