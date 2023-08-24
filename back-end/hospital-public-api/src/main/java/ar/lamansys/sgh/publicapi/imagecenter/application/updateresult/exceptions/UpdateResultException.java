package ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.exceptions;

public class UpdateResultException extends Exception {

	public UpdateResultException(String message, Exception e) {
		super(message, e);
	}
}