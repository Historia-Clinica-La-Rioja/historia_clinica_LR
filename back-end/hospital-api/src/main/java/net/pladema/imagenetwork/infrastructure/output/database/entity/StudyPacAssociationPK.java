package net.pladema.imagenetwork.infrastructure.output.database.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class StudyPacAssociationPK implements Serializable {

	private static final long serialVersionUID = -5575450403238844545L;

	@Column(name = "image_id", nullable = false)
	private String studyInstanceUID;

	@Column(name = "pac_server_id", nullable = false)
	private Integer pacServerId;
}
