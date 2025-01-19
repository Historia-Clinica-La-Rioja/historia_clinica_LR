package ar.lamansys.sgh.publicapi.activities.application.port.out;

public interface ProcessActivityStorage {

	void processActivity(String refsetCode, Long activityId);

}
