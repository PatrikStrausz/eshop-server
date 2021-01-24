package sk.itsovy.strausz.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.itsovy.strausz.model.Orders;
import sk.itsovy.strausz.model.Products;
import sk.itsovy.strausz.repository.OrdersRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/")
@CrossOrigin(origins = "*")
public class OrdersController {

    @Autowired
    private OrdersRepository ordersRepository;





    @RequestMapping(value = "/orders", method= RequestMethod.GET)
    public List<Orders> getAllOrders(){
        return ordersRepository.getAllOrders();
    }


    @RequestMapping(value = "/orders/{id}",

            method=RequestMethod.GET)
    public Orders getOrdersById(@PathVariable int id){
        return ordersRepository.getOrdersById(id);
    }


    @PostMapping("/orders/new")
    public ResponseEntity<?> createOrder(@RequestBody String body){

        JSONObject response = new JSONObject();
       Orders order = new Orders();

        List<Products> productsList = new ArrayList<>();

        Products product = new Products();

        try {
            System.out.println("BODY: \n" + body);
            JSONObject json = new JSONObject(body);
            JSONArray jsonArr = json.getJSONArray("products");
            response = new JSONObject();


            order.setUser_id(json.getInt("user_id"));




            for(int i =0; i< jsonArr.length(); i++){
                JSONObject jsons = jsonArr.getJSONObject(i);
                if(jsons.has("quantity") && jsons.has("product_id")) {
                    product.setId(jsons.getInt("product_id"));
                    product.setQuantity(jsons.getInt("quantity"));
                }
            }




            productsList.add(product);


            boolean check = ordersRepository.createOrder(order, productsList);
            if (check) {
                response.put("success", "Order created");
                return ResponseEntity.status(200).body(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Order not created");
            return ResponseEntity.status(400).body(response.toString());
        }
        return null;

    };

    @PostMapping("/orders/payment")
    public ResponseEntity<?> createOrder() {

        JSONObject response = new JSONObject();

        try{
            Thread.sleep(3000);

        }catch (Exception e){
            e.printStackTrace();
        }

        response.put("success", "Payment");
        return ResponseEntity.status(200).body(response.toString());

     }

}
