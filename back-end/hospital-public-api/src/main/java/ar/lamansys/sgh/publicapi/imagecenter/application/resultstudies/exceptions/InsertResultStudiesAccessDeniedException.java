package ar.lamansys.sgh.publicapi.imagecenter.application.resultstudies.exceptions;

import ar.lamansys.sgh.publicapi.generalexceptions.PublicApiAccessDeniedException;

public class InsertResultStudiesAccessDeniedException extends PublicApiAccessDeniedException {
	public InsertResultStudiesAccessDeniedException() {
		super("ImageCenter", "InsertResultStudies");
	}
}
