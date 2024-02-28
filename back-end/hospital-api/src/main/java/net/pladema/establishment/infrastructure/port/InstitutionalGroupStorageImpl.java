package net.pladema.establishment.infrastructure.port;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.application.port.InstitutionalGroupStorage;

import net.pladema.establishment.repository.InstitutionalGroupRepository;

import net.pladema.establishment.service.domain.InstitutionalGroupBo;

import net.pladema.permissions.repository.enums.ERole;
import net.pladema.permissions.service.LoggedUserService;
import net.pladema.user.application.port.UserRoleStorage;
import net.pladema.user.domain.UserRoleBo;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class InstitutionalGroupStorageImpl implements InstitutionalGroupStorage {

	private final InstitutionalGroupRepository institutionalGroupRepository;

	private final UserRoleStorage userRoleStorage;

	@Override
	public List<InstitutionalGroupBo> getInstitutionalGroupsByUserId(Integer userId){
		log.debug("Input parameters -> userId {}", userId);
		List<InstitutionalGroupBo> result = Collections.emptyList();
		Optional<Short> managerRole = userRoleStorage.getRolesByUser(userId).stream()
				.filter(userRoleBo -> (userRoleBo.getRoleId() == ERole.GESTOR_DE_ACCESO_DE_DOMINIO.getId()
							|| userRoleBo.getRoleId() == ERole.GESTOR_DE_ACCESO_LOCAL.getId()
							|| userRoleBo.getRoleId() == ERole.GESTOR_DE_ACCESO_REGIONAL.getId()))
				.map(UserRoleBo::getRoleId)
				.findFirst();
		if (managerRole.isPresent()){
			if (managerRole.get().equals(ERole.GESTOR_DE_ACCESO_DE_DOMINIO.getId()))
				result = institutionalGroupRepository.findAll(Sort.by(Sort.Order.asc("name"))).stream().map(InstitutionalGroupBo::new).collect(Collectors.toList());
			else
				result = institutionalGroupRepository.getInstitutionalGroupByUserId(userId).stream().map(InstitutionalGroupBo::new).collect(Collectors.toList());
		}
		log.debug("Output -> result {}", result);
		return result;
	}

}
