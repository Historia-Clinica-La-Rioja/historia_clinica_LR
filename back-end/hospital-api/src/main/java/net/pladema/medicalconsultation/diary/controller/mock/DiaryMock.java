package net.pladema.medicalconsultation.diary.controller.mock;

import net.pladema.medicalconsultation.diary.controller.dto.DiaryListDto;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class DiaryMock {

    private DiaryMock(){
        super();
    }

    public static DiaryListDto mockDiaryListDto(Integer healthcareProfessionalId){
        DiaryListDto result = new DiaryListDto();
        result.setId(healthcareProfessionalId);
        Random rand = new Random();
        Integer appointmentDuration = rand.nextInt(4) + 1;
        result.setAppointmentDuration(appointmentDuration.shortValue());
        result.setDoctorsOfficeId(healthcareProfessionalId);
        result.setStartDate("2020-07-01");
        result.setEndDate("2020-08-31");
        result.setProfessionalAssignShift(true);
        result.setIncludeHoliday(true);
        return result;
    }

    public static List<DiaryListDto> mockListDiaryListDto(Integer healthcareProfessionalId) {
        return IntStream.range(0,2)
                .parallel()
                .mapToObj(v -> mockDiaryListDto(healthcareProfessionalId))
                .collect(Collectors.toList());
    }
}
