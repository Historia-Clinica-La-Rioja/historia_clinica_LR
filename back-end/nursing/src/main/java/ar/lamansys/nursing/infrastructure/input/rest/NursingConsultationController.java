package ar.lamansys.nursing.infrastructure.input.rest;

import ar.lamansys.nursing.application.CreateNursingConsultation;
import ar.lamansys.nursing.infrastructure.input.rest.dto.NursingConsultationDto;
import ar.lamansys.nursing.infrastructure.input.rest.mapper.NursingConsultationMapper;
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

        createNursingConsultation.run(nursingConsultationBo);

        return true;
    }

}
