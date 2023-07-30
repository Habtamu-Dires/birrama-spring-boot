package com.hab.birrama.auth;


import com.hab.birrama.config.JwtService;
import com.hab.birrama.exception.NotFoundException;
import com.hab.birrama.token.Token;
import com.hab.birrama.token.TokenRepository;
import com.hab.birrama.token.TokenType;
import com.hab.birrama.user.Role;
import com.hab.birrama.user.User;
import com.hab.birrama.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        var savedUser = repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        //save token
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            ); //in case the email or password not correct an exception will be thrown
            //so at this point the user is authenticated
            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();

            revokeAllUserToken(user);

            var jwtToken = jwtService.generateToken(user);

            //save token
            saveUserToken(user, jwtToken);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();

    }

    //this is my code to get the user using token
    public UserResponse getUser(UserRequest request){

        var token = tokenRepository.findByToken(request.token)
                .orElseThrow(
                        () -> new RuntimeException("user with the token not found")
                );

        if(token.isRevoked() || token.isExpired()){
            throw new NotFoundException(
                    "InValid Exception"
            );
        }

        var user = token.getUser();

        return UserResponse.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .build();
    }

    private void revokeAllUserToken(User user) {
        var validUserToken = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserToken.isEmpty())
            return;
        validUserToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepository.saveAll(validUserToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();

        tokenRepository.save(token);
    }

    // my code to check if the token save locally in client is valid
    public AuthenticationResponse tokenAuth(AuthenticationResponse request) {
       var token = tokenRepository.findByToken(request.getToken()).
               orElseThrow(
                       () -> new RuntimeException("Invalid Token")
               );

           if(token.isRevoked() || token.isExpired()){
               throw new NotFoundException(
                       "InValid Exception"
               );
           }
        return  AuthenticationResponse.builder()
                .token(token.getToken())
                .build();
    }

    // my code to
    public AuthenticationResponse updateUser(UserUpdateRequest request) {
        var token = tokenRepository.findByToken(request.getToken())
                .orElseThrow();

        var user = token.getUser();
        try {
            user.setEmail(request.getEmail());
            user.setFirstname(request.getFirstname());
            user.setLastname(request.getLastname());
            repository.save(user);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        return  AuthenticationResponse.builder()
                .token(token.getToken())
                .build();
    }

    public AuthenticationResponse forgetPassword(ForgetPasswordRequest request) throws MessagingException {
        var user = repository.findByEmail(request.email)
                .orElseThrow();

        //revoke all token
        revokeAllUserToken(user);
        //generate new token
        var jwtToken = jwtService.generateToken(user);
        //save token
        saveUserToken(user, jwtToken);

        String clientURl = "http://localhost:3000";

        String link = String.format("" +
                "%s/reset-password?token=%s",
                clientURl,jwtToken);

        String body  = String.format("" +
                "<h3>Hello, %s </h3><br/>" +
                "<h4>You requested to reset your Birrama Admin password</h4>" +
                "<h4>Please, click the link below to reset your password</h4>" +
                "<a href='%s'>Reset Password</a><br />" +
                "<p>Ignore this message if it was not you.</p>",
                user.getFirstname(), link);

        emailService.sendEmail(
                user.getEmail(),
                "Birrama Admin Password Recovery",
                body
        );

        return AuthenticationResponse.builder().
                token("Recovery link has been sent to your email")
                .build();
    }

    public AuthenticationResponse resetPassword(ResetPasswordRequest request) {

        var token = tokenRepository.findByToken(request.token)
                .orElseThrow();

        if(token.isRevoked() || token.isExpired()){
            throw new NotFoundException(
                    "InValid Exception"
            );
        }

        var user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        repository.save(user);

        revokeAllUserToken(user);
        //generate new token
        var jwtToken = jwtService.generateToken(user);
        //save token
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder().
                token(jwtToken)
                .build();
    }
}