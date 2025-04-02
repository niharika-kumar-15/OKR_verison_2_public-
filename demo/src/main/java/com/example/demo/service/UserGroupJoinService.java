package com.example.demo.service;

import com.example.demo.model.UserGroupJoin;
import com.example.demo.repository.UserGroupJoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserGroupJoinService {

    private final UserGroupJoinRepository userGroupJoinRepository;

    @Autowired
    public UserGroupJoinService(UserGroupJoinRepository userGroupJoinRepository) {
        this.userGroupJoinRepository = userGroupJoinRepository;
    }

    // Create a new UserGroupJoin record
    public UserGroupJoin createUserGroupJoin(long groupId, long userId) {
        UserGroupJoin userGroupJoin = new UserGroupJoin();
        userGroupJoin.setGroupId(groupId);
        userGroupJoin.setUserId(userId);
        return userGroupJoinRepository.save(userGroupJoin);
    }

    // Get all UserGroupJoin records
    public List<UserGroupJoin> getAllUserGroupJoins() {
        return userGroupJoinRepository.findAll();
    }

    // Get UserGroupJoin by ID
    public Optional<UserGroupJoin> getUserGroupJoinById(Long id) {
        return userGroupJoinRepository.findById(id);
    }

    // Delete UserGroupJoin by ID
    public void deleteUserGroupJoin(Long id) {
        userGroupJoinRepository.deleteById(id);
    }

    // Get all user IDs by group ID
    public List<UserGroupJoin> getUserGroupJoinsByGroupId(Long groupId) {
        return userGroupJoinRepository.findAll().stream()
                .filter(join -> join.getGroupId() == groupId)
                .toList();
    }
    // Get all group IDs by user ID
    public List<UserGroupJoin> getUserGroupJoinsByUserId(Long userId) {
        return userGroupJoinRepository.findAll().stream()
                .filter(join -> join.getUserId() == userId)
                .toList();
    }
}
