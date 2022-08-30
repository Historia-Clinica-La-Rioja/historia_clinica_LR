package net.pladema.clinichistory.hospitalization.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.dto.summary.DocumentsSummaryDto;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class InternmentPatientDto {

    private Integer internmentId;

    private Integer patientId;

    private Short identificationTypeId;

    private String identificationNumber;

    private String firstName;

    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private Short genderId;

    private String nameSelfDetermination;

	private String bedNumber;

	private String roomNumber;

	private String sectorDescription;

	private boolean hasPhysicalDischarge;

	private DocumentsSummaryDto documentsSummary;

	private boolean hasAdministrativeDischarge;

	private boolean hasMedicalDischarge;

}
