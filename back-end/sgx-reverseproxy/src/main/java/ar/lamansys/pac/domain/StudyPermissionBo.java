package ar.lamansys.pac.domain;

import ar.lamansys.pac.infrastructure.output.inmemory.entity.StudyPermission;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;

@Getter
@Setter
@ToString
public class StudyPermissionBo {
	private String token;
	private String studyInstanceUID;
	private long expireDuration;

	public StudyPermissionBo(String token, String studyInstanceUID, Duration expiration) {
		this.token = token;
		this.studyInstanceUID = studyInstanceUID;
		this.expireDuration = System.currentTimeMillis() + expiration.toMillis();
	}

	public StudyPermissionBo(StudyPermission studyPermission) {
		this.token = studyPermission.getToken();
		this.studyInstanceUID = studyPermission.getStudyInstanceUID();
		this.expireDuration = studyPermission.getExpireDuration();
	}

	public boolean expired() {
		return System.currentTimeMillis() > expireDuration;
	}
}
