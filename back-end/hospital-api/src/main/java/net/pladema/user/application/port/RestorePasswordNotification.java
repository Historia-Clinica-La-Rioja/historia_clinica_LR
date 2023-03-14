package net.pladema.user.application.port;

import net.pladema.user.domain.notification.RestorePasswordNotificationBo;

public interface RestorePasswordNotification {

	String run(RestorePasswordNotificationBo restorePasswordNotificationBo);

}
