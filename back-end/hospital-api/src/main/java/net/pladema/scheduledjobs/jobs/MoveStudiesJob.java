package net.pladema.scheduledjobs.jobs;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;
import net.pladema.establishment.service.domain.OrchestratorBO;
import net.pladema.establishment.service.domain.PacServerBO;
import net.pladema.imagenetwork.application.generatetokenstudypermissions.GenerateStudyTokenJWT;
import net.pladema.imagenetwork.derivedstudies.service.MoveStudiesService;

import net.pladema.imagenetwork.derivedstudies.service.domain.MoveStudiesBO;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.lamansys.mqtt.application.ports.MqttClientService;
import ar.lamansys.mqtt.domain.MqttMetadataBo;
import ar.lamansys.sgx.auth.jwt.application.login.exceptions.BadLoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.pladema.establishment.service.OrchestratorService;
import net.pladema.establishment.service.PacServerService;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
		value="scheduledjobs.movestudies.enabled",
		havingValue = "true",
		matchIfMissing = false)
public class MoveStudiesJob {

	public static final String MOVING = "MOVING";

	public static final String FINISHED = "FINISHED";

	public static final String PENDING = "PENDING";

	private static final Double MEGA = 1000000.0;

	private final GenerateStudyTokenJWT generateStudyTokenJWT;

	private final MoveStudiesService moveStudiesService;
	private final MqttClientService mqttClientService;

	private final OrchestratorService orchestratorService;

	private final PacServerService pacServerService;



	@Scheduled(cron = "${scheduledjobs.movestudies.seconds} " +
			"${scheduledjobs.movestudies.minutes} " +
			"${scheduledjobs.movestudies.hours} " +
			"${scheduledjobs.movestudies.dayofmonth} " +
			"${scheduledjobs.movestudies.month} " +
			"${scheduledjobs.movestudies.dayofweek}")
	@SchedulerLock(name = "MoveStudiesJob")
	@Transactional
	public void execute() throws BadLoginException, InterruptedException {
		log.warn("Scheduled MoveStudiesJob starting at {}", new Date());

		List<MoveStudiesBO> moveStudies = moveStudiesService.getMoveStudies();
		Map<Integer, List<MoveStudiesBO>> orchestrators = new HashMap<>();
		for(MoveStudiesBO moveStudyBO:moveStudies){
			List<MoveStudiesBO> list = orchestrators.get(moveStudyBO.getOrchestratorId());
			if (list == null){
				List<MoveStudiesBO> newList = new ArrayList<>();
				newList.add(moveStudyBO);
				orchestrators.put(moveStudyBO.getOrchestratorId(), newList);
			} else{
				list.add(moveStudyBO);
			}

		}
		List<Thread> threads = new ArrayList<>();
		for (Map.Entry<Integer, List<MoveStudiesBO>> entry : orchestrators.entrySet()) {
			List<MoveStudiesBO> result = entry.getValue();
			OrchestratorBO orchestrator = orchestratorService.getOrchestrator(entry.getKey());
			LocalTime now = LocalTime.now();
			Time executionStartTime = orchestrator.getExecutionStartTime();
			Time executionEndTime = orchestrator.getExecutionEndTime();
			if( now.isAfter(executionStartTime.toLocalTime())
					|| now.isBefore(executionEndTime.toLocalTime()) ){
				priorityProcessing(result, orchestrator);
				Integer numberToMove = orchestrator.getNumberToMove();
				List<MoveStudiesBO> subResult;
				if(result.size() > numberToMove){
					subResult = result.subList(0, numberToMove);
				} else {
					subResult = result;
				}
				if (!subResult.isEmpty() && !MOVING.equals(subResult.get(0).getStatus())){
					threads.add(new Thread(new MoveImageByOrchestrator(generateStudyTokenJWT, subResult,orchestrator, pacServerService, mqttClientService, moveStudiesService)));
				}
			}
		}
		threads.forEach(thread -> thread.run());

		log.warn("Scheduled MoveStudiesJob done at {}", new Date());
	}

