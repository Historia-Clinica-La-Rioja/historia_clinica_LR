package ar.lamansys.sgh.publicapi.userinformation.infrastructure.output;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.userinformation.application.port.out.FetchUserProfessionalLicensesFromTokenStorage;
import ar.lamansys.sgh.publicapi.userinformation.domain.FetchUserProfessionalLicensesFromTokenBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FetchUserProfessionalLicensesFromTokenStorageImpl implements FetchUserProfessionalLicensesFromTokenStorage {

	private final SharedStaffPort sharedStaffPort;
	private final SharedHospitalUserPort sharedHospitalUserPort;

	@Override
	public Optional<List<FetchUserProfessionalLicensesFromTokenBo>> getProfessionalLicensesFromToken(String token) {
		var userInfo = sharedHospitalUserPort.fetchUserInfoFromNormalToken(token);
		if (userInfo.isPresent()) {
			var healthcareProfessionalId = sharedStaffPort.getProfessionalId(userInfo.get().getId());

			return Optional.of(sharedStaffPort.getLicenses(healthcareProfessionalId).orElse(List.of())
					.stream()
					.map(l -> FetchUserProfessionalLicensesFromTokenBo.builder()
							.licenseNumber(l.getNumber()).
									licenseType(l.getType()).build())
					.collect(Collectors.toList()));
		}
		return Optional.empty();
	}
}
