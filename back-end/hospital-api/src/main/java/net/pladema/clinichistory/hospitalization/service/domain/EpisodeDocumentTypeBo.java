package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.staff.repository.entity.EpisodeDocumentType;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeDocumentTypeBo {
	private Integer id;
	private String description;
	private String richTextBody;
	private Integer consentId;

	public EpisodeDocumentTypeBo(EpisodeDocumentType entity) {
		this.id = entity.getId();
		this.description = entity.getDescription();
		this.richTextBody = entity.getRichTextBody();
		this.consentId = entity.getConsentId();
	}
}
