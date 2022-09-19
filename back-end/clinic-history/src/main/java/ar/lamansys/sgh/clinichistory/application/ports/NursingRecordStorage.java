package ar.lamansys.sgh.clinichistory.application.ports;

import ar.lamansys.sgh.clinichistory.domain.ips.IndicationSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.NursingRecordBo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface NursingRecordStorage {

	List<Integer> createNursingRecordsFromIndication(IndicationSummaryBo indication);

	List<NursingRecordBo> getInternmentEpisodeNursingRecords(Integer internmentEpisodeId);

	boolean updateStatus(Integer id, Short statusId, LocalDateTime administrationTime, Integer userId, String reason);

	Optional<Integer> getIndicationIdById (Integer id);

	List<NursingRecordBo> getIndicationNursingRecords(Integer indicationId);

}
