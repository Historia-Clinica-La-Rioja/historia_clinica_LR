package ar.lamansys.odontology.infrastructure.controller.consultation;

import ar.lamansys.odontology.application.createConsultation.CreateOdontologyConsultation;
import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.infrastructure.controller.consultation.dto.OdontologyConsultationDto;
import ar.lamansys.odontology.infrastructure.controller.consultation.mapper.OdontologyConsultationMapper;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/odontology/consultation")
@Api(value="Odontology consultation", tags= { "Odontology Consultation" } )
public class OdontologyConsultationController {

    private static final Logger LOG = LoggerFactory.getLogger(OdontologyConsultationController.class);

    private final CreateOdontologyConsultation createOdontologyConsultation;

    private final OdontologyConsultationMapper odontologyConsultationMapper;

    public OdontologyConsultationController(CreateOdontologyConsultation createOdontologyConsultation,
                                            OdontologyConsultationMapper odontologyConsultationMapper) {
        this.createOdontologyConsultation = createOdontologyConsultation;
        this.odontologyConsultationMapper = odontologyConsultationMapper;
    }

    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping
    public boolean createConsultation(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId")  Integer patientId,
            @RequestBody @Valid OdontologyConsultationDto consultationDto) {
        LOG.debug("Input parameters -> institutionId {}, patientId {}, odontologyConsultationDto {}", institutionId, patientId, consultationDto);

        ConsultationBo consultationBo = odontologyConsultationMapper.fromOdontologyConsultationDto(consultationDto);
        consultationBo.setInstitutionId(institutionId);
        consultationBo.setPatientId(patientId);

        createOdontologyConsultation.run(consultationBo);
        return true;
    }
}
