package net.pladema.clinichistory.documents.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.documents.domain.ECHDocumentType;
import net.pladema.clinichistory.documents.domain.ECHEncounterType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CHSearchFilterDto {

	private List<ECHEncounterType> encounterTypeList;

	private List<ECHDocumentType> documentTypeList;

}
