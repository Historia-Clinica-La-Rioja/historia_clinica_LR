package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BasicDataPersonDto implements Serializable {


    private static final Logger LOG = LoggerFactory.getLogger(BasicDataPersonDto.class);
    private static final String OUTPUT = "create result -> {}";

    /**
	 * 
	 */
	private static final long serialVersionUID = -4171735891439867357L;

	private Integer id;

    private String firstName;

    private String middleNames;

    private String lastName;

    private String otherLastNames;

    private Short identificationTypeId;

    private String identificationType;
    
    private String identificationNumber;
    
    private GenderDto gender;

	private String selfPerceivedGender;

    private PersonAgeDto personAge;

    private LocalDate birthDate;

	private String nameSelfDetermination;

	private List<PersonFileDto> files;

	private String occupation;

	private String educationLevel;

	private String religion;

	private String ethnicity;

	private String email;

	/*public BasicDataPersonDto(Integer id, String firstName, String middleNames, String lastName, String otherLastNames, Short identificationTypeId, String identificationType, String identificationNumber, GenderDto gender, String selfPerceivedGender, Short age, LocalDate birthDate, String nameSelfDetermination, List<PersonFileDto> files) {
		this.id = id;
		this.firstName = firstName;
		this.middleNames = middleNames;
		this.lastName = lastName;
		this.otherLastNames = otherLastNames;
		this.identificationTypeId = identificationTypeId;
		this.identificationType = identificationType;
		this.identificationNumber = identificationNumber;
		this.gender = gender;
		this.selfPerceivedGender = selfPerceivedGender;
		this.age = age;
		this.birthDate = birthDate;
		this.nameSelfDetermination = nameSelfDetermination;
		this.files = files;
	}*/

	public String completeName(){
        if (lastName == null && firstName == null && middleNames == null && otherLastNames==null) {
            return null;
        }
        String completeFirsName = (firstName != null && middleNames != null)
                ? firstName + " " + middleNames
                : firstName == null ?
                middleNames
                : middleNames == null ?
                firstName :
                null;

        String completeLastName = (lastName != null && otherLastNames != null)
                ? lastName + " " + otherLastNames
                : lastName == null ?
                otherLastNames
                : otherLastNames == null ?
                lastName :
                null;

        String result =  (completeLastName != null && completeFirsName != null) ?
                completeLastName + ", " + completeFirsName
                : completeFirsName == null ?
                completeLastName :
                completeFirsName;
        LOG.debug(OUTPUT, result);
        return result;
    }

	public Short getGenderId() {
		return gender != null ? gender.getId(): null;
	}

	public Short getAge() { return this.personAge != null ? this.personAge.getYears() : null; }

}
