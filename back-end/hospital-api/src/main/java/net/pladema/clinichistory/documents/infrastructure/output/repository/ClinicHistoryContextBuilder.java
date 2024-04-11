package net.pladema.clinichistory.documents.infrastructure.output.repository;

import ar.lamansys.refcounterref.infraestructure.output.repository.counterreference.CounterReferenceRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.EDocumentType;
import ar.lamansys.sgh.shared.infrastructure.input.service.institution.SharedInstitutionPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.nursing.SharedNursingConsultationPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.odontology.SharedOdontologyConsultationPort;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
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
import net.pladema.staff.domain.ProfessionalCompleteBo;
import net.pladema.staff.service.domain.ClinicalSpecialtyBo;
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

@RequiredArgsConstructor
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
		String patientAge = document.getPatientAgePeriod().contains("Y") ? document.getPatientAgePeriod().substring(1, document.getPatientAgePeriod().indexOf("Y")) : "0";
		context.put("patientAge", patientAge);
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
			ProfessionalCompleteBo professionalCompleteBo = healthcareProfessionalStorage.fetchProfessionalByUserId(document.getCreatedBy());

			List<String> professionalInfoList = new ArrayList<>();

			professionalInfoList.add(selfPerceived  && professionalCompleteBo.getNameSelfDetermination() != null && !professionalCompleteBo.getNameSelfDetermination().isBlank() ?
					professionalCompleteBo.getCompleteName(professionalCompleteBo.getNameSelfDetermination()) : professionalCompleteBo.getCompleteName(professionalCompleteBo.getFirstName()));

			List<ProfessionBo> professionalRelatedProfession = professionalCompleteBo.getProfessions();

			if (!professionalRelatedProfession.isEmpty()) {
				List<String> professionsInfoList = professionalRelatedProfession.stream().map(ProfessionBo::getDescription).collect(Collectors.toList());
				List<ProfessionSpecialtyBo> professionSpecialties = new ArrayList<>();
				List<LicenseNumberBo> licenses = new ArrayList<>();
				professionalRelatedProfession.forEach(profession -> {
					professionSpecialties.addAll(profession.getSpecialties());
					licenses.addAll(profession.getLicenses());
				});
				professionSpecialties.forEach(specialty -> {
					professionsInfoList.add(specialty.getSpecialty().getName());
					licenses.addAll(specialty.getLicenses());
				});
				licenses.forEach(licence -> professionsInfoList.add(LINE_BREAK + licence.getType() + ": " + licence.getNumber()));
				professionalInfoList.addAll(professionsInfoList.stream().distinct().collect(Collectors.toList()));
			}
			String professionalInfo = professionalInfoList.toString().substring(1, professionalInfoList.toString().length() - 1).replace(", ", LINE_BREAK);
			List<ClinicalRecordBo> documentRecords = completeDocumentRecords(document, professionalInfo);
			result.addAll(documentRecords);
		}
		return result;
	}

	private void addOutpatientProfessionalInfo(Map<String, Object> context, CHDocumentBo document, boolean selfPerceived){
		var professionalInformation = healthcareProfessionalStorage.fetchProfessionalByUserId(document.getCreatedBy());
		context.put("professionalCompleteName", (selfPerceived ? professionalInformation.getNameSelfDetermination() : professionalInformation.getFirstName()) + ' ' + professionalInformation.getLastName());
		List<ProfessionBo> professionalRelatedProfessions = professionalInformation.getProfessions();
		if (!professionalRelatedProfessions.isEmpty()){
			List<ProfessionSpecialtyBo> specialties = new ArrayList<>();
			List<LicenseNumberBo> licenses = new ArrayList<>();
			if (document.getDocumentTypeId().equals(EDocumentType.OUTPATIENT.getId()) || document.getDocumentTypeId().equals(EDocumentType.ODONTOLOGY.getId()) || document.getDocumentTypeId().equals(EDocumentType.NURSING.getId())) {
				professionalRelatedProfessions = professionalRelatedProfessions.stream()
						.filter(profession -> profession.getSpecialties().stream().anyMatch(specialty -> specialty.getSpecialty().getName().equals(document.getClinicalSpecialty()))).collect(Collectors.toList());
				context.put("clinicalSpecialty", document.getClinicalSpecialty());
				professionalRelatedProfessions.forEach(profession -> {
					profession.getSpecialties().forEach(specialty -> {
						if (specialty.getSpecialty().getName().equals(document.getClinicalSpecialty()))
							licenses.addAll(specialty.getLicenses());
					});
				});
			} else {
				professionalRelatedProfessions.forEach(profession -> specialties.addAll(profession.getSpecialties()));
				specialties.forEach(specialty -> licenses.addAll(specialty.getLicenses()));
			}
			List<String> professions = professionalRelatedProfessions.stream().map(ProfessionBo::getDescription).collect(Collectors.toList());
			context.put("professionalProfessions", professions.toString().substring(1, professions.toString().length() - 1));
			if (!specialties.isEmpty()) {
				List<String> specialtiesString = specialties.stream().map(ProfessionSpecialtyBo::getSpecialty).map(ClinicalSpecialtyBo::getName).distinct().collect(Collectors.toList());
				context.put("clinicalSpecialty", specialtiesString.toString().substring(1, specialtiesString.toString().length() - 1));
			}
			if(!licenses.isEmpty()){
				List<String> licensesWithType = new ArrayList<>();
				licenses.forEach(license -> licensesWithType.add(license.getType().getAcronym() + ": " + license.getNumber()));
				context.put("licenses", licensesWithType.toString().substring(1, licensesWithType.toString().length() - 1));
			}
		} else {
			context.put("clinicalSpecialty", document.getClinicalSpecialty());
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
