package net.pladema.clinichistory.hospitalization.service.indication.nursingrecord;

import ar.lamansys.sgh.shared.infrastructure.input.service.NursingRecordDto;

import java.time.LocalDateTime;
import java.util.List;

public interface InternmentNursingRecordService {

	List<NursingRecordDto> getInternmentEpisodeNursingRecords(Integer internmentEpisodeId);

	boolean updateNursingRecordStatus(Integer id, String status, LocalDateTime administrationTime, Integer userId, String reason);

}
