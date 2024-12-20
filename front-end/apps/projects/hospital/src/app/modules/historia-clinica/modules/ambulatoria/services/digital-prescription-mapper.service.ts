import { Injectable } from '@angular/core';
import { HCEHealthConditionDto, SnomedDto } from '@api-rest/api-model';
import { NewPrescriptionItem } from '../dialogs/add-digital-prescription-item/add-digital-prescription-item.component';

@Injectable({
	providedIn: 'root'
})
export class DigitalPrescriptionMapperService {

	toNewPrescriptionItem: (
		item: NewPrescriptionItem, 
		showStudyCategory: boolean, 
		snomedConcept: SnomedDto, 
		healthProblems: HCEHealthConditionDto[], 
		formValues, 
		commercialMedicationPrescription,
		studyCategoryOptions,
		isDailyInterval: boolean,
	) => NewPrescriptionItem = DigitalPrescriptionMapperService._toNewPrescriptionItem;

    private static _toNewPrescriptionItem(item: NewPrescriptionItem, 
		showStudyCategory: boolean, 
		snomedConcept: SnomedDto, 
		healthProblems: HCEHealthConditionDto[], 
		formValues, 
		commercialMedicationPrescription,
		studyCategoryOptions,
		isDailyInterval: boolean
	): NewPrescriptionItem {
		return {
			id: item ? item.id : null,
			snomed: snomedConcept,
			healthProblem: {
				id: healthProblems.find(hpo => hpo.snomed.sctid === formValues.healthProblem).id,
				description: healthProblems.find(hpo => hpo.snomed.sctid === formValues.healthProblem).snomed.pt,
				sctId: formValues.healthProblem
			},
			administrationTimeDays: formValues.administrationTimeDays,
			studyCategory: {
				id: showStudyCategory ? formValues.studyCategory : null,
				description: showStudyCategory ? studyCategoryOptions.find(sc => sc.id === formValues.studyCategory).description : null
			},
			observations: formValues.observations,
			unitDose: formValues.quantity,
			dayDose: formValues.frequency,
			quantity: {
				value: formValues.totalQuantity || 0,
				unit: formValues.unit || null
			},
			frequencyType: formValues.frequencyType,
			commercialMedicationPrescription: commercialMedicationPrescription,
			suggestedCommercialMedication: formValues.isSuggestCommercialMedicationChecked ? formValues.suggestedCommercialMedication : null,
			isDailyInterval,
		};
	}
}