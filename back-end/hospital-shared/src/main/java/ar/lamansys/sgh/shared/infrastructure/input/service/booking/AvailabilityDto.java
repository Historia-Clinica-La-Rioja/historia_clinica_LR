package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class AvailabilityDto {
    private final LocalDate date;
    private final List<String> slots;

}
