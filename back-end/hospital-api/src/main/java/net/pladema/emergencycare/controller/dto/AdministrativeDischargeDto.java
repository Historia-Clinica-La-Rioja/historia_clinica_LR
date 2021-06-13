package net.pladema.emergencycare.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
public class AdministrativeDischargeDto {

    private DateTimeDto administrativeDischargeOn;

    private Short hospitalTransportId;

    @Length(max = 15)
    private String ambulanceCompanyId;
}
