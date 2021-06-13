package net.pladema.person.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.patient.repository.domain.PatientPersonVo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8310665281279550155L;


    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", length = 40)
    private String firstName;

    @Column(name = "middle_names", length = 40)
    private String middleNames;

    @Column(name = "last_name", length = 40)
    private String lastName;

    @Column(name = "other_last_names", length = 40)
    private String otherLastNames;

    @Column(name = "identification_type_id")
    private Short identificationTypeId;

    @Column(name = "identification_number", length = 11)
    private String identificationNumber;

    @Column(name = "gender_id")
    private Short genderId;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    public Person(PatientPersonVo patientPersonVo) {
        this.id = patientPersonVo.getPersonId();
        this.firstName = patientPersonVo.getFirstName();
        this.middleNames = patientPersonVo.getMiddleNames();
        this.lastName = patientPersonVo.getLastName();
        this.otherLastNames = patientPersonVo.getOtherLastNames();
        this.identificationTypeId = patientPersonVo.getIdentificationTypeId();
        this.identificationNumber = patientPersonVo.getIdentificationNumber();
        this.genderId = patientPersonVo.getGenderId();
        this.birthDate = patientPersonVo.getBirthDate();
    }

    @JsonIgnore
    public Short getAge(){
        if (birthDate == null)
            return null;
        LocalDate today = LocalDate.now();
        Period p = Period.between(birthDate, today);
        return (short) p.getYears();
    }

}
