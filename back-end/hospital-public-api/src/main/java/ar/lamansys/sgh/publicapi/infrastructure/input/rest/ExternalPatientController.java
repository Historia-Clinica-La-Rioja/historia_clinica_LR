package ar.lamansys.sgh.publicapi.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.application.saveexternalpatient.SaveExternalPatient;
import ar.lamansys.sgh.publicapi.domain.ExternalPatientExtendedBo;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalPatientExtendedBoException;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalPatientBoException;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.ExternalPatientExtendedDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/public-api/patient")
@Api(value = "External Patient", tags = {"External Patient"})
public class ExternalPatientController {

    private static final String OUTPUT = "Output -> {}";

    private final SaveExternalPatient saveExternalPatient;

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
                externalPatientDto.getId(),
                externalPatientDto.getPatientId(),
                externalPatientDto.getExternalId(),
                externalPatientDto.getExternalEncounterId(),
                externalPatientDto.getExternalEncounterDate(),
                externalPatientDto.getEExternalEncounterType().toString(),
                externalPatientDto.getBirthDate(),
                externalPatientDto.getFirstName(),
                externalPatientDto.getGenderId(),
                externalPatientDto.getIdentificationNumber(),
                externalPatientDto.getIdentificationTypeId(),
                externalPatientDto.getLastName(),
                externalPatientDto.getPhoneNumber(),
                externalPatientDto.getEmail());
    }
}
