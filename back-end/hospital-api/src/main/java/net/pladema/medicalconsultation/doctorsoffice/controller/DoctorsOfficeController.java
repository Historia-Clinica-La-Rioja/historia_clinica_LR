package net.pladema.medicalconsultation.doctorsoffice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.medicalconsultation.doctorsoffice.controller.dto.DoctorsOfficeDto;
import net.pladema.medicalconsultation.doctorsoffice.controller.mapper.DoctorsOfficeMapper;
import net.pladema.medicalconsultation.doctorsoffice.service.DoctorsOfficeService;
import net.pladema.medicalconsultation.doctorsoffice.service.domain.DoctorsOfficeBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/doctorsOffice")
@Tag(name = "Doctor's office", description = "Doctor's office")
public class DoctorsOfficeController {

    private static final Logger LOG = LoggerFactory.getLogger(DoctorsOfficeController.class);
    public static final String OUTPUT = "Output -> {}";

    private final DoctorsOfficeService doctorsOfficeService;

    private final DoctorsOfficeMapper doctorsOfficeMapper;

    public DoctorsOfficeController(DoctorsOfficeService doctorsOfficeService,
                                   DoctorsOfficeMapper doctorsOfficeMapper){
        super();
        this.doctorsOfficeService = doctorsOfficeService;
        this.doctorsOfficeMapper = doctorsOfficeMapper;
    }

    @GetMapping("/sector/{sectorId}")
    public ResponseEntity<List<DoctorsOfficeDto>> getAll(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "sectorId") Integer sectorId) {
        LOG.debug("Input parameters -> institutionId {}, sectorId {}",
                institutionId, sectorId);
        List<DoctorsOfficeBo> doctorsOfficeBos = doctorsOfficeService
                .getAllDoctorsOffice(institutionId, sectorId);
        List<DoctorsOfficeDto> result = doctorsOfficeMapper.toListDoctorsOfficeDto(doctorsOfficeBos);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/sectorType/{sectorTypeId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public ResponseEntity<List<DoctorsOfficeDto>> getBySectorType(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "sectorTypeId") Short sectorTypeId) {
        LOG.debug("Input parameters -> institutionId {}, sectorTypeId {}", institutionId, sectorTypeId);
        List<DoctorsOfficeBo> doctorsOfficeBos = doctorsOfficeService
                .getDoctorsOfficeBySectorType(institutionId, sectorTypeId);
        List<DoctorsOfficeDto> result = doctorsOfficeMapper.toListDoctorsOfficeDto(doctorsOfficeBos);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok().body(result);
    }
}
