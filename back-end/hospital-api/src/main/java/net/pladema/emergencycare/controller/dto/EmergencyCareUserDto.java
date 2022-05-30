package net.pladema.emergencycare.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class EmergencyCareUserDto {

    private Integer id;

    private String firstName;

    private String lastName;

	private String nameSelfDetermination;
}
