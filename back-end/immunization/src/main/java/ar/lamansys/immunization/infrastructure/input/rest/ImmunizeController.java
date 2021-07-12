package ar.lamansys.immunization.infrastructure.input.rest;


import ar.lamansys.immunization.application.immunizePatient.ImmunizePatient;
import ar.lamansys.immunization.domain.consultation.ImmunizePatientBo;
import ar.lamansys.immunization.domain.immunization.ImmunizationInfoBo;
import ar.lamansys.immunization.domain.snomed.SnomedBo;
import ar.lamansys.immunization.infrastructure.input.rest.dto.ImmunizePatientDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ImmunizationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
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
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/immunize")
public class ImmunizeController {

    private final Logger logger;

    private final ImmunizePatient immunizePatient;

    private final LocalDateMapper localDateMapper;

    public ImmunizeController(ImmunizePatient immunizePatient,
                              LocalDateMapper localDateMapper) {
        this.immunizePatient = immunizePatient;
        this.localDateMapper = localDateMapper;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @PreAuthorize("hasPermission(#institutionId, 'ENFERMERO')")
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping
    public boolean immunizePatient(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId")  Integer patientId,
            @RequestBody @Valid ImmunizePatientDto immunizePatientDto) {
        logger.debug("Input parameters -> institutionId {}, patientId {}, vaccineDto {}",
                institutionId, patientId, immunizePatientDto);
        var immunizePatientBo = new ImmunizePatientBo(
                patientId, institutionId,
                immunizePatientDto.getClinicalSpecialtyId(),
                immunizePatientDto.getImmunizations().stream()
                        .map(i -> mapImmunization(i, institutionId)).collect(Collectors.toList()));
        immunizePatient.run(immunizePatientBo);
        return true;
    }

    private ImmunizationInfoBo mapImmunization(ImmunizationDto immunizationDto, Integer institutionId) {
        return new ImmunizationInfoBo(null,
                    immunizationDto.isBillable() ? institutionId : immunizationDto.getInstitutionId(),
                    mapSnomed(immunizationDto.getSnomed()),
                    immunizationDto.getConditionId(),
                    immunizationDto.getSchemeId(),
                    immunizationDto.getDoseId(),
                    localDateMapper.fromStringToLocalDate(immunizationDto.getAdministrationDate()),
                    immunizationDto.getLotNumber(),
                    immunizationDto.getNote(),
                    immunizationDto.isBillable());

    }

    private SnomedBo mapSnomed(SnomedDto snomed) {
        if (snomed == null)
            return null;
        return new SnomedBo(snomed.getId(),
                snomed.getSctid(), snomed.getPt(),
                snomed.getParentId(), snomed.getParentFsn());
    }

}