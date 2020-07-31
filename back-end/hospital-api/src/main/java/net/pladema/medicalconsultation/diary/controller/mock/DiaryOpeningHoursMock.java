package net.pladema.medicalconsultation.diary.controller.mock;

import net.pladema.medicalconsultation.diary.controller.dto.DiaryOpeningHoursDto;
import net.pladema.medicalconsultation.diary.controller.dto.OpeningHoursDto;

import java.util.Arrays;
import java.util.List;


public class DiaryOpeningHoursMock {

    private DiaryOpeningHoursMock(){
        super();
    }

    public static List<DiaryOpeningHoursDto> mockListDiaryOpeningHoursDto(List<Integer> ids){
        return Arrays.asList(
                mockDiaryOpeningHoursDto(66, (short) 1, "08:00", "12:00", (short) 5),
                mockDiaryOpeningHoursDto(67, (short) 0, "14:00", "18:00", (short) 5));
    }

    private static DiaryOpeningHoursDto mockDiaryOpeningHoursDto(Integer id, short medicalAttentionTypeId, String from, String to, Short overturnCount) {
        DiaryOpeningHoursDto result = new DiaryOpeningHoursDto();
        result.setMedicalAttentionTypeId(medicalAttentionTypeId);
        result.setOpeningHours(mockOpeningHoursDto(id.shortValue(), from, to));
        result.setOverturnCount(overturnCount);
        return result;
    }

    private static OpeningHoursDto mockOpeningHoursDto(Short id, String from, String to) {
        OpeningHoursDto result = new OpeningHoursDto();
        result.setId(id);
        result.setDayWeekId((short)1);
        result.setFrom(from);
        result.setTo(to);
        return result;
    }

}

