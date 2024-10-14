package ar.lamansys.odontology.infrastructure.controller.consultation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.odontology.application.createConsultation.CreateOdontologyConsultation;
import ar.lamansys.odontology.application.fetchCpoCeoIndices.FetchCpoCeoIndices;
import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import ar.lamansys.odontology.domain.consultation.CreateOdontologyConsultationServiceRequestBo;
import ar.lamansys.odontology.infrastructure.controller.consultation.dto.OdontologyConsultationDto;
import ar.lamansys.odontology.infrastructure.controller.consultation.dto.OdontologyConsultationIndicesDto;
import ar.lamansys.odontology.infrastructure.controller.consultation.dto.OdontologyProcedureDto;
import ar.lamansys.odontology.infrastructure.controller.consultation.mapper.CpoCeoIndicesMapper;
import ar.lamansys.odontology.infrastructure.controller.consultation.mapper.OdontologyConsultationMapper;
import ar.lamansys.sgh.shared.domain.servicerequest.SharedAddObservationsCommandVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.ConsultationResponseDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceClosureDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto.CreateOutpatientServiceRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Validated
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/odontology/consultation")
@Tag(name = "Odontology Consultation", description = "Odontology Consultation")
public class OdontologyConsultationController {

    private static final Logger LOG = LoggerFactory.getLogger(OdontologyConsultationController.class);

    private final CreateOdontologyConsultation createOdontologyConsultation;

    private final OdontologyConsultationMapper odontologyConsultationMapper;

    private final FetchCpoCeoIndices fetchCpoCeoIndices;

    private final CpoCeoIndicesMapper cpoCeoIndicesMapper;

    public OdontologyConsultationController(CreateOdontologyConsultation createOdontologyConsultation,
                                            OdontologyConsultationMapper odontologyConsultationMapper,
                                            FetchCpoCeoIndices fetchCpoCeoIndices,
                                            CpoCeoIndicesMapper cpoCeoIndicesMapper) {
        this.createOdontologyConsultation = createOdontologyConsultation;
        this.odontologyConsultationMapper = odontologyConsultationMapper;
        this.fetchCpoCeoIndices = fetchCpoCeoIndices;
        this.cpoCeoIndicesMapper = cpoCeoIndicesMapper;
    }


    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<ConsultationResponseDto> createConsultation(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId")  Integer patientId,
            @RequestBody @Valid OdontologyConsultationDto consultationDto) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, odontologyConsultationDto {}", institutionId, patientId, consultationDto);

        ConsultationBo consultationBo = odontologyConsultationMapper.fromOdontologyConsultationDto(consultationDto);

        consultationBo.setInstitutionId(institutionId);
        consultationBo.setPatientId(patientId);
		var newOdontologyConsultation = createOdontologyConsultation.run(consultationBo, toConsultationServiceRequestsBo(consultationDto.getProcedures()));
		ConsultationResponseDto result = new ConsultationResponseDto(newOdontologyConsultation.getEncounterId(), newOdontologyConsultation.getOrderIds());
		return ResponseEntity.ok(result);
    }

	/**
	 * Maps the service request and observations data from the DTO to one or more BOs
	 * Creates a list of {@link CreateOdontologyConsultationServiceRequestBo} for each procedure.
	 * @param procedures
	 * @return
	 */
	private List<CreateOdontologyConsultationServiceRequestBo> toConsultationServiceRequestsBo(List<OdontologyProcedureDto> procedures) {
		return procedures.stream().map(procedure -> {
			CreateOutpatientServiceRequestDto procedureServiceRequest = procedure.getServiceRequest();

			if (procedureServiceRequest != null) {
				var ret = new CreateOdontologyConsultationServiceRequestBo();
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

	@ResponseStatus(code = HttpStatus.OK)
    @GetMapping("/indices")
    public List<OdontologyConsultationIndicesDto> getConsultationIndices(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId")  Integer patientId) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
        List<CpoCeoIndicesBo> cpoCeoIndicesBoList = fetchCpoCeoIndices.run(patientId);
        List<OdontologyConsultationIndicesDto> result = cpoCeoIndicesMapper.fromCpoCeoIndicesBoList(cpoCeoIndicesBoList);
        LOG.debug("Output -> {}", result);
        return result;
    }
}
