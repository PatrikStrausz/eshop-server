package sk.itsovy.strausz.controller;


import org.springframework.beans.factory.annotation.Autowired;
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

}
