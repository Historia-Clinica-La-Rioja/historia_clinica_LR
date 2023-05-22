package net.pladema.clinichistory.requests.medicationrequests.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DocumentRequestBo {

	private Integer requestId;

	private Long documentId;
}
