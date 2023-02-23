package net.pladema.user.infrastructure.output.notification;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.user.application.port.HospitalUserStorage;
import net.pladema.user.domain.notification.RestorePasswordNotificationBo;
import net.pladema.user.application.port.RestorePasswordNotification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.user.infrastructure.output.notification.exceptions.RestorePasswordNotificationException;

import net.pladema.user.infrastructure.output.notification.exceptions.RestorePasswordNotificationExceptionEnum;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class RestorePasswordNotificationImpl implements RestorePasswordNotification {

	private final HospitalUserStorage hospitalUserStorage;
	private final UserNotificationSender userNotificationSender;
	private final FeatureFlagsService featureFlagsService;
	protected static final String ROUTE = "/auth/password-reset/";

 	@Override
	public String run(RestorePasswordNotificationBo restorePasswordNotificationBo) {
		var userPersonInfo = hospitalUserStorage.getUserPersonInfo(restorePasswordNotificationBo.userId).get();
		if ((userPersonInfo.getEmail() == null || userPersonInfo.getEmail().isBlank())){
			throw new RestorePasswordNotificationException(RestorePasswordNotificationExceptionEnum.INVALID_EMAIL, String.format("El usuario con id %s no posee una cuenta de correo válida.", userPersonInfo.getId()));
		}
		var notificationArgs = RestorePasswordNotificationArgs.builder();

		String link = ROUTE + restorePasswordNotificationBo.token;

		String fullName = (featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && userPersonInfo.getNameSelfDetermination() != null)
				? userPersonInfo.getNameSelfDetermination()+ " " + userPersonInfo.getLastName() + (userPersonInfo.getOtherLastNames() != null ? " " + userPersonInfo.getOtherLastNames() : "")
				: userPersonInfo.getFirstName() + " " + (userPersonInfo.getMiddleNames() != null ? userPersonInfo.getMiddleNames() + " " : "" ) + userPersonInfo.getLastName() + (userPersonInfo.getOtherLastNames() != null ? " " + userPersonInfo.getOtherLastNames() : "");

		notificationArgs.fullname(fullName);
		notificationArgs.link(link);
		userNotificationSender.send(
				new UserRecipient(restorePasswordNotificationBo.userId),
				new RestorePasswordTemplateInput(notificationArgs.build())
		);
		return userPersonInfo.getEmail();
	}

}
