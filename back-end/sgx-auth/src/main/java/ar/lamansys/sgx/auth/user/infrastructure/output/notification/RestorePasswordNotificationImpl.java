package ar.lamansys.sgx.auth.user.infrastructure.output.notification;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;
import ar.lamansys.sgx.auth.user.application.port.RestorePasswordNotification;
import ar.lamansys.sgx.auth.user.domain.notification.RestorePasswordNotificationBo;
import ar.lamansys.sgx.auth.user.infrastructure.output.notification.exceptions.RestorePasswordNotificationException;
import ar.lamansys.sgx.auth.user.infrastructure.output.notification.exceptions.RestorePasswordNotificationExceptionEnum;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@AllArgsConstructor
@Slf4j
@Service
public class RestorePasswordNotificationImpl implements RestorePasswordNotification {

	private final SharedHospitalUserPort sharedHospitalUserPort;
	private final UserNotificationSender userNotificationSender;
	private final FeatureFlagsService featureFlagsService;

	private final String ROUTE = "auth/password-reset/";

 	@Override
	public String run(RestorePasswordNotificationBo restorePasswordNotificationBo) {
		var userCompleteInfo = sharedHospitalUserPort.getUserCompleteInfo(restorePasswordNotificationBo.userId);
		if ((userCompleteInfo.getEmail() == null || userCompleteInfo.getEmail().isBlank())){
			throw new RestorePasswordNotificationException(RestorePasswordNotificationExceptionEnum.INVALID_EMAIL, String.format("El usuario con id %s no posee una cuenta de correo válida.", userCompleteInfo.getId()));
		}
		String fullName = (featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && userCompleteInfo.getNameSelfDetermination() != null)
				? userCompleteInfo.getNameSelfDetermination()
				: userCompleteInfo.getFirstName() + " " + userCompleteInfo.getLastName();
		var notificationArgs = RestorePasswordNotificationArgs.builder();
		String path = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
		String link = (path.substring(0, path.length()-3)).concat(ROUTE).concat(restorePasswordNotificationBo.token);
		notificationArgs.fullname(fullName);
		notificationArgs.link(link);
		userNotificationSender.send(
				new UserRecipient(restorePasswordNotificationBo.userId),
				new RestorePasswordTemplateInput(notificationArgs.build())
		);
		return userCompleteInfo.getEmail();
	}
}
