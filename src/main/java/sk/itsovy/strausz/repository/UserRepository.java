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

    public List<Users> getAllUsers() {
        Database database = new Database();
        Users user;

        List<Users> users = new ArrayList<>();

        try {
            PreparedStatement statement = database.getConnection().prepareStatement("select * from users");
            ResultSet rs = statement.executeQuery();


            while (rs.next()) {
                user = new Users();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getInt("role"));

                users.add(user);

            }
            database.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;

    }


    public Users getUserById(int id) {
        Database database = new Database();
        Users user = new Users();

        try {

            PreparedStatement statement = database.getConnection().prepareStatement("select * from users where id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getInt("role"));

            }
            database.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    public Users getUserByUsername(String username) {
        Database database = new Database();
        Users user = new Users();

        try {

            PreparedStatement statement = database.getConnection().prepareStatement("select * from users where username =  ?");
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getInt("role"));

            }
            database.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    public boolean registerUser(Users user) {
        Database database = new Database();
        try {

            PreparedStatement statement = database.getConnection().prepareStatement(
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
            database.getConnection().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean updateUser(Users user) {
        Database database = new Database();
        try {

            PreparedStatement statement = database.getConnection().prepareStatement(
                    "update users set password =? where id = ?");



            statement.setString(1, user.getPassword());

            statement.setInt(2, user.getId());


            int executeUpdate = statement.executeUpdate();



            if (executeUpdate == 1) {

                System.out.println("User is updated: " +
                        user.getId() + " " + user.getPassword());
                return true;

            }
            database.getConnection().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }


    public boolean checkUserConflicts(Users user) {
        if (checkUsername(user.getUsername())) {
            if (checkEmail(user.getEmail())) {


                return true;

            } else {
                return false;
            }

        } else {
            return false;
        }


    }

    public boolean deleteUser(Users user) {
        Database database = new Database();
        try {

            PreparedStatement statement = database.getConnection().prepareStatement(
                    "delete from users where id =? and password =?");


            statement.setInt(1, user.getId());
            statement.setString(2, user.getPassword());


            int executeUpdate = statement.executeUpdate();

            if (executeUpdate == 1) {
                System.out.println("User deleted: " + user.getId());


                return true;
            }
            database.getConnection().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean loginUser(Users user) {

        if (checkPassword(user.getPassword(), user.getUsername())) {
            return true;
        } else {
            return false;
        }
    }


    public void logout(String username) {
        Database database = new Database();
        try {

            PreparedStatement statement = database.getConnection().prepareStatement(
                    "update users set token = ?  where username =?");


            statement.setString(1, "");
            statement.setString(2, username);


            boolean executeUpdate = statement.execute();

            if (executeUpdate) {
                System.out.println("Logout successful: ");


            }
            database.getConnection().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static String generateToken() {
        Random rand = new Random();
        long longToken = Math.abs(rand.nextLong());

        return Long.toString(longToken, 16);
    }


    public boolean checkUsername(String login) {
        Database database = new Database();

        try {
            final String queryCheck = "SELECT count(*) from users WHERE username = ?";
            final PreparedStatement ps = database.getConnection().prepareStatement(queryCheck);
            ps.setString(1, login);
            final ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                final int count = resultSet.getInt(1);


                if (count >= 1) {
                    return false;
                } else {
                    return true;
                }
            }
            database.getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;

    }


    public boolean checkPassword(String password, String username) {
        Database database = new Database();
        try {
            final String queryCheck = "SELECT count(*) from users WHERE password = ? and username =?";
            final PreparedStatement ps = database.getConnection().prepareStatement(queryCheck);
            ps.setString(1, password);
            ps.setString(2, username);
            final ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                final int count = resultSet.getInt(1);


                System.out.println("Count:" + count);
                if (count >= 1) {


                    return true;
                } else {
                    return false;
                }
            }
            database.getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return false;

    }


    public boolean checkEmail(String email) {
        Database database = new Database();
        try {
            final String queryCheck = "SELECT count(*) from users WHERE email = ?";
            final PreparedStatement ps = database.getConnection().prepareStatement(queryCheck);
            ps.setString(1, email);
            final ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                final int count = resultSet.getInt(1);

                System.out.println("Email count " + count);


                if (count >= 1) {
                    return false;
                } else {
                    return true;
                }
            }
            database.getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;

    }

    public boolean checkToken(String token) {
        Database database = new Database();
        try {
            final String queryCheck = "SELECT count(*) from users WHERE token = ?";
            final PreparedStatement ps = database.getConnection().prepareStatement(queryCheck);
            ps.setString(1, token);
            final ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                final int count = resultSet.getInt(1);


                if (count == 1) {
                    return true;
                } else {
                    return false;
                }
            }
            database.getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;

    }
}
