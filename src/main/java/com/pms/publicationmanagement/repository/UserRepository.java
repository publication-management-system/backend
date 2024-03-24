package com.pms.publicationmanagement.repository;


import com.pms.publicationmanagement.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private final List<User> users;

    public UserRepository() {
        users = new ArrayList<>();
    }

    public User save(User user) {
        users.add(user);

        return user;
    }

    public User findByUsernameAndPassword(String email, String password) {
        for(User user : users) {
            if(user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public User findById(Integer id) {
        for(User user: users) {
                if(user.getId().equals(id)) {
                    return user;
                }
        }
        return null;
    }

    public User findByName(String firstName, String middleName, String lastName) {
        for(User user : users) {
            if(user.getFirstName().equals(firstName) && user.getMiddleName().equals(middleName) &&
                    user.getLastName().equals(lastName)){
                return user;
            }
        }
        return null;
    }

    public void deleteById(Integer id) {
        for (User user : users) {
            if(user.getId().equals(id)) {
                users.remove(user);
                return;
            }
        }
    }

    public List<User> findAll() { return users; }
}
