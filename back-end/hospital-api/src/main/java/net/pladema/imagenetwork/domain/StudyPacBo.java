package net.pladema.imagenetwork.domain;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StudyPacBo {

	private String studyInstanceUID;
	private URL pacGlobalURL;

	public StudyPacBo(String studyInstanceUID, String url) throws URISyntaxException {
		this.studyInstanceUID = studyInstanceUID;
		this.pacGlobalURL = buildURL(url);
	}

	private static URL buildURL(String url) throws URISyntaxException {
		try {
			URI uriObj = new URI(url);
			return new URL(uriObj.getScheme(), uriObj.getHost(), "");
		} catch (IOException | NullPointerException e) {
			throw new URISyntaxException(url, "La URL no está bien formada, por favor revise que posea el formato adecuado");
		}
	}

	public String getDomain() {
		return this.pacGlobalURL.getHost();
	}

}
