package com.hab.birrama.auth;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
            return ResponseEntity.ok(service.authenticate(request));
    }

    // this my code to authenticate the token save in client
    @PostMapping("/token-auth")
    public ResponseEntity<AuthenticationResponse> tokenAuth(
            @RequestBody AuthenticationResponse request
    ) {
        return ResponseEntity.ok(service.tokenAuth(request));
    }



    //this is my code to get user detail
    @PostMapping("/getuser")
    public ResponseEntity<UserResponse> getUser(
            @RequestBody UserRequest request
    ){
        return ResponseEntity.ok(service.getUser(request));
    }

    // this to update user detail
    @PostMapping("/update-user")
    public ResponseEntity<AuthenticationResponse> updateUser(
            @RequestBody UserUpdateRequest request
    ){
        return ResponseEntity.ok(service.updateUser(request));
    }

    // forget password request this to send email
    @PostMapping("/forget-password")
    public ResponseEntity<AuthenticationResponse> forgetPassword(
            @RequestBody ForgetPasswordRequest request
    ) throws MessagingException {
       return ResponseEntity.ok(service.forgetPassword(request));
    }

    // reset password request
    @PostMapping("/reset-password")
    public ResponseEntity<AuthenticationResponse> resetPassword(
            @RequestBody ResetPasswordRequest request
    ){
        return ResponseEntity.ok(service.resetPassword(request));
    }


}
