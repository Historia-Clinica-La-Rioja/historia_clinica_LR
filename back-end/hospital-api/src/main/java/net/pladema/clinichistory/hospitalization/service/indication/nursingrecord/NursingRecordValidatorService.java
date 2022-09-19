package net.pladema.clinichistory.hospitalization.service.indication.nursingrecord;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;

public interface NursingRecordValidatorService {

	boolean validateStatusUpdate(String status, String reason, DateTimeDto administrationTime);

}
