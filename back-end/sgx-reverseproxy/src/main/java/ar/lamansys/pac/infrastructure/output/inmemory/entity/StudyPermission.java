package ar.lamansys.pac.infrastructure.output.inmemory.entity;

import ar.lamansys.pac.domain.StudyPermissionBo;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudyPermission {

	private String token;
	private String studyInstanceUID;
	private long expireDuration;

	public StudyPermission(StudyPermissionBo studyPermissionBo) {
		this.token = studyPermissionBo.getToken();
		this.studyInstanceUID = studyPermissionBo.getStudyInstanceUID();
		this.expireDuration = studyPermissionBo.getExpireDuration();
	}
}
