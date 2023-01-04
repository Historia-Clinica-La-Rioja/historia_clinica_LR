package net.pladema.user.domain.notification;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RestorePasswordNotificationBo {

	public final Integer userId;
	public final String token;

}
