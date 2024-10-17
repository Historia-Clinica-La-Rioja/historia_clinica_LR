package net.pladema.medicalconsultation.appointment.service.impl;

import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferenceCounterReference;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.service.DiagnosticReportInfoService;
import net.pladema.clinichistory.requests.transcribed.application.getbyappointmentid.GetTranscribedServiceRequestByAppointmentId;
import net.pladema.establishment.service.EquipmentService;
import net.pladema.establishment.service.OrchestratorService;
import net.pladema.establishment.service.domain.EquipmentBO;
import net.pladema.establishment.service.domain.OrchestratorBO;
import net.pladema.imagenetwork.application.exception.StudyException;
import net.pladema.imagenetwork.derivedstudies.application.exception.MoveStudiesException;
import net.pladema.imagenetwork.derivedstudies.domain.exception.EMoveStudiesException;
import net.pladema.imagenetwork.domain.exception.EStudyException;
import net.pladema.medicalconsultation.appointment.repository.EquipmentAppointmentAssnRepository;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.EquipmentAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.CompleteEquipmentDiaryBo;
import net.pladema.modality.service.ModalityService;
import net.pladema.modality.service.domain.ModalityBO;
import net.pladema.patient.controller.service.PatientExternalService;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class EquipmentAppointmentServiceImpl implements EquipmentAppointmentService {

	private static final String OUTPUT = "Output -> {}";

	private final AppointmentService appointmentService;

	private final EquipmentService equipmentService;

	private final EquipmentDiaryService equipmentDiaryService;

	private final OrchestratorService orchestratorService;

	private final EquipmentAppointmentAssnRepository equipmentAppointmentAssnRepository;

	private final ModalityService modalityService;

	private final PatientExternalService patientExternalService;

	private final SharedReferenceCounterReference sharedReferenceCounterReference;

	private final AppointmentOrderImageService appointmentOrderImageService;

	private final DiagnosticReportInfoService diagnosticReportInfoService;

	private final GetTranscribedServiceRequestByAppointmentId getTranscribedServiceRequestByAppointmentId;

	@Override
	public Optional<AppointmentBo> getEquipmentAppointment(Integer appointmentId) {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		AppointmentBo result = equipmentAppointmentAssnRepository.getEquipmentAppointment(appointmentId).stream()
				.findFirst()
				.map(AppointmentBo::fromAppointmentVo)
				.map(appointmentBo -> {
					Integer diaryId = appointmentBo.getDiaryId();
					setIsAppointmentProtected(appointmentBo, diaryId);
					appointmentBo.setOrderData(diagnosticReportInfoService.getByAppointmentId(appointmentId));
					appointmentBo.setTranscribedOrderData(getTranscribedServiceRequestByAppointmentId.run(appointmentId).orElse(null));
                    return appointmentBo;
                })
				.orElse(null);

		log.debug("Output -> appointmentId {}, exist {}", appointmentId, result != null);
		return Optional.ofNullable(result);
	}

	private void setIsAppointmentProtected(AppointmentBo appointment, Integer diaryId) {
		boolean isProtected = !sharedReferenceCounterReference.getProtectedAppointmentsIds(List.of(diaryId)).isEmpty();
		appointment.setProtected(isProtected);
	}

	@Override
	public MqttMetadataBo setToPublishWorkList(Integer institutionId, Integer appointmentId) {

		AppointmentBo appointment = appointmentService.getEquipmentAppointment(appointmentId)
				.orElseThrow(() -> new StudyException(EStudyException.APPOINTMENT_NOT_FOUND, "appointment.not.found"));

		Integer diaryId = appointment.getDiaryId();
		CompleteEquipmentDiaryBo equipmentDiary = equipmentDiaryService.getEquipmentDiary(diaryId)
				.orElseThrow(() -> new StudyException(EStudyException.DIARY_NOT_FOUND, "diary.invalid.id"));

		Integer equipmentId = equipmentDiary.getEquipmentId();
		EquipmentBO equipmentBO = Optional.ofNullable(equipmentService.getEquipment(equipmentId))
				.orElseThrow(() -> new StudyException(EStudyException.EQUIPMENT_NOT_FOUND, "app.imagenetwork.error.equipment-not-found"));

		Integer orchestratorId = equipmentBO.getOrchestratorId();
		OrchestratorBO orchestrator = Optional.ofNullable(orchestratorService.getOrchestrator(orchestratorId))
				.orElseThrow(() -> new MoveStudiesException(EMoveStudiesException.ORCHESTRATOR_NOT_FOUND, "orchestrator.invalid.id"));

		Integer modalityId = equipmentBO.getModalityId();
		ModalityBO modalityBO = Optional.ofNullable(modalityService.getModality(modalityId))
				.orElseThrow(() -> new StudyException(EStudyException.MODALITY_NOT_FOUND, "app.imagenetwork.error.modality-not-found"));

		Integer patientId =appointment.getPatientId();
		BasicPatientDto basicDataPatient = Optional.ofNullable(patientExternalService.getBasicDataFromPatient(patientId))
				.orElseThrow(() -> new StudyException(EStudyException.PATIENT_NOT_FOUND, "app.imagenetwork.error.patient-not-found"));

		String identificationNumber = basicDataPatient.getIdentificationNumber();
		String identification = identificationNumber == null ?basicDataPatient.getId()+"": identificationNumber;
		String UID= "1." + ThreadLocalRandom.current().nextInt(1,10) + "." +
				ThreadLocalRandom.current().nextInt(1,10) + "." +
				ThreadLocalRandom.current().nextInt(1,100) + "." +
				ThreadLocalRandom.current().nextInt(1,100) + "." +
				ThreadLocalRandom.current().nextInt(1,1000) + "." +
				ThreadLocalRandom.current().nextInt(1,1000) + "." +
				identification;
		String date = appointment.getDate().toString().replace("-","");
		String time = appointment.getHour().toString().replace(":","") + "00";
		String birthDate = null;
		char gender = 0;
		BasicDataPersonDto person = basicDataPatient.getPerson();
		if (person != null) {
			birthDate = person.getBirthDate() != null ? person.getBirthDate().toString().replace("-", "") : null;

			gender = person.getGender() != null
					&& person.getGender().getDescription() != null
					&& basicDataPatient.getPerson().getGender().getDescription().toCharArray().length > 0 ? basicDataPatient.getPerson().getGender().getDescription().toCharArray()[0] : ' ';
		}
		String accessionNumber =   "    \"AccessionNumber\": \"" + institutionId +"-" + appointmentId + "\", \n";
		String studyInstanceUID =   "    \"StudyInstanceUID\": \"" + UID+ "\", \n";
		String aeTitle =   "    \"ScheduledStationAETitle\": \"" + equipmentBO.getAeTitle() + "\",\n";
		String startDate = "    \"ScheduledProcedureStepStartDate\": \"" + date + "\",\n";
		String startTime = "    \"ScheduledProcedureStepStartTime\": \"" + time + "\",\n";
		String patientIdStr = "    \"PatientID\": \"" + identification + "\",\n";
		String patientName = "    \"PatientName\": \"" + basicDataPatient.getLastName() + " " + basicDataPatient.getFirstName() + "\",\n";
		String patientBirthDate = "    \"PatientBirthDate\": \"" + birthDate + "\",\n";
		String patientSex = "    \"PatientSex\": \"" + gender + "\",\n";
		String studyDescription = "    \"StudyDescription\": \"" + "description order" + "\",\n";
		String modality = "    \"Modality\": \"" + modalityBO.getAcronym() + "\"\n";
		String json =  "{\n" + accessionNumber;
		if(!equipmentBO.getCreateId()){
				json += studyInstanceUID;
		}
		json += aeTitle
				+ startDate
				+ startTime
				+ patientIdStr
				+ patientName
				+ patientSex
				+ studyDescription
				+ patientBirthDate
				+ modality
				+ "}";

		String topic = orchestrator.getBaseTopic() + "/LISTATRABAJO";


		MqttMetadataBo data = new MqttMetadataBo(topic, json,false,2);

		if(!equipmentBO.getCreateId()){
			appointmentOrderImageService.setImageId(appointmentId,	UID);
		}
		else{
			appointmentOrderImageService.setImageId(appointmentId,	"AccessionNumber" + institutionId +"-" + appointmentId);
		}

		return data;
	}
}
