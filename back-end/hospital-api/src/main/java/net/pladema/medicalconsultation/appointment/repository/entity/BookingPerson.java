package net.pladema.medicalconsultation.appointment.repository.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.repository.domain.BookingPersonBo;

@Entity
@Table(name = "booking_person")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookingPerson implements Serializable {

    @Id
    @Column(name = "id", columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "identification_number")
    private String identificationNumber;

    @Column(name = "gender_id")
    private Short genderId;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    public static BookingPerson fromBookingPersonBo(BookingPersonBo person) {
        BookingPerson b = new BookingPerson();
        b.setBirthDate(person.getBirthDate());
        b.setEmail(person.getEmail());
        b.setFirstName(person.getFirstName());
        b.setLastName(person.getLastName());
        b.setGenderId(person.getGenderId());
        b.setIdentificationNumber(person.getIdNumber());
        return b;
    }

}
