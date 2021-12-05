package net.pladema.establishment.controller;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.controller.mapper.ClinicalSpecialtyMapper;
import net.pladema.establishment.service.ClinicalSpecialtyCareLineService;
import net.pladema.staff.service.domain.ClinicalSpecialtyBo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(value = "ClinicalSpecialtyCareLine", tags = { "Clinical Specialty Care Line" })
@RequestMapping("/institution/{institutionId}/careline/{careLineId}/clinicalspecialties")
public class ClinicalSpecialtyCareLineController {
    
    private final ClinicalSpecialtyCareLineService clinicalSpecialtyCareLineService;
    private final ClinicalSpecialtyMapper clinicalSpecialtyMapper;

    @GetMapping()
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<List<ClinicalSpecialtyDto>> getAll(@PathVariable(name = "institutionId") Integer institutionId,
                                                               @PathVariable(name= "careLineId") Integer careLineId) {
        List<ClinicalSpecialtyBo> clinicalSpecialties = clinicalSpecialtyCareLineService.getClinicalSpecialties(careLineId);
        log.debug("Get all Clinical Specialties by CareLine {} => {}", careLineId, clinicalSpecialties);
        return ResponseEntity.ok(clinicalSpecialtyMapper.fromListClinicalSpecialtyBo(clinicalSpecialties));
    }

}

