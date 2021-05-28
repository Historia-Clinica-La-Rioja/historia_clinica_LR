package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "document_status")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	public static final String DRAFT = "445667001";
	public static final String FINAL = "445665009";
	public static final String FIXED = "445664008";
	public static final String ERROR = "723510000";

	@Id
	@Column(name = "id", length = 20)
	private String id;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

}
