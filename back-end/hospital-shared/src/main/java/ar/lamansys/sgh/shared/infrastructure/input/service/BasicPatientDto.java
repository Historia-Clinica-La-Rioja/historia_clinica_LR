package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BasicPatientDto implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 2121769190622994359L;

	private Integer id;

    private BasicDataPersonDto person;

    private Short typeId;

	public BasicPatientDto(Integer patientId, Short typeId) {
		this.id = patientId;
		this.typeId = typeId;
	}

    public String getFirstName() {
        return person != null ? person.getFirstName() : null;
    }

    public String getMiddleName() {
        return person != null ? person.getMiddleNames() : null;
    }

    public String getLastName() {
        return person != null ? person.getLastName() : null;
    }

    public String getIdentificationNumber(){
        return person != null ? person.getIdentificationNumber() : null;
    }

    public String getIdentificationType() { return person != null ? person.getIdentificationType() : null; }

	public GenderDto getGender() { return person != null ? person.getGenderId() != null ? person.getGender(): null: null; }

	public LocalDate getBirthDate() { return person != null ? person.getBirthDate(): null; }

}
