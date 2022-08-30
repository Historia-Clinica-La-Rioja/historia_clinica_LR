package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BasicListedPatientBo {

    private Integer internmentId;

    private Integer patientId;

    private Short identificationTypeId;

    private String identificationNumber;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private Short genderId;

    private String nameSelfDetermination;

	private String bedNumber;

	private String roomNumber;

	private String sectorDescription;

	private boolean hasPhysicalDischarge;

	private DocumentsSummaryBo documentsSummary;

	private boolean hasAdministrativeDischarge;

	private boolean hasMedicalDischarge;

    public BasicListedPatientBo(Integer patientId, Short identificationTypeId, String identificationNumber,
                                String firstName, String lastName, String nameSelfDetermination, LocalDate birthDate,
                                Short genderId, Integer internmentId, String bedNumber, String roomNumber, String sectorDescription,
								boolean hasPhysicalDischarge, boolean hasAdministrativeDischarge, boolean hasMedicalDischarge){
        this.internmentId = internmentId;
        this.patientId = patientId;
        this.identificationTypeId = identificationTypeId;
        this.identificationNumber = identificationNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.genderId = genderId;
        this.nameSelfDetermination = nameSelfDetermination;
		this.bedNumber = bedNumber;
		this.roomNumber = roomNumber;
		this.sectorDescription = sectorDescription;
		this.hasPhysicalDischarge = hasPhysicalDischarge;
		this.hasAdministrativeDischarge = hasAdministrativeDischarge;
		this.hasMedicalDischarge = hasMedicalDischarge;
    }
}
