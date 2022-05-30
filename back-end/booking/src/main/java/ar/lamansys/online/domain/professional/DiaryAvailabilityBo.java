package ar.lamansys.online.domain.professional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter

public class DiaryAvailabilityBo {
    private DiaryListBo diary;
    private AvailabilityBo slots;
}
