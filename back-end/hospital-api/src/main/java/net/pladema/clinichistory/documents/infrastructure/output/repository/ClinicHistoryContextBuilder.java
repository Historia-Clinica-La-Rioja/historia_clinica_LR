package net.pladema.clinichistory.documents.infrastructure.output.repository;

import ar.lamansys.refcounterref.infraestructure.output.repository.counterreference.CounterReferenceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.nursing.SharedNursingConsultationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.SharedOdontologyConsultationPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.clinichistory.documents.domain.CHDocumentBo;

import net.pladema.clinichistory.documents.domain.ClinicalRecordBo;
import net.pladema.clinichistory.documents.domain.ECHEncounterType;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.establishment.service.SectorService;
import net.pladema.medicalconsultation.appointment.service.DocumentAppointmentService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.establishment.service.BedService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.patient.service.PatientMedicalCoverageService;
import net.pladema.staff.application.ports.HealthcareProfessionalStorage;
import net.pladema.staff.domain.LicenseNumberBo;

import net.pladema.staff.domain.ProfessionBo;
import net.pladema.staff.domain.ProfessionSpecialtyBo;
import net.pladema.staff.service.domain.ELicenseNumberTypeBo;
import net.pladema.user.service.HospitalUserService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClinicHistoryContextBuilder {

	private static final String LINE_BREAK = "<br />";

	private final PatientExternalService patientExternalService;
	private final SharedInstitutionPort sharedInstitutionPort;
	private final HealthcareProfessionalStorage healthcareProfessionalStorage;
	private final LocalDateMapper localDateMapper;
	private final HospitalUserService hospitalUserService;
	private final OutpatientConsultationRepository outpatientConsultationRepository;
	private final ServiceRequestRepository serviceRequestRepository;
	private final CounterReferenceRepository counterReferenceRepository;
	private final MedicationRequestRepository medicationRequestRepository;
	private final PatientMedicalCoverageService patientMedicalCoverageService;
	private final DocumentAppointmentService documentAppointmentService;
	private final DiaryService diaryService;
	private final FeatureFlagsService featureFlagsService;
	private final SharedOdontologyConsultationPort sharedOdontologyConsultationPort;
	private final SharedNursingConsultationPort sharedNursingConsultationPort;
	private final InternmentEpisodeService internmentEpisodeService;
	private final BedService bedService;
	private final EmergencyCareEpisodeService emergencyCareEpisodeService;
	private final SectorService sectorService;

	public ClinicHistoryContextBuilder(PatientExternalService patientExternalService,
									   SharedInstitutionPort sharedInstitutionPort,
									   HealthcareProfessionalStorage healthcareProfessionalStorage,
									   LocalDateMapper localDateMapper,
									   HospitalUserService hospitalUserService,
									   OutpatientConsultationRepository outpatientConsultationRepository,
									   ServiceRequestRepository serviceRequestRepository,
									   CounterReferenceRepository counterReferenceRepository,
									   MedicationRequestRepository medicationRequestRepository,
									   PatientMedicalCoverageService patientMedicalCoverageService,
									   DocumentAppointmentService documentAppointmentService,
									   DiaryService diaryService,
									   FeatureFlagsService featureFlagsService,
									   SharedOdontologyConsultationPort sharedOdontologyConsultationPort,
									   SharedNursingConsultationPort sharedNursingConsultationPort,
									   InternmentEpisodeService internmentEpisodeService,
									   BedService bedService,
									   EmergencyCareEpisodeService emergencyCareEpisodeService,
									   SectorService sectorService) {
		this.patientExternalService = patientExternalService;
		this.sharedInstitutionPort = sharedInstitutionPort;
		this.healthcareProfessionalStorage = healthcareProfessionalStorage;
		this.localDateMapper = localDateMapper;
		this.hospitalUserService = hospitalUserService;
		this.outpatientConsultationRepository = outpatientConsultationRepository;
		this.serviceRequestRepository = serviceRequestRepository;
		this.counterReferenceRepository = counterReferenceRepository;
		this.medicationRequestRepository = medicationRequestRepository;
		this.patientMedicalCoverageService = patientMedicalCoverageService;
		this.documentAppointmentService = documentAppointmentService;
		this.diaryService = diaryService;
		this.featureFlagsService = featureFlagsService;
		this.sharedOdontologyConsultationPort = sharedOdontologyConsultationPort;
		this.sharedNursingConsultationPort = sharedNursingConsultationPort;
		this.internmentEpisodeService = internmentEpisodeService;
		this.bedService = bedService;
		this.emergencyCareEpisodeService = emergencyCareEpisodeService;
		this.sectorService = sectorService;
	}


	public Map<String, Object> buildOutpatientContext(CHDocumentBo document, Integer currentInstitutionId){
		Map<String, Object> ctx = new HashMap<>();
		boolean selfPerceived = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS);
		/* Patient Info */
		addPatientInfo(ctx, document);
		/* Encounter Info */
		addEncounterInfo(ctx, document, document.getSourceId(), ECHEncounterType.OUTPATIENT);
		/* Find the related appointment, if exists */
		var appointment = documentAppointmentService.getDocumentAppointmentForDocument(document.getId());
		appointment.ifPresent(documentAppointment -> {
			var diary = diaryService.getCompleteDiaryByAppointment(documentAppointment.getAppointmentId());
			diary.ifPresent(completeDiaryBo ->{
				ctx.put("sector", completeDiaryBo.getSectorDescription());
				ctx.put("place", completeDiaryBo.getDoctorsOfficeDescription());
			});
		});
		/* Medical Coverage */
		getOutpatientMedicalCoverageId(document).ifPresent(id -> {
			var patientMedicalCoverage = patientMedicalCoverageService.getCoverage(id);
			ctx.put("medicalCoverage", patientMedicalCoverage.orElse(null));
		});
		/* Professional Info */
 		addOutpatientProfessionalInfo(ctx, document, selfPerceived);
		/* Clinical Records */
		ctx.put("clinicalRecords", document.getClinicalRecords());
		/* Footer Info */
		addFooterInfo(ctx, currentInstitutionId, selfPerceived);
		return ctx;
	}

	public Map<String, Object> buildEpisodeContext(Integer episodeId, List<CHDocumentBo> documents, Integer currentInstitutionId, ECHEncounterType encounterType) {
		if (!documents.isEmpty()) {
			Map<String, Object> ctx = new HashMap<>();
			boolean selfPerceived = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS);
			CHDocumentBo referentialDocument = documents.get(0);
			/* PatientInfo */
			addPatientInfo(ctx, referentialDocument);
			/* Episode info */
			addEncounterInfo(ctx, referentialDocument, episodeId, encounterType);
			/* Clinical records */
			ctx.put("clinicalRecords", getEpisodeRecords(documents, selfPerceived));
			/* Footer Info */
			addFooterInfo(ctx, currentInstitutionId, selfPerceived);
			return ctx;
		}
		return new HashMap<>();
	}

	private void addPatientInfo(Map<String, Object> context, CHDocumentBo document){
		context.put("patient", patientExternalService.getBasicDataFromPatient(document.getPatientId()));
		context.put("patientAge", document.getPatientAgePeriod().substring(1, document.getPatientAgePeriod().indexOf("Y")));
	}

	private void addEncounterInfo(Map<String, Object> context, CHDocumentBo document, Integer episodeId, ECHEncounterType encounterType){
		context.put("encounterId", episodeId);
		context.put("encounterType", document.getEncounterType().getValue());
		if (document.getStartDate() != null) context.put("startDate", document.getStartDate());
		if (document.getEndDate() != null) context.put("endDate", document.getEndDate());
		context.put("institution", document.getInstitution());
		if (encounterType.equals(ECHEncounterType.HOSPITALIZATION)){
			var bedInfo = bedService.getBedInfo(internmentEpisodeService.getInternmentEpisode(episodeId, document.getInstitutionId()).getBedId());
			bedInfo.ifPresent(bedInfoVo -> {
				context.put("sector", bedInfoVo.getSector().getDescription());
				context.put("place", bedInfoVo.getRoom().getDescription() + " | " + bedInfoVo.getBed().getBedNumber());
			});
			var patientMedicalCoverage = internmentEpisodeService.getMedicalCoverage(episodeId);
			patientMedicalCoverage.ifPresent(patientMedicalCoverageBo -> context.put("medicalCoverage", patientMedicalCoverageBo));
		}
		if(encounterType.equals(ECHEncounterType.OUTPATIENT)){
			/* Find the related appointment, if exists */
			var appointment = documentAppointmentService.getDocumentAppointmentForDocument(document.getId());
			appointment.ifPresent(documentAppointment -> {
				var diary = diaryService.getCompleteDiaryByAppointment(documentAppointment.getAppointmentId());
				diary.ifPresent(completeDiaryBo ->{
					context.put("sector", completeDiaryBo.getSectorDescription());
					context.put("place", completeDiaryBo.getDoctorsOfficeDescription());
				});
			});
			/* Medical Coverage */
			getOutpatientMedicalCoverageId(document).ifPresent(id -> {
				var patientMedicalCoverage = patientMedicalCoverageService.getCoverage(id);
				context.put("medicalCoverage", patientMedicalCoverage.orElse(null));
			});
		}
		if (encounterType.equals(ECHEncounterType.EMERGENCY_CARE)){
			var emergencyCareEpisode = emergencyCareEpisodeService.get(episodeId, document.getInstitutionId());
			if (emergencyCareEpisode.getDoctorsOffice() != null) {
				context.put("place", emergencyCareEpisode.getDoctorsOffice().getDescription());
			} else if (emergencyCareEpisode.getShockroom() != null) {
				context.put("place", emergencyCareEpisode.getShockroom().getDescription());
			} else if (emergencyCareEpisode.getBed() != null) {
				var bedInfo = bedService.getBedInfo(emergencyCareEpisode.getBed().getId());
				bedInfo.ifPresent(bedInfoVo -> {
					context.put("sector", bedInfoVo.getSector().getDescription());
					context.put("place", bedInfoVo.getRoom().getDescription() + " | " + bedInfoVo.getBed().getBedNumber());
				});
			};
			var patientMedicalCoverage = patientMedicalCoverageService.getCoverage(emergencyCareEpisodeService.getPatientMedicalCoverageIdByEpisode(episodeId));
			patientMedicalCoverage.ifPresent(patientMedicalCoverageBo -> context.put("medicalCoverage", patientMedicalCoverageBo));
		}
	}

	private List<ClinicalRecordBo> getEpisodeRecords (List<CHDocumentBo> documents, boolean selfPerceived){
		List<ClinicalRecordBo> result = new ArrayList<>();
		for (CHDocumentBo document : documents) {
			var professionalInfo = healthcareProfessionalStorage.fetchProfessionalByUserId(document.getCreatedBy());
			String professional = selfPerceived  && professionalInfo.getNameSelfDetermination() != null && !professionalInfo.getNameSelfDetermination().isBlank() ? professionalInfo.getCompleteName(professionalInfo.getNameSelfDetermination()) : professionalInfo.getCompleteName(professionalInfo.getFirstName());
			var professionalRelatedProfession = professionalInfo.getProfessions().stream().filter(profession -> profession.getSpecialties().stream().anyMatch(specialty -> specialty.getSpecialty().getName().equals(document.getClinicalSpecialty()))).findFirst();
			if (professionalRelatedProfession.isPresent()) {
				professional = professional.concat(LINE_BREAK + professionalRelatedProfession.get().getDescription() + LINE_BREAK + document.getClinicalSpecialty());

				var nationalLicenseData = professionalRelatedProfession.get().getLicenses().stream().filter(license -> license.getType().equals(ELicenseNumberTypeBo.NATIONAL)).findFirst();
				if(nationalLicenseData.isPresent())
					professional = professional.concat(LINE_BREAK + nationalLicenseData.get().getType().getAcronym() + ": " + nationalLicenseData.get().getNumber());

				var stateLicenseData = professionalRelatedProfession.get().getLicenses().stream().filter(license -> license.getType().equals(ELicenseNumberTypeBo.PROVINCE)).findFirst();
				if (stateLicenseData.isPresent())
					professional = professional.concat(LINE_BREAK + stateLicenseData.get().getType().getAcronym() + ": " + stateLicenseData.get().getNumber());
			}
			List<ClinicalRecordBo> documentRecords = completeDocumentRecords(document, professional);
			result.addAll(documentRecords);
		}
		return result;
	}

	private void addOutpatientProfessionalInfo(Map<String, Object> context, CHDocumentBo document, boolean selfPerceived){
		var professionalInformation = healthcareProfessionalStorage.fetchProfessionalByUserId(document.getCreatedBy());
		context.put("professionalCompleteName", (selfPerceived ? professionalInformation.getNameSelfDetermination() : professionalInformation.getFirstName()) + ' ' + professionalInformation.getLastName());
		context.put("clinicalSpecialty", document.getClinicalSpecialty());
		var professionalRelatedProfessions = professionalInformation.getProfessions().stream()
				.filter(profession -> profession.getSpecialties().stream().anyMatch(specialty -> specialty.getSpecialty().getName().equals(document.getClinicalSpecialty()))).collect(Collectors.toList());
		if (!professionalRelatedProfessions.isEmpty()){
			var professions = professionalRelatedProfessions.stream().map(ProfessionBo::getDescription).collect(Collectors.toList());
			context.put("professionalProfessions", professions.toString().substring(1, professions.toString().length() - 1));
			var specialties = new ArrayList<ProfessionSpecialtyBo>();
			professionalRelatedProfessions.forEach(profession -> {
				specialties.addAll(profession.getSpecialties().stream().filter(specialty -> specialty.getSpecialty().getName().equals(document.getClinicalSpecialty())).collect(Collectors.toList()));
			});
			var licenses = new ArrayList<LicenseNumberBo>();
			specialties.forEach(specialty -> {
				licenses.addAll(specialty.getLicenses());
			});
			var licensesWithType = new ArrayList<String>();
			licenses.forEach(license -> licensesWithType.add(license.getType().getAcronym() + ": " + license.getNumber()));
			if(!licensesWithType.isEmpty()) context.put("licenses", licensesWithType.toString().substring(1, licensesWithType.toString().length() - 1));
		}
	}

	private void addFooterInfo(Map<String, Object> context, Integer institutionId, boolean selfPerceived){
		var printDateTime = LocalDateTime.now().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3"));
		var userInfo = hospitalUserService.getUserPersonInfo(UserInfo.getCurrentAuditor());
		context.put("user", (selfPerceived && userInfo.getNameSelfDetermination() != null && userInfo.getNameSelfDetermination().isBlank() ? userInfo.getNameSelfDetermination() : userInfo.getFirstName()) + ' ' + userInfo.getLastName());
		context.put("printDate", printDateTime);
		context.put("currentInstitution", sharedInstitutionPort.fetchInstitutionById(institutionId).getName());
	}

	private static List<ClinicalRecordBo> completeDocumentRecords(CHDocumentBo document, String professionalInfo) {
		List<ClinicalRecordBo> documentRecords = document.getClinicalRecords();
		documentRecords.forEach(record -> {
			record.setProfessionalInfo(professionalInfo);
			String createdOn = document.getCreatedOn().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")).toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + LINE_BREAK + document.getCreatedOn().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")).toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " hs";
			record.setCreatedOn(createdOn);
		});
		return documentRecords;
	}

	private Optional<Integer> getOutpatientMedicalCoverageId(CHDocumentBo documentBo){
		switch (documentBo.getDocumentTypeId()){
			case (DocumentType.OUTPATIENT):
				return outpatientConsultationRepository.getPatientMedicalCoverageId(documentBo.getSourceId());
			case (DocumentType.ORDER):
				return serviceRequestRepository.getMedicalCoverageId(documentBo.getSourceId());
			case (DocumentType.COUNTER_REFERENCE):
				return counterReferenceRepository.getPatientMedicalCoverageId(documentBo.getSourceId());
			case (DocumentType.RECIPE):
				return medicationRequestRepository.getMedicalCoverageId(documentBo.getSourceId());
			case (DocumentType.ODONTOLOGY):
				return sharedOdontologyConsultationPort.getPatientMedicalCoverageId(documentBo.getSourceId());
			case (DocumentType.NURSING):
				return sharedNursingConsultationPort.getPatientMedicalCoverageId(documentBo.getSourceId());
			default:
				return Optional.empty();
		}
	}

}
