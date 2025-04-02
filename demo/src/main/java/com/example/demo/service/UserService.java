package com.example.demo.service;

import com.example.demo.model.Group;
import com.example.demo.model.User;
import com.example.demo.model.UserGroupJoin;
import com.example.demo.repository.GroupRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupJoinService userGroupJoinService;

    @Autowired
    private GroupRepository groupRepository;

    public User createUser(String username, String designation, long managerId, String email) {

        User existingUser = userRepository.findByUsername(username);

        if (existingUser != null) {
            return existingUser; // Throw error
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setDesignation(designation);
        newUser.setManagerId(managerId);
        newUser.setEmail(email);
        newUser.setObjectives(new ArrayList<>());  // Empty list at the start
        newUser.setGroupId(null);
        return userRepository.save(newUser);
    }


    private Long findManagerIdByName(String managerName) {
        return userRepository.findByUsername(managerName).getUserId();
    }

    public boolean deleteUser(String email) {
        // Find the user by email
        User existingUser = userRepository.findByEmail(email);

        // Check if the user exists
        if (existingUser != null) {
            // Get the manager's name from the user
            String managerName = findManagerNameByUserId(existingUser.getManagerId());

            // Notify the manager about the user transfer
            notifyManager(managerName, existingUser);

            // Optionally, transfer the tasks assigned to this user
            transferTasks(existingUser);

            // Delete the user
            userRepository.delete(existingUser);

            return true; // User was successfully deleted
        }

        // Return false if no user was found with the given email
        return false;
    }

    private String findManagerNameByUserId(Long managerId) {
        // Find the manager by their user ID
        User manager = userRepository.findUserById(managerId);
        return manager != null ? manager.getUsername() : "Unknown Manager";
    }

    private void notifyManager(String managerName, User user) {
        // Here, we're simply printing a message, but you can implement
        // actual email or messaging service for notifications
        System.out.println("Notification: " + managerName + ", the user " + user.getUsername() +
                " is being deleted. Please transfer their tasks.");
    }

    private void transferTasks(User user) {
        // Logic to transfer tasks to another user (e.g., manager or another colleague)
        // You will need to have a task service or repository to handle this

        // Assuming you have a TaskRepository and Task model, for example:
        // taskRepository.updateTaskAssignee(user.getId(), newAssigneeId);

        // This is a placeholder for task reassignment logic
        System.out.println("Transferring tasks from user: " + user.getUsername());
    }


    public User getUserById(Long userId) {
        return  userRepository.findUserById(userId);
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByObjectiveId(Long objectiveId) {
        return userRepository.findUsersByObjectiveId(objectiveId);
    }

    public Long findIdByName(String managerName) {
        User manager = userRepository.findByUsername(managerName);
        if (manager != null) {
            return manager.getUserId(); // Return manager's ID
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    public User updateUser(User user) {
        return userRepository.save(user);  // Assuming you're using JPA and userRepository is a JpaRepository
    }

    public List<Group> getAllTeamsOfUser(Long userId) {
        List<UserGroupJoin> userGroupJoins = userGroupJoinService.getUserGroupJoinsByUserId(userId);
        List<Group> groups = new ArrayList<>();
        for (UserGroupJoin userGroupJoin : userGroupJoins) {
            Optional<Group> optionalGroup = groupRepository.findById(userGroupJoin.getGroupId());
            optionalGroup.ifPresent(groups::add);
        }
        return groups;
    }

}
