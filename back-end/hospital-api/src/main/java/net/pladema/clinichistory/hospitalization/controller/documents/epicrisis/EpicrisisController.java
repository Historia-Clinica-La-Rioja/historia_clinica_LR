package net.pladema.clinichistory.hospitalization.controller.documents.epicrisis;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.documents.events.OnGenerateInternmentDocumentEvent;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.EDocumentType;
import net.pladema.clinichistory.documents.service.CreateDocumentFile;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.generalstate.EncounterGeneralState;
import net.pladema.clinichistory.documents.service.generalstate.EncounterGeneralStateBuilder;
import net.pladema.clinichistory.hospitalization.controller.constraints.CanCreateEpicrisis;
import net.pladema.clinichistory.hospitalization.controller.constraints.DocumentValid;
import net.pladema.clinichistory.hospitalization.controller.constraints.InternmentValid;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.EpicrisisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.EpicrisisGeneralStateDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.dto.ResponseEpicrisisDto;
import net.pladema.clinichistory.hospitalization.controller.documents.epicrisis.mapper.EpicrisisMapper;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.CreateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.EpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.UpdateEpicrisisService;
import net.pladema.clinichistory.hospitalization.service.epicrisis.domain.EpicrisisBo;
import net.pladema.featureflags.service.FeatureFlagsService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.sgx.exceptions.NotFoundException;
import net.pladema.sgx.featureflags.AppFeature;
import net.pladema.sgx.pdf.PDFDocumentException;
import org.apache.http.MethodNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/institutions/{institutionId}/internments/{internmentEpisodeId}/epicrisis")
@Api(value = "Epicrisis", tags = { "Epicrisis" })
@Validated
@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO')")
public class EpicrisisController {

    private static final Logger LOG = LoggerFactory.getLogger(EpicrisisController.class);

    public static final String OUTPUT = "Output -> {}";
    
    public static final String INVALID_EPISODE = "internmentepisode.invalid";

    private final InternmentEpisodeService internmentEpisodeService;

    private final CreateEpicrisisService createEpicrisisService;

    private final UpdateEpicrisisService updateEpicrisisService;

    private final EpicrisisService epicrisisService;

    private final EpicrisisMapper epicrisisMapper;

    private final EncounterGeneralStateBuilder encounterGeneralStateBuilder;

    private final CreateDocumentFile createDocumentFile;

    private final FeatureFlagsService featureFlagsService;

    private final PatientExternalService patientExternalService;

    public EpicrisisController(
            InternmentEpisodeService internmentEpisodeService,
            CreateEpicrisisService createEpicrisisService,
            UpdateEpicrisisService updateEpicrisisService,
            EpicrisisService epicrisisService,
            EpicrisisMapper epicrisisMapper,
            EncounterGeneralStateBuilder encounterGeneralStateBuilder,
            CreateDocumentFile createDocumentFile,
            FeatureFlagsService featureFlagsService,
            PatientExternalService patientExternalService
    ) {
        this.internmentEpisodeService = internmentEpisodeService;
        this.createEpicrisisService = createEpicrisisService;
        this.updateEpicrisisService = updateEpicrisisService;
        this.epicrisisService = epicrisisService;
        this.epicrisisMapper = epicrisisMapper;
        this.encounterGeneralStateBuilder = encounterGeneralStateBuilder;
        this.createDocumentFile = createDocumentFile;
        this.featureFlagsService = featureFlagsService;
        this.patientExternalService = patientExternalService;
    }


