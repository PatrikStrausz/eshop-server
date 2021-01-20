package sk.itsovy.strausz.controller;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.itsovy.strausz.Database;
import sk.itsovy.strausz.model.Users;
import sk.itsovy.strausz.repository.UserRepository;

import java.sql.PreparedStatement;
import java.util.List;


@RestController
@RequestMapping(path = "/")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;




    @GetMapping(path = "/users")
    public List<Users> getAllUsers(){
        return userRepository.getAllUsers();
    }

    @GetMapping(path = "/users/{id}")
    public Users getUserById(@PathVariable int id){
        return userRepository.getUserById(id);
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
                    boolean check = userRepository.registerUser(user);
                    if (check) {


                        response.put("success", "User created");
                        return ResponseEntity.status(200).body(response.toString());


                    } else {
                        response.put("error", "Username, password or email is missing");
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
        JSONObject response = new JSONObject();
        Users user = new Users();

        try {
            System.out.println("Login BODY: \n" + body);
            JSONObject json = new JSONObject(body);
            response = new JSONObject();


            if(json.has("username") && json.has("password")){


                user.setUsername(json.getString("username"));
                user.setPassword(json.getString("password"));


                boolean check = userRepository.loginUser(user);
                System.out.println(check);
                if (check) {
                    String token = UserRepository.generateToken();

                    user.setToken(token);



                    PreparedStatement statement = Database.getConnection().prepareStatement(
                            "update users set token = ? where password = ? and username =?");

                    statement.setString(1,user.getToken());
                    statement.setString(2,user.getPassword());

                    statement.setString(3,user.getUsername());

                    statement.executeUpdate();



                    response.put("success", "User logged in \n Your token: " + user.getToken());
                    return ResponseEntity.status(200).body(response.toString());
                }
            }else {
                response.put("error", "Username and password are mandatory fields");
                return ResponseEntity.status(400).body(response.toString());
            }


        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "User not logged");
            return ResponseEntity.status(400).body(response.toString());
        }
        return null;

    };


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody String body, @RequestHeader(name = "Authorization") String token){

        JSONObject json = new JSONObject(body);
        JSONObject result = new JSONObject();

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
