package net.pladema.establishment.controller.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PacServerTypeDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7440954986318704558L;

	private Short id;

	private String description;
}
