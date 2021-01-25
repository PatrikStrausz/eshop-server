package sk.itsovy.strausz.controller;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.itsovy.strausz.model.Addresses;
import sk.itsovy.strausz.repository.AddressesRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/")
@CrossOrigin(origins = "*")
public class AddressesController {



    @Autowired
    private AddressesRepository addressesRepository;


    @GetMapping(value = "/addresses")
    public List<Addresses> getAllAddresses(){
        return addressesRepository.getAllAddresses();
    }

    @GetMapping(value = "/addresses/{id}")
    public Addresses getAddressById(@PathVariable int id){
        return addressesRepository.getAddressById(id);
    }

    @GetMapping(value = "/addresses/user/{id}")
    public Addresses getAddressByUserId(@PathVariable int id){
        return addressesRepository.getAddressByUserId(id);
    }


    @PostMapping(value = "/addresses/update")
    public ResponseEntity<String> updateUserAddress(@RequestBody String body){
        JSONObject response = new JSONObject();
        Addresses address = new Addresses();

        try {



            System.out.println("BODY: \n" + body);
            JSONObject json = new JSONObject(body);
            response = new JSONObject();


        address.setUser_id(json.getInt("user_id"));
        address.setPincode(json.getInt("pincode"));
        address.setPhone(json.getString("phone"));
        address.setCountry(json.getString("country"));
        address.setState(json.getString("state"));
        address.setCity(json.getString("city"));
        address.setLine1(json.getString("line1"));



                    boolean check = addressesRepository.updateUserAddress(address);
                    if (check) {


                        response.put("success", "Address updated");
                        return ResponseEntity.status(200).body(response.toString());


                    }




        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Address not updated");
            return ResponseEntity.status(400).body(response.toString());
        }

        response.put("error", "Address not updated");
        return ResponseEntity.status(400).body(response.toString());
    }
}
