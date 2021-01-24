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
}
