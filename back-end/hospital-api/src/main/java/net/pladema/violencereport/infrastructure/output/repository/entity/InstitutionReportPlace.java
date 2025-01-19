package net.pladema.violencereport.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.infrastructure.output.repository.embedded.InstitutionReportPlacePK;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "institution_report_place")
@Entity
public class InstitutionReportPlace implements Serializable {

	private static final long serialVersionUID = -4207536672722211935L;

	@EmbeddedId
	private InstitutionReportPlacePK pk;

	@Column(name = "other_report_place", length = 100)
	private String otherReportPlace;

}
