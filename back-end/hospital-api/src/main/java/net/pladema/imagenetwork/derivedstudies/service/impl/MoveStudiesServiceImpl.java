package net.pladema.imagenetwork.derivedstudies.service.impl;

import ar.lamansys.mqtt.application.ports.MqttClientService;
import ar.lamansys.mqtt.domain.MqttMetadataBo;
import lombok.RequiredArgsConstructor;
import net.pladema.establishment.service.EquipmentService;
import net.pladema.establishment.service.OrchestratorService;
import net.pladema.establishment.service.PacServerService;
import net.pladema.establishment.service.domain.EquipmentBO;
import net.pladema.establishment.service.domain.OrchestratorBO;
import net.pladema.establishment.service.domain.PacServerBO;
import net.pladema.imagenetwork.application.savepacsherestudyishosted.SavePacWhereStudyIsHosted;
import net.pladema.imagenetwork.derivedstudies.repository.MoveStudiesRepository;
import net.pladema.imagenetwork.derivedstudies.repository.entity.MoveStudies;
import net.pladema.imagenetwork.derivedstudies.service.MoveStudiesService;
import net.pladema.imagenetwork.derivedstudies.service.domain.MoveStudiesBO;
import net.pladema.imagenetwork.domain.StudyPacBo;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentBo;
import net.pladema.medicalconsultation.equipmentdiary.service.EquipmentDiaryService;
import net.pladema.medicalconsultation.equipmentdiary.service.domain.CompleteEquipmentDiaryBo;
import net.pladema.scheduledjobs.jobs.MoveStudiesJob;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoveStudiesServiceImpl implements MoveStudiesService {

	private final MoveStudiesRepository moveStudiesRepository;
	private final AppointmentService appointmentService;
	private final EquipmentService equipmentService;
	private final AppointmentOrderImageService appointmentOrderImageService;
	private final EquipmentDiaryService equipmentDiaryService;

	private final OrchestratorService orchestratorService;

	private final PacServerService pacServerService;

	private final MqttClientService mqttClientService;
	private final SavePacWhereStudyIsHosted savePacWhereStudyIsHosted;
	@Override
	public Integer save(MoveStudiesBO moveStudyBO) {
		MoveStudies moveStudy = new MoveStudies( moveStudyBO.getAppointmentId(),
				moveStudyBO.getOrchestratorId(),
				moveStudyBO.getImageId(),
				moveStudyBO.getPacServerId(),
				moveStudyBO.getPriority(),
				new Date(),
				moveStudyBO.getPriorityMax(),
				moveStudyBO.getAttempsNumber(),
				moveStudyBO.getStatus());
		return moveStudiesRepository.save(moveStudy).getId();
	}

	@Override
	public Integer create(Integer appointmentId) {
		List<PacServerBO> pacServers = pacServerService.getAllPacServer();
		String imageId = appointmentOrderImageService.getImageId(appointmentId).orElse("none");
		MoveStudiesBO moveStudyBO = new MoveStudiesBO();
		if ( !pacServers.isEmpty() && !imageId.equals("none")){
			moveStudyBO.setAppointmentId(appointmentId);
			moveStudyBO.setStatus(MoveStudiesJob.PENDING);
			moveStudyBO.setPriorityMax(0);
			moveStudyBO.setAttempsNumber(0);
			Integer pacServerId = pacServers.get(0).getId();
			moveStudyBO.setPacServerId(pacServerId);

			AppointmentBo appointment = appointmentService.getEquipmentAppointment(appointmentId).orElse(null);
			Integer diaryId = appointment.getDiaryId();
			CompleteEquipmentDiaryBo equipmentDiary = equipmentDiaryService.getEquipmentDiary(diaryId).orElse(null);
			Integer equipmentId = equipmentDiary.getEquipmentId();
			EquipmentBO equipmentBO = equipmentService.getEquipment(equipmentId);
			Integer orchestratorId = equipmentBO.getOrchestratorId();
			moveStudyBO.setOrchestratorId(orchestratorId);
			moveStudyBO.setImageId(imageId);
			moveStudyBO.setPriority(5);
			return save(moveStudyBO);
		}
		return -1;

	}

	@Override
	public List<MoveStudiesBO> getMoveStudies() {
		List<MoveStudies> listMoveStudies = moveStudiesRepository.getListMoveStudies("FINISHED");
		List<MoveStudiesBO> result = listMoveStudies.stream().map(this::createMoveStudyBOInstance).collect(Collectors.toList());
		return result;
	}

	@Override
	public void updateSize(Integer idMove, Integer size) {
		moveStudiesRepository.updateSize(idMove, size);
	}

	@Override
	public void updateStatus(Integer idMove, String status) {
		moveStudiesRepository.updateStatus(idMove, status);
	}

	@Override
	public void updateStatusAndResult(Integer idMove, String status, String result) {
		moveStudiesRepository.updateStatusandResult(idMove, status, result);

		if ("200".equals(result)) {
			Optional<MoveStudies> moveStudy = moveStudiesRepository.findById(idMove);
			if (moveStudy.isPresent()) {
				MoveStudies ms = moveStudy.get();
				savePacWhereStudyIsHosted.run(new StudyPacBo(ms.getImageId(), ms.getPacServerId()));
			}
		}

	}

	@Override
	public void updateAttemps(Integer idMove, Integer attemps) {
		moveStudiesRepository.updateAttemps(idMove,attemps);
	}

	@Override
	public void getSizeFromOrchestrator(Integer idMove) {

		Optional<MoveStudies> optionalMoveStudies = moveStudiesRepository.findById(idMove);

		if(optionalMoveStudies.isPresent()){
			MoveStudies moveStudy = optionalMoveStudies.get();
			moveStudy.getOrchestratorId();
			OrchestratorBO orchestrator = orchestratorService.getOrchestrator(moveStudy.getOrchestratorId());
			String topic = orchestrator.getBaseTopic() + "/SIZE";
			String studyInstanceUID =   "   \"StudyInstanceUID\": \"" + moveStudy.getImageId()+ "\", \n";
			String idMoveJson =  "    \"IdMove\": \"" +idMove + "\"\n";
			String json =  "{\n"
					+ studyInstanceUID
					+ idMoveJson
					+ "}";



			MqttMetadataBo data = new MqttMetadataBo(topic, json,false,2);
			mqttClientService.publish(data);
		}

	}

	private MoveStudiesBO createMoveStudyBOInstance(MoveStudies moveStudy){
		MoveStudiesBO moveStudyBO = new MoveStudiesBO();
		moveStudyBO.setId(moveStudy.getId());
		moveStudyBO.setAppointmentId(moveStudy.getAppointmentId());
		moveStudyBO.setAttempsNumber(moveStudy.getAttempsNumber());
		moveStudyBO.setStatus(moveStudy.getStatus());
		moveStudyBO.setImageId(moveStudy.getImageId());
		moveStudyBO.setPacServerId(moveStudy.getPacServerId());
		moveStudyBO.setOrchestratorId(moveStudy.getOrchestratorId());
		moveStudyBO.setPriority(moveStudy.getPriority());
		moveStudyBO.setDerivedDate(moveStudy.getMoveDate());
		moveStudyBO.setPriorityMax(moveStudy.getPriorityMax());
		moveStudyBO.setResult(moveStudy.getResult());
		moveStudyBO.setSizeImage(moveStudy.getSizeImage());
		return moveStudyBO;
	}
}
