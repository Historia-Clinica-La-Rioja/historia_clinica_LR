package net.pladema.patient.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@ToString
@Table(name = "audit_type")
@AllArgsConstructor
@NoArgsConstructor
public class AuditType {

	public static final Short UNAUDITED = 1;
	public static final Short TO_AUDIT = 2;
	public static final Short AUDITED = 3;

	@Id
	@Column(name = "id", nullable = false)
	private Short id;

	@Column(name = "description", length = 30, nullable = false)
	private String description;

}
