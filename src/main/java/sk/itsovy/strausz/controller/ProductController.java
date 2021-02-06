package sk.itsovy.strausz.controller;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

            method = RequestMethod.GET)
    public List<Products> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @PostMapping(value = "/products/delete")
    public ResponseEntity<?> deleteProduct(@RequestBody String body) {
        JSONObject response = new JSONObject();
        Products product = new Products();

        try {


            System.out.println("BODY: \n" + body);
            JSONObject json = new JSONObject(body);
            response = new JSONObject();


            product.setId(json.getInt("id"));
            product.setTitle(json.getString("title"));
            product.setImage(json.getString("image"));
            product.setImages(json.getString("images"));
            product.setDescription(json.getString("description"));
            product.setQuantity(json.getInt("quantity"));
            product.setShort_desc(json.getString("short_desc"));
            product.setCat_id(json.getInt("cat_id"));


            boolean check = productRepository.deleteProduct(product);
            if (check) {


                response.put("success", "Product deleted");
                return ResponseEntity.status(200).body(response.toString());


            }


        } catch (Exception e) {
            e.printStackTrace();

        }
        response.put("error", "Product not deleted");
        return ResponseEntity.status(400).body(response.toString());
    }

    @PostMapping(value = "/product/edit")
    public ResponseEntity<?> updateUser(@RequestBody String body) {


        JSONObject response = new JSONObject();
        Products product = new Products();

        try {


            System.out.println("BODY: \n" + body);
            JSONObject json = new JSONObject(body);
            response = new JSONObject();


            product.setId(json.getInt("id"));
            product.setTitle(json.getString("title"));
            product.setImage(json.getString("image"));
            product.setImages(json.getString("images"));
            product.setDescription(json.getString("description"));
            product.setQuantity(json.getInt("quantity"));
            product.setShort_desc(json.getString("short_desc"));
            product.setPrice(json.getDouble("price"));
//            product.setCat_id(json.getInt("cat_id"));

            System.out.println(">>>>>IMAGE "+ json.getString("image"));


            boolean check = productRepository.updateProduct(product);
            if (check) {


                response.put("success", "Product updated");
                return ResponseEntity.status(200).body(response.toString());


            }


        } catch (Exception e) {
            e.printStackTrace();

        }

        response.put("error", "Product not created");
        return ResponseEntity.status(400).body(response.toString());


    }


    @RequestMapping(value = "/products/{id}",

            method = RequestMethod.GET)
    public Products getProductById(@PathVariable int id) {
        return productRepository.getProductById(id);
    }


    @RequestMapping(value = "/products/category/{id}", method = RequestMethod.GET)
    public Categories getCategoryName(@PathVariable int id) {
        return productRepository.getCategoryName(id);
    }

    @RequestMapping(value = "/list/products/category/{id}", method = RequestMethod.GET)
    public List<Products> getProductsByCategory(@PathVariable int id) {
        return productRepository.getProductsByCategory(id);
    }


    @PostMapping(value = "/products/add")
    public ResponseEntity<?> createProduct(@RequestBody String body) {
        JSONObject response = new JSONObject();
        Products product = new Products();

        try {


            System.out.println("BODY: \n" + body);
            JSONObject json = new JSONObject(body);
            response = new JSONObject();


            product.setTitle(json.getString("title"));
            product.setImage(json.getString("image"));
            product.setImages(json.getString("images"));
            product.setDescription(json.getString("description"));
            product.setQuantity(json.getInt("quantity"));
            product.setShort_desc(json.getString("short_desc"));
            product.setPrice(json.getDouble("price"));
            product.setCat_id(json.getInt("cat_id"));

            boolean check = productRepository.createProduct(product);
            if (check) {


                response.put("success", "Product created");
                return ResponseEntity.status(200).body(response.toString());

            }


        } catch (Exception e) {
            e.printStackTrace();

        }
        response.put("error", "Product not created");
        return ResponseEntity.status(400).body(response.toString());

    }


}
