package ar.lamansys.sgh.publicapi.userinformation.infrastructure.output;

import ar.lamansys.sgh.publicapi.userinformation.application.port.out.PublicUserStorage;
import ar.lamansys.sgh.publicapi.userinformation.domain.authorities.PublicAuthorityBo;
import ar.lamansys.sgh.publicapi.userinformation.domain.PublicUserInfoBo;

import ar.lamansys.sgh.shared.infrastructure.input.service.RoleInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedHospitalUserPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPermissionPort;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PublicUserStorageImpl implements PublicUserStorage {

	private final SharedPermissionPort sharedPermissionPort;

	private final SharedHospitalUserPort sharedHospitalUserPort;

	public PublicUserStorageImpl(SharedPermissionPort sharedPermissionPort, SharedHospitalUserPort sharedHospitalUserPort) {
		this.sharedPermissionPort = sharedPermissionPort;
		this.sharedHospitalUserPort = sharedHospitalUserPort;
	}

	@Override
	public Optional<PublicUserInfoBo> fetchUserInfoFromToken(String token) {
		return sharedHospitalUserPort.fetchUserInfoFromNormalToken(token)
				.map(user -> new PublicUserInfoBo(user.getId(), user.getUsername()));
	}

	@Override
	public List<PublicAuthorityBo> fetchRolesFromToken(String token) {
		return sharedPermissionPort.fetchAuthoritiesFromToken(token).stream()
				.map(this::mapTo)
				.collect(Collectors.toList());
	}

	private PublicAuthorityBo mapTo(RoleInfoDto roleInfoDto) {
		return new PublicAuthorityBo(roleInfoDto.getId(), roleInfoDto.getInstitution(), roleInfoDto.getValue());
	}
}
