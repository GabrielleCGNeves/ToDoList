package com.gabrielle.todolist.user;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.var;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/users")


public class UserController {

    @Autowired //gerencia o ciclo de vida
    private IUserRepository userRepository;

    @PostMapping("/")
    public UserModel create(@RequestBody UserModel usermodel){
        
        var user = this.userRepository.findByUserName(usermodel.getUserName());
        
        if(user != null){
            System.out.println("Usuário já existe.");
            return null;
        }
        
        var userCreated =  this.userRepository.save(usermodel);
        return userCreated;
        
    }
}
