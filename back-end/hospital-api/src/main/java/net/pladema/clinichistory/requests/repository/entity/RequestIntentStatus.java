package net.pladema.clinichistory.requests.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "request_intent")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class RequestIntentStatus {

	public static final String PROPOSAL = "1";
	public static final String PLAN = "2";
	public static final String ORDER = "3";
	public static final String ORIGINAL_ORDER = "4";
	public static final String REFLEX_ORDER = "5";
	public static final String FILLER_ORDER = "6";
	public static final String INSTANCE_ORDER = "7";
	public static final String OPTION = "8";

	@Id
	@Column(name = "id", length = 20)
	private String id;

	@Column(name = "description", nullable = false, length = 50)
	private String description;

}
