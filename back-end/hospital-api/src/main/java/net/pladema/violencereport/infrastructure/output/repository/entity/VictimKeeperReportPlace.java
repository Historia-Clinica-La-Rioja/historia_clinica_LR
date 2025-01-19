package net.pladema.violencereport.infrastructure.output.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.infrastructure.output.repository.embedded.VictimKeeperReportPlacePK;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "victim_keeper_report_place")
@Entity
public class VictimKeeperReportPlace implements Serializable {

	private static final long serialVersionUID = 4791420842673644464L;

	@EmbeddedId
	private VictimKeeperReportPlacePK pk;

}
