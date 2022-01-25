package net.pladema.staff.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.staff.application.getprofessionalspecialties.GetProfessionalSpecialties;
import net.pladema.staff.controller.dto.ProfessionalSpecialtyDto;
import net.pladema.staff.controller.mapper.ProfessionalSpecialtyMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/professionalSpecialty")
//@Api(value = "professional-specialty", tags = {"Professional Specialty"})
public class ProfessionalSpecialtyController {

    private final GetProfessionalSpecialties getProfessionalSpecialties;

    private final ProfessionalSpecialtyMapper professionalSpecialtyMapper;

    @GetMapping
    public ResponseEntity<List<ProfessionalSpecialtyDto>> getAll() {
        log.debug("No input parameters");
        List<ProfessionalSpecialtyDto> result = professionalSpecialtyMapper.fromListProfessionalSpecialtyBo(getProfessionalSpecialties.execute());
        log.debug("Output -> {}", result);
        return ResponseEntity.ok().body(result);
    }
}
