package net.pladema.staff.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalRegistrationNumbersDto {

    private Integer healthcareProfessionalId;

    private String firstName;

    private String lastName;

    @Nullable
    private String nameSelfDetermination;

    private List<ProfessionalLicenseNumberDto> license;
}
