package ar.lamansys.nursing.infrastructure.input.rest;

import ar.lamansys.nursing.application.CreateNursingConsultation;
import ar.lamansys.nursing.domain.CreateNursingConsultationServiceRequestBo;
import ar.lamansys.nursing.infrastructure.input.rest.dto.NursingConsultationDto;
import ar.lamansys.nursing.infrastructure.input.rest.dto.NursingProcedureDto;
import ar.lamansys.nursing.infrastructure.input.rest.mapper.NursingConsultationMapper;
import ar.lamansys.sgh.shared.domain.servicerequest.SharedAddObservationsCommandVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceClosureDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto.CreateOutpatientServiceRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/nursing/consultation")
@Tag(name = "Nursing consultation", description = "Nursing consultation")
public class NursingConsultationController {

    private static final Logger LOG = LoggerFactory.getLogger(NursingConsultationController.class);

    private final CreateNursingConsultation createNursingConsultation;
    private final NursingConsultationMapper nursingConsultationMapper;

    public NursingConsultationController(CreateNursingConsultation createNursingConsultation,
                                         NursingConsultationMapper nursingConsultationMapper) {
        this.createNursingConsultation = createNursingConsultation;
        this.nursingConsultationMapper = nursingConsultationMapper;
    }


    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO')")
    public boolean createConsultation(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId,
            @RequestBody @Valid NursingConsultationDto nursingConsultationDto) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, nursingConsultationDto {}", institutionId, patientId, nursingConsultationDto);

        var nursingConsultationBo = nursingConsultationMapper.fromNursingConsultationDto(nursingConsultationDto);
        nursingConsultationBo.setInstitutionId(institutionId);
        nursingConsultationBo.setPatientId(patientId);

        createNursingConsultation.run(nursingConsultationBo, toConsultationServiceRequestsBo(nursingConsultationDto.getProcedures()));

        return true;
    }

	/**
	 * Maps the service request and observations data from the DTO to one or more BOs
	 * Creates a list of {@link CreateOdontologyConsultationServiceRequestBo} for each procedure.
	 * @param procedures
	 * @return
	 */
	private List<CreateNursingConsultationServiceRequestBo> toConsultationServiceRequestsBo(List<NursingProcedureDto> procedures) {
		return procedures.stream().map(procedure -> {
					CreateOutpatientServiceRequestDto procedureServiceRequest = procedure.getServiceRequest();

					if (procedureServiceRequest != null) {
						var ret = new CreateNursingConsultationServiceRequestBo();
						ret.setCategoryId(procedureServiceRequest.getCategoryId());
						ret.setHealthConditionSctid(procedureServiceRequest.getHealthConditionSctid());
						ret.setHealthConditionPt(procedureServiceRequest.getHealthConditionPt());
						ret.setCreationStatusIsFinal(procedureServiceRequest.getCreationStatus().isFinal());
						ret.setSnomedSctid(procedure.getSnomed().getSctid());
						ret.setSnomedPt(procedure.getSnomed().getPt());
						if (procedureServiceRequest.getObservations() != null) {
							var observationsDto = procedureServiceRequest.getObservations();
							var observationData = new SharedAddObservationsCommandVo();
							observationData.setIsPartialUpload(observationsDto.getIsPartialUpload());
							observationData.setProcedureTemplateId(observationsDto.getProcedureTemplateId());
							if (observationsDto.getReferenceClosure() != null)
								observationData.setReferenceClosure(mapReferenceClosure(observationsDto.getReferenceClosure()));
							if (observationsDto.getValues() != null) {
								var valuesDto = observationsDto.getValues();
								valuesDto.forEach(value -> {
									observationData.addValue(
											value.getProcedureParameterId(),
											value.getValue(),
											value.getUnitOfMeasureId(),
											value.getSnomedSctid(),
											value.getSnomedPt()
									);
								});
							}
							ret.setSharedAddObservationsCommandVo(observationData);
						}
						return ret;
					}
					return null;
				})
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
	private SharedAddObservationsCommandVo.SharedReferenceRequestClosureBo mapReferenceClosure(ReferenceClosureDto referenceClosure) {
		return new SharedAddObservationsCommandVo.SharedReferenceRequestClosureBo(
				referenceClosure.getReferenceId(),
				referenceClosure.getClinicalSpecialtyId(),
				referenceClosure.getCounterReferenceNote(),
				referenceClosure.getClosureTypeId(),
				referenceClosure.getFileIds()
		);
	}

}
