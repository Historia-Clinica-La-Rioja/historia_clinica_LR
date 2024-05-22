package ar.lamansys.sgh.publicapi.reports.domain.fetchdailyhoursbydate;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DiaryOhsBo {
	Integer institutionId;
	String institutionName;
	Integer diaryId;
	LocalDate diaryStart;
	LocalDate diaryEnd;
	Short appointmentDuration;
	Integer hierarchicalUnitId;
	String hierarchicalUnitAlias;
	Integer openingHourId;
	Short overturnCount;
	LocalTime openingHourFrom;
	LocalTime openingHourTo;
	Short dayOfTheWeek;
	Integer professionalId;
	String cuil;
	String identificationType;
	String firstName;
	String middleName;
	String lastName;
	String otherLastNames;
	String selfPerceivedName;
	String identificationNumber;
	String clinicalSpecialtySnomed;
	String clinicalSpecialtyName;
	String diaryType;
	Integer hierarchicalUnitType;
	Integer hierarchicalParentServiceUnitId;
	String hierarchicalParentServiceUnitAlias;

}
