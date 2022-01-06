package net.pladema.user.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserDataDto {
    @Nullable
    private Integer id;
    @Nullable
    private String username;
    @Nullable
    private Boolean enable;
    @Nullable
    private LocalDateTime lastLogin;

    public UserDataDto(Integer id){
        this.id = id;
    }
}
