package net.pladema.imagenetwork.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedStudyPermissionPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.imagenetwork.application.checktokenstudypermissions.CheckTokenStudyPermissions;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class StudyPermissionExternalService implements SharedStudyPermissionPort {

	private final CheckTokenStudyPermissions checkTokenStudyPermissions;

	@Override
	public String checkTokenStudyPermissions(String studyInstanceUID, String tokenStudy) {
		return checkTokenStudyPermissions.run(studyInstanceUID, tokenStudy);
	}
}
