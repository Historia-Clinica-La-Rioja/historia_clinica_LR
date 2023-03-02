package net.pladema.establishment.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeSnomedGroupValidator;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.rest.dto.BackofficeDeleteResponse;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.SnomedRelatedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedGroup;
import net.pladema.snowstorm.repository.entity.SnomedGroupType;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;

@RestController
@RequestMapping("backoffice/institutionpractices")
public class BackofficeInstitutionPracticesController extends BackofficeSnomedGroupController{

	private final SnomedGroupRepository snomedGroupRepository;
	private final SnomedRelatedGroupRepository snomedRelatedGroupRepository;
	private final BackofficeAuthoritiesValidator authoritiesValidator;

	public BackofficeInstitutionPracticesController(SnomedGroupRepository repository, DateTimeProvider dateTimeProvider, SnomedGroupRepository snomedGroupRepository, BackofficeSnomedGroupValidator backofficeSnomedGroupValidator, BackofficeAuthoritiesValidator authoritiesValidator, SnomedRelatedGroupRepository snomedRelatedGroupRepository, BackofficeAuthoritiesValidator authoritiesValidator1) {
		super(repository, dateTimeProvider, snomedGroupRepository, backofficeSnomedGroupValidator);
		this.snomedGroupRepository = snomedGroupRepository;
		this.snomedRelatedGroupRepository = snomedRelatedGroupRepository;
		this.authoritiesValidator = authoritiesValidator1;
	}

	@Override
	public Page<SnomedGroup> getList(Pageable pageable, SnomedGroup entity) {
		Integer procedureId = snomedGroupRepository.getIdByDescription(SnomedECL.PROCEDURE.toString());
		String ecl = snomedGroupRepository.getById(procedureId).getEcl();
		entity.setEcl(ecl);
		entity.setGroupType(SnomedGroupType.SEARCH_GROUP);

		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR))
			return super.getList(pageable, entity);

		var allTerms = super.getList(Pageable.ofSize(Integer.MAX_VALUE), entity);
		var allowedInstitutions = authoritiesValidator.allowedInstitutionIds(List.of(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
		var practicesList = allTerms.getContent()
				.stream()
				.filter(item -> allowedInstitutions.contains(item.getInstitutionId()))
				.collect(Collectors.toList());
		int minIndex = pageable.getPageNumber() * pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(practicesList.subList(minIndex, Math.min(maxIndex, practicesList.size())), pageable, practicesList.size());
	}

	@Override
	public SnomedGroup create(@Valid @RequestBody SnomedGroup entity) {
		boolean hasPersisted = snomedGroupRepository.findByInstitutionIdAndGroupIdAndGroupType(
				entity.getInstitutionId(), entity.getGroupId(), entity.getGroupType()).isPresent();
		if(hasPersisted)
			throw new BackofficeValidationException("Esta institución ya posee un grupo de prácticas");
		return super.create(entity);
	}

	@Override
	public BackofficeDeleteResponse<Integer> delete(@PathVariable("id") Integer id) {
		if(authoritiesValidator.hasRole(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE)) {
			if(isDeletable(id))
				snomedGroupRepository.deleteById(id);
			else
				throw new BackofficeValidationException("El grupo tiene prácticas asociadas, por favor elimine las prácticas");
		}
		return new BackofficeDeleteResponse<>(id);
	}

	private Boolean isDeletable(Integer groupId) {
		Integer lastOrden = snomedRelatedGroupRepository.getLastOrdenByGroupId(groupId).orElse(null);
		return lastOrden == null;
	}
}
