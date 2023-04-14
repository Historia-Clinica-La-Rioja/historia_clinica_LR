package ar.lamansys.sgh.clinichistory.domain.document;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.DocumentDownloadDataVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDownloadDataBo {

	private Long id;

	private String fileName;

	public DocumentDownloadDataBo(DocumentDownloadDataVo documentDownloadData) {
		this.id = documentDownloadData.getId();
		this.fileName = documentDownloadData.getFileName();
	}

}
