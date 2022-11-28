package net.pladema.clinichistory.requests.medicationrequests.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile.DocumentAuthorFinder;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorDto;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.pdf.PdfService;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.ChangeStateMedicationRequestDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.MedicationInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.mapper.CreateMedicationRequestMapper;
import net.pladema.clinichistory.requests.medicationrequests.controller.mapper.ListMedicationInfoMapper;
import net.pladema.clinichistory.requests.medicationrequests.service.ChangeStateMedicationService;
import net.pladema.clinichistory.requests.medicationrequests.service.CreateMedicationRequestService;
import net.pladema.clinichistory.requests.medicationrequests.service.GetMedicationRequestInfoService;
import net.pladema.clinichistory.requests.medicationrequests.service.ListMedicationInfoService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.ChangeStateMedicationRequestBo;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationFilterBo;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationRequestBo;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.patient.controller.service.PatientExternalMedicalCoverageService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

@RestController
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/medication-requests")
@Tag(name = "Medication Request", description = "Medication Request")
@Validated
public class MedicationRequestController {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationRequestController.class);
    public static final String CHANGE_STATE_REQUEST = "change-state -> institutionId {}, patientId {}, changeStateRequest {}";

    private final CreateMedicationRequestService createMedicationRequestService;

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final CreateMedicationRequestMapper createMedicationRequestMapper;

    private final ListMedicationInfoService listMedicationInfoService;

    private final ListMedicationInfoMapper listMedicationInfoMapper;

    private final ChangeStateMedicationService  changeStateMedicationService;

    private final PatientExternalService patientExternalService;

    private final GetMedicationRequestInfoService getMedicationRequestInfoService;

    private final PatientExternalMedicalCoverageService patientExternalMedicalCoverageService;

    private final PdfService pdfService;

	private final FeatureFlagsService featureFlagsService;

	private final Function<Long, ProfessionalCompleteDto> authorFromDocumentFunction;


	public MedicationRequestController(CreateMedicationRequestService createMedicationRequestService,
									   HealthcareProfessionalExternalService healthcareProfessionalExternalService,
									   CreateMedicationRequestMapper createMedicationRequestMapper,
									   ListMedicationInfoService listMedicationInfoService,
									   ListMedicationInfoMapper listMedicationInfoMapper,
									   ChangeStateMedicationService changeStateMedicationService,
									   PatientExternalService patientExternalService,
									   GetMedicationRequestInfoService getMedicationRequestInfoService,
									   PatientExternalMedicalCoverageService patientExternalMedicalCoverageService,
									   PdfService pdfService,
									   FeatureFlagsService featureFlagsService,
									   DocumentAuthorFinder documentAuthorFinder) {
        this.createMedicationRequestService = createMedicationRequestService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.createMedicationRequestMapper = createMedicationRequestMapper;
        this.listMedicationInfoService = listMedicationInfoService;
        this.listMedicationInfoMapper = listMedicationInfoMapper;
        this.changeStateMedicationService = changeStateMedicationService;
        this.patientExternalService = patientExternalService;
        this.getMedicationRequestInfoService = getMedicationRequestInfoService;
        this.patientExternalMedicalCoverageService = patientExternalMedicalCoverageService;
        this.pdfService = pdfService;
		this.featureFlagsService = featureFlagsService;
		this.authorFromDocumentFunction = (Long documentId) -> documentAuthorFinder.getAuthor(documentId);
	}


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public @ResponseBody
    Integer create(@PathVariable(name = "institutionId") Integer institutionId,
                   @PathVariable(name = "patientId") Integer patientId,
                   @RequestBody @Valid PrescriptionDto medicationRequest){
        LOG.debug("create -> institutionId {}, patientId {}, medicationRequest {}", institutionId, patientId, medicationRequest);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        MedicationRequestBo medicationRequestBo = createMedicationRequestMapper.parseTo(doctorId, patientDto, medicationRequest);
        medicationRequestBo.setInstitutionId(institutionId);
        Integer result = createMedicationRequestService.execute(medicationRequestBo);
        LOG.debug("create result -> {}", result);
        return result;
    }



    @PutMapping(value = "/suspend")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public void suspendMedication(@PathVariable(name = "institutionId") Integer institutionId,
                                  @PathVariable(name = "patientId") Integer patientId,
                                  @RequestBody ChangeStateMedicationRequestDto changeStateRequest) {
        LOG.debug(CHANGE_STATE_REQUEST, institutionId, patientId, changeStateRequest);
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfoBo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        changeStateMedicationService.execute(patientInfoBo, new ChangeStateMedicationRequestBo(MedicationStatementStatus.SUSPENDED, changeStateRequest.getDayQuantity(), changeStateRequest.getObservations(), changeStateRequest.getMedicationsIds()));
        LOG.debug("suspend success");
    }


    @PutMapping(value = "/finalize")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public void finalizeMedication(@PathVariable(name = "institutionId") Integer institutionId,
                        @PathVariable(name = "patientId") Integer patientId,
                        @RequestBody ChangeStateMedicationRequestDto changeStateRequest) {
        LOG.debug(CHANGE_STATE_REQUEST, institutionId, patientId, changeStateRequest);
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfoBo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        changeStateMedicationService.execute(patientInfoBo, new ChangeStateMedicationRequestBo(MedicationStatementStatus.STOPPED, null, null, changeStateRequest.getMedicationsIds()));
        LOG.debug("finalize success");
    }

    @PutMapping(value = "/reactivate")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public void reactivateMedication(@PathVariable(name = "institutionId") Integer institutionId,
                                     @PathVariable(name = "patientId") Integer patientId,
                                     @RequestBody ChangeStateMedicationRequestDto changeStateRequest) {
        LOG.debug(CHANGE_STATE_REQUEST, institutionId, patientId, changeStateRequest);
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfoBo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        changeStateMedicationService.execute(patientInfoBo, new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, null, null, changeStateRequest.getMedicationsIds()));
        LOG.debug("reactivate success");
    }


    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public @ResponseBody
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_FARMACIA')")
    List<MedicationInfoDto> medicationRequestList(@PathVariable(name = "institutionId") Integer institutionId,
                                                  @PathVariable(name = "patientId") Integer patientId,
                                                  @RequestParam(value = "statusId", required = false) String statusId,
                                                  @RequestParam(value = "medicationStatement", required = false) String medicationStatement,
                                                  @RequestParam(value = "healthCondition", required = false) String healthCondition) {
        LOG.debug("medicationRequestList -> institutionId {}, patientId {}, statusId {}, medicationStatement {}, healthCondition {}", institutionId, patientId, statusId, medicationStatement, healthCondition);
        List<MedicationBo> resultService = listMedicationInfoService.execute(new MedicationFilterBo(patientId, statusId, medicationStatement, healthCondition));
        List<MedicationInfoDto> result = resultService.stream()
                .map(mid -> {
                    ProfessionalDto professionalDto = healthcareProfessionalExternalService.findProfessionalByUserId(mid.getUserId());
                    return listMedicationInfoMapper.parseTo(mid, professionalDto);
                })
                .collect(Collectors.toList());
        LOG.debug("medicationRequestList result -> {}", result);
        return result;
    }

    @GetMapping(value = "/{medicationRequestId}/download")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_FARMACIA')")
    public ResponseEntity<InputStreamResource> download(@PathVariable(name = "institutionId") Integer institutionId,
                                                        @PathVariable(name = "patientId") Integer patientId,
                                                        @PathVariable(name = "medicationRequestId") Integer medicationRequestId) throws PDFDocumentException {
        LOG.debug("medicationRequestList -> institutionId {}, patientId {}, medicationRequestId {}", institutionId, patientId, medicationRequestId);
        var medicationRequestBo = getMedicationRequestInfoService.execute(medicationRequestId);
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        var patientCoverageDto = patientExternalMedicalCoverageService.getCoverage(medicationRequestBo.getMedicalCoverageId());
        var context = createContext(medicationRequestBo, patientDto, patientCoverageDto);

        String template = "recipe_order_table";

        ByteArrayOutputStream os = pdfService.writer(template, context);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(os.toByteArray());
        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(os.size())
                .body(resource);
    }

    private Map<String, Object> createContext(MedicationRequestBo medicationRequestBo,
                                              BasicPatientDto patientDto,
                                              PatientMedicalCoverageDto patientCoverageDto){
        LOG.debug("Input parameters -> medicationRequestBo {}, patientDto {}, professionalDto {}, patientCoverageDto {}",
                medicationRequestBo, patientDto, patientCoverageDto);
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("recipe", true);
        ctx.put("order", false);
        ctx.put("request", medicationRequestBo);
        ctx.put("patient", patientDto);
		ctx.put("professional", authorFromDocumentFunction.apply(medicationRequestBo.getId()));
        ctx.put("patientCoverage", patientCoverageDto);
        var date = medicationRequestBo.getRequestDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		ctx.put("nameSelfDeterminationFF", featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));
        ctx.put("requestDate", date); LOG.debug("Output -> {}", ctx);
        return ctx;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ IllegalArgumentException.class })
    public ApiErrorDto handleValidationExceptions(IllegalArgumentException ex) {
        LOG.error("Constraint violation -> {}", ex.getMessage());
        return new ApiErrorDto("Constraint violation", ex.getMessage());
    }
}