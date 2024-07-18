package ar.lamansys.sgh.publicapi.imagecenter.application.resultstudies.exceptions;

import ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions.PublicApiAccessDeniedException;

public class InsertResultStudiesAccessDeniedException extends PublicApiAccessDeniedException {
	public InsertResultStudiesAccessDeniedException() {
		super("ImageCenter", "InsertResultStudies");
	}
}
