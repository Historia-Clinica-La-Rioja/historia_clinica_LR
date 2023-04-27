package net.pladema.imagenetwork.infrastructure.output.inmemory.entity;

import lombok.*;
import net.pladema.imagenetwork.domain.StudyPermissionBo;

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
