package net.pladema.establishment.controller;

import java.util.List;

import net.pladema.staff.repository.ClinicalSpecialtyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

@RestController
@Api(value = "ClinicalSpecialty", tags = { "Clinical Specialty" })
@RequestMapping("/institution/{institutionId}/clinicalspecialty")
public class ClinicalSpecialtyController {

    private static final Logger LOG = LoggerFactory.getLogger(ClinicalSpecialtyController.class);

    private ClinicalSpecialtyRepository clinicalSpecialtyRepository;

    public ClinicalSpecialtyController(ClinicalSpecialtyRepository clinicalSpecialtyRepository) {
        this.clinicalSpecialtyRepository = clinicalSpecialtyRepository;
    }

    @GetMapping("/professional/{professionalId}")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<List<ClinicalSpecialty>> getAllSpecialtyByProfessional(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "professionalId") Integer professionalId) {
        List<ClinicalSpecialty> clinicalSpecialties = clinicalSpecialtyRepository
                .getAllByProfessional(professionalId);
        LOG.debug("Get all Clinical Specialty by Professional {} and Institution {} => {}", professionalId, institutionId,
                clinicalSpecialties);
        return ResponseEntity.ok(clinicalSpecialties);
    }
}

