package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "diagnostic_report_tree")
@Entity
/**
 * The purpose of this entity is keeping record
 * of each completed diagnostic report's parent
 *
 * When a dr is completed, the following happens:
 * 1 The endpoint complete/{drId} is called. The referenced dr is in the REGISTERED status.
 * 2 A new dr row is created. With a new id (newDrId), a status of FINAL, and no connection
 * to drId other than the document id
 * 3 The newDrId observations are created and linked to drId
 *
 * Latter, to obtain the observations for {newDrId} there's no way to link it to {drId}
 *
 * This table serves the purpose of keeping that link. Each time new dr is created with a
 * status of FINAL a new row is stored here to link both drs.
 * The new row stores the pair (root dr id, new dr id). When we need to access
 * the observations of a completed dr we look in this table for the root dr.
 *
 * See HSI-6750
 *
 */
public class DiagnosticReportTree {

	@EmbeddedId
	private DiagnosticReportTreePK pk;

	public DiagnosticReportTree(Integer parentId, Integer childId) {
		this.pk = new DiagnosticReportTreePK(parentId, childId);
	}
}
