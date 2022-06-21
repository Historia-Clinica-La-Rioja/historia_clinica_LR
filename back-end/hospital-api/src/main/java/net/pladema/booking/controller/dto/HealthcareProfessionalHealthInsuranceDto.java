package net.pladema.booking.controller.dto;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HealthcareProfessionalHealthInsuranceDto implements Serializable {
    private Integer id;
    private Integer medicalCoverageId;
    private Integer healthcareProfessionalId;
}
