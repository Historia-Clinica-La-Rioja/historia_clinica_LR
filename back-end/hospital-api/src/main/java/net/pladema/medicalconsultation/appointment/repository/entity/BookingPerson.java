package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.*;
import net.pladema.medicalconsultation.appointment.repository.domain.BookingPersonBo;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

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
        b.setBirthDate(LocalDate.parse(person.getBirthDate()));
        b.setEmail(person.getEmail());
        b.setFirstName(person.getFirstName());
        b.setLastName(person.getLastName());
        b.setGenderId(person.getGenderId());
        b.setIdentificationNumber(person.getIdNumber());
        return b;
    }

}
