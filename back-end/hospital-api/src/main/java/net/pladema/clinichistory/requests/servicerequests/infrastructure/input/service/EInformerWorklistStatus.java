package net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EInformerWorklistStatus {
	
	COMPLETED(1, "Completed"),
	DERIVED(2, "Derived"),
	PENDING(3, "Pending");

	private Short id;
	private String description;

	EInformerWorklistStatus(Number id, String description) {
		this.id = id.shortValue();
		this.description = description;
	}

	public Short getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	@JsonCreator
	public static List<EInformerWorklistStatus> getAll(){
		return Stream.of(EInformerWorklistStatus.values()).collect(Collectors.toList());
	}
}
