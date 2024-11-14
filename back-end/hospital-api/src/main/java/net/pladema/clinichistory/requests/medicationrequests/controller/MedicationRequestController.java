package net.pladema.clinichistory.requests.medicationrequests.controller;

import static ar.lamansys.sgx.shared.files.pdf.EPDFTemplate.RECIPE_ORDER_TABLE;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions.SnowstormPortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
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

import ar.lamansys.sgh.clinichistory.application.fetchdocumentfile.FetchDocumentFileById;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile.DocumentAuthorFinder;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.MedicationStatementStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorDto;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.files.pdf.GeneratedPdfResponseService;
import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import ar.lamansys.sgx.shared.security.UserInfo;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.requests.controller.dto.DocumentRequestDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.ChangeStateMedicationRequestDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.MedicationInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.mapper.CreateMedicationRequestMapper;
import net.pladema.clinichistory.requests.medicationrequests.controller.mapper.ListMedicationInfoMapper;
import net.pladema.clinichistory.requests.medicationrequests.service.CancelPrescriptionLineState;
import net.pladema.clinichistory.requests.medicationrequests.service.ChangeStateMedicationService;
import net.pladema.clinichistory.requests.medicationrequests.service.CreateMedicationRequestService;
import net.pladema.clinichistory.requests.medicationrequests.service.FetchMostFrequentPharmacos;
import net.pladema.clinichistory.requests.medicationrequests.service.GetMedicationRequestByDocument;
import net.pladema.clinichistory.requests.medicationrequests.service.GetMedicationRequestInfoService;
import net.pladema.clinichistory.requests.medicationrequests.service.ListMedicationInfoService;
import net.pladema.clinichistory.requests.medicationrequests.service.NewMedicationRequestNotification;
import net.pladema.clinichistory.requests.medicationrequests.service.ValidateMedicationRequestGenerationService;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.ChangeStateMedicationRequestBo;
import net.pladema.clinichistory.requests.medicationrequests.service.domain.MedicationFilterBo;
import ar.lamansys.sgh.clinichistory.domain.document.impl.MedicationRequestBo;
import net.pladema.clinichistory.requests.medicationrequests.service.impl.notification.NewMedicationRequestNotificationArgs;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.patient.controller.service.PatientExternalMedicalCoverageService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.dto.ProfessionalLicenseNumberValidationResponseDto;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;

