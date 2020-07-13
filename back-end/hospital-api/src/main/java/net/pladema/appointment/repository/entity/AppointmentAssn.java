package net.pladema.appointment.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "appointment_assn")
@Getter
@Setter
@ToString
public class AppointmentAssn implements Serializable {

    @EmbeddedId
    private AppointmentAssnPK pk;
}
