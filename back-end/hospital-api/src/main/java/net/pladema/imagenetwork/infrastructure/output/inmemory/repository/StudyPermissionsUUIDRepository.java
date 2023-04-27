package net.pladema.imagenetwork.infrastructure.output.inmemory.repository;

import lombok.NoArgsConstructor;
import net.pladema.imagenetwork.domain.StudyPermissionBo;
import net.pladema.imagenetwork.infrastructure.output.inmemory.entity.StudyPermission;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
public class StudyPermissionsUUIDRepository {

	private final Map<String, StudyPermission> storage = new HashMap<>();

	public Optional<StudyPermission> getStudyPermission(String uuidOfStudy) {
		return Optional.ofNullable(storage.get(uuidOfStudy));
	}

	public String saveStudyPermission(StudyPermissionBo studyPermissionBo) {
		storage.put(studyPermissionBo.getToken(), new StudyPermission(studyPermissionBo));
		return studyPermissionBo.getToken();
	}

	public void removeStudyPermission(String uuidOfStudy) {
		storage.remove(uuidOfStudy);
	}
}
