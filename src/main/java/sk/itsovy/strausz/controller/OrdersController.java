package sk.itsovy.strausz.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.itsovy.strausz.model.Orders;
import sk.itsovy.strausz.model.Products;
import sk.itsovy.strausz.repository.OrdersRepository;

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

        Products product = new Products();

        try {
            System.out.println("BODY: \n" + body);
            JSONObject json = new JSONObject(body);
            response = new JSONObject();

            order.setUser_id(json.getInt("user_id"));

            product.setId(json.getInt("product_id"));
            product.setQuantity(json.getInt("quantity"));
            boolean check = ordersRepository.createOrder(order, product);
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
}
