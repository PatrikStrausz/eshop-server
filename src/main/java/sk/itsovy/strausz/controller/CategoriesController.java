package sk.itsovy.strausz.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.itsovy.strausz.model.Categories;
import sk.itsovy.strausz.repository.CategoriesRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/")
@CrossOrigin(origins = "*")
public class CategoriesController {



    @Autowired
    private CategoriesRepository categoriesRepository;





    @GetMapping(value = "/categories")
    public List<Categories> getAllOrders(){
        return categoriesRepository.getAllCategories();
    }


    @GetMapping(value = "/categories/{id}")
    public Categories getOrdersById(@PathVariable int id){
        return categoriesRepository.getCategoriesById(id);
    }

}
