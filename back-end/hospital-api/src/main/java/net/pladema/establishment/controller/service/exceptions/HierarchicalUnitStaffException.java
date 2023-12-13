package net.pladema.establishment.controller.service.exceptions;

import lombok.Getter;

@Getter
public class HierarchicalUnitStaffException extends RuntimeException {

	private final HierarchicalUnitStaffEnumException code;

	public HierarchicalUnitStaffException(HierarchicalUnitStaffEnumException code, String message) {
		super(message);
		this.code = code;
	}

}
