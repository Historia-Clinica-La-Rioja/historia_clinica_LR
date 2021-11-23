package net.pladema.staff.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthcareProfessionalCompleteBo {

    @Nullable
    private Integer id;

    private Integer personId;

    private String licenseNumber;
}
