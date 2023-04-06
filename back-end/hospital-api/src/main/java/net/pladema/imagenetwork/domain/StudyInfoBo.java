package net.pladema.imagenetwork.domain;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
	private URL pacGlobalURL;
	private static final String SCHEMA = "https";

	public StudyInfoBo(StudyPacAssociation studyPacAssociation) throws MalformedURLException {
		this.studyInstanceUID = studyPacAssociation.getStudyInstanceUID();
		this.pacGlobalURL = getURL(studyPacAssociation.getPacGlobal());
	}

	public StudyInfoBo(String studyInstanceUID, String url) throws MalformedURLException, URISyntaxException {
		this.studyInstanceUID = studyInstanceUID;
		this.pacGlobalURL = buildURL(url);
	}

	private static URL getURL(PacServer pacServer) throws MalformedURLException {
		return new URL(SCHEMA, pacServer.getDomain(), "");
	}

	private static URL buildURL(String url) throws MalformedURLException, URISyntaxException {
		URI uriObj = new URI(url);
		return new URL(uriObj.getScheme(), uriObj.getHost(), "");
	}

	public String getDomain() {
		return this.pacGlobalURL.getHost();
	}

}
