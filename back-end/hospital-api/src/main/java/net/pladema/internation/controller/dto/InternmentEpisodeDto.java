package net.pladema.internation.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InternmentEpisodeDto {

    private Integer id;

    private PatientDto patient;

    private BedDto bed;

    private ResponsibleDoctorDto doctor;






}
