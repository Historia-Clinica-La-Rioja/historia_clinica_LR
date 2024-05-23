package net.pladema.errata.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrataRequestDTO {

	private String description;

	private Integer documentId;

	private Integer userId;

}
