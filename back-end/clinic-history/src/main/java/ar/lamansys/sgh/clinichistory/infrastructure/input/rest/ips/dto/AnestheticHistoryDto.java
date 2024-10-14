package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;


import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AnestheticHistoryDto {

    @Nullable
    private Short stateId;

    @Nullable
    @JsonIgnore
    private String previousAnesthesiaState;

    @Nullable
    private Short zoneId;

    @Nullable
    @JsonIgnore
    private String anesthesiaZone;

    public boolean hasStringValues() {
        return (previousAnesthesiaState != null  && !previousAnesthesiaState.isEmpty())
                || (anesthesiaZone != null  && !anesthesiaZone.isEmpty());
    }
}
