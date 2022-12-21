package ar.lamansys.sgx.auth.user.domain.notification;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RestorePasswordNotificationBo {

	public final Integer userId;
	public final String token;

}
