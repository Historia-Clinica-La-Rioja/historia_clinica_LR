package net.pladema.staff.repository.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "episode_document_types")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EpisodeDocumentType implements Serializable {

	public static final short REGULAR = (short)1;
	public static final short ADMISSION_CONSENT = (short)2;
	public static final short SURGICAL_CONSENT = (short)3;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

	@Column(name = "rich_text_body", columnDefinition = "TEXT")
	private String richTextBody;

	@Column(name = "consent_id")
	private Integer consentId;
}
