package net.pladema.establishment.infrastructure.input.rest;

import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import net.pladema.establishment.application.bed.DoBedRelocation;
import net.pladema.establishment.domain.bed.BedRelocationBo;
import net.pladema.establishment.infrastructure.input.rest.mapper.BedRelocationMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.infrastructure.input.rest.dto.PatientBedRelocationDto;
import net.pladema.establishment.service.BedService;

@RequestMapping("/institution/{institutionId}")
@Tag(name = "Patient bed relocation", description = "Patient bed relocation")
@Slf4j
@RequiredArgsConstructor
@RestController
public class PatientBedRelocationController {

    private final BedRelocationMapper bedRelocationMapper;
    private final BedService bedService;
    private final DoBedRelocation doBedRelocation;

    @PostMapping("/bed/relocation")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES')")
    public ResponseEntity<PatientBedRelocationDto> addPatientBedRelocation(@PathVariable(name = "institutionId") Integer institutionId,
                                                                           @RequestBody PatientBedRelocationDto patientBedRelocationDto) {
        log.debug("Input parameters -> institutionId {}, patientBedRelocationDto {}", institutionId, patientBedRelocationDto);
        BedRelocationBo bedRelocationBo = bedRelocationMapper.fromPatientBedRelocationDto(patientBedRelocationDto);
        var resultService = doBedRelocation.run(bedRelocationBo);
        PatientBedRelocationDto result = bedRelocationMapper.toPatientBedRelocationDto(resultService);
        log.debug("Output -> {}", result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/internment/{internmentEpisodeId}/bed/relocation/last")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES')")
    public ResponseEntity<PatientBedRelocationDto> getLastPatientBedRelocation(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
        log.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        Optional<BedRelocationBo> historicPatientBedRelocation = bedService.getLastPatientBedRelocation(internmentEpisodeId);
        if (historicPatientBedRelocation.isPresent()) {
            log.debug("Output -> {}", historicPatientBedRelocation.get());
            return ResponseEntity.ok(bedRelocationMapper.toPatientBedRelocationDto(historicPatientBedRelocation.get()));
        } else return ResponseEntity.noContent().build();
    }

}
