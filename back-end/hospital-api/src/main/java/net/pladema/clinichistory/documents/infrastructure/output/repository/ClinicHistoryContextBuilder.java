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
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationRepository;
import net.pladema.clinichistory.requests.medicationrequests.repository.MedicationRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.medicalconsultation.appointment.service.DocumentAppointmentService;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.patient.service.PatientMedicalCoverageService;
import net.pladema.staff.application.ports.HealthcareProfessionalStorage;
import net.pladema.staff.domain.LicenseNumberBo;

import net.pladema.staff.domain.ProfessionBo;
import net.pladema.staff.domain.ProfessionSpecialtyBo;
import net.pladema.staff.service.domain.ELicenseNumberTypeBo;
import net.pladema.user.service.HospitalUserService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClinicHistoryContextBuilder {

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
									   SharedNursingConsultationPort sharedNursingConsultationPort) {
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
	}


	public Map<String, Object> buildContext(CHDocumentBo document, Integer currentInstitutionId){
		Map<String, Object> ctx = new HashMap<>();
		boolean selfPerceived = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS);
		//Patient Info
		ctx.put("patient", patientExternalService.getBasicDataFromPatient(document.getPatientId()));
		ctx.put("patientAge", document.getPatientAgePeriod().substring(1, document.getPatientAgePeriod().indexOf("Y")));
		//Encounter Info
		ctx.put("encounterId", document.getSourceId());
		ctx.put("encounterType", document.getEncounterType().getValue());
		if (document.getStartDate() != null) ctx.put("startDate", document.getStartDate().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")));
		if (document.getEndDate() != null) ctx.put("endDate", document.getEndDate().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3")));

		ctx.put("institution", document.getInstitution());
		//Find the appointment related, if exists
		var appointment = documentAppointmentService.getDocumentAppointmentForDocument(document.getId());
		appointment.ifPresent(documentAppointment -> {
			var diary = diaryService.getCompleteDiaryByAppointment(documentAppointment.getAppointmentId());
			diary.ifPresent(completeDiaryBo ->{
				ctx.put("sector", completeDiaryBo.getSectorDescription());
				ctx.put("place", completeDiaryBo.getDoctorsOfficeDescription());
			});
		});
		getMedicalCoverageId(document).ifPresent(id -> {
			var patientMedicalCoverage = patientMedicalCoverageService.getCoverage(id);
			ctx.put("medicalCoverage", patientMedicalCoverage.orElse(null));
		});
		//Professional Info
		var professionalInformation = healthcareProfessionalStorage.fetchProfessionalByUserId(document.getCreatedBy());
		ctx.put("professionalCompleteName", (selfPerceived ? professionalInformation.getNameSelfDetermination() : professionalInformation.getFirstName()) + ' ' + professionalInformation.getLastName());
		ctx.put("clinicalSpecialty", document.getClinicalSpecialty());
		var professionalRelatedProfessions = professionalInformation.getProfessions().stream()
				.filter(profession -> profession.getSpecialties().stream().anyMatch(specialty -> specialty.getSpecialty().getName().equals(document.getClinicalSpecialty()))).collect(Collectors.toList());
		if (!professionalRelatedProfessions.isEmpty()){
			var professions = professionalRelatedProfessions.stream().map(ProfessionBo::getDescription).collect(Collectors.toList());
			ctx.put("professionalProfessions", professions.toString().substring(1, professions.toString().length() - 1));
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
			if(!licensesWithType.isEmpty()) ctx.put("licenses", licensesWithType.toString().substring(1, licensesWithType.toString().length() - 1));
		}
		//Clinical Records
		List<ClinicalRecordBo> clinicalRecords = document.getClinicalRecords();
		ctx.put("clinicalRecords", clinicalRecords);
		//Footer Info
		var printDateTime = LocalDateTime.now().atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("UTC-3"));
		var userInfo = hospitalUserService.getUserPersonInfo(UserInfo.getCurrentAuditor());
		ctx.put("user", (selfPerceived && userInfo.getNameSelfDetermination() != null && userInfo.getNameSelfDetermination().isBlank() ? userInfo.getNameSelfDetermination() : userInfo.getFirstName()) + ' ' + userInfo.getLastName());
		ctx.put("printDate", printDateTime);
		ctx.put("currentInstitution", sharedInstitutionPort.fetchInstitutionById(currentInstitutionId).getName());

		return ctx;
	}

	private Optional<Integer> getMedicalCoverageId(CHDocumentBo documentBo){
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