    @PostMapping
    @Transactional
    @InternmentValid
    @CanCreateEpicrisis
    public ResponseEntity<ResponseEpicrisisDto> createDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @RequestBody @Valid EpicrisisDto epicrisisDto) throws IOException, PDFDocumentException {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, ananmnesis {}",
                institutionId, internmentEpisodeId, epicrisisDto);
        PatientInfoBo patientInfo = internmentEpisodeService.getPatient(internmentEpisodeId)
                .map(patientExternalService::getBasicDataFromPatient)
                .map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
                .orElseThrow(() -> new NotFoundException("El paciente no existe", "El paciente no existe"));
        EpicrisisBo epicrisis = epicrisisMapper.fromEpicrisisDto(epicrisisDto);
        epicrisis = createEpicrisisService.createDocument(internmentEpisodeId, patientInfo, epicrisis);
        ResponseEpicrisisDto result = epicrisisMapper.fromEpicrisis(epicrisis);
        LOG.debug(OUTPUT, result);
        generateDocument(epicrisis, institutionId, internmentEpisodeId, patientInfo.getId());
        return  ResponseEntity.ok().body(result);
    }


    @PutMapping("/{epicrisisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = false, documentType = DocumentType.EPICRISIS)
    public ResponseEntity<ResponseEpicrisisDto> updateDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "epicrisisId") Long epicrisisId,
            @Valid @RequestBody EpicrisisDto epicrisisDto) throws IOException, PDFDocumentException, MethodNotSupportedException {
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}, epicrisisDto {}",
                institutionId, internmentEpisodeId, epicrisisId, epicrisisDto);

        if (!this.featureFlagsService.isOn(AppFeature.HABILITAR_UPDATE_DOCUMENTS))
            throw new MethodNotSupportedException("Funcionalidad no soportada por el momento");

        EpicrisisBo epicrisis = epicrisisMapper.fromEpicrisisDto(epicrisisDto);
        PatientInfoBo patientInfo = internmentEpisodeService.getPatient(internmentEpisodeId)
                .map(patientExternalService::getBasicDataFromPatient)
                .map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
                .orElseThrow(() -> new NotFoundException("El paciente no existe", "El paciente no existe"));
        epicrisis = updateEpicrisisService.updateDocument(internmentEpisodeId, patientInfo, epicrisis);
        ResponseEpicrisisDto result = epicrisisMapper.fromEpicrisis(epicrisis);
        LOG.debug(OUTPUT, result);
        generateDocument(epicrisis, institutionId, internmentEpisodeId, patientInfo.getId());
        return  ResponseEntity.ok().body(result);
    }

    private void generateDocument(EpicrisisBo epicrisis, Integer institutionId, Integer internmentEpisodeId,
                                  Integer patientId) throws IOException, PDFDocumentException {
        OnGenerateInternmentDocumentEvent event = new OnGenerateInternmentDocumentEvent(epicrisis, institutionId, internmentEpisodeId,
                EDocumentType.map(DocumentType.EPICRISIS), patientId);
        createDocumentFile.execute(event);
    }

    @GetMapping("/{epicrisisId}")
    @InternmentValid
    @DocumentValid(isConfirmed = false, documentType = DocumentType.EPICRISIS)
    public ResponseEntity<ResponseEpicrisisDto> getDocument(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId,
            @PathVariable(name = "epicrisisId") Long epicrisisId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}, epicrisisId {}",
                institutionId, internmentEpisodeId, epicrisisId);
        EpicrisisBo epicrisis = epicrisisService.getDocument(epicrisisId);
        ResponseEpicrisisDto result = epicrisisMapper.fromEpicrisis(epicrisis);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }

    @GetMapping("/general")
    @InternmentValid
    public ResponseEntity<EpicrisisGeneralStateDto> internmentGeneralState(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId){
        LOG.debug("Input parameters -> institutionId {}, internmentEpisodeId {}", institutionId, internmentEpisodeId);
        EncounterGeneralState interment = encounterGeneralStateBuilder.getInternmentGeneralState(internmentEpisodeId);
        EpicrisisGeneralStateDto result = epicrisisMapper.toEpicrisisGeneralStateDto(interment);
        LOG.debug(OUTPUT, result);
        return  ResponseEntity.ok().body(result);
    }
}