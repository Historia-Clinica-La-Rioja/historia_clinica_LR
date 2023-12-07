package net.pladema.clinichistory.documents.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.infrastructure.output.repository.entity.HistoricClinicHistoryDownload;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HistoricClinicHistoryDownloadBo {

	private Integer id;
	private Integer patientId;
	private Integer institutionId;
	private Integer userId;
	private LocalDateTime downloadDate;

	public HistoricClinicHistoryDownloadBo (HistoricClinicHistoryDownload entity){
		this.id = entity.getId();
		this.patientId = entity.getPatientId();
		this.institutionId = entity.getInstitutionId();
		this.userId = entity.getUserId();
		this.downloadDate = entity.getDownloadDate();
	}

}
