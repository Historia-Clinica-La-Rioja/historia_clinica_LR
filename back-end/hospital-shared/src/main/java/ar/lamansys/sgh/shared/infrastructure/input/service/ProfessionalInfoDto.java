package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProfessionalInfoDto {

    private Integer id;

    private String licenceNumber;

    private String firstName;

    private String lastName;

    private String identificationNumber;

    private String phoneNumber;

    private final List<ClinicalSpecialtyDto> clinicalSpecialties;

	private String nameSelfDetermination;

    public ProfessionalInfoDto(Integer id, String licenceNumber, String firstName, String lastName,
                               String identificationNumber, String phoneNumber, List<ClinicalSpecialtyDto> clinicalSpecialties, String nameSelfDetermination) {
        this.id = id;
        this.licenceNumber = licenceNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identificationNumber = identificationNumber;
        this.phoneNumber = phoneNumber;
        this.clinicalSpecialties = clinicalSpecialties;
		this.nameSelfDetermination = nameSelfDetermination;
    }
}
