package ar.lamansys.sgh.publicapi.activities.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BedRelocationInfoBo {

	private Integer bedId;
	private LocalDateTime relocationDate;
	private String careType;
	private SnomedBo service;
}
