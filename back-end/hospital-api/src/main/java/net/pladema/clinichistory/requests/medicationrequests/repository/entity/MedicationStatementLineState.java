package net.pladema.clinichistory.requests.medicationrequests.repository.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "medication_statement_line_state")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MedicationStatementLineState implements Serializable {

	public static final Short ACTIVO = 1;
	public static final Short DISPENSADO_CERRADO = 2;
	public static final Short DISPENSADO_PROVISORIO = 3;
	public static final Short VENCIDO = 4;
	public static final Short CANCELADO_DISPENSA = 5;
	public static final Short CANCELADO_RECETA = 6;
	public static final List<Short> STATES = List.of(ACTIVO, DISPENSADO_CERRADO, DISPENSADO_PROVISORIO, VENCIDO, CANCELADO_DISPENSA, CANCELADO_RECETA);

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;

	@Column(name = "description", nullable = false)
	private String description;
}
