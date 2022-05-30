package ar.lamansys.online.domain.professional;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class AvailabilityBo {
    private final LocalDate date;
    private final List<String> slots;
}
