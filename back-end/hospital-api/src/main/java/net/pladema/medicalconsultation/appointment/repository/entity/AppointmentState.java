package net.pladema.medicalconsultation.appointment.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class AppointmentState implements Serializable {

	private static final long serialVersionUID = -6349210572359248285L;
	
	public static final short ASSIGNED = 1;
    public static final short CONFIRMED = 2;
    public static final short ABSENT = 3;
    public static final short CANCELLED = 4;
    public static final short SERVED = 5;
    public static final short BOOKED = 6;
    public static final short BLOCKED = 7;
	public static final short OUT_OF_DIARY = 8;
    
    public static final String CANCELLED_STR = "4";
	public static final String OUT_OF_DIARY_STR = "8";
	public static final String BOOKED_STR = "6";

    @Id
    @Column(name = "id")
    private Short id;

    @Column(name = "description", nullable = false, length = 10)
    private String description;

    public AppointmentState(Short id, String description) {
        this.id = id;
        this.description = description;
    }
}
