package net.pladema.permissions.service.impl;

import net.pladema.permissions.repository.UserRoleRepository;
import net.pladema.permissions.repository.entity.UserRole;
import net.pladema.permissions.service.InstitutionRoleAssignmentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstitutionRoleAssignmentServiceImpl implements InstitutionRoleAssignmentService {

	private static final Logger logger = LoggerFactory.getLogger(InstitutionRoleAssignmentServiceImpl.class);
	private final UserRoleRepository userRoleRepository;

	public InstitutionRoleAssignmentServiceImpl(UserRoleRepository userRoleRepository) {
		this.userRoleRepository = userRoleRepository;
	}

	@Override
	public void removeAllPermissionsFromInstitution(Integer institutionId) {
		logger.debug("Input parameters -> institutionId {}", institutionId);
		List<UserRole> toDelete = userRoleRepository.findByInstitutionId(institutionId);
		userRoleRepository.deleteAll(toDelete);
		logger.debug("Output -> {}",Boolean.TRUE);
	}
}
