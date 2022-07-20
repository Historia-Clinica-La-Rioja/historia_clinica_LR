package ar.lamansys.sgx.auth.user.domain.user.model;

import ar.lamansys.sgx.auth.user.domain.userpassword.UserPasswordBo;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserBo {

    @Getter
    private Integer id;

    @Getter
    private String username;

    @Getter
    private boolean enable;

    @Getter
    private LocalDateTime lastLogin;

	@Getter
	private LocalDateTime previousLogin;

    private UserPasswordBo userPasswordBo;

    public UserBo(String username){
        this.username = username;
    }
    public UserBo(Integer id,String username, Boolean enable){
        this.id = id;
        this.username = username;
        this.enable = enable;
    }

    public UserBo(String username, String password, String salt, String hashAlgorithm) {
        this(null, username, false, password, salt, hashAlgorithm);
    }

    public UserBo(Integer id, String username, boolean enable, String password, String salt, String hashAlgorithm) {
        this(id, username, enable, password, salt, hashAlgorithm, null, null);
    }

    public UserBo(Integer id, String username, boolean enable, String password, String salt, String hashAlgorithm, LocalDateTime lastLogin, LocalDateTime previousLogin) {
        validations(username);
        this.id = id;
        this.username = username;
        this.enable = enable;
        this.lastLogin = lastLogin;
		this.previousLogin = previousLogin;
        this.userPasswordBo = new UserPasswordBo(password, salt, hashAlgorithm);
    }

    private void validations(String username) {
        Objects.requireNonNull(username, () -> {
            throw new UserException(UserEnumException.NULL_USERNAME, "El username es obligatorio");
        });
    }

    public void setUserPasswordBo(String password, String salt, String hashAlgorithm) {
        this.userPasswordBo = new UserPasswordBo(password, salt, hashAlgorithm);
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPassword(){
        return userPasswordBo != null ? userPasswordBo.getPassword() : null;
    }

    public String getSalt(){
        return userPasswordBo != null ? userPasswordBo.getSalt() : null;
    }

    public String getHashAlgorithm(){
        return userPasswordBo != null ? userPasswordBo.getHashAlgorithm() : null;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setUsername(String username){
        this.username = username;
    }

	public void setPrevoiusLogin(LocalDateTime previousLogin) {
		this.previousLogin = previousLogin;
	}
}
