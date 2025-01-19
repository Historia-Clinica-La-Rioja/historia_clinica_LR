package ar.lamansys.sgh.publicapi.patient.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AppointmentCancellationBo {
	private String reason;
	private LocalDateTime cancellationTime;
}
