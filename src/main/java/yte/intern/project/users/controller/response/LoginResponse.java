package yte.intern.project.users.controller.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LoginResponse {
    private final String jwt;

}
