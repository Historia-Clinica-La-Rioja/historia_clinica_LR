package net.pladema.medicalconsultation.diary.controller.mock;

import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.dto.OpeningHoursDto;

import java.util.Arrays;
import java.util.List;


public class DiaryOpeningHoursMock {

    private DiaryOpeningHoursMock(){
        super();
    }

    public static List<DiaryOpeningHoursDto> mockListDiaryOpeningHoursDto(){
        return Arrays.asList(
                mockDiaryOpeningHoursDto((short) 1, "08:00", "12:00", (short) 5),
                mockDiaryOpeningHoursDto((short) 0, "14:00", "18:00", (short) 5));
    }

    private static DiaryOpeningHoursDto mockDiaryOpeningHoursDto(short medicalAttentionTypeId, String from, String to, Short overturnCount) {
        DiaryOpeningHoursDto result = new DiaryOpeningHoursDto();
        result.setMedicalAttentionTypeId(medicalAttentionTypeId);
        result.setOpeningHours(mockOpeningHoursDto(from, to));
        result.setOverturnCount(overturnCount);
        return result;
    }

    private static OpeningHoursDto mockOpeningHoursDto(String from, String to) {
        OpeningHoursDto result = new OpeningHoursDto();
        result.setDayWeekId((short)1);
        result.setFrom(from);
        result.setTo(to);
        return result;
    }

}

