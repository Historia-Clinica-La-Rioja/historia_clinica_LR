package net.pladema.establishment.domain.bed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BedRelocationBo {

    private Integer originBedId;
    private Integer destinationBedId;
    private Integer internmentEpisodeId;
    private LocalDateTime relocationDate;
    private boolean originBedFree = true;

}