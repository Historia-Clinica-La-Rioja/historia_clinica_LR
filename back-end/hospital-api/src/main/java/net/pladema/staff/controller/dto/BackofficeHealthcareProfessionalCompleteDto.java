package net.pladema.staff.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BackofficeHealthcareProfessionalCompleteDto {

    private Integer id;

    private Integer personId;

    private String licenseNumber;

    private Integer professionalSpecialtyId;

    private Integer clinicalSpecialtyId;

    private boolean deleted = false;
}
