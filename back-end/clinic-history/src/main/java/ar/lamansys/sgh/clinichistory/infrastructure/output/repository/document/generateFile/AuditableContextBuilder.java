package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.generateFile;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DentalActionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.mapper.RiskFactorMapper;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.shared.domain.general.ContactInfoBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedAddressPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPersonPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedDiagnosticImagingOrder;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.SharedImmunizationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.immunization.VaccineDoseInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.medicalcoverage.PatientMedicalCoverageService;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionCompleteDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import net.pladema.assets.service.AssetsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuditableContextBuilder {

	private final Logger logger;
	private final Function<Integer, BasicPatientDto> basicDataFromPatientLoader;
	private final Function<Long, ProfessionalCompleteDto> authorFromDocumentFunction;
	private final Function<Integer, ClinicalSpecialtyDto> clinicalSpecialtyDtoFunction;
	private final SharedImmunizationPort sharedImmunizationPort;
	private final RiskFactorMapper riskFactorMapper;
	private final LocalDateMapper localDateMapper;
	private final FeatureFlagsService featureFlagsService;

	private final SharedInstitutionPort sharedInstitutionPort;

	private final PatientMedicalCoverageService patientMedicalCoverageService;

	private final AssetsService assetsService;
	private final SharedStaffPort sharedStaffPort;
	private final SharedDiagnosticImagingOrder sharedDiagnosticImagingOrder;

	private final Function<Integer, ContactInfoBo> personContactInfoFunction;

	private final SharedPersonPort sharedPersonPort;

	private final Function<Integer, String> sectorNameFunction;

	private final Function<Integer, String> roomNumberFunction;

	private final Function<Integer, String> doctorsOfficeDescriptionFunction;

	private final Function<Integer, String> shockRoomDescriptionFunction;
	
	private final DocumentInvolvedProfessionalFinder documentInvolvedProfessionalFinder;

	private final SharedAddressPort sharedAddressPort;

	@Value("${prescription.domain.number}")
	private Integer recipeDomain;

	public AuditableContextBuilder(
			SharedPatientPort sharedPatientPort,
			DocumentAuthorFinder documentAuthorFinder,
			ClinicalSpecialtyFinder clinicalSpecialtyFinder,
			SharedImmunizationPort sharedImmunizationPort,
			RiskFactorMapper riskFactorMapper,
			LocalDateMapper localDateMapper,
			FeatureFlagsService featureFlagsService, SharedInstitutionPort sharedInstitutionPort,
			PatientMedicalCoverageService patientMedicalCoverageService,
			AssetsService assetsService,
			SharedStaffPort sharedStaffPort,
			SharedDiagnosticImagingOrder sharedDiagnosticImagingOrder,
			SharedPersonPort sharedPersonPort,
			SectorFinder sectorFinder,
			RoomFinder roomFinder,
			DoctorsOfficeFinder doctorsOfficeFinder,
			ShockRoomFinder shockRoomFinder,
			DocumentInvolvedProfessionalFinder documentInvolvedProfessionalFinder,
			SharedAddressPort sharedAddressPort) {
		this.sharedImmunizationPort = sharedImmunizationPort;
		this.localDateMapper = localDateMapper;
		this.sharedInstitutionPort = sharedInstitutionPort;
		this.sharedStaffPort = sharedStaffPort;
		this.sharedDiagnosticImagingOrder = sharedDiagnosticImagingOrder;
		this.logger = LoggerFactory.getLogger(getClass());
		this.basicDataFromPatientLoader = sharedPatientPort::getBasicDataFromPatient;
		this.authorFromDocumentFunction = documentAuthorFinder::getAuthor;
		this.clinicalSpecialtyDtoFunction = clinicalSpecialtyFinder::getClinicalSpecialty;
		this.riskFactorMapper = riskFactorMapper;
		this.featureFlagsService = featureFlagsService;
		this.patientMedicalCoverageService = patientMedicalCoverageService;
		this.assetsService = assetsService;
		this.personContactInfoFunction = sharedPersonPort::getPersonContactInfoById;
		this.sharedPersonPort = sharedPersonPort;
		this.sectorNameFunction = sectorFinder::getSectorName;
		this.roomNumberFunction = roomFinder::getRoomNumber;
		this.doctorsOfficeDescriptionFunction = doctorsOfficeFinder::getDoctorsOfficeDescription;
		this.shockRoomDescriptionFunction = shockRoomFinder::getShockRoomDescription;
		this.documentInvolvedProfessionalFinder = documentInvolvedProfessionalFinder;
		this.sharedAddressPort = sharedAddressPort;
	}

	public <T extends IDocumentBo> Map<String,Object> buildContext(T document, Integer patientId){
		logger.debug("Input parameters -> document {}", document);
		Map<String,Object> contextMap = new HashMap<>();
		addPatientInfo(contextMap, patientId, document.getDocumentType());
		if (document.getDocumentType() == DocumentType.DIGITAL_RECIPE) {
			addDigitalRecipeContextDocumentData(contextMap, document);
			logger.debug("Built context for patient {} and document {} is {}", patientId, document.getId(), contextMap);
			return contextMap;
		}
		if (document.getDocumentType() == DocumentType.RECIPE) {
			addRecipeContextDocumentData(contextMap, document);
			logger.debug("Built context for patient {} and document {} is {}", patientId, document.getId(), contextMap);
			return contextMap;
		}
		if (document.getDocumentType() == DocumentType.MEDICAL_IMAGE_REPORT) {
			addImageReportData(contextMap, document);
			logger.debug("Built context for patient {} and document {} is {}", patientId, document.getId(), contextMap);
			return contextMap;
		}
		addDocumentInfo(contextMap, document);
		logger.debug("Built context for patient {} and document {} is {}", patientId, document.getId(), contextMap);
		return contextMap;
	}
	private void addPatientInfo(Map<String,Object> contextMap, Integer patientId, Short documentType) {
		var patientDto = basicDataFromPatientLoader.apply(patientId);
		contextMap.put("patient", patientDto);
		contextMap.put("patientCompleteName", patientDto.getCompletePersonName(featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS)));
		contextMap.put("patientAge", calculatePatientAge(patientDto));

		contextMap.put("selfPerceivedFF", featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));
		if (documentType == DocumentType.DIGITAL_RECIPE) {
			var patientIdentificationNumberBarCode = generateDigitalRecipeBarCode(patientDto.getIdentificationNumber());
			contextMap.put("patientIdentificationNumberBarCode", patientIdentificationNumberBarCode);
		}
	}
	
	private <T extends IDocumentBo> void addDocumentInfo(Map<String,Object> contextMap, T document) {
		contextMap.put("mainDiagnosis", document.getMainDiagnosis());
		contextMap.put("diagnosis", document.getDiagnosis());
		contextMap.put("reasons", document.getReasons());
		contextMap.put("procedures", document.getProcedures());
		contextMap.put("problems", document.getProblems());
		contextMap.put("personalHistories", document.getPersonalHistories());
		contextMap.put("familyHistories", document.getFamilyHistories());
		contextMap.put("allergies", document.getAllergies());
		contextMap.put("dentalDiagnostics", document.getDentalActions().stream().filter(DentalActionBo::isDiagnostic).collect(Collectors.toList()));
		contextMap.put("dentalProcedures", document.getDentalActions().stream().filter(DentalActionBo::isProcedure).collect(Collectors.toList()));
		contextMap.put("cipresFieldsFF", featureFlagsService.isOn(AppFeature.HABILITAR_CAMPOS_CIPRES_EPICRISIS));
		contextMap.put("otherProblems", document.getOtherProblems());
		contextMap.put("externalCause", document.getExternalCause());
		contextMap.put("obstetricEvent", document.getObstetricEvent());
		contextMap.put("conclusions", document.getConclusions());
		contextMap.put("healthcareProfessionals", document.getHealthcareProfessionals());
		contextMap.put("preoperativeDiagnosis", document.getPreoperativeDiagnosis());
		contextMap.put("postoperativeDiagnosis", document.getPostoperativeDiagnosis());
		contextMap.put("surgeryProcedures", document.getSurgeryProcedures());
		contextMap.put("anesthesia", document.getAnesthesia());
		contextMap.put("cultures", document.getCultures());
		contextMap.put("frozenSectionBiopsies", document.getFrozenSectionBiopsies());
		contextMap.put("drainages", document.getDrainages());
		contextMap.put("prosthesis", document.getProsthesisDescription());
		contextMap.put("description", document.getDescription());

		contextMap.put("anestheticHistory", document.getAnestheticHistory());
		contextMap.put("preMedications", document.getPreMedications());
		contextMap.put("histories", document.getHistories());
		contextMap.put("procedureDescription", document.getProcedureDescription());
		contextMap.put("anestheticPlans", document.getAnestheticPlans());
		contextMap.put("analgesicTechniques", document.getAnalgesicTechniques());
		contextMap.put("anestheticTechniques", document.getAnestheticTechniques());
		contextMap.put("fluidAdministrations", document.getFluidAdministrations());
		contextMap.put("anestheticAgents", document.getAnestheticAgents());
		contextMap.put("nonAnestheticDrugs", document.getNonAnestheticDrugs());
		contextMap.put("antibioticProphylaxis", document.getAntibioticProphylaxis());
		contextMap.put("measuringPoints", document.getMeasuringPoints());
		contextMap.put("postAnesthesiaStatus", document.getPostAnesthesiaStatus());
		contextMap.put("chart", document.getAnestheticChart());

		var immunizations =  mapImmunizations(document.getImmunizations());
		contextMap.put("billableImmunizations", immunizations.stream().filter(ImmunizationInfoDto::isBillable).collect(Collectors.toList()));
		contextMap.put("nonBillableImmunizations", immunizations.stream().filter(i -> !i.isBillable()).collect(Collectors.toList()));
		contextMap.put("institutionName",sharedInstitutionPort.fetchInstitutionById(document.getInstitutionId()).getName());
		contextMap.put("medications", document.getMedications());
		contextMap.put("anthropometricData", document.getAnthropometricData());
		contextMap.put("riskFactors", riskFactorMapper.toRiskFactorsReportDto(document.getRiskFactors()));
		contextMap.put("otherRiskFactors", document.getOtherRiskFactors());
		contextMap.put("notes", document.getNotes());

		var author = authorFromDocumentFunction.apply(document.getId());
		contextMap.put("author", author);

		var involvedProfessionals = documentInvolvedProfessionalFinder.find(document.getInvolvedHealthcareProfessionalIds());
		contextMap.put("involvedProfessionals", involvedProfessionals);

		contextMap.put("clinicalSpecialty", clinicalSpecialtyDtoFunction.apply(document.getClinicalSpecialtyId()));
		contextMap.put("performedDate", document.getPerformedDate().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")));
		contextMap.put("nameSelfDeterminationFF", featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS));
		contextMap.put("appLogo", generatePdfImage("pdf/hsi-footer-118x21.png"));
		contextMap.put("institutionLogo", generatePdfImage("pdf/hsi-header-250x72.png"));
		contextMap.put("encounterId", document.getEncounterId());
		contextMap.put("institution",sharedInstitutionPort.fetchInstitutionById(document.getInstitutionId()));
		contextMap.put("sector", sectorNameFunction.apply(document.getSectorId()));
		contextMap.put("room", roomNumberFunction.apply(document.getRoomId()));
		contextMap.put("doctorsOffice", doctorsOfficeDescriptionFunction.apply(document.getDoctorsOfficeId()));
		contextMap.put("shockRoom", shockRoomDescriptionFunction.apply(document.getShockRoomId()));

		var patientCoverage = patientMedicalCoverageService.getCoverage(document.getMedicalCoverageId());
		patientCoverage.ifPresent(sharedPatientMedicalCoverageBo -> contextMap.put("patientCoverage", sharedPatientMedicalCoverageBo));
	}

	private <T extends IDocumentBo> void addRecipeContextDocumentData(Map<String, Object> ctx, T document) {
		ctx.put("recipe", true);
		ctx.put("order", false);
		ctx.put("request", document);
		var professional = authorFromDocumentFunction.apply(document.getId());
		ctx.put("professional", professional);
		ctx.put("professionalName", sharedPersonPort.getCompletePersonNameById(professional.getPersonId()));

		ctx.put("contactInfo", personContactInfoFunction.apply(((BasicPatientDto) ctx.get("patient")).getPerson().getId()));

		var date = document.getPerformedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		ctx.put("requestDate", date);
		ctx.put("institution",sharedInstitutionPort.fetchInstitutionById(document.getInstitutionId()));
	}

	private <T extends IDocumentBo> void addImageReportData(Map<String, Object> ctx, T document) {
		var diagnosticReportList = document.getDiagnosticReports().stream()
				.map(DiagnosticReportBo::getDiagnosticReportSnomedPt)
				.collect(Collectors.joining(", "));
		ctx.put("diagnosticReportList", diagnosticReportList);
		ctx.put("institutionHeader",sharedInstitutionPort.fetchInstitutionDataById(document.getInstitutionId()));
		ctx.put("institutionAddress",sharedInstitutionPort.fetchInstitutionAddress(document.getInstitutionId()));
		ctx.put("author", authorFromDocumentFunction.apply(document.getId()));

		setAuthorOrder(ctx, document);

		ctx.put("performedDate", document.getPerformedDate().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")));
		ctx.put("evolutionNote", document.getEvolutionNote());
		ctx.put("conclusions", document.getConclusions());
	}

	private <T extends IDocumentBo> void setAuthorOrder(Map<String, Object> ctx, T document) {
		var diagnosticReports = document.getDiagnosticReports();
		if (!diagnosticReports.isEmpty()) {
			document.getDiagnosticReports()
					.stream()
					.findFirst()
					.flatMap(l -> sharedDiagnosticImagingOrder.getDiagnosticImagingOrderAuthorId(document.getEncounterId()))
					.ifPresent(professionalId -> ctx.put("authorOrder", sharedStaffPort.getProfessionalComplete(professionalId)));

			document.getDiagnosticReports()
					.stream()
					.findFirst()
					.map(DiagnosticReportBo::getEncounterId)
					.flatMap(sharedDiagnosticImagingOrder::getDiagnosticImagingTranscribedOrderAuthor)
					.ifPresent(professionalName -> ctx.put("authorTranscribedOrder", professionalName));
		}
	}

	private <T extends IDocumentBo> void addDigitalRecipeContextDocumentData(Map<String, Object> ctx, T document) {
		var date = document.getPerformedDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		var dateUntil = document.getPerformedDate().plusDays(30).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		ctx.put("requestDate", date);
		ctx.put("dateUntil", dateUntil);
		ctx.put("institution",sharedInstitutionPort.fetchInstitutionById(document.getInstitutionId()));


		var recipeNumberWithDomain = recipeDomain + "-" + document.getEncounterId().toString();
		var recipeNumberBarCode = generateDigitalRecipeBarCode(recipeNumberWithDomain);
		ctx.put("recipeNumberBarCode", recipeNumberBarCode);
		ctx.put("recipeNumber", recipeNumberWithDomain);

		var recipeUuidWithDomain = completeDomain(recipeDomain.toString()) + "-" + document.getUuid().toString();
		var recipeUuidBarCode = generateDigitalRecipeBarCode(recipeUuidWithDomain);
		ctx.put("recipeUuidBarCode", recipeUuidBarCode);
		ctx.put("recipeUuid", recipeUuidWithDomain);

		var professionalInformation = authorFromDocumentFunction.apply(document.getId());
		var professionalRelatedProfession = professionalInformation.getProfessions().stream().filter(profession -> profession.getSpecialties().stream().anyMatch(specialty -> specialty.getSpecialty().getId().equals(document.getClinicalSpecialtyId()))).findFirst();

		var patientCoverage = patientMedicalCoverageService.getCoverage(document.getMedicalCoverageId());

		patientCoverage.ifPresent(sharedPatientMedicalCoverageBo -> ctx.put("patientCoverage", sharedPatientMedicalCoverageBo));
		ctx.put("professional", professionalInformation);
		ctx.put("medications", document.getMedications());
		ctx.put("professionalProfession", professionalRelatedProfession.<Object>map(ProfessionCompleteDto::getDescription).orElse(null));

		if (professionalRelatedProfession.isPresent()) {
			var clinicalSpecialty = professionalRelatedProfession.get().getSpecialties().stream().filter(specialty -> specialty.getSpecialty().getId().equals(document.getClinicalSpecialtyId())).findFirst();
			ctx.put("clinicalSpecialty", clinicalSpecialty.<Object>map(professionSpecialtyDto -> professionSpecialtyDto.getSpecialty().getName()).orElse(null));

			var nationalLicenseData = professionalRelatedProfession.get().getAllLicenses().stream().filter(license -> license.getType().equals("MN")).findFirst();
			nationalLicenseData.ifPresent(licenseNumberDto -> ctx.put("nationalLicense", licenseNumberDto.getNumber()));

			var stateProvinceData = professionalRelatedProfession.get().getAllLicenses().stream().filter(license -> license.getType().equals("MP")).findFirst();
			stateProvinceData.ifPresent(licenseNumberDto -> ctx.put("stateLicense", licenseNumberDto.getNumber()));
		}

		ctx.put("logo", generatePdfImage("pdf/digital_recipe_logo.png"));
		ctx.put("headerLogos", generatePdfImage("pdf/digital_recipe_header_logo.png"));
		ctx.put("isArchived", document.getIsArchived());
		ctx.put("institution",sharedInstitutionPort.fetchInstitutionById(document.getInstitutionId()));
		ctx.put("patientAddress", sharedAddressPort.fetchPatientCompleteAddress(document.getPatientId()));
	}

	private List<ImmunizationInfoDto> mapImmunizations(List<ImmunizationBo> immunizations) {
		return immunizations.stream().map(this::mapToImmunizationInfoDto).collect(Collectors.toList());
	}

	private ImmunizationInfoDto mapToImmunizationInfoDto(ImmunizationBo immunizationBo) {
		ImmunizationInfoDto result = new ImmunizationInfoDto();
		result.setId(immunizationBo.getId());
		result.setBillable(immunizationBo.isBillable());
		result.setLotNumber(immunizationBo.getLotNumber());
		result.setSnomed(new SnomedDto(immunizationBo.getSnomed().getSctid(), immunizationBo.getSnomed().getPt()));
		result.setAdministrationDate(localDateMapper.fromLocalDateToString(immunizationBo.getAdministrationDate()));
		result.setInstitutionInfo(immunizationBo.getInstitutionInfo());
		result.setDoctorInfo(immunizationBo.getDoctorInfo());
		result.setNote(immunizationBo.getNote());
		result.setCondition(immunizationBo.getConditionId() != null ?
				sharedImmunizationPort.fetchVaccineConditionInfo(immunizationBo.getConditionId()) : null);
		result.setScheme(immunizationBo.getSchemeId() != null ?
				sharedImmunizationPort.fetchVaccineSchemeInfo(immunizationBo.getSchemeId()) : null);
		result.setDose(immunizationBo.getDose() != null ?
						new VaccineDoseInfoDto(immunizationBo.getDose().getDescription(), immunizationBo.getDose().getOrder()) : null);
		return result;
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
		StoredFileBo asset = assetsService.getFile(path);
		try {
			var image = ImageIO.read(asset.getResource().getStream());
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "png", outputStream);
			return Base64.getEncoder().encodeToString(outputStream.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String calculatePatientAge(BasicPatientDto patientDto) {
		if (patientDto.getPerson() == null || patientDto.getPerson().getPersonAge() == null)
			return "";
		var personAge = patientDto.getPerson().getPersonAge();
		if (personAge.getTotalDays() < 46)
			return personAge.getTotalDays() + " días";
		if (personAge.getYears() > 0)
			return personAge.getYears() + (personAge.getYears() == 1 ? " año" : " años");
		return personAge.getMonths() + (personAge.getMonths() == 1 ? " mes " : " meses ") + personAge.getDays() + (personAge.getDays() == 1 ? " día" : " días" );
	}

	private String completeDomain(String domain) {
		return String.format("%0" + (11 - domain.length()) + "d%s", 0, domain);

	}
}

