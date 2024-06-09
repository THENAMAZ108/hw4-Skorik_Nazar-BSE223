package com.example.security.auth;

import com.example.security.config.JwtService;
import com.example.security.session.Session;
import com.example.security.session.SessionRepository;
import com.example.security.user.Role;
import com.example.security.user.User;
import com.example.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegistrationResponse register(RegistrationRequest request) {

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Пользователь с таким email уже зарегистрирован."
            );
        });

        if (isNicknameEmpty(request.getNickname())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Никнейм не может быть пустой строкой."
            );
        }

        if (!emailMatcher(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Неверный формат email."
            );
        }

        if (!passwordMatcher(request.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Пароль должен состоять из не менее восьми символов, включая буквы обоих регистров, " +
                            "цифры и специальные символы."
            );
        }

        var user = User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.User)
                .created(new Date())
                .build();

        userRepository.save(user);

        return new RegistrationResponse(
                HttpStatus.OK + ": Регистрация успешно завершена."
        );
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Неправильный email или пароль."
            );
        }

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Пользователь не найден."
                ));

        var jwtToken = jwtService.generateToken(user);

        sessionRepository.deleteByUserId(user.getId());

        var session = new Session();
        session.setUserId(user.getId());
        session.setToken(jwtToken);
        session.setExpires(jwtService.extractExpiration(jwtToken));

        sessionRepository.save(session);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .response(HttpStatus.OK + ": Успешная аутентификация. Время истечения срока действия сеанса: 2 минуты.")
                .build();
    }

    private boolean emailMatcher(String email) {
        String emailPattern =
                "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        return email.matches(emailPattern);
    }

    private boolean passwordMatcher(String password) {
        String passwordPattern =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }

    private boolean isNicknameEmpty(String nickname) {
        return nickname.isEmpty();
    }
}
