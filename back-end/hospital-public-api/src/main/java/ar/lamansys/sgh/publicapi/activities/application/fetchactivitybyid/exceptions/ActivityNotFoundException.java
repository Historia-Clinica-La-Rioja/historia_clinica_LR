package ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class ActivityNotFoundException extends Exception {
	public final String refsetCode;
	public final Long activityId;
}