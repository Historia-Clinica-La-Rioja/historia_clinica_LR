package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCareEpisodeInProgressDto {

	@Nullable
	private Integer id;

	private boolean inProgress;

}