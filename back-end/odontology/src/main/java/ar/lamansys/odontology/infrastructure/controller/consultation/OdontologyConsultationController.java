package ar.lamansys.odontology.infrastructure.controller.consultation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.mapper.ServiceRequestToFileMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.odontology.application.createConsultation.CreateOdontologyConsultation;
import ar.lamansys.odontology.application.fetchCpoCeoIndices.FetchCpoCeoIndices;
import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import ar.lamansys.odontology.domain.consultation.CreateOdontologyConsultationServiceRequestBo;
import ar.lamansys.odontology.infrastructure.controller.consultation.dto.OdontologyConsultationDto;
import ar.lamansys.odontology.infrastructure.controller.consultation.dto.OdontologyConsultationIndicesDto;
import ar.lamansys.odontology.infrastructure.controller.consultation.mapper.CpoCeoIndicesMapper;
import ar.lamansys.odontology.infrastructure.controller.consultation.mapper.OdontologyConsultationMapper;
import ar.lamansys.sgh.shared.domain.servicerequest.SharedAddObservationsCommandVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.ConsultationResponseDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceClosureDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto.CreateOutpatientServiceRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.multipart.MultipartFile;

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
            @RequestPart("createConsultationDto") @Valid OdontologyConsultationDto consultationDto,
			@RequestPart(value = "serviceRequestFiles", required = false) MultipartFile[] serviceRequestFiles
	) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, odontologyConsultationDto {}", institutionId, patientId, consultationDto);

        ConsultationBo consultationBo = odontologyConsultationMapper.fromOdontologyConsultationDto(consultationDto);

        consultationBo.setInstitutionId(institutionId);
        consultationBo.setPatientId(patientId);
		var newOdontologyConsultation = createOdontologyConsultation.run(
			consultationBo,
			toConsultationServiceRequestsBo(consultationDto.getServiceRequests(), asList(serviceRequestFiles))
		);
		ConsultationResponseDto result = new ConsultationResponseDto(newOdontologyConsultation.getEncounterId(), newOdontologyConsultation.getOrderIds());
		return ResponseEntity.ok(result);
    }

	private List<MultipartFile> asList(MultipartFile[] serviceRequestFiles) {
		if (serviceRequestFiles == null) return Collections.emptyList();
		else return Arrays.asList(serviceRequestFiles);
	}

	/**
	 * Maps the service request and observations data from the DTO to one or more BOs
	 * Links each service request with its corresponding file
	 */
	private List<CreateOdontologyConsultationServiceRequestBo> toConsultationServiceRequestsBo(List<CreateOutpatientServiceRequestDto> serviceRequests, List<MultipartFile> serviceRequestFiles) {

		Map<CreateOutpatientServiceRequestDto, List<MultipartFile>> requestFiles = ServiceRequestToFileMapper.
				buildRequestFilesMap(serviceRequests, serviceRequestFiles);

		return serviceRequests.stream().map(serviceRequest -> {

			if (serviceRequest != null) {
				var ret = new CreateOdontologyConsultationServiceRequestBo();
				ret.setCategoryId(serviceRequest.getCategoryId());
				ret.setHealthConditionSctid(serviceRequest.getHealthConditionSctid());
				ret.setHealthConditionPt(serviceRequest.getHealthConditionPt());
				ret.setCreationStatusIsFinal(serviceRequest.getCreationStatus().isFinal());
				ret.setSnomedSctid(serviceRequest.getSnomedSctid());
				ret.setSnomedPt(serviceRequest.getSnomedPt());
				if (serviceRequest.getObservations() != null) {
					var observationsDto = serviceRequest.getObservations();
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
								value.getSnomedPt(),
								value.getValueNumeric()
							);
						});
					}
					ret.setSharedAddObservationsCommandVo(observationData);
				}
				ret.setFiles(requestFiles.get(serviceRequest));
				ret.setObservation(serviceRequest.getObservation());
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
