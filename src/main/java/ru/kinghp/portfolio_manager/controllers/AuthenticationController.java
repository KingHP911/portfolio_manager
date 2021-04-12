package ru.kinghp.portfolio_manager.controllers;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.kinghp.portfolio_manager.security.AuthRequest;
import ru.kinghp.portfolio_manager.security.AuthResponse;
import ru.kinghp.portfolio_manager.security.JWTService;

@Data
@RestController
public class AuthenticationController {

    final private AuthenticationManager authenticationManager;
    final private JWTService jwtService;

    @PostMapping("/authenticate")
    public AuthResponse createAuthenticationToken(@RequestBody AuthRequest authRequest) {

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Имя или пароль неправильны", e);
        }
        // при создании токена в него кладется username как Subject claim
        String jwt = jwtService.generateToken((UserDetails) authentication.getPrincipal());

        AuthResponse response = new AuthResponse();
        response.setJwtToken(jwt);
        return response;
    }
}
