package net.pladema.patient.repository.domain;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientMedicalCoverageVo {

    private Integer id;

    private String affiliateNumber;

    private LocalDate vigencyDate;

    private Boolean active;

    private MedicalCoverageVo medicalCoverage;

    private PrivateHealthInsuranceDetailsVo privateHealthInsuranceDetails;

    public PatientMedicalCoverageVo(Integer id, String affiliateNumber, LocalDate vigencyDate, Boolean active, Integer medicalCoverageId, String name,
                                    Integer rnos, String acronym, String plan, Integer privateHealthInsuranceDetailsId, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.affiliateNumber = affiliateNumber;
        this.vigencyDate = vigencyDate;
        this.active = active;
        if (rnos != null)
            this.medicalCoverage = new HealthInsuranceVo(medicalCoverageId, name, rnos, acronym);
        else{
            this.medicalCoverage = new PrivateHealthInsuranceVo(medicalCoverageId, name, plan);
            this.privateHealthInsuranceDetails = new PrivateHealthInsuranceDetailsVo(privateHealthInsuranceDetailsId, startDate, endDate);
        }
    }

       public PatientMedicalCoverageVo(Integer id, String affiliateNumber, LocalDate vigencyDate, Boolean active, Integer medicalCoverageId, String name, Integer rnos, String acronym){
        this.id = id;
        this.affiliateNumber = affiliateNumber;
        this.vigencyDate = vigencyDate;
        this.active = active;
        this.medicalCoverage = new HealthInsuranceVo(medicalCoverageId, name, rnos, acronym);
    }

    public PatientMedicalCoverageVo(Integer id, String affiliateNumber, LocalDate vigencyDate, Boolean active, Integer medicalCoverageId, String name, Integer privateHealthInsuranceDetailsId, LocalDate startDate, LocalDate endDate, String plan){
        this.id = id;
        this.affiliateNumber = affiliateNumber;
        this.vigencyDate = vigencyDate;
        this.active = active;
        this.medicalCoverage = new PrivateHealthInsuranceVo(medicalCoverageId, name, plan);
        this.privateHealthInsuranceDetails = new PrivateHealthInsuranceDetailsVo(privateHealthInsuranceDetailsId, startDate, endDate);
    }
}
