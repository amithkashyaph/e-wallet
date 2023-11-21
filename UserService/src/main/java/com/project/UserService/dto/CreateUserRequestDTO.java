package com.project.UserService.dto;

import com.project.Commons.UserIdentifier;
import com.project.UserService.model.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequestDTO {
    private String name;

    @NotBlank
    private String contact;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private UserIdentifier userIdentifier;

    private String userIdentifierValue;


    public User toUser() {
        return User.builder().contact(contact)
                .name(name)
                .email(email).password(password)
                .userIdentifier(userIdentifier)
                .userIdentifierValue(userIdentifierValue)
                .build();
    }
}
