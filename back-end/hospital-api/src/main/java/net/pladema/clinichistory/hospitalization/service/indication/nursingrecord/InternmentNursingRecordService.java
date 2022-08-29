package net.pladema.clinichistory.hospitalization.service.indication.nursingrecord;

import ar.lamansys.sgh.shared.infrastructure.input.service.DietDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.NursingRecordDto;

import java.util.List;

public interface InternmentNursingRecordService {

	List<NursingRecordDto> getInternmentEpisodeNursingRecords(Integer internmentEpisodeId);

}
