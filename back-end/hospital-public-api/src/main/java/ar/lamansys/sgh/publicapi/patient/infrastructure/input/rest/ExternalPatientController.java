package ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.patient.application.saveexternalpatient.SaveExternalPatient;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalCoverageBo;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalPatientCoverageBo;
import ar.lamansys.sgh.publicapi.patient.domain.ExternalPatientExtendedBo;
import ar.lamansys.sgh.publicapi.patient.domain.exceptions.ExternalPatientBoException;
import ar.lamansys.sgh.publicapi.patient.domain.exceptions.ExternalPatientExtendedBoException;
import ar.lamansys.sgh.publicapi.patient.infrastructure.input.rest.dto.ExternalPatientExtendedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ExternalPatientCoverageDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/public-api/patient")
@Tag(name = "PublicApi Pacientes", description = "External patient Api")
public class ExternalPatientController {

    private static final String OUTPUT = "Output -> {}";
    private final SaveExternalPatient saveExternalPatient;

	public ExternalPatientController(SaveExternalPatient saveExternalPatient) {
		this.saveExternalPatient = saveExternalPatient;
	}

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Integer save(@RequestBody ExternalPatientExtendedDto externalPatientExtendedDto)  throws ExternalPatientBoException, ExternalPatientExtendedBoException {
        log.debug("Input parameters -> externalPatientDto {}", externalPatientExtendedDto);
        Integer result = saveExternalPatient.run(toExternalPatientExtendedBo(externalPatientExtendedDto));
        log.debug(OUTPUT, result);
        return result;
    }

    private ExternalPatientExtendedBo toExternalPatientExtendedBo(ExternalPatientExtendedDto externalPatientDto) throws ExternalPatientBoException, ExternalPatientExtendedBoException {
        return new ExternalPatientExtendedBo(
                externalPatientDto.getPatientId(),
                externalPatientDto.getExternalId(),
                externalPatientDto.getBirthDate(),
                externalPatientDto.getFirstName(),
                externalPatientDto.getGenderId(),
                externalPatientDto.getIdentificationNumber(),
                externalPatientDto.getIdentificationTypeId(),
                externalPatientDto.getLastName(),
                externalPatientDto.getPhoneNumber(),
                externalPatientDto.getEmail(),
                toExternalPatientCoverageListBo(externalPatientDto.getMedicalCoverages()),
                externalPatientDto.getInstitutionId()
        );
    }

    private List<ExternalPatientCoverageBo> toExternalPatientCoverageListBo(List<ExternalPatientCoverageDto> medicalCoverageListDto) {
        List<ExternalPatientCoverageBo> result = new ArrayList<>();
        medicalCoverageListDto
                .forEach(mc -> result.add(new ExternalPatientCoverageBo(
                        new ExternalCoverageBo(
                                mc.getMedicalCoverage().getId(),
                                mc.getMedicalCoverage().getCuit(),
                                mc.getMedicalCoverage().getPlan(),
                                mc.getMedicalCoverage().getName(),
                                mc.getMedicalCoverage().getType().toString()),
                        mc.getAffiliateNumber(),
                        mc.getActive(),
                        mc.getVigencyDate(),
						mc.getCondition())));
        return result;
    }
}
