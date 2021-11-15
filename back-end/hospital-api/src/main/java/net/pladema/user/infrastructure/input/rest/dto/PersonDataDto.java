package net.pladema.user.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
public class PersonDataDto {

    @Nullable
    private String firstName;

    @Nullable
    private String lastName;

    private String identificationType;

    private String identificationNumber;

    private Integer userId;

    @Nullable
    private String username;
}
