package ar.lamansys.sgh.clinichistory.infrastructure.input.service.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AllergyConditionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.AnthropometricDataDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DentalActionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosisDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DiagnosticReportDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DocumentHealthcareProfessionalDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.DocumentObservationsDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthConditionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.HealthHistoryConditionDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ImmunizationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.MedicationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.PersonalHistoryDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ProblemDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ProcedureDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.RiskFactorDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DocumentDto {

    private Long id;

    private short documentType;

    private Integer encounterId;

    private Integer institutionId;

    private Short documentSource;

    private Integer patientId;

    private Integer clinicalSpecialtyId;

	private String clinicalSpecialtyName;

    private Integer medicalCoverageId;

    private DocumentObservationsDto notes;

    private HealthConditionDto mainDiagnosis;

    private List<DiagnosisDto> diagnosis = new ArrayList<>();

    private ReferableItemDto<PersonalHistoryDto> personalHistories;

    private List<ProblemDto> problems = new ArrayList<>();

    private List<ProcedureDto> procedures = new ArrayList<>();

    private List<HealthHistoryConditionDto> familyHistories = new ArrayList<>();

    private  List<MedicationDto> medications = new ArrayList<>();

    private List<ImmunizationDto> immunizations= new ArrayList<>();

    private ReferableItemDto<AllergyConditionDto> allergies;

    private List<DiagnosticReportDto>  diagnosticReports = new ArrayList<>();

    private AnthropometricDataDto anthropometricData;

    private RiskFactorDto riskFactors;

    private List<ReasonDto> reasons;

    private List<DentalActionDto> dentalActions  = new ArrayList<>();

    private DateDto performedDate;

	private List<DocumentHealthcareProfessionalDto> healthcareProfessionals = new ArrayList<>();

}
