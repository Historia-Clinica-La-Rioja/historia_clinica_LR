package net.pladema.snvs.domain.report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.snvs.domain.problem.SnvsProblemBo;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SnvsReportBo {

	private Integer id;

	private Integer groupEventId;

	private Integer eventId;

	private Integer manualClassificationId;

	private Integer patientId;

	private SnvsProblemBo problemBo;

	private String status;

	private Short responseCode;

	private Integer professionalId;

	private Integer institutionId;

	private Integer sisaRegisteredId;

	private LocalDate lastUpdate;
}
