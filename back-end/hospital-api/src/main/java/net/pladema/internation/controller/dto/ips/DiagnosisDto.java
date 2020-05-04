package net.pladema.internation.controller.dto.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@ToString
public class DiagnosisDto extends HealthConditionDto {

    private boolean presumptive = false;
}
