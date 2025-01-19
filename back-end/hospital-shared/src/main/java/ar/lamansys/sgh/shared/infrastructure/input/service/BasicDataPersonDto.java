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

	public String completeName(boolean selfPerceivedFeatureFlag){
        if (lastName == null && firstName == null && middleNames == null && otherLastNames==null) {
            return null;
        }
		
		String completeFirstName;

		if (selfPerceivedFeatureFlag && nameSelfDetermination != null)
			completeFirstName = nameSelfDetermination;
		else {
			completeFirstName = firstName != null && middleNames != null
					? firstName + " " + middleNames
					: firstName == null ?
					middleNames
					: firstName;
		}

		String completeLastName = lastName != null && otherLastNames != null
                ? lastName + " " + otherLastNames
                : lastName == null ?
                otherLastNames
                : lastName;

        String result =  (completeLastName != null && completeFirstName != null) ?
                completeLastName + ", " + completeFirstName
                : completeFirstName == null ?
                completeLastName :
				completeFirstName;
        LOG.debug(OUTPUT, result);
        return result;
    }

	public Short getGenderId() {
		return gender != null ? gender.getId(): null;
	}

	public Short getAge() { return this.personAge != null ? this.personAge.getYears() : null; }

}
