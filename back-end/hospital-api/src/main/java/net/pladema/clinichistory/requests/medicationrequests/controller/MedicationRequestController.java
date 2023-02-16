package net.pladema.clinichistory.requests.medicationrequests.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile.DocumentAuthorFinder;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionCompleteDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;

import com.google.zxing.common.BitMatrix;

import net.pladema.clinichistory.requests.medicationrequests.service.ValidateMedicationRequestGenerationService;

import net.pladema.staff.controller.dto.ProfessionalLicenseNumberValidationResponseDto;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.oned.Code128Writer;
import net.pladema.staff.service.domain.ELicenseNumberTypeBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.files.pdf.PdfService;
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

    private final PdfService pdfService;

	private final FeatureFlagsService featureFlagsService;

	private final ValidateMedicationRequestGenerationService validateMedicationRequestGenerationService;

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
									   DocumentAuthorFinder documentAuthorFinder,
									   ValidateMedicationRequestGenerationService validateMedicationRequestGenerationService) {
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
		this.validateMedicationRequestGenerationService = validateMedicationRequestGenerationService;
		this.authorFromDocumentFunction = (Long documentId) -> documentAuthorFinder.getAuthor(documentId);
	}


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
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
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
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
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA, PRESCRIPTOR')")
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
    public ResponseEntity<InputStreamResource> download(@PathVariable(name = "institutionId") Integer institutionId,
                                                        @PathVariable(name = "patientId") Integer patientId,
                                                        @PathVariable(name = "medicationRequestId") Integer medicationRequestId) throws PDFDocumentException {
        LOG.debug("medicationRequestList -> institutionId {}, patientId {}, medicationRequestId {}", institutionId, patientId, medicationRequestId);
        var medicationRequestBo = getMedicationRequestInfoService.execute(medicationRequestId);
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        var patientCoverageDto = patientExternalMedicalCoverageService.getCoverage(medicationRequestBo.getMedicalCoverageId());
		Map<String, Object> context;
		String template;
		if (!featureFlagsService.isOn(AppFeature.HABILITAR_RECETA_DIGITAL)) {
			context = createContext(medicationRequestBo, patientDto, patientCoverageDto);
			template = "recipe_order_table";
		}
		else {
			context = createContextDigitalRecipe(medicationRequestBo, patientDto, patientCoverageDto);
			template = "digital_recipe";
		}

        ByteArrayOutputStream os = pdfService.writer(template, context);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(os.toByteArray());
        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(os.size())
                .body(resource);
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

	private Map<String, Object> createContextDigitalRecipe(MedicationRequestBo medicationRequestBo,
											  BasicPatientDto patientDto,
											  PatientMedicalCoverageDto patientCoverageDto) {
		LOG.debug("Input parameters -> medicationRequestBo {}, patientDto {}, patientCoverageDto {}", medicationRequestBo, patientDto, patientCoverageDto);
		Map<String, Object> ctx = new HashMap<>();

		var date = medicationRequestBo.getRequestDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		var dateUntil = medicationRequestBo.getRequestDate().plusDays(30).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		ctx.put("requestDate", date);
		ctx.put("dateUntil", dateUntil);

		var patientIdentificationNumberBarCode = generateDigitalRecipeBarCode(patientDto.getIdentificationNumber());
		ctx.put("patientIdentificationNumberBarCode", patientIdentificationNumberBarCode);

		var recipeNumberBarCode = generateDigitalRecipeBarCode(recipeDomain + "." + medicationRequestBo.getMedicationRequestId().toString());
		ctx.put("recipeNumberBarCode", recipeNumberBarCode);

		var professionalInformation = authorFromDocumentFunction.apply(medicationRequestBo.getId());
		var professionalRelatedProfession = professionalInformation.getProfessions().stream().filter(profession -> profession.getSpecialties().stream().anyMatch(specialty -> specialty.getSpecialty().getId().equals(medicationRequestBo.getClinicalSpecialtyId()))).findFirst();

		ctx.put("patient", patientDto);
		ctx.put("patientCoverage", patientCoverageDto);
		ctx.put("professional", professionalInformation);
		ctx.put("medications", medicationRequestBo.getMedications());
		ctx.put("professionalProfession", professionalRelatedProfession.<Object>map(ProfessionCompleteDto::getDescription).orElse(null));

		if (professionalRelatedProfession.isPresent()) {
			var clinicalSpecialty = professionalRelatedProfession.get().getSpecialties().stream().filter(specialty -> specialty.getSpecialty().getId().equals(medicationRequestBo.getClinicalSpecialtyId())).findFirst();
			ctx.put("clinicalSpecialty", clinicalSpecialty.<Object>map(professionSpecialtyDto -> professionSpecialtyDto.getSpecialty().getName()).orElse(null));

			var nationalLicenseData = professionalRelatedProfession.get().getAllLicenses().stream().filter(license -> license.getType().equals(ELicenseNumberTypeBo.NATIONAL.getAcronym())).findFirst();
			nationalLicenseData.ifPresent(licenseNumberDto -> ctx.put("nationalLicense", licenseNumberDto.getNumber()));

			var stateProvinceData = professionalRelatedProfession.get().getAllLicenses().stream().filter(license -> license.getType().equals(ELicenseNumberTypeBo.PROVINCE.getAcronym())).findFirst();
			stateProvinceData.ifPresent(licenseNumberDto -> ctx.put("stateLicense", licenseNumberDto.getNumber()));
		}

		ctx.put("logo", generatePdfImage("/assets/webapp/pdf/health_ministry_logo.png"));
		ctx.put("headerLogos", generatePdfImage("/assets/webapp/pdf/digital_recipe_header_logo.png"));

		return ctx;
	}

	private String generateDigitalRecipeBarCode(String dataToEncode) {
		Code128Writer writer = new Code128Writer();
		BitMatrix barCode = writer.encode(dataToEncode, BarcodeFormat.CODE_128, 200, 100);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			MatrixToImageWriter.writeToStream(barCode, "JPEG" , outputStream, new MatrixToImageConfig());
			return Base64.getEncoder().encodeToString(outputStream.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String generatePdfImage(String path) {
		try {
			var image = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", outputStream);
			return Base64.getEncoder().encodeToString(outputStream.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ RuntimeException.class })
    public ApiErrorDto handleValidationExceptions(RuntimeException ex) {
        LOG.error("Constraint violation -> {}", ex.getMessage());
        return new ApiErrorDto("Constraint violation", ex.getMessage());
    }
}