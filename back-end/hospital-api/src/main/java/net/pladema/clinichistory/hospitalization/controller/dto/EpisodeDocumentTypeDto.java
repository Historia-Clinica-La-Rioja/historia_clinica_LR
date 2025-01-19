package net.pladema.clinichistory.hospitalization.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentTypeBo;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeDocumentTypeDto {
	private Integer id;
	private String description;
	private String richTextBody;
	private Integer consentId;

	public EpisodeDocumentTypeDto(EpisodeDocumentTypeBo bo) {
		this.id = bo.getId();
		this.description = bo.getDescription();
		this.richTextBody = bo.getRichTextBody();
		this.consentId = bo.getConsentId();
	}
}
