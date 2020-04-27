package net.pladema.person.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1708640067849754088L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_names")
    private String middleNames;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "other_last_names")
    private String otherLastNames;

    @Column(name = "identification_type_id")
    private Short identificationTypeId;

    @Column(name = "identification_number", length = 11)
    private String identificationNumber;

    @Column(name = "gender_id")
    private Short genderId;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    public Short getAge(){
        LocalDate today = LocalDate.now();
        Period p = Period.between(birthDate, today);
        return (short) p.getYears();
    }

}
