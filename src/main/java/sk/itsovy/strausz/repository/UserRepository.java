package sk.itsovy.strausz.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import sk.itsovy.strausz.Database;
import sk.itsovy.strausz.model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Repository
@CrossOrigin(origins = "*")
public class UserRepository {

    public List<Users> getAllUsers(){
        Users user;

        List<Users> users = new ArrayList<>();

        try{
            PreparedStatement statement = Database.getConnection().prepareStatement("select * from users");
            ResultSet rs = statement.executeQuery();


            while (rs.next()){
                user= new Users();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getInt("role"));

                users.add(user);

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return users;

    }



    public Users getUserById(int id) {

        Users user = new Users();

        try {

            PreparedStatement statement = Database.getConnection().prepareStatement("select * from users where id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getInt("role"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean registerUser(Users user) {
        try{

            PreparedStatement statement = Database.getConnection().prepareStatement(
                    "INSERT INTO `users`( `username`, `password`, `email`, `role`,`token`) VALUES (?,?,?,?,?)");



            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getRole());
            statement.setString(5, user.getToken());




            int executeUpdate = statement.executeUpdate();

            if (executeUpdate == 1) {

                    System.out.println("User is created: " +
                            user.getId() + " " + user.getUsername());
                    return true;

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }




    public boolean loginUser(Users user) {
        try{

            PreparedStatement statement = Database.getConnection().prepareStatement(
                    "select * from users where username = ? and password = ?");



            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());




            boolean executeUpdate = statement.execute();

            if (executeUpdate ) {
                System.out.println("Login successful: " +
                        user.getId() + " " + user.getUsername());




                return true;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void logout(String username) {
        try{

            PreparedStatement statement = Database.getConnection().prepareStatement(
                    "update users set token = ?  where username =?");



            statement.setString(1, "");
            statement.setString(2, username);




            boolean executeUpdate = statement.execute();

            if (executeUpdate ) {
                System.out.println("Logout successful: ");





            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static String generateToken() {
        Random rand = new Random();
        long longToken = Math.abs(rand.nextLong());

        return Long.toString(longToken, 16);
    }


    public boolean checkUsername(String login){

        try{
            final String queryCheck = "SELECT count(*) from users WHERE username = ?";
            final PreparedStatement ps = Database.getConnection().prepareStatement(queryCheck);
            ps.setString(1, login);
            final ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()) {
                final int count = resultSet.getInt(1);


                if(count >= 1){
                    return false;
                }else{
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return false;

    }

    public boolean checkToken(String token){

        try{
            final String queryCheck = "SELECT count(*) from users WHERE token = ?";
            final PreparedStatement ps = Database.getConnection().prepareStatement(queryCheck);
            ps.setString(1, token);
            final ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()) {
                final int count = resultSet.getInt(1);


                if(count == 1){
                    return true;
                }else{
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return false;

    }
}
