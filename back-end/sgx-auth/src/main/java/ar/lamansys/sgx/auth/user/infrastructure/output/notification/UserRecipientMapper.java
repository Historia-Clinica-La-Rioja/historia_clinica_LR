package ar.lamansys.sgx.auth.user.infrastructure.output.notification;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.notifications.application.RecipientMapper;
import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import lombok.AllArgsConstructor;


import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserRecipientMapper implements RecipientMapper<UserRecipient> {

	private final SharedHospitalUserPort sharedHospitalUserPort;
	private final FeatureFlagsService featureFlagsService;

	@Override
	public Optional<RecipientBo> toRecipient(UserRecipient userRecipient) {
		return Optional.of(sharedHospitalUserPort.getUserCompleteInfo(userRecipient.userId))
				.map(userCompleteInfo -> new RecipientBo(
								(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) && userCompleteInfo.getNameSelfDetermination() != null)
								? userCompleteInfo.getNameSelfDetermination()
								: userCompleteInfo.getFirstName(),
						userCompleteInfo.getLastName(),
						userCompleteInfo.getEmail(),
						null));
	}

}
