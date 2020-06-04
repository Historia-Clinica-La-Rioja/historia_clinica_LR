package net.pladema.patient.repository.entity;

import lombok.*;
import net.pladema.patient.service.domain.AdditionalDoctorBo;

import javax.persistence.*;


@Entity
@Table(name = "additional_doctor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdditionalDoctor {
/*We keep the id column to accept more than 1 doctor with the same doctorType in future*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "patient_id", nullable = false)
    private Integer patientId;

    @Column(name = "full_name", length = 80)
    private String fullName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "general_practitioner")
    private boolean generalPractitioner;

    public AdditionalDoctor (AdditionalDoctorBo additionalDoctorBo){
        this.id = additionalDoctorBo.getId();
        this.patientId = additionalDoctorBo.getPatientId();
        this.fullName = additionalDoctorBo.getFullName();
        this.phoneNumber = additionalDoctorBo.getPhoneNumber();
        this.generalPractitioner = additionalDoctorBo.getGeneralPractitioner();
    }

    public boolean isPamiDoctor() {
		return !generalPractitioner;
	}

}
