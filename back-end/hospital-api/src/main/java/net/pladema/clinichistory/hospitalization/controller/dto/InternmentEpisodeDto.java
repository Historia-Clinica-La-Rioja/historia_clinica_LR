package net.pladema.clinichistory.hospitalization.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.dto.summary.DocumentsSummaryDto;
import net.pladema.establishment.controller.dto.BedDto;

@Getter
@Setter
@ToString
public class InternmentEpisodeDto {

    private Integer id;

    private PatientDto patient;

    private BedDto bed;

    private ResponsibleDoctorDto doctor;

	private boolean hasPhysicalDischarge;

	private DocumentsSummaryDto documentsSummary;

}
