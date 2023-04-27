package net.pladema.imagenetwork.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.imagenetwork.infrastructure.output.inmemory.entity.StudyPermission;

import java.time.Duration;

@Getter
@Setter
@ToString
public class StudyPermissionBo {
	private String token;
	private String studyInstanceUID;
	private long expireDuration;

	public StudyPermissionBo(String token, String studyInstanceUID, Duration duration) {
		this.token = token;
		this.studyInstanceUID = studyInstanceUID;
		this.expireDuration = System.currentTimeMillis() + duration.toMillis();
	}

	public StudyPermissionBo(String token, String studyInstanceUID) {
		this.token = token;
		this.studyInstanceUID = studyInstanceUID;
		this.expireDuration = -1L;
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
