package net.pladema.person.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

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

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_names")
    private String middleNames;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "other_last_names")
    private String otherLastNames;

    @Column(name = "identification_type_id", nullable = false)
    private Short identificationTypeId;

    @Column(name = "identification_number")
    private String identificationNumber;

    @Column(name = "gender_id", nullable = false)
    private Short genderId;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

}
