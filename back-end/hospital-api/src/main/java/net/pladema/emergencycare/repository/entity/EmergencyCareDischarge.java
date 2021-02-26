package net.pladema.emergencycare.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "emergency_care_discharge")
public class EmergencyCareDischarge {

    public static Integer WITHOUT_DOCTOR = -1;

    @Id
    @Column(name = "emergency_care_episode_id")
    private Integer emergencyCareEpisodeId;

    @Column(name = "medical_discharge_on")
    private LocalDateTime medicalDischargeOn;


    @Column(name = "medical_discharge_by_professional", nullable = false, columnDefinition = "int default -1")
    private Integer medicalDischargeByProfessional;

    @Column(name = "administrative_discharge_on")
    private LocalDateTime administrativeDischargeOn;

    @Column(name = "administrative_discharge_by_user", nullable = false, columnDefinition = "int default -1")
    private Integer administrativeDischargeByUser = -1;

    @Column(name = "autopsy")
    private Boolean autopsy;

    @Column(name = "discharge_type_id",  nullable = false)
    private Short dischargeTypeId;

    @Column(name = "hospital_transport_id")
    private Short hospitalTransportId;

    @Column(name = "ambulance_company_id", length = 15)
    private String ambulanceCompanyId;

    public EmergencyCareDischarge(Integer emergencyCareEpisodeId, LocalDateTime medicalDischargeOn, Integer medicalDischargeByUser, Boolean autopsy, Short dischargeTypeId) {
        this.emergencyCareEpisodeId = emergencyCareEpisodeId;
        this.medicalDischargeOn = medicalDischargeOn;
        this.medicalDischargeByProfessional = medicalDischargeByUser;
        this.autopsy = autopsy;
        this.dischargeTypeId = dischargeTypeId;
    }

    @PrePersist
    void preInsert() {

        if (this.administrativeDischargeByUser == null)
            this.administrativeDischargeByUser = -1;
    }

    public EmergencyCareDischarge(Integer emergencyCareEpisodeId, LocalDateTime administrativeDischargeOn, Integer administrativeDischargeByUser, Short hospitalTransportId, String ambulanceCompanyId) {
        this.emergencyCareEpisodeId = emergencyCareEpisodeId;
        this.administrativeDischargeOn = administrativeDischargeOn;
        this.administrativeDischargeByUser = administrativeDischargeByUser;
        this.hospitalTransportId = hospitalTransportId;
        this.ambulanceCompanyId = ambulanceCompanyId;
    }

    public EmergencyCareDischarge(Integer emergencyCareEpisodeId, LocalDateTime administrativeDischargeOn, Integer userId, Short dischargeTypeId, Integer medicalDischargeByProfessional) {
        this.emergencyCareEpisodeId = emergencyCareEpisodeId;
        this.setAdministrativeDischargeOn(administrativeDischargeOn);
        this.setAdministrativeDischargeByUser(userId);
        this.setDischargeTypeId(dischargeTypeId);
        this.medicalDischargeByProfessional = medicalDischargeByProfessional;
    }

}
