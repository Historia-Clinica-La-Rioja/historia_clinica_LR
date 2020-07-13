package net.pladema.appointment.repository.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "appointment_state")
@Getter
@Setter
@ToString
public class AppointmentState implements Serializable {

    public static final short ASIGNADO = 1;
    public static final short CONFIRMADO = 2;
    public static final short AUSENTE = 3;
    public static final short CANCELADO = 4;

    @Id
    @Column(name = "id")
    private Short id;

    @Column(name = "description", nullable = false, length = 10)
    private String description;
}
