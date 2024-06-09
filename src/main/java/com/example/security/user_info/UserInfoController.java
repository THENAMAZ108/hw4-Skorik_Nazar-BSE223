package com.example.security.user_info;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/userinfo")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping
    public ResponseEntity<UserInfoResponse> sayHello(HttpServletRequest request) {
        ResponseStatusException exception = (ResponseStatusException) request.getAttribute("jwtException");
        if (exception != null) {
            return ResponseEntity
                    .status(exception.getStatusCode())
                    .body(new UserInfoResponse(
                            null,
                            null,
                            null,
                            exception.getStatusCode() + ": " + exception.getReason()));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        UserInfoResponse userInfoResponse = userInfoService.getUserInfo(email);

        return ResponseEntity.ok(userInfoResponse);
    }

}
