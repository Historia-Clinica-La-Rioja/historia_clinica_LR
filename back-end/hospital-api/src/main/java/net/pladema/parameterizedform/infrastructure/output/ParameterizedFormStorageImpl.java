package net.pladema.parameterizedform.infrastructure.output;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.InstitutionalParameterizedFormRepository;
import net.pladema.establishment.repository.entity.InstitutionalParameterizedForm;
import net.pladema.parameterizedform.application.port.output.ParameterizedFormStorage;


import net.pladema.parameterizedform.domain.enums.EFormScope;
import net.pladema.parameterizedform.domain.enums.EFormStatus;

import net.pladema.parameterizedform.domain.ParameterizedFormBo;
import net.pladema.parameterizedform.infrastructure.input.rest.dto.ParameterizedFormDto;
import net.pladema.parameterizedform.infrastructure.output.repository.ParameterizedFormRepository;

import net.pladema.parameterizedform.infrastructure.output.repository.entity.ParameterizedForm;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParameterizedFormStorageImpl implements ParameterizedFormStorage {

	private final static String OUTPUT = "Output -> result {}";

	private final ParameterizedFormRepository parameterizedFormRepository;
	private final InstitutionalParameterizedFormRepository institutionalParameterizedFormRepository;

	@Override
	public void updateStatus(Integer formId) {
		log.debug("Input parameters -> formId {}", formId);
		parameterizedFormRepository.findById(formId).ifPresent(form -> {
			EFormStatus actualState = EFormStatus.map(form.getStatusId());
			EFormStatus nextState = actualState.getNextState();
			if (form.getIsDomain())
				assertDomainFormName(formId, form.getName(), nextState);
			else {
				List<InstitutionalParameterizedForm> institutionalParameterizedForm = institutionalParameterizedFormRepository.getByParameterizedFormId(formId);
				var institutionId = institutionalParameterizedForm.get(0).getInstitutionId();
				assertEnabledDomainParameterizedFormName(formId, form.getName(), nextState, institutionId);
				assertInstitutionalFormName(formId, form.getName(), nextState, institutionId);
			}
			parameterizedFormRepository.updateStatusByFormId(formId, nextState.getId());
			handleInstitutionalFormStatus(formId, nextState, form.getIsDomain());
		});
	}

	@Override
	public Page<ParameterizedFormDto> getFormByFilters(List<Short> statusIds, String name, Boolean isDomain, Pageable pageable) {
		log.debug("Input parameters -> statusIds {}, name {}, isDomain {}, pageable {}", statusIds, name, isDomain, pageable);
		Page<ParameterizedForm> resultParameterizedFormPage = (name == null || name.trim().isEmpty()) ?	parameterizedFormRepository.getFormsByStatusAndDomain(statusIds, isDomain, pageable) : parameterizedFormRepository.getFormsByFilters(statusIds, isDomain, name, pageable);

		List<ParameterizedFormDto> result = resultParameterizedFormPage.getContent().stream()
				.map(this::mapEntityToDto)
				.collect(Collectors.toList());

		Page<ParameterizedFormDto> resultPage = new PageImpl<>(
				result,
				resultParameterizedFormPage.getPageable(),
				resultParameterizedFormPage.getTotalElements()
		);

		log.debug("Output -> result {}", resultPage);
		return resultPage;
	}

	@Override
	public Optional<Short> findFormStatus(Integer formId) {
		log.debug("Input parameters -> formId {}", formId);
		Optional<Short> result = parameterizedFormRepository.findStatusById(formId);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public void updateFormEnablementInInstitution(Integer parameterizedFormId, Integer institutionId, Boolean enablement) {
		log.debug("Input parameters -> parameterizedFormId {}, institutionId {}, enablement {}", parameterizedFormId, institutionId, enablement);
		parameterizedFormRepository.findById(parameterizedFormId).ifPresent(
				parameterizedForm -> {
					assertEnablementValidations(parameterizedForm, institutionId, enablement);
					institutionalParameterizedFormRepository.findByParameterizedFormIdAndInstitutionId(parameterizedFormId, institutionId).ifPresentOrElse(
							institutionalParameterizedForm -> updateInsitutionalParameterizedForm(institutionalParameterizedForm, enablement),
							() -> saveInsitutionalParameterizedForm(parameterizedForm.getId(), institutionId, enablement)
					);
				}
		);
	}

	@Override
	public List<ParameterizedFormBo> getActiveFormsByInstitutionAndScope(Integer institutionId, EFormScope formScope) {
		log.debug("Input parameters -> institutionId {}, formScope {}", institutionId, formScope);
		List<ParameterizedFormBo> result = parameterizedFormRepository.getActiveFormsByInstitution(institutionId)
				.stream()
				.filter(pf -> filterByScope(pf, formScope))
				.collect(Collectors.toList());

		log.debug(OUTPUT, result);
		return result;
	}

	private void assertDomainFormName(Integer formId, String name, EFormStatus nextState) {
		Boolean nextStateIsActive = nextState.getId().equals(EFormStatus.ACTIVE.getId());
		Boolean existsDomainParameterizedFormByName = parameterizedFormRepository.existsDomainParameterizedFormByName(formId, name);
		if (nextStateIsActive && existsDomainParameterizedFormByName)
			throw new NotFoundException("form-with-same-name", String.format("Ya existe un formulario activo con ese nombre", name));
	}

	private void assertEnabledDomainParameterizedFormName(Integer formId, String name, EFormStatus nextState, Integer institutionId) {
		Boolean nextStateIsActive = nextState.getId().equals(EFormStatus.ACTIVE.getId());
		Boolean existsDomainParameterizedFormByName = institutionalParameterizedFormRepository.existsParameterizedFormByNameAndInsitutionIdAndDomain(formId, institutionId, name, true);
		if (nextStateIsActive && existsDomainParameterizedFormByName)
			throw new NotFoundException("form-with-same-name", String.format("Ya existe un formulario activo con ese nombre", name));
	}

	private void assertInstitutionalFormName(Integer formId, String name, EFormStatus nextState, Integer institutionId) {
		Boolean nextStateIsActive = nextState.getId().equals(EFormStatus.ACTIVE.getId());
		Boolean existsFormWithNameInInstitution = institutionalParameterizedFormRepository.existsParameterizedFormByNameAndInsitutionIdAndDomain(formId, institutionId, name, false);
		if (nextStateIsActive && existsFormWithNameInInstitution)
			throw new NotFoundException("form-with-same-name", String.format("Ya existe un formulario con ese nombre", name));
	}

	ParameterizedFormDto mapEntityToDto(ParameterizedForm entity) {
		return new ParameterizedFormDto (
				entity.getId(),
				entity.getName(),
				entity.getStatusId(),
				entity.getOutpatientEnabled(),
				entity.getInternmentEnabled(),
				entity.getEmergencyCareEnabled(),
				entity.getIsDomain()
		);
	}

	private void updateInsitutionalParameterizedForm(InstitutionalParameterizedForm institutionalParameterizedForm, Boolean enablement) {
		institutionalParameterizedForm.setIsEnabled(enablement);
		institutionalParameterizedFormRepository.save(institutionalParameterizedForm);
	}

	private void saveInsitutionalParameterizedForm(Integer parameterizedFormId, Integer institutionId, Boolean enablement) {
		InstitutionalParameterizedForm institutionalParameterizedForm = new InstitutionalParameterizedForm(parameterizedFormId, institutionId, enablement);
		institutionalParameterizedFormRepository.save(institutionalParameterizedForm);
	}

	private Boolean filterByScope(ParameterizedFormBo parameterizedForm, EFormScope formScope){
		if (formScope.equals(EFormScope.OUTPATIENT)) return parameterizedForm.getOutpatientEnabled();
		if (formScope.equals(EFormScope.INTERNMENT)) return parameterizedForm.getInternmentEnabled();
		if (formScope.equals(EFormScope.EMERGENCY_CARE)) return parameterizedForm.getEmergencyCareEnabled();
		return Boolean.FALSE;
	}

	private void handleInstitutionalFormStatus(Integer formId, EFormStatus nextState, Boolean isDomain) {
		List<InstitutionalParameterizedForm> formEnabledInInstitutions = institutionalParameterizedFormRepository.getByParameterizedFormId(formId);
		Boolean isInactive = nextState.getId().equals(EFormStatus.INACTIVE.getId());
		if (isInactive && isDomain && !formEnabledInInstitutions.isEmpty()) {
			institutionalParameterizedFormRepository.updateInstitutionalParameterizedFormEnabled(formId);
		}
	}

	private void assertEnablementValidations(ParameterizedForm pf, Integer institutionId, Boolean enablement) {
		if (!enablement)
			return;
		if (pf.getStatusId().equals(EFormStatus.DRAFT.getId())) {
			throw new NotFoundException("draft-form", "No se puede habilitar un formulario con estado borrador.");
		}

		Boolean existsParameterizedFormByName = institutionalParameterizedFormRepository.existsParameterizedFormByNameAndInsitutionIdAndDomain(pf.getId(), institutionId, pf.getName(), false);
		if (existsParameterizedFormByName)
			throw new NotFoundException("institutional-form-with-same-name", String.format("No se puede habilitar ya que existe un formulario institucional activo con el mismo nombre", pf.getName()));
	}
}
