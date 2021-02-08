package sk.itsovy.strausz.repository;


import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import sk.itsovy.strausz.Database;
import sk.itsovy.strausz.model.Addresses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@CrossOrigin(origins = "*")

public class AddressesRepository {


    public List<Addresses> getAllAddresses(){
        Addresses address;

        List<Addresses> addressList = new ArrayList<>();

        try{
            PreparedStatement statement = Database.getConnection().prepareStatement("select * from addresses");
            ResultSet rs = statement.executeQuery();


            while (rs.next()){
                address= new Addresses();
                address.setId(rs.getInt("id"));
                address.setLine1(rs.getString("line1"));
                address.setCity(rs.getString("city"));
                address.setState(rs.getString("state"));
                address.setCountry(rs.getString("country"));
                address.setPhone(rs.getString("phone"));
                address.setPincode(rs.getInt("pincode"));
                address.setUser_id(rs.getInt("user_id"));

                addressList.add(address);

            }
            Database.getConnection().close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return addressList;

    }


    public  Addresses getAddressById(int id){
        Addresses address = new Addresses();;



        try{
            PreparedStatement statement = Database.getConnection().prepareStatement("select * from addresses where id =?");
           statement.setInt(1,id);

            ResultSet rs = statement.executeQuery();


            while (rs.next()){
                address.setId(rs.getInt("id"));
                address.setLine1(rs.getString("line1"));
                address.setCity(rs.getString("city"));
                address.setState(rs.getString("state"));
                address.setCountry(rs.getString("country"));
                address.setPhone(rs.getString("phone"));
                address.setPincode(rs.getInt("pincode"));
                address.setUser_id(rs.getInt("user_id"));



            }
            Database.getConnection().close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return address;

    }


    public  Addresses getAddressByUserId(int id){
        Addresses address = new Addresses();;



        try{
            PreparedStatement statement = Database.getConnection().prepareStatement("select * from addresses where user_id =?");
            statement.setInt(1,id);

            ResultSet rs = statement.executeQuery();


            while (rs.next()){
                address.setId(rs.getInt("id"));
                address.setLine1(rs.getString("line1"));
                address.setCity(rs.getString("city"));
                address.setState(rs.getString("state"));
                address.setCountry(rs.getString("country"));
                address.setPhone(rs.getString("phone"));
                address.setPincode(rs.getInt("pincode"));
                address.setUser_id(rs.getInt("user_id"));



            }
            Database.getConnection().close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return address;

    }


    public boolean updateUserAddress(Addresses address){




        try{
            PreparedStatement statement = Database.getConnection().prepareStatement("update addresses set line1 =?, city =?, state = ?,country =?, phone = ?, pincode = ?, user_id =?  where user_id = ?");

            PreparedStatement statement1 = Database.getConnection().prepareStatement("insert into addresses(line1,city,state,country,phone,pincode,user_id) values(?,?,?,?,?,?,?)");


            statement.setString(1,address.getLine1());
            statement.setString(2,address.getCity());
            statement.setString(3,address.getState());
            statement.setString(4,address.getCountry());
            statement.setString(5,address.getPhone());
            statement.setInt(6,address.getPincode());
            statement.setInt(7,address.getUser_id());
            statement.setInt(8,address.getUser_id());


            statement1.setString(1,address.getLine1());
            statement1.setString(2,address.getCity());
            statement1.setString(3,address.getState());
            statement1.setString(4,address.getCountry());
            statement1.setString(5,address.getPhone());
            statement1.setInt(6,address.getPincode());
            statement1.setInt(7,address.getUser_id());


            int executeUpdate = statement.executeUpdate();
            System.out.println(executeUpdate);


            if(checkAddress(address.getUser_id())==0){
                int executeInsert = statement1.executeUpdate();

                if(executeInsert == 1){
                    System.out.println("Address created");
                }
            }
          else

            if (executeUpdate == 1) {

                System.out.println("Address is updated: " +
                        address.getId() + " " + address.getUser_id());
                return true;

            }
            Database.getConnection().close();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean deleteAddress(int user_id){
        try{
            PreparedStatement statement = Database.getConnection().prepareStatement("delete from addresses where user_id=?");



            statement.setInt(1,user_id);

            int executeUpdate = statement.executeUpdate();




            if (executeUpdate == 1) {

                System.out.println("Address is deleted: ");
                return true;

            }
            Database.getConnection().close();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;


    }

    public int checkAddress(int user_id) {

        try {
            final String queryCheck = "SELECT count(*) from addresses WHERE user_id = ?";
            final PreparedStatement ps = Database.getConnection().prepareStatement(queryCheck);
            ps.setInt(1, user_id);
            final ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                final int count = resultSet.getInt(1);

                System.out.println("Addresses count " + count);


               return count;
            }
            Database.getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();

        }
       return -1;

    }
}
