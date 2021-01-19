package sk.itsovy.strausz.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import sk.itsovy.strausz.Database;
import sk.itsovy.strausz.model.Orders;
import sk.itsovy.strausz.model.Products;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


@Repository
@CrossOrigin(origins = "*")
public class OrdersRepository {
    public List<Orders> getAllOrders() {
        Orders order;

       List<Orders> ordersList = new ArrayList<>();

        try{
            PreparedStatement statement = Database.getConnection().prepareStatement("select * from orders");
            ResultSet rs = statement.executeQuery();


            while (rs.next()){
                order= new Orders();
               order.setId(rs.getInt("id"));
               order.setUser_id(rs.getInt("user_id"));



                ordersList.add(order);

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return ordersList;
    }

    public Orders getOrdersById(int id) {
        Orders order = new Orders();

        try {

            PreparedStatement statement = Database.getConnection().prepareStatement("select * from orders where id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                order.setId(rs.getInt("id"));
                order.setUser_id(rs.getInt("user_id"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public boolean createOrder(Orders order, Products product){
      try{

          PreparedStatement statement = Database.getConnection().prepareStatement(
                  "insert into orders(user_id) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

          PreparedStatement statement2 = Database.getConnection().prepareStatement(
                  "INSERT INTO `orders_details`( `order_id`, `product_id`, `quantity`) VALUES (?,?,?)");




          statement.setInt(1, order.getUser_id());




          int executeUpdate = statement.executeUpdate();

          ResultSet id = statement.getGeneratedKeys();

          if(id.next()){
              statement2.setInt(1, id.getInt(1));
              statement2.setInt(2, product.getId());
              statement2.setInt(3, product.getQuantity());
          }



          int executeUpdate2 = statement2.executeUpdate();

          if (executeUpdate == 1) {
              if(executeUpdate2 == 1) {
                  System.out.println("Order is created: " +
                          order.getId() + " " + order.getUser_id());
                  return true;
              }
          }

      }catch (SQLException e) {
          e.printStackTrace();
      }
      return false;
    }

}
