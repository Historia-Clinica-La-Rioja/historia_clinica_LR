package ar.lamansys.online.application.diary;

public interface IsAppointmentAlreadyAssignedService {

	boolean run(Integer diaryId, Integer openingHoursId, String date, String time);

}
