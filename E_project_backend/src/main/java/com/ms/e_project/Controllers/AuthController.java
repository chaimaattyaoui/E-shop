package com.ms.e_project.Controllers;

import com.ms.e_project.Entities.User;
import com.ms.e_project.Repository.UserRepository;
import com.ms.e_project.Services.auth.AuthService;
import com.ms.e_project.dto.AuthenticationRequest;
import com.ms.e_project.dto.SignupRequest;
import com.ms.e_project.dto.UserDto;
import com.ms.e_project.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthService authService;

    // Define constants for headers and token prefix
    private static final String HEADER_STRING = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(Map.of("error", "Invalid username or password"), HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());

        if (optionalUser.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("userId", optionalUser.get().getId());
            response.put("role", optionalUser.get().getRole());
            response.put("token", TOKEN_PREFIX + jwt);

            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HEADER_STRING, TOKEN_PREFIX + jwt);
            headers.add("Access-Control-Expose-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-Header");

            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }

        return new ResponseEntity<>(Map.of("error", "User not found"), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        if (authService.hasUserWithEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }
        UserDto userDto = authService.createUser(signupRequest);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }
}