@RestController
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/medication-requests")
@Tag(name = "Medication Request", description = "Medication Request")
@Validated
public class MedicationRequestController {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationRequestController.class);
    public static final String CHANGE_STATE_REQUEST = "change-state -> institutionId {}, patientId {}, changeStateRequest {}";

	@Value("${prescription.domain.number}")
	private Integer recipeDomain;

    private final CreateMedicationRequestService createMedicationRequestService;

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final CreateMedicationRequestMapper createMedicationRequestMapper;

    private final ListMedicationInfoService listMedicationInfoService;

    private final ListMedicationInfoMapper listMedicationInfoMapper;

    private final ChangeStateMedicationService  changeStateMedicationService;

    private final PatientExternalService patientExternalService;

    private final GetMedicationRequestInfoService getMedicationRequestInfoService;

    private final PatientExternalMedicalCoverageService patientExternalMedicalCoverageService;

	private final GeneratedPdfResponseService generatedPdfResponseService;

	private final FeatureFlagsService featureFlagsService;

	private final ValidateMedicationRequestGenerationService validateMedicationRequestGenerationService;

	private final Function<Long, ProfessionalCompleteDto> authorFromDocumentFunction;

	private final NewMedicationRequestNotification newMedicationRequestNotification;

	private final GetMedicationRequestByDocument getMedicationRequestByDocument;

	private final FetchMostFrequentPharmacos fetchMostFrequentPharmacos;

	private final CancelPrescriptionLineState cancelPrescriptionLineState;

	private final FetchDocumentFileById fetchDocumentFileById;

	private final SharedPersonPort sharedPersonPort;

	public MedicationRequestController(CreateMedicationRequestService createMedicationRequestService,
									   HealthcareProfessionalExternalService healthcareProfessionalExternalService,
									   CreateMedicationRequestMapper createMedicationRequestMapper,
									   ListMedicationInfoService listMedicationInfoService,
									   ListMedicationInfoMapper listMedicationInfoMapper,
									   ChangeStateMedicationService changeStateMedicationService,
									   PatientExternalService patientExternalService,
									   GetMedicationRequestInfoService getMedicationRequestInfoService,
									   PatientExternalMedicalCoverageService patientExternalMedicalCoverageService,
									   GeneratedPdfResponseService generatedPdfResponseService,
									   FeatureFlagsService featureFlagsService,
									   DocumentAuthorFinder documentAuthorFinder,
									   ValidateMedicationRequestGenerationService validateMedicationRequestGenerationService,
									   NewMedicationRequestNotification newMedicationRequestNotification,
									   GetMedicationRequestByDocument getMedicationRequestByDocument,
									   FetchMostFrequentPharmacos fetchMostFrequentPharmacos,
									   CancelPrescriptionLineState cancelPrescriptionLineState,
									   FetchDocumentFileById fetchDocumentFileById,
									   SharedPersonPort sharedPersonPort) {
        this.createMedicationRequestService = createMedicationRequestService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.createMedicationRequestMapper = createMedicationRequestMapper;
        this.listMedicationInfoService = listMedicationInfoService;
        this.listMedicationInfoMapper = listMedicationInfoMapper;
        this.changeStateMedicationService = changeStateMedicationService;
        this.patientExternalService = patientExternalService;
        this.getMedicationRequestInfoService = getMedicationRequestInfoService;
        this.patientExternalMedicalCoverageService = patientExternalMedicalCoverageService;
        this.generatedPdfResponseService = generatedPdfResponseService;
		this.featureFlagsService = featureFlagsService;
		this.validateMedicationRequestGenerationService = validateMedicationRequestGenerationService;
		this.authorFromDocumentFunction = (Long documentId) -> documentAuthorFinder.getAuthor(documentId);
		this.newMedicationRequestNotification = newMedicationRequestNotification;
		this.getMedicationRequestByDocument = getMedicationRequestByDocument;
		this.fetchMostFrequentPharmacos = fetchMostFrequentPharmacos;
		this.cancelPrescriptionLineState = cancelPrescriptionLineState;
		this.fetchDocumentFileById = fetchDocumentFileById;
		this.sharedPersonPort = sharedPersonPort;
	}


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
    public @ResponseBody
    List<DocumentRequestDto> create(@PathVariable(name = "institutionId") Integer institutionId,
									@PathVariable(name = "patientId") Integer patientId,
									@RequestBody @Valid PrescriptionDto medicationRequest){
        LOG.debug("create -> institutionId {}, patientId {}, medicationRequest {}", institutionId, patientId, medicationRequest);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        MedicationRequestBo medicationRequestBo = createMedicationRequestMapper.parseTo(institutionId, doctorId, patientId, medicationRequest);
		List<DocumentRequestDto> result = createMedicationRequestService.execute(medicationRequestBo)
				.stream().map(dr-> new DocumentRequestDto(dr.getRequestId(), dr.getDocumentId()))
				.collect(Collectors.toList());
        LOG.debug("create result -> {}", result);
        return result;
    }

    @PutMapping(value = "/suspend")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
    public void suspendMedication(@PathVariable(name = "institutionId") Integer institutionId,
                                  @PathVariable(name = "patientId") Integer patientId,
                                  @RequestBody ChangeStateMedicationRequestDto changeStateRequest) {
        LOG.debug(CHANGE_STATE_REQUEST, institutionId, patientId, changeStateRequest);
        changeStateMedicationService.execute(patientId, new ChangeStateMedicationRequestBo(MedicationStatementStatus.SUSPENDED, changeStateRequest.getDayQuantity(), changeStateRequest.getObservations(), changeStateRequest.getMedicationsIds()));
        LOG.debug("suspend success");
    }


    @PutMapping(value = "/finalize")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
    public void finalizeMedication(@PathVariable(name = "institutionId") Integer institutionId,
                        @PathVariable(name = "patientId") Integer patientId,
                        @RequestBody ChangeStateMedicationRequestDto changeStateRequest) {
        LOG.debug(CHANGE_STATE_REQUEST, institutionId, patientId, changeStateRequest);
        changeStateMedicationService.execute(patientId, new ChangeStateMedicationRequestBo(MedicationStatementStatus.STOPPED, null, null, changeStateRequest.getMedicationsIds()));
        LOG.debug("finalize success");
    }

    @PutMapping(value = "/reactivate")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public void reactivateMedication(@PathVariable(name = "institutionId") Integer institutionId,
                                     @PathVariable(name = "patientId") Integer patientId,
                                     @RequestBody ChangeStateMedicationRequestDto changeStateRequest) {
        LOG.debug(CHANGE_STATE_REQUEST, institutionId, patientId, changeStateRequest);
        changeStateMedicationService.execute(patientId, new ChangeStateMedicationRequestBo(MedicationStatementStatus.ACTIVE, null, null, changeStateRequest.getMedicationsIds()));
        LOG.debug("reactivate success");
    }


	@GetMapping("/medicationRequestList")
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

	@GetMapping("/medicationRequestListByUser")
	@ResponseStatus(code = HttpStatus.OK)
	public @ResponseBody
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_FARMACIA, PRESCRIPTOR')")
	List<MedicationInfoDto> medicationRequestListByUser(@PathVariable(name = "institutionId") Integer institutionId,
												  @PathVariable(name = "patientId") Integer patientId,
												  @RequestParam(value = "statusId", required = false) String statusId,
												  @RequestParam(value = "medicationStatement", required = false) String medicationStatement,
												  @RequestParam(value = "healthCondition", required = false) String healthCondition) {
		LOG.debug("medicationRequestList -> institutionId {}, patientId {}, statusId {}, medicationStatement {}, healthCondition {}", institutionId, patientId, statusId, medicationStatement, healthCondition);
		List<MedicationBo> resultService = listMedicationInfoService.execute(new MedicationFilterBo(patientId, statusId, medicationStatement, healthCondition), UserInfo.getCurrentAuditor());
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
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_FARMACIA, PRESCRIPTOR')")
    public ResponseEntity<Resource> download(@PathVariable(name = "institutionId") Integer institutionId,
											 @PathVariable(name = "patientId") Integer patientId,
											 @PathVariable(name = "medicationRequestId") Integer medicationRequestId) throws PDFDocumentException {
        LOG.debug("medicationRequestList -> institutionId {}, patientId {}, medicationRequestId {}", institutionId, patientId, medicationRequestId);
        var medicationRequestBo = getMedicationRequestInfoService.execute(medicationRequestId);
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        var patientCoverageDto = patientExternalMedicalCoverageService.getCoverage(medicationRequestBo.getMedicalCoverageId());
		Map<String, Object> context = createContext(medicationRequestBo, patientDto, patientCoverageDto);
		String filename = String.format("%s_%s", patientDto.getIdentificationNumber(), medicationRequestId);

		return StoredFileResponse.sendGeneratedBlob(//MedicationRequestService.download
				generatedPdfResponseService.generatePdf(RECIPE_ORDER_TABLE, context, filename)
		);
    }

	@GetMapping(value = "/documentId/{documentId}/notify")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_FARMACIA, PRESCRIPTOR')")
	public void notify(@PathVariable(name = "institutionId") Integer institutionId,
														@PathVariable(name = "patientId") Integer patientId,
														@PathVariable(name = "documentId") Long documentId,
					   									@RequestParam(value = "patientEmail") String patientEmail) throws PDFDocumentException, IOException {
		LOG.debug("medicationRequestList -> institutionId {}, patientId {}, documentId {}, patientEmail {}", institutionId, patientId, documentId, patientEmail);
		var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
		var savedDocument = fetchDocumentFileById.run(documentId);
		Integer medicationRequestId = getMedicationRequestByDocument.run(documentId);
		NewMedicationRequestNotificationArgs args = mapToMedicationRequestNotificationBo(
				medicationRequestId,
				patientDto,
				savedDocument
		);
		newMedicationRequestNotification.run(args, patientEmail);
	}

	@GetMapping(value = "/validate")
	public ResponseEntity<ProfessionalLicenseNumberValidationResponseDto> validateMedicationRequestGeneration(@PathVariable(name = "institutionId") Integer institutionId,
																											  @PathVariable(name = "patientId") Integer patientId) {
		LOG.debug("medicationRequestList -> institutionId {}, patientId {}", institutionId, patientId);
		BasicPatientDto patientBasicData = patientExternalService.getBasicDataFromPatient(patientId);
		Integer healthcareProfessionalUserId = UserInfo.getCurrentAuditor();
		ProfessionalDto healthcareProfessionalData = healthcareProfessionalExternalService.findProfessionalByUserId(UserInfo.getCurrentAuditor());
		ProfessionalLicenseNumberValidationResponseDto response = validateMedicationRequestGenerationService.execute(healthcareProfessionalUserId, healthcareProfessionalData, patientBasicData);
		return ResponseEntity.of(Optional.of(response));
	}

	@GetMapping(value = "/most-frequent-pharmacos")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
	public ResponseEntity<List<SharedSnomedDto>> mostFrequentPharmacosPreinscription(@PathVariable(name = "institutionId") Integer institutionId,
																					   @PathVariable(name = "patientId") Integer patientId) throws SnowstormPortException {
		LOG.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
		List<SharedSnomedDto> result = fetchMostFrequentPharmacos.run(institutionId)
				.stream()
				.map(bo ->  new SharedSnomedDto(bo.getSctid(), bo.getPt()))
				.collect(Collectors.toList());
		LOG.debug("SharedSnomedDto result -> {}", result);
		return ResponseEntity.ok(result);
	}

	@PutMapping(value = "/cancel-prescription-line-state")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
	public void cancelPrescriptionLineState(@PathVariable(name = "institutionId") Integer institutionId,
											@PathVariable(name = "patientId") Integer patientId,
											@RequestParam(value = "medicationStatementId") Integer medicationStatementId) {
		LOG.debug("Input parameters -> institutionId {}, medicationStatementId {}, patientId {}", institutionId, medicationStatementId, patientId);

		if (!featureFlagsService.isOn(AppFeature.HABILITAR_RECETA_DIGITAL))
			throw new ConstraintViolationException("Receta digital no se encuentra activa", Collections.emptySet());

		cancelPrescriptionLineState.execute(medicationStatementId);
		LOG.debug("cancel success");
	}

	private Map<String, Object> createContext(MedicationRequestBo medicationRequestBo,
                                              BasicPatientDto patientDto,
                                              PatientMedicalCoverageDto patientCoverageDto){
        LOG.debug("Input parameters -> medicationRequestBo {}, patientDto {}, patientCoverageDto {}",
                medicationRequestBo, patientDto, patientCoverageDto);
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("recipe", true);
        ctx.put("order", false);
        ctx.put("request", medicationRequestBo);
        ctx.put("patient", patientDto);
		var professional = authorFromDocumentFunction.apply(medicationRequestBo.getId());
		ctx.put("professional", professional);
		ctx.put("professionalName", sharedPersonPort.getCompletePersonNameById(professional.getPersonId()));
        ctx.put("patientCoverage", patientCoverageDto);
        var date = medicationRequestBo.getRequestDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        ctx.put("requestDate", date);
		ctx.put("selfPerceivedFF", featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));

		LOG.debug("Output -> {}", ctx);
        return ctx;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ RuntimeException.class })
    public ApiErrorDto handleValidationExceptions(RuntimeException ex) {
        LOG.error("Constraint violation -> {}", ex.getMessage());
        return new ApiErrorDto("Constraint violation", ex.getMessage());
    }

	private NewMedicationRequestNotificationArgs mapToMedicationRequestNotificationBo(
			Integer recipeId, BasicPatientDto patientDto, StoredFileBo resource) {
		LOG.debug("Input parameters -> recipeId {}, patientDto {}, resource {}", recipeId, patientDto, resource);
		NewMedicationRequestNotificationArgs result = NewMedicationRequestNotificationArgs.builder()
				.recipeId(recipeId)
				.patient(patientDto)
				.resources(List.of(resource))
				.recipeIdWithDomain(recipeDomain + "-" + recipeId)
				.build();
		return result;
	}
}