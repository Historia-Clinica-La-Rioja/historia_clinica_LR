package ar.lamansys.pac.infrastructure.output.inmemory.repository;

import ar.lamansys.pac.domain.StudyPermissionBo;
import ar.lamansys.pac.infrastructure.output.inmemory.entity.StudyPermission;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
@Repository
public class StudyPermissionsUUIDRepository {

    private final Map<String, StudyPermission> storage = new HashMap<>();

    public Optional<StudyPermissionBo> getStudyPermission(String tokenStudy) {
        return Optional.ofNullable(storage.get(tokenStudy))
				.map(StudyPermissionBo::new);
    }

    public StudyPermissionBo saveStudyPermission(StudyPermissionBo studyPermissionBo) {
        StudyPermission toSave = new StudyPermission(studyPermissionBo);
        storage.put(studyPermissionBo.getToken(), toSave);
        return new StudyPermissionBo(toSave);
    }

    public void removeStudyPermission(StudyPermissionBo studyPermissionBo) {
        storage.remove(studyPermissionBo.getStudyInstanceUID());
    }
}
