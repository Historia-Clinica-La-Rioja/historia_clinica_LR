package ar.lamansys.sgh.publicapi.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InternmentBo {

    private String id;
    private LocalDate entryDate;
    private LocalDate dischargeDate;
}
