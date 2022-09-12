package net.pladema.clinichistory.hospitalization.service.indication.nursingrecord;


import ar.lamansys.sgh.shared.infrastructure.input.service.ENursingRecordStatus;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;

import lombok.NoArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;

import static ar.lamansys.sgh.shared.infrastructure.input.service.ENursingRecordStatus.*;

@Service
@NoArgsConstructor
public class NursingRecordValidatorServiceImpl implements NursingRecordValidatorService {

	private static final Logger LOG = LoggerFactory.getLogger(NursingRecordValidatorServiceImpl.class);

	@Override
	public boolean validateStatusUpdate(String status, String reason, DateTimeDto administrationTime) {
		LOG.debug("Input parameters -> status {}, administrationTime {}, reason {}", status, administrationTime, reason);
		ENursingRecordStatus mappedStatus = ENursingRecordStatus.map(status.toLowerCase());
		if(mappedStatus.equals(COMPLETED) && administrationTime == null){
			throw new ValidationException("nursing.record.administration.time.invalid");
		}
		if(mappedStatus.equals(REJECTED) && reason == null){
			throw new ValidationException("nursing.record.reason.invalid");
		}
		LOG.debug("Output -> {}", Boolean.TRUE);
		return Boolean.TRUE;
	}

}
