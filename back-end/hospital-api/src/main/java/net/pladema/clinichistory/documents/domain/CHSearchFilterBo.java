package net.pladema.clinichistory.documents.domain;

import net.pladema.clinichistory.documents.infrastructure.input.rest.dto.CHSearchFilterDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CHSearchFilterBo {

	private List<ECHEncounterType> encounterTypeList;

	private List<ECHDocumentType> documentTypeList;

	public CHSearchFilterBo(CHSearchFilterDto dto){
		this.encounterTypeList = dto.getEncounterTypeList();
		this.documentTypeList = dto.getDocumentTypeList();
	}

}
