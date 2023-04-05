package net.pladema.imagenetwork.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.PacServer;
import net.pladema.imagenetwork.infrastructure.output.entity.StudyPacAssociation;

@Getter
@Setter
@ToString
public class StudyInfoBo {

	private String studyInstanceUID;
	private String pacGlobalURL;
	private static final String SCHEMA = "https";

	public StudyInfoBo(StudyPacAssociation studyPacAssociation) {
		this.studyInstanceUID = studyPacAssociation.getStudyInstanceUID();
		this.pacGlobalURL = buildURL(studyPacAssociation.getPacGlobal());
	}

	private static String buildURL(PacServer pacServer) {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
				.scheme(SCHEMA)
				.host(pacServer.getDomain());
		return builder.toUriString();
	}
}