	public void priorityProcessing(List<MoveStudiesBO>moveStudies, OrchestratorBO orchestrator){

		moveStudies.forEach(moveStudyBO ->calculatedPriority(moveStudyBO, orchestrator));
		Collections.sort(moveStudies, new Comparator<MoveStudiesBO>() {
			@Override
			public int compare(MoveStudiesBO p1, MoveStudiesBO p2) {
				return Double.compare(p2.getCalculatedPriority(), p1.getCalculatedPriority());
			}
		});
	}

	private void calculatedPriority(MoveStudiesBO moveStudyBO, OrchestratorBO orchestrator){

		String status = moveStudyBO.getStatus();
		if (MOVING.equals(status)){
			moveStudyBO.setCalculatedPriority(Double.MAX_VALUE -1);
		} else {
			if(moveStudyBO.getPriorityMax() > 0){
				moveStudyBO.setCalculatedPriority(Double.MAX_VALUE);
			} else{
				Double weightSize = orchestrator.getWeightSize();
				Double weightDays = orchestrator.getWeightDays();
				Double weightPriority = orchestrator.getWeightPriority();

				Double priority = moveStudyBO.getPriority() * weightPriority;

				Double megabytes = (moveStudyBO.getSizeImage() != null ? moveStudyBO.getSizeImage() : 1) / MEGA;
				Double size = megabytes * weightSize;

				LocalDate dateMove = moveStudyBO.getDerivedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate currentDate = LocalDate.now();
				long differenceDays = ChronoUnit.DAYS.between(dateMove, currentDate);
				Double old = differenceDays * weightDays ;

				moveStudyBO.setCalculatedPriority(priority + size + old);
			}
		}

	}
}

@Slf4j
class MoveImageByOrchestrator implements Runnable {


	private List<MoveStudiesBO>moveStudies;

	private final OrchestratorBO orchestrator;

	private final PacServerService pacServerService;

	private final MqttClientService mqttClientService;

	private final MoveStudiesService moveStudiesService;

	private final GenerateStudyTokenJWT generateStudyTokenJWT;

	public MoveImageByOrchestrator(GenerateStudyTokenJWT generateStudyTokenJWT, List<MoveStudiesBO>moveStudies, OrchestratorBO orchestrator, PacServerService pacServerService, MqttClientService mqttClientService, MoveStudiesService moveStudiesService) {
		this.moveStudies = moveStudies;
		this.orchestrator = orchestrator;
		this.pacServerService = pacServerService;
		this.mqttClientService = mqttClientService;
		this.moveStudiesService = moveStudiesService;
		this.generateStudyTokenJWT = generateStudyTokenJWT;
	}

	@SneakyThrows
	@Override
	public void run() {
		log.warn("Thread MoveImageByOrchestrator starting at {}", new Date());
		String topic = orchestrator.getBaseTopic() + "/MOVERESTUDIO";
		Integer attempsNumber = orchestrator.getAttempsNumber();
		for(MoveStudiesBO moveStudy: moveStudies){
			if(moveStudy.getAttempsNumber() < attempsNumber){
				PacServerBO pacServer = pacServerService.getPacServer(moveStudy.getPacServerId());
				String hostMove =pacServer.getDomanin();
				String token = generateStudyTokenJWT.run(moveStudy.getImageId());
				String json = "{\n" + "    \"StudyInstanceUID\": \"" + moveStudy.getImageId() + "\",\n" +
						"    \"to_host\": \"" + hostMove + "\",\n" +
						"    \"IdMove\": \"" + moveStudy.getId() + "\",\n" +
						"    \"Token\": \"" + token + "\" \n}" ;
				MqttMetadataBo data = new MqttMetadataBo(topic, json,false,2);
				mqttClientService.publish(data);

				if(!MoveStudiesJob.PENDING.equals(moveStudy.getStatus())){
					moveStudiesService.updateAttemps(moveStudy.getId(), moveStudy.getAttempsNumber() + 1);
				}


			} else {
				moveStudiesService.updateStatus(moveStudy.getId(), MoveStudiesJob.FINISHED);
			}
		}
		log.warn("Thread MoveImageByOrchestrator done at {}", new Date());

	}
}
