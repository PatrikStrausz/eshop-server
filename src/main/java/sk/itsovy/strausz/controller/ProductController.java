package sk.itsovy.strausz.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.itsovy.strausz.model.Categories;
import sk.itsovy.strausz.model.Products;
import sk.itsovy.strausz.repository.ProductRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/")
@CrossOrigin(origins = "*")
public class ProductController {


    @Autowired
    private ProductRepository productRepository;





    @RequestMapping(value = "/products",

            method=RequestMethod.GET)
    public List<Products> getAllProducts(){
        return productRepository.getAllProducts();
    }


    @RequestMapping(value = "/products/{id}",

            method=RequestMethod.GET)
    public Products getProductById(@PathVariable int id){
        return productRepository.getProductById(id);
    }


    @RequestMapping(value = "/products/category/{id}", method=RequestMethod.GET)
    public Categories getCategoryName(@PathVariable int id){
        return productRepository.getCategoryName(id);
    }

    @RequestMapping(value = "/list/products/category/{id}", method=RequestMethod.GET)
    public List<Products> getProductsByCategory(@PathVariable int id){
        return productRepository.getProductsByCategory(id);
    }





}
