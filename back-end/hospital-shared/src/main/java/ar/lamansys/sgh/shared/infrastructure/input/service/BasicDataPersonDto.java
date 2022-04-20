package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@ToString
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

    private Short age;

    private LocalDate birthDate;

	private String nameSelfDetermination;

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
}
