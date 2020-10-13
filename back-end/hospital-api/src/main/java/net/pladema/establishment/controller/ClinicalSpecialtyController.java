package net.pladema.establishment.controller;

import java.util.List;

import net.pladema.staff.controller.dto.ProfessionalsByClinicalSpecialtyDto;
import net.pladema.staff.controller.mapper.ClinicalSpecialtyMapper;
import net.pladema.staff.repository.ClinicalSpecialtyRepository;
import net.pladema.staff.service.HealthcareProfessionalSpecialtyService;
import net.pladema.staff.service.domain.ProfessionalsByClinicalSpecialtyBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

@RestController
@Api(value = "ClinicalSpecialty", tags = { "Clinical Specialty" })
@RequestMapping("/institution/{institutionId}/clinicalspecialty")
public class ClinicalSpecialtyController {

    private static final Logger LOG = LoggerFactory.getLogger(ClinicalSpecialtyController.class);

    private ClinicalSpecialtyRepository clinicalSpecialtyRepository;

    private HealthcareProfessionalSpecialtyService healthcareProfessionalSpecialtyService;

    private ClinicalSpecialtyMapper clinicalSpecialtyMapper;

    public ClinicalSpecialtyController(
            ClinicalSpecialtyRepository clinicalSpecialtyRepository,
            HealthcareProfessionalSpecialtyService healthcareProfessionalSpecialtyService,
            ClinicalSpecialtyMapper clinicalSpecialtyMapper) {
        this.clinicalSpecialtyRepository = clinicalSpecialtyRepository;
        this.healthcareProfessionalSpecialtyService = healthcareProfessionalSpecialtyService;
        this.clinicalSpecialtyMapper = clinicalSpecialtyMapper;
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

    @GetMapping(value = "/professional", params = "professionalsIds")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<List<ProfessionalsByClinicalSpecialtyDto>> getManyByProfessionals(
            @PathVariable(name = "institutionId") Integer institutionId,
            @RequestParam List<Integer> professionalsIds) {

        List<ProfessionalsByClinicalSpecialtyBo> clinicalSpecialties =
                this.healthcareProfessionalSpecialtyService.getProfessionalsByClinicalSpecialtyBo(professionalsIds);

        List<ProfessionalsByClinicalSpecialtyDto> professionalsByClinicalSpecialtyDtos =
                clinicalSpecialtyMapper.fromProfessionalsByClinicalSpecialtyBoList(clinicalSpecialties);

        LOG.debug("Get all Clinical Specialty by Professionals {} and Institution {} => {}", professionalsIds, institutionId,
                professionalsByClinicalSpecialtyDtos);
        return ResponseEntity.ok(professionalsByClinicalSpecialtyDtos);
    }

}

