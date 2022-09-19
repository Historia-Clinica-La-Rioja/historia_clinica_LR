package net.pladema.medicalconsultation.diary.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DiaryListDto {

    private Integer id;

    private String doctorsOfficeDescription;

    private String startDate;

    private String endDate;

    private Short appointmentDuration;

    private Boolean professionalAssignShift = false;

    private Boolean includeHoliday = false;

	private String alias;

	private String clinicalSpecialtyName;

}
