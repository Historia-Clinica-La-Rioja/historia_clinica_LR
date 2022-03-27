package ar.lamansys.nursing.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NursingConsultationBo {

    private Integer id;

    private Integer patientId;

    private Integer institutionId;

    private Integer clinicalSpecialtyId;

    private String evolutionNote;

    private Integer patientMedicalCoverageId;

    private NursingProblemBo problem;

    private NursingAnthropometricDataBo anthropometricData;

    private NursingRiskFactorBo riskFactors;

    private List<@Valid NursingProcedureBo> procedures = new ArrayList<>();

}