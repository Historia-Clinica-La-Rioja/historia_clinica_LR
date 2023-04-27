package net.pladema.imagenetwork.application.port;

import net.pladema.imagenetwork.domain.StudyPermissionBo;

import java.util.Optional;

public interface StudyPermissionUUIDStorage {
	Optional<StudyPermissionBo> getStudyPermission(String uuidOfStudy);
	String saveStudyPermission(StudyPermissionBo studyPermissionBo);
	void removeStudyPermission(StudyPermissionBo studyPermissionBo);
}
