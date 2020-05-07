package net.pladema.internation.repository.masterdata.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
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

	@Id
	@Column(name = "id")
	private Short id;

	@Column(name = "description", nullable = false, length = 100)
	private String description;
}

