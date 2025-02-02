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
@Table(name = "document_type")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class DocumentType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	public static final short ANAMNESIS = (short)1;
	public static final short EVALUATION_NOTE = (short)2;
	public static final short EPICRISIS = (short)3;
	public static final short OUTPATIENT = (short)4;
	public static final short RECIPE = (short)5;
	public static final short ORDER = (short)6;
	public static final short EMERGENCY_CARE = (short)7;
	public static final short IMMUNIZATION = (short)8;
	public static final short ODONTOLOGY = (short)9;
	public static final short NURSING = (short)10;
	public static final short COUNTER_REFERENCE = (short)11;
	public static final short INDICATION = (short)12;
	public static final short NURSING_EVOLUTION_NOTE  = (short)13;
	public static final short DIGITAL_RECIPE = (short)14;
	public static final short TRIAGE = (short) 15;
	public static final short EMERGENCY_CARE_EVOLUTION_NOTE = (short) 16;
	public static final short MEDICAL_IMAGE_REPORT = (short) 17;
	public static final short SURGICAL_HOSPITALIZATION_REPORT = (short) 18;
	public static final short EMERGENCY_SURGICAL_REPORT = (short) 19;
	public static final short ANESTHETIC_REPORT = (short) 20;
	public static final short NURSING_EMERGENCY_CARE_EVOLUTION = (short) 21;
	//Should be kept in sync with EDocumentType

	@Id
	@Column(name = "id")
	private Short id;

	@Column(name = "description", nullable = false, length = 100)
	private String description;
}

