package net.pladema.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDataBo {

    private Integer id;
    private String username;
    private Boolean enable;
    private LocalDateTime lastLogin;

    public UserDataBo(Integer id){
        this(id,null,null);
    }
    public UserDataBo(Integer id, String username, Boolean enable){
        this.id = id;
        this.username = username;
        this.enable = enable;
    }
}