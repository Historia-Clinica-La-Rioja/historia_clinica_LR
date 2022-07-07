package net.pladema.clinichistory.hospitalization.controller.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponsibleDoctorDto implements Serializable {

    private Integer userId;

    private String firstName;

    private String lastName;

    private List<String> licenses;

	private String nameSelfDetermination;
}
