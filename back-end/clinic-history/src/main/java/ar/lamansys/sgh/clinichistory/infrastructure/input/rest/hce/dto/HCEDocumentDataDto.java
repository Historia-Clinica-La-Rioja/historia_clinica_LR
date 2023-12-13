package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HCEDocumentDataDto {

    private Long id;

    private String filename;

}
