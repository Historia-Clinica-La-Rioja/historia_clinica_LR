package ar.lamansys.sgh.publicapi.userinformation.infrastructure.output;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.publicapi.userinformation.application.port.out.FetchUserPersonFromTokenStorage;
import ar.lamansys.sgh.publicapi.userinformation.domain.FetchUserPersonFromTokenBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class FetchUserPersonFromTokenStorageImpl implements FetchUserPersonFromTokenStorage {

	private final SharedHospitalUserPort sharedHospitalUserPort;

	private final FetchUserPersonFromTokenRepository repository;

	@Override
	public Optional<FetchUserPersonFromTokenBo> getUserPersonFromToken(String token) {
		return sharedHospitalUserPort.fetchUserInfoFromNormalToken(token)
				.flatMap(info -> repository.findById(info.getId()))
				.map(result -> new FetchUserPersonFromTokenBo(
						result.getId(),
						result.getSub(),
						result.getEmail(),
						result.getGivenName(),
						result.getFamilyName(),
						result.getCuil(),
						result.getIdentificationType(),
						result.getIdentificationNumber(),
						result.getGender()
				));
		}
}
