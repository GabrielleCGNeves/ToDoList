package com.gabrielle.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gabrielle.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component //precisa colocar para toda classe que quer que o string gerencie
public class FilterTaskAuth extends OncePerRequestFilter{

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                var servletPath = request.getServletPath();
                if(servletPath.equals("/tasks/")){
                    var autorization = request.getHeader("Autorization");
    
                    var authEncoded = autorization.substring("Basic".length()).trim();
                    
                    byte[] authDecode = Base64.getDecoder().decode(authEncoded);
                    
                    var authString = new String(authDecode);
    
                    System.out.println("Autorization");
    
                    String[] credentials = authString.split(":");
                    String username = credentials[0];
                    String password = credentials[1];
    
    
                    //Validar o usuário
                    var user = this.userRepository.findByUserName((username));
                    if(user == null){
                        response.sendError(401);
                    }else{
    
                        //Vallidar senha
                        var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                        if(passwordVerify.verified){
                            filterChain.doFilter(request, response);
                        }else{
                            response.sendError(401);
                        }
    
                    }
                } else{
                    filterChain.doFilter(request, response);
                }

    }

 }
    

