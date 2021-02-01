package sk.itsovy.strausz.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.itsovy.strausz.model.Orders;
import sk.itsovy.strausz.model.Products;
import sk.itsovy.strausz.repository.OrdersRepository;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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


    @RequestMapping(value = "/orders/username/{username}", method= RequestMethod.GET)
    public List<Orders> getAllOrdersByUsername(@PathVariable String username){

        return ordersRepository.getAllOrdersByUsername(username);


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
            JSONObject jsonOrder = json.getJSONObject("order");
            response = new JSONObject();






            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String date = sdf.format(timestamp);

            order.setOrder_created(date);

            System.out.println(date);






            for(int i =0; i< jsonArr.length(); i++){
                JSONObject jsons = jsonArr.getJSONObject(i);
                if(jsons.has("quantity") && jsons.has("product_id")) {
                    product.setId(jsons.getInt("product_id"));
                    product.setQuantity(jsons.getInt("quantity"));
                }
            }
            String username = json.getString("username");




                    order.setUser_id(json.getInt("user_id"));
                    order.setState(false);
                    order.setTotal(jsonOrder.getDouble("total"));
                    order.setUsername(username);






            productsList.add(product);


            boolean check = ordersRepository.createOrder(order, productsList,username );
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

        response.put("success", true);
        return ResponseEntity.status(200).body(response.toString());

     }

    @PostMapping(value = "/order/edit")
    public ResponseEntity<?> updateOrder(@RequestBody String body){


        JSONObject response = new JSONObject();
        Orders order = new Orders();

        try {



            System.out.println("BODY: \n" + body);
            JSONObject json = new JSONObject(body);
            response = new JSONObject();


           order.setState(json.getBoolean("state"));


                    boolean check = ordersRepository.updateOrder(order);
                    if (check) {


                        response.put("success", "Order updated");
                        return ResponseEntity.status(200).body(response.toString());

                    }




        } catch (Exception e) {
            e.printStackTrace();

        }

        response.put("error", "Order not created");
        return ResponseEntity.status(400).body(response.toString());


    }


}
