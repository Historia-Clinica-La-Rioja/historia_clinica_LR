package net.pladema.establishment.service.impl;

import ar.lamansys.sgx.auth.user.infrastructure.input.service.UserExternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.HierarchicalUnitRepository;
import net.pladema.establishment.repository.HierarchicalUnitStaffRepository;
import net.pladema.establishment.repository.entity.HierarchicalUnit;
import net.pladema.establishment.controller.service.exceptions.HierarchicalUnitStaffEnumException;
import net.pladema.establishment.controller.service.exceptions.HierarchicalUnitStaffException;
import net.pladema.establishment.repository.entity.HierarchicalUnitStaff;
import net.pladema.establishment.service.HierarchicalUnitStaffService;

import net.pladema.establishment.service.domain.HierarchicalUnitStaffBo;

import net.pladema.user.application.port.UserRoleStorage;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class HierarchicalUnitStaffServiceImpl implements HierarchicalUnitStaffService {

	private final HierarchicalUnitStaffRepository hierarchicalUnitStaffRepository;
	private final HierarchicalUnitRepository hierarchicalUnitRepository;
	private final UserExternalService userExternalService;
	private final UserRoleStorage userRoleStorage;

	@Override
	public List<HierarchicalUnitStaffBo> getByUserId(Integer userId, Integer institutionId) {
		log.debug("Input userId {}, institutionId {} ", userId, institutionId);
		List<HierarchicalUnitStaffBo> result = hierarchicalUnitStaffRepository.findByUserIdAndInstitutionId(userId, institutionId).stream()
				.map( hus -> {
					HierarchicalUnit hu = hierarchicalUnitRepository.findById(hus.getHierarchicalUnitId()).get();
					return new HierarchicalUnitStaffBo(hus.getId(), hus.isResponsible(), hu.getId(), hu.getAlias());
		}).collect(Collectors.toList());
		log.debug("Output {} ", result);
		return result;
	}

	@Override
	public Boolean updateHierarchicalUnits(Integer institutionId, Integer userId, List<HierarchicalUnitStaffBo> list) {
		log.debug("Input parameters userId {} , hierarchicalUnitStaffBo list {} ", userId, list);
		assertHierarchicalUnitStaff(userId, list);

		List<HierarchicalUnitStaff> personHU = hierarchicalUnitStaffRepository.findByUserIdAndInstitutionId(userId, institutionId);
		List<HierarchicalUnitStaff> deletedHUPerson = hierarchicalUnitStaffRepository.findDeletedByInstitutionIdAndPersonId(institutionId, userId);

		List<HierarchicalUnitStaff> newHU = new ArrayList<>();
		List<HierarchicalUnitStaff> huToUpdateDeleted = new ArrayList<>();
		List<HierarchicalUnitStaff> huToUpdateResponsible = new ArrayList<>();

		list.forEach(hu -> {

			HierarchicalUnitStaff hus = new HierarchicalUnitStaff(hu.getId(), hu.getHierarchicalUnitId(), userId, hu.isResponsible());

			boolean updateResponsible = personHU.stream()
					.anyMatch(hu1 -> hu1.equals(hus) && hus.isResponsible() != hu1.isResponsible());

			if (updateResponsible) {
				huToUpdateResponsible.add(hus);
			}

			if (deletedHUPerson.contains(hus)) {
				huToUpdateDeleted.add(hus);
				if (deletedHUPerson.stream().anyMatch(hu1 -> hu1.equals(hus) && hus.isResponsible() != hu1.isResponsible())) {
					huToUpdateResponsible.add(hus);
				}
			}
			else
				newHU.add(hus);
		});

		hierarchicalUnitStaffRepository.deleteAll(huToDelete(personHU, newHU));

		boolean hasRole = userRoleStorage.hasRoleInInstitution(userId, institutionId);

		if (hasRole) {
			huToUpdateResponsible.forEach(hu -> hierarchicalUnitStaffRepository.updateResponsible(userId, hu.getHierarchicalUnitId(), hu.isResponsible()));
			huToUpdateDeleted.forEach(hu -> hierarchicalUnitStaffRepository.setDeletedFalse(userId, hu.getHierarchicalUnitId()));
			hierarchicalUnitStaffRepository.saveAll(huToCreate(newHU, personHU));
			log.debug("Output {} ", Boolean.TRUE);
		}

		return Boolean.TRUE;
	}

	private void assertHierarchicalUnitStaff(Integer userId, List<HierarchicalUnitStaffBo> list) {
		Boolean existsUser = userExternalService.findById(userId);
		if (existsUser == false)
			throw new HierarchicalUnitStaffException(HierarchicalUnitStaffEnumException.NO_USER_EXISTS, "establishment.errors.hierarchical-unit-staff.no.user");

		Boolean enabled = userExternalService.userIsEnabled(userId);
		if (enabled == false)
			throw new HierarchicalUnitStaffException(HierarchicalUnitStaffEnumException.USER_DISABLED, "establishment.errors.hierarchical-unit-staff.user.disabled");

		list.forEach(hus -> {
			Optional<HierarchicalUnitStaff> alreadyExistsStaff = hierarchicalUnitStaffRepository.findByHierarchicalUnitIdAndUserId(hus.getHierarchicalUnitId(), userId);
			if ( alreadyExistsStaff.isPresent() && !hus.getId().equals(alreadyExistsStaff.get().getId()))
				throw new HierarchicalUnitStaffException(HierarchicalUnitStaffEnumException.ALREADY_HIERARCHICAL_UNIT_STAFF, "establishment.errors.hierarchical-unit-staff.user.already.staff");
		});
	}

	private List<HierarchicalUnitStaff> huToCreate(List<HierarchicalUnitStaff> newHU, List<HierarchicalUnitStaff> currentHU) {
		return newHU.stream()
				.filter(hu -> currentHU.stream().noneMatch(hu::equals))
				.collect(Collectors.toList());
	}
	private List<HierarchicalUnitStaff> huToDelete(List<HierarchicalUnitStaff> currentHU, List<HierarchicalUnitStaff> newHU) {
		return currentHU.stream()
				.filter(hu -> newHU.stream().noneMatch(hu::equals))
				.collect(Collectors.toList());
	}

}
