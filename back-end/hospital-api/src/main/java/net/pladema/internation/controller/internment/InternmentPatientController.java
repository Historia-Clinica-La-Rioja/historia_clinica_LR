package net.pladema.internation.controller.internment;

import io.swagger.annotations.Api;
import net.pladema.internation.controller.internment.dto.InternmentEpisodeDto;
import net.pladema.internation.controller.internment.dto.InternmentPatientDto;
import net.pladema.internation.controller.internment.mapper.InternmentEpisodeMapper;
import net.pladema.internation.repository.core.InternmentEpisodeRepository;
import net.pladema.internation.service.internment.InternmentPatientService;
import net.pladema.internation.service.internment.domain.BasicListedPatientBo;
import net.pladema.internation.service.internment.domain.InternmentEpisodeBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/patients")
@Api(value = "Internment Patient", tags = { "Internment Patient" })
public class InternmentPatientController {

    private static final Logger LOG = LoggerFactory.getLogger(InternmentPatientController.class);

    private final InternmentPatientService internmentPatientService;

    private final InternmentEpisodeMapper internmentEpisodeMapper;

    private final InternmentEpisodeRepository internmentEpisodeRepository;

    public InternmentPatientController(InternmentPatientService internmentPatientService, InternmentEpisodeMapper internmentEpisodeMapper, InternmentEpisodeRepository internmentEpisodeRepository) {
        this.internmentPatientService = internmentPatientService;
        this.internmentEpisodeMapper = internmentEpisodeMapper;
        this.internmentEpisodeRepository = internmentEpisodeRepository;
    }

    @GetMapping
    public ResponseEntity<List<InternmentEpisodeDto>> getAllInternmentPatient(
            @PathVariable(name = "institutionId") Integer institutionId){
        List<InternmentEpisodeBo> internmentEpisodes = internmentPatientService.getAllInternmentPatient(institutionId);
        List<InternmentEpisodeDto> result = internmentEpisodeMapper.toListInternmentEpisodeDto(internmentEpisodes);
        LOG.debug("Internment patients -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/basicdata")
    public ResponseEntity<List<InternmentPatientDto>> getAllInternmentPatientsBasicData(
            @PathVariable(name = "institutionId") Integer institutionId){
        LOG.debug("Input parameters -> {}", institutionId);
        List<BasicListedPatientBo> basicListedPatientBos = internmentPatientService.getInternmentPatients(institutionId);
        List<InternmentPatientDto> result = internmentEpisodeMapper.toListInternmentPatientDto(basicListedPatientBos);
        LOG.debug("Output -> {}", result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/{patientId}/internmentEpisodeIdInProcess")
    public ResponseEntity<Integer> internmentEpisodeIdInProcess(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId){
        LOG.debug("Input parameters -> {}", patientId);
        Optional<Integer> internmentEpisodeId = internmentEpisodeRepository.internmentEpisodeInProcess(institutionId,patientId);
        return internmentEpisodeId.isPresent() ? ResponseEntity.ok().body(internmentEpisodeId.get()) :  ResponseEntity.ok().build();
    }
}