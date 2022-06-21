package net.pladema.clinichistory.requests.servicerequests.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "service_request_category")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ServiceRequestCategory {

	public static final String LABORATORY_PROCEDURE = "108252007";
	public static final String PROCEDURE = "363679005";
	public static final String COUNSELLING = "409063005";
	public static final String EDUCATION = "409073007";
	public static final String SURGICAL_PROCEDURE = "387713003";

	@Id
	@Column(name = "id", length = 20)
	private String id;

	@Column(name = "description", nullable = false, length = 50)
	private String description;

	@Column(name = "orden")
	private Short orden;

}
