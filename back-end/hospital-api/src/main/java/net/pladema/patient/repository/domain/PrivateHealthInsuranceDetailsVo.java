package net.pladema.patient.repository.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PrivateHealthInsuranceDetailsVo {

    private Integer id;

    private LocalDate startDate;

    private LocalDate endDate;

    public PrivateHealthInsuranceDetailsVo(Integer id, LocalDate startDate, LocalDate endDate){
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
