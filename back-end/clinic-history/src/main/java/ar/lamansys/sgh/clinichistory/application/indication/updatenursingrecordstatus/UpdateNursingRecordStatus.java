package ar.lamansys.sgh.clinichistory.application.indication.updatenursingrecordstatus;

import ar.lamansys.sgh.clinichistory.application.ports.IndicationStorage;
import ar.lamansys.sgh.clinichistory.application.ports.NursingRecordStorage;
import ar.lamansys.sgh.clinichistory.domain.ips.NursingRecordBo;
import ar.lamansys.sgh.shared.infrastructure.input.service.EIndicationStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.ENursingRecordStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateNursingRecordStatus {

	private final NursingRecordStorage nursingRecordStorage;
	private final IndicationStorage indicationStorage;

	public boolean run (Integer id, String status, LocalDateTime administrationTime, Integer userId, String reason){
		log.debug("Input parameter -> id {}, status {}, administrationTime {}, userId {}, reason {}", id, status, administrationTime, userId, reason);
		boolean result = nursingRecordStorage.updateStatus(id, ENursingRecordStatus.map(status.toLowerCase()).getId(), administrationTime, userId, reason);
		if(result){
			nursingRecordStorage.getIndicationIdById(id).ifPresent(indicationId -> {
				Short indicationStatusId = getUpdatedIndicationStatusId(indicationId);
				indicationStorage.updateStatus(indicationId, indicationStatusId, userId);
			});
		}
		log.debug("Output -> {}", result);
		return result;
	}

	private Short getUpdatedIndicationStatusId(Integer indicationId){
		List<NursingRecordBo> indicationRecords = nursingRecordStorage.getIndicationNursingRecords(indicationId);
		int pending = 0;
		int completed = 0;
		int rejected = 0;
		for (NursingRecordBo nr: indicationRecords){
			ENursingRecordStatus status = ENursingRecordStatus.map(nr.getStatusId());
			switch (status) {
				case COMPLETED: {
					completed++;
					break;
				}
				case REJECTED: {
					rejected++;
					break;
				}
				default: {
					pending++;
					break;
				}
			}
		}
		if (rejected == indicationRecords.size()){
			return EIndicationStatus.REJECTED.getId();
		}
		if (completed == indicationRecords.size()){
			return EIndicationStatus.COMPLETED.getId();
		}
		if(pending == indicationRecords.size() || (completed == 0 && rejected != 0)) {
			return EIndicationStatus.INDICATED.getId();
		}
		return EIndicationStatus.IN_PROGRESS.getId();
	}


}
