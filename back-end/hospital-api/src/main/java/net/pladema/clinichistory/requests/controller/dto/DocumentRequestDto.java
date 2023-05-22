package net.pladema.clinichistory.requests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DocumentRequestDto {

	private Integer requestId;

	private Long documentId;
}
