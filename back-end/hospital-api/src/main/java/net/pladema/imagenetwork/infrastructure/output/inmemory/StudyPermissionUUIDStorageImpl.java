package net.pladema.imagenetwork.infrastructure.output.inmemory;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.imagenetwork.application.port.StudyPermissionUUIDStorage;
import net.pladema.imagenetwork.domain.StudyPermissionBo;
import net.pladema.imagenetwork.infrastructure.output.inmemory.repository.StudyPermissionsUUIDRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@NoArgsConstructor
@Service
public class StudyPermissionUUIDStorageImpl implements StudyPermissionUUIDStorage {

	private final StudyPermissionsUUIDRepository studyPermissionsUUIDRepository = new StudyPermissionsUUIDRepository();

	@Override
	public Optional<StudyPermissionBo> getStudyPermission(String uuidOfStudy) {
		return studyPermissionsUUIDRepository.getStudyPermission(uuidOfStudy)
				.map(StudyPermissionBo::new);
	}

	@Override
	public String saveStudyPermission(StudyPermissionBo studyPermissionBo) {
		return studyPermissionsUUIDRepository.saveStudyPermission(studyPermissionBo);
	}

	@Override
	public void removeStudyPermission(StudyPermissionBo studyPermissionBo) {
		studyPermissionsUUIDRepository.removeStudyPermission(studyPermissionBo.getStudyInstanceUID());
	}
}
