package ar.lamansys.sgh.publicapi.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BedRelocationInfoBo {

    private LocalDateTime relocationDate;
    private Integer careTypeId;
    private SnomedBo service;
}
