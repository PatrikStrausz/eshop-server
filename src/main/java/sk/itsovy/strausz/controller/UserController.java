package sk.itsovy.strausz.controller;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.itsovy.strausz.Database;
import sk.itsovy.strausz.model.Users;
import sk.itsovy.strausz.repository.AddressesRepository;
import sk.itsovy.strausz.repository.UserRepository;

import java.sql.PreparedStatement;
import java.util.List;


@RestController
@RequestMapping(path = "/")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private AddressesRepository addressesRepository;



    @GetMapping(path = "/users")
    public List<Users> getAllUsers(){
        return userRepository.getAllUsers();
    }

    @GetMapping(path = "/users/{id}")
    public Users getUserById(@PathVariable int id){
        return userRepository.getUserById(id);
    }


    @GetMapping(path = "/users/name/{username}")
    public Users getUserByUsername( @PathVariable String username){
        return userRepository.getUserByUsername(username);
    }

    @PostMapping(value = "/user/edit")
    public ResponseEntity<?> updateUser(@RequestBody String body){


        JSONObject response = new JSONObject();
        Users user = new Users();

        try {



            System.out.println("BODY: \n" + body);
            JSONObject json = new JSONObject(body);


                user.setId(json.getInt("id"));
                user.setPassword(json.getString("password"));



                        boolean check = userRepository.updateUser(user);
            System.out.println(check);
                        if (check) {


                            response.put("success", "User updated");
                            return ResponseEntity.status(200).body(response.toString());


                        }



        } catch (Exception e) {
            e.printStackTrace();

        }

            response.put("error", "Username, password or email is missing");
            return ResponseEntity.status(400).body(response.toString());


    }

    @PostMapping(value = "/user-conflicts")
    public ResponseEntity<?> userConflicts(@RequestBody String body){
        JSONObject response = new JSONObject();
        Users user = new Users();

        try {



            System.out.println("BODY: \n" + body);
            JSONObject json = new JSONObject(body);



                user.setUsername(json.getString("username"));
                user.setEmail(json.getString("email"));
                user.setPassword(json.getString("password"));



           if (userRepository.checkUserConflicts(user)){

               response.put("success", "User successfully updated");
               return ResponseEntity.status(200).body(response.toString());

            }else {

               response.put("error", "Email or username already exists");
               return ResponseEntity.status(400).body(response.toString());
           }

        } catch (Exception e) {
            e.printStackTrace();


        }
        response.put("error", "Email or username already exists");
        return ResponseEntity.status(400).body(response.toString());



    }

    @PostMapping("/user/delete")
    public ResponseEntity<?> deleteUser(@RequestBody String body){

        JSONObject response = new JSONObject();
        Users user = new Users();

        try {



            System.out.println("BODY: \n" + body);
            JSONObject json = new JSONObject(body);
            response = new JSONObject();

            if(json.has("id") && json.has("password")) {

                user.setId(json.getInt("id"));
                user.setPassword(json.getString("password"));


                    boolean check = userRepository.deleteUser(user);
                    if (check) {

                        addressesRepository.deleteAddress(user.getId());
                        response.put("success", "User deleted");
                        return ResponseEntity.status(200).body(response.toString());


                    }



            }
            response.put("error", "Id or password  is missing");
            return ResponseEntity.status(400).body(response.toString());


        } catch (Exception e) {
            e.printStackTrace();

        }
        response.put("error", "User not deleted");
        return ResponseEntity.status(400).body(response.toString());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody String body){

        JSONObject response = new JSONObject();
        Users user = new Users();

        try {



            System.out.println("BODY: \n" + body);
            JSONObject json = new JSONObject(body);
            response = new JSONObject();

            if(json.has("username") && json.has("password") && json.has("email")) {
                user.setUsername(json.getString("username"));
                user.setPassword(json.getString("password"));
                user.setEmail(json.getString("email"));
                user.setRole(555);
                user.setToken("");

                if(userRepository.checkUsername(user.getUsername())){
                    if(userRepository.checkEmail(user.getEmail())) {
                        boolean check = userRepository.registerUser(user);
                        if (check) {


                            response.put("success", "User created");
                            return ResponseEntity.status(200).body(response.toString());


                        } else {
                            response.put("error", "Username, password or email is missing");
                            return ResponseEntity.status(400).body(response.toString());
                        }
                    }else{
                        response.put("error", "Email already exists");
                        return ResponseEntity.status(400).body(response.toString());
                    }
                }else{
                    response.put("error", "Username already exists");
                    return ResponseEntity.status(400).body(response.toString());
                }



            }


        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "User not created");
            return ResponseEntity.status(400).body(response.toString());
        }
        return null;

    };

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody String body) {

        Database database = new Database();
        JSONObject response = new JSONObject();
        Users user = new Users();

        try {
            System.out.println("Login BODY: \n" + body);
            JSONObject json = new JSONObject(body);
            response = new JSONObject();


            if(json.has("username") && json.has("password")) {


                user.setUsername(json.getString("username"));
                user.setPassword(json.getString("password"));



                if (userRepository.checkPassword(user.getPassword(),user.getUsername())) {

                    boolean check = userRepository.loginUser(user);

                    if (check) {

                        String token = UserRepository.generateToken();

                        user.setToken(token);


                        PreparedStatement statement = database.getConnection().prepareStatement(
                                "update users set token = ? where password = ? and username =?");

                        statement.setString(1, user.getToken());
                        statement.setString(2, user.getPassword());

                        statement.setString(3, user.getUsername());

                        statement.executeUpdate();


                        return ResponseEntity.status(200).body(user.getToken());
                    }
                } else {
                    response.put("error", "Wrong username or password");
                    return ResponseEntity.status(400).body(response.toString());
                }
            }else{
                response.put("error", "Username and password are mandatory fields");
                return ResponseEntity.status(400).body(response.toString());
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
        response.put("error", "User not logged");
        return ResponseEntity.status(400).body(response.toString());

    };


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody String body, @RequestHeader(name = "Authorization") String token){

        JSONObject json = new JSONObject(body);
        JSONObject result = new JSONObject();

        System.out.println("Logout BODY: \n" + body+"\n Token: " + token+"\n");

        String login = json.getString("username");


        Users user = new Users() ;
        user.setUsername(login);


        if(user.getUsername()==null){
            result.put("error", "Login do not exist in database");
            return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(result.toString());
        }else{
            if(userRepository.checkToken(token)) {
                userRepository.logout(user.getUsername());
                user.setToken(null);
                result.put("success", "Logout");
                return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body(result.toString());
            }else{
                result.put("error", "Wrong token");
                return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON).body(result.toString());
            }
        }


    }










}
