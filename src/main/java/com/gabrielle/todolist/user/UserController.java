package com.gabrielle.todolist.user;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.var;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/users")


public class UserController {

    @Autowired //gerencia o ciclo de vida
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel usermodel){
        
        var user = this.userRepository.findByUserName(usermodel.getUserName());
        
        if(user != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe.");
        }
        
       var passwordHashed =  BCrypt.withDefaults().hashToString(12, usermodel.getPassword().toCharArray());

       usermodel.setPassword(passwordHashed);

        var userCreated =  this.userRepository.save(usermodel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
        
    }
}
