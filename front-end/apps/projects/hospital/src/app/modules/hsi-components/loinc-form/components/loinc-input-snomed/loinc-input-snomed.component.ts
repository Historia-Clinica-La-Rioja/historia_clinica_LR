import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SnomedDto, SnomedECL } from '@api-rest/api-model';
import { ConceptTypeaheadInfo } from '@hsi-components/concept-typeahead-search-v2/concept-typeahead-search-v2.component';


const snomedEclMap: Record<string, SnomedECL> = {
	ALLERGY: SnomedECL.ALLERGY,
	ANESTHESIA: SnomedECL.ANESTHESIA,
	BLOOD_TYPE: SnomedECL.BLOOD_TYPE,
	CARDIOVASCULAR_DISORDER: SnomedECL.CARDIOVASCULAR_DISORDER,
	CONSULTATION_REASON: SnomedECL.CONSULTATION_REASON,
	DIABETES: SnomedECL.DIABETES,
	DIAGNOSIS: SnomedECL.DIAGNOSIS,
	ELECTROCARDIOGRAPHIC_PROCEDURE: SnomedECL.ELECTROCARDIOGRAPHIC_PROCEDURE,
	EVENT: SnomedECL.EVENT,
	FAMILY_RECORD: SnomedECL.FAMILY_RECORD,
	HOSPITAL_REASON: SnomedECL.HOSPITAL_REASON,
	HYPERTENSION: SnomedECL.HYPERTENSION,
	MEDICINE_WITH_UNIT_OF_PRESENTATION: SnomedECL.MEDICINE_WITH_UNIT_OF_PRESENTATION,
	MEDICINE: SnomedECL.MEDICINE,
	PERSONAL_RECORD: SnomedECL.PERSONAL_RECORD,
	PROCEDURE: SnomedECL.PROCEDURE,
	VACCINE: SnomedECL.VACCINE,
	VIOLENCE_MODALITY: SnomedECL.VIOLENCE_MODALITY,
	VIOLENCE_PROBLEM: SnomedECL.VIOLENCE_PROBLEM,
	VIOLENCE_TYPE: SnomedECL.VIOLENCE_TYPE,
};

const MAX_SEARCH_RESULTS: number = 30;

@Component({
	selector: 'app-loinc-input-snomed',
	templateUrl: './loinc-input-snomed.component.html',
	styleUrls: ['./loinc-input-snomed.component.scss']
})
export class LoincInputSnomedComponent {

	eclSelected: SnomedECL;
	preloadSnomed: SnomedDto;
	conceptTypeaheadInfo: ConceptTypeaheadInfo = {
		clearButton: true,
		required: false,
		maxSearchResults: MAX_SEARCH_RESULTS,
		debounceTime: 300,
	}
	@Input() set ecl(snomedGroupId: string) {
		this.eclSelected = convertStringToEnum(snomedGroupId);
	};
	@Input() title: string;
	@Input() set preload(preload) {
		if (!preload) return;

		this.preloadSnomed = {
			sctid:  preload.snomedSctid,
			pt: preload.snomedPt
		}
	}
	@Output() valueSelected: EventEmitter<SnomedDto> = new EventEmitter<SnomedDto>();

}

function convertStringToEnum(value: string): SnomedECL | undefined {
	return snomedEclMap[value as keyof typeof snomedEclMap];
}
