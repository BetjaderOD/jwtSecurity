package com.utez.sda.springjwt.control;


import com.utez.sda.springjwt.model.AuthRequest;
import com.utez.sda.springjwt.model.UserInfo;
import com.utez.sda.springjwt.service.JwtService;
import com.utez.sda.springjwt.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

public class UserController {

@Autowired
    private UserInfoService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

   @Autowired
    private JwtService jwtService  ;

   @Autowired
    private AuthenticationManager authenticationManager;


   @GetMapping("/index")
   public  String index(){
       return "Servicio index";
   }


   @PostMapping("/registrame")
   public  String registrame(@RequestBody UserInfo userInfo){
      return service.guardarUser(userInfo);
   }
   @PostMapping("/admin/test")
   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public  String soloAdmin (){
       return "Este endpint es solo para admin";
   }

@PostMapping("/user/test")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
   public  String parauser (){
       return  "Este es pa user";
   }
   @PostMapping("/login")
   public  String login (@RequestBody AuthRequest  authRequest){
       try {
           Authentication authentication =
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword())
           )   ;
           if (authentication.isAuthenticated()){
               return jwtService.generateToken(authRequest.getUsername());
           }else {
               System.out.println("No valido");
               throw  new UsernameNotFoundException("Ususario no valido");

           }
       }catch (Exception e){
           System.out.println("No valido catch");

           throw  new UsernameNotFoundException("Ususario no valido");

       }
   }

}
