package net.pladema.establishment.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.dates.configuration.DateTimeProvider;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeSnomedGroupValidator;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.sgx.backoffice.rest.dto.BackofficeDeleteResponse;
import net.pladema.snowstorm.repository.SnomedGroupRepository;
import net.pladema.snowstorm.repository.entity.SnomedGroup;
import net.pladema.snowstorm.repository.entity.SnomedGroupType;
import net.pladema.snowstorm.services.domain.semantics.SnomedECL;
import net.pladema.user.controller.BackofficeAuthoritiesValidator;

@RestController
@RequestMapping("backoffice/institutionpractices")
public class BackofficeInstitutionPracticesController extends BackofficeSnomedGroupController{

	private final SnomedGroupRepository snomedGroupRepository;
	private final BackofficeAuthoritiesValidator authoritiesValidator;

	public BackofficeInstitutionPracticesController(SnomedGroupRepository repository, DateTimeProvider dateTimeProvider, SnomedGroupRepository snomedGroupRepository, BackofficeSnomedGroupValidator backofficeSnomedGroupValidator, BackofficeAuthoritiesValidator authoritiesValidator, BackofficeAuthoritiesValidator authoritiesValidator1) {
		super(repository, dateTimeProvider, snomedGroupRepository, backofficeSnomedGroupValidator);
		this.snomedGroupRepository = snomedGroupRepository;
		this.authoritiesValidator = authoritiesValidator1;
	}

	@Override
	public Page<SnomedGroup> getList(Pageable pageable, SnomedGroup entity) {
		var page = super.getList(Pageable.ofSize(Integer.MAX_VALUE), entity);
		Integer procedureId = snomedGroupRepository.getIdByDescription(SnomedECL.PROCEDURE.toString());
		String ecl = snomedGroupRepository.getById(procedureId).getEcl();

		if (authoritiesValidator.hasRole(ERole.ROOT) || authoritiesValidator.hasRole(ERole.ADMINISTRADOR)) {
			var practicesList = page.getContent().stream()
					.filter(item -> (item.getEcl().equals(ecl)
							&& Objects.equals(item.getGroupType(), SnomedGroupType.SEARCH_GROUP)))
					.collect(Collectors.toList());
			return new PageImpl<>(practicesList, pageable, practicesList.size());
		}

		List<Integer> allowedInstitutions = authoritiesValidator.allowedInstitutionIds(Arrays.asList(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE));
		var practicesList = page.getContent().stream()
				.filter(item -> (item.getEcl().equals(ecl)
						&& Objects.equals(item.getGroupType(), SnomedGroupType.SEARCH_GROUP)
						&& allowedInstitutions.contains(item.getInstitutionId())))
				.collect(Collectors.toList());
		return new PageImpl<>(practicesList, pageable, practicesList.size());
	}

	@Override
	public BackofficeDeleteResponse<Integer> delete(@PathVariable("id") Integer id) {
		if(authoritiesValidator.hasRole(ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE))
			snomedGroupRepository.deleteById(id);
		return new BackofficeDeleteResponse<>(id);
	}
}
