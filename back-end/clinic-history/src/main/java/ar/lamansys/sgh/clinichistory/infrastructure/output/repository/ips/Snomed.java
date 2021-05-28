package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "snomed")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Snomed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "sctid", length = 20)
	private String sctid;

	@Column(name = "pt", nullable = false)
	private String pt;

	@Column(name = "parent_id", length = 20)
	private String parentId;

	@Column(name = "parent_fsn", nullable = false)
	private String parentFsn;

	public Snomed(String sctid, String pt, String parentId, String parentFsn) {
		this.sctid = sctid;
		this.pt = pt;
		this.parentId = parentId;
		this.parentFsn = parentFsn;
	}
}
