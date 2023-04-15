package com.umg.ws.bc.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @NotNull
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @NotNull
    @Size(max = 100)
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 50)
    private String password;

    private Set<String> role;
}
