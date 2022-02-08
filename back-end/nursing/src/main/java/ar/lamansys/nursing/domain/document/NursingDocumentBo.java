package ar.lamansys.nursing.domain.document;

import ar.lamansys.nursing.domain.NursingAnthropometricDataBo;
import ar.lamansys.nursing.domain.NursingConsultationBo;
import ar.lamansys.nursing.domain.NursingProblemBo;
import ar.lamansys.nursing.domain.NursingProcedureBo;
import ar.lamansys.nursing.domain.NursingRiskFactorBo;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class NursingDocumentBo {

    private final Long id;

    private final Integer patientId;

    private final Integer encounterId;

    private final Integer institutionId;

    private final Integer doctorId;

    private final Integer clinicalSpecialtyId;

    private final NursingAnthropometricDataBo anthropometricData;

    private final NursingRiskFactorBo riskFactors;

    private final String evolutionNote;

    private final Integer patientMedicalCoverage;

    private final List<NursingProcedureBo> procedures;

    private final List<NursingProblemBo> problems;

    private final LocalDate performedDate;

    public NursingDocumentBo(Long id,
                             NursingConsultationBo nursingConsultationBo,
                             Integer encounterId,
                             Integer doctorId,
                             LocalDate performedDate) {

        List <NursingProblemBo> problems = new ArrayList<>();
        problems.add(nursingConsultationBo.getProblem());

        this.id = id;
        this.encounterId = encounterId;
        this.doctorId = doctorId;
        this.patientId = nursingConsultationBo.getPatientId();
        this.institutionId = nursingConsultationBo.getInstitutionId();
        this.clinicalSpecialtyId = nursingConsultationBo.getClinicalSpecialtyId();
        this.problems = problems;
        this.anthropometricData = nursingConsultationBo.getAnthropometricData();
        this.riskFactors = nursingConsultationBo.getRiskFactors();
        this.procedures = nursingConsultationBo.getProcedures();
        this.evolutionNote = nursingConsultationBo.getEvolutionNote();
        this.patientMedicalCoverage = nursingConsultationBo.getPatientMedicalCoverageId();
        this.performedDate = performedDate;
    }

}
