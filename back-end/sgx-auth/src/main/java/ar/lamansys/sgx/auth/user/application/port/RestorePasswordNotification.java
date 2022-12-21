package ar.lamansys.sgx.auth.user.application.port;

import ar.lamansys.sgx.auth.user.domain.notification.RestorePasswordNotificationBo;

public interface RestorePasswordNotification {

	String run(RestorePasswordNotificationBo restorePasswordNotificationBo);

}
