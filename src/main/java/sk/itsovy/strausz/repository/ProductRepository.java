package sk.itsovy.strausz.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import sk.itsovy.strausz.Database;
import sk.itsovy.strausz.model.Products;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@CrossOrigin(origins = "*")
public class ProductRepository {

    public List<Products> getAllProducts(){
        Products product;

        List<Products> productsList = new ArrayList<>();

        try{
            PreparedStatement statement = Database.getConnection().prepareStatement("select * from products");
            ResultSet rs = statement.executeQuery();


            while (rs.next()){
                product= new Products();
                product.setId(rs.getInt("id"));
                product.setTitle(rs.getString("title"));
                product.setImage(rs.getString("image"));
                product.setImages(rs.getString("images"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setShort_desc(rs.getString("short_desc"));
                product.setCat_id(rs.getInt("cat_id"));



                productsList.add(product);

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return productsList;

    }


    public Products getProductById(int id ){
        Products product = new Products();

        try {

            PreparedStatement statement = Database.getConnection().prepareStatement("select * from products where id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                product.setId(rs.getInt("id"));
                product.setTitle(rs.getString("title"));
                product.setImage(rs.getString("image"));
                product.setImages(rs.getString("images"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setShort_desc(rs.getString("short_desc"));
                product.setCat_id(rs.getInt("cat_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

}
