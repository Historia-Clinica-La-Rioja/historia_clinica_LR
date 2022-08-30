package ar.lamansys.online.infraestructure.output.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "booking_institution")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookingInstitution implements Serializable {
    @Id
    @Column(name = "institution_id")
    private Integer institutionId;
}

