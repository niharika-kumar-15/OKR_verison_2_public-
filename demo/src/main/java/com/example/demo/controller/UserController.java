package com.example.demo.controller;

import com.example.demo.enums.Role;
import com.example.demo.model.Group;
import com.example.demo.model.Objective;
import com.example.demo.model.User;
import com.example.demo.service.ObjectiveService;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final ObjectiveService objectiveService;

    public UserController(UserService userService , ObjectiveService objectiveService) {
        this.userService = userService;
        this.objectiveService = objectiveService;
    }

    @GetMapping("/{userId}")

    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        System.out.println("The api for getting Users called ::" + userId);
        User user = userService.getUserById(Long.parseLong(userId));
        System.out.println(user.toString());
        return ResponseEntity.ok(user);
    }

    // This is not working properly

    @GetMapping("/by-objective/{objectiveId}")

    public ResponseEntity<List<User>> getUsersByObjectiveId(@PathVariable String objectiveId) {
        System.out.println(objectiveId + "has been received...");
        List<User> users = userService.getUsersByObjectiveId(Long.parseLong(objectiveId));
        System.out.println("I m inside api call objective id .......");

        if (users.isEmpty()) {
            System.out.println("The list was empty ....");
            return ResponseEntity.noContent().build();  // Return 204 if no users found
        }

        for(User u : users){
            System.out.println(u.toString());
        }
        return ResponseEntity.ok(users);  // Return 200 with the list of users
    }

    @GetMapping("/usersByObjective")

    public ResponseEntity<List<User>> getUsersByObjective(@RequestParam Long objectiveId) {
        List<User> users = userService.getUsersByObjectiveId(objectiveId);
        System.out.println("I am inside the getUsersbyObjectives ");

        for(User u : users){
            System.out.println(u.toString());
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/message")

    public String getMessage() {
        return "Hello, this is a simple message to checkkkkk!";
    }


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {

        // Call the service to create the user

        User createdUser = userService.createUser(

                user.getUsername(),

                user.getDesignation(),

                user.getManagerId(), // Manager name is passed as managerId

                user.getEmail()

        );


        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);

    }



    // This should be in objective controller

    @GetMapping("/getObjectives")

    public ResponseEntity<List<Objective>> getObjectivesByUser(@RequestParam Long userId){

        ArrayList<Objective> objectiveList = (ArrayList<Objective>) objectiveService.findObjectivesByUser(userId);

        return ResponseEntity.ok(objectiveList);

    }


    @GetMapping

    public ResponseEntity<List<User>> getAllUsers() {

        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(users);

    }



    @PatchMapping("/{userId}/role")

    public ResponseEntity<User> updateUserRole(@PathVariable Long userId, @RequestParam String role) {

        // Retrieve the user by their ID

        User user = userService.getUserById(userId);

        if (user == null) {

            // Return a 404 response if the user is not found

            return ResponseEntity.notFound().build();

        }

        // Update the user's role
        user.addRole(Role.valueOf(role));

        // Save the updated user back to the database

        User updatedUser = userService.updateUser(user);

        // Return a 200 response with the updated user

        return ResponseEntity.ok(updatedUser);

    }


    @GetMapping("/getAllTeams/{userId}")

    public ResponseEntity<List<Group>> getAllTeamsOfUser(@PathVariable  Long userId){

        List<Group> allTeams = userService.getAllTeamsOfUser(userId);

        return ResponseEntity.ok(allTeams);

    }

}

