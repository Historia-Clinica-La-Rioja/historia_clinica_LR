package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class PacServerProtocolDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6358418632476688721L;

	private Short id;

	private String description;
}
