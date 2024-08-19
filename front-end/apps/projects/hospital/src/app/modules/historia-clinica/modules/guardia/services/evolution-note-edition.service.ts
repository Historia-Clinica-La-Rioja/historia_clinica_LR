import { Injectable } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { EmergencyCareEvolutionNoteDto } from '@api-rest/api-model';
import { toAllergies, toAnthropometricDataValues, toFamilyHistories, toMedications, toMotives, toProcedures, toRiskFactorsValue, } from '@historia-clinica/mappers/emergency-care-evolution-note.mapper';
import { Alergia } from '@historia-clinica/modules/ambulatoria/services/alergias-nueva-consulta.service';
import { AntecedenteFamiliar } from '@historia-clinica/modules/ambulatoria/services/antecedentes-familiares-nueva-consulta.service';
import { AnthropometricDataValues } from '@historia-clinica/modules/ambulatoria/services/datos-antropometricos-nueva-consulta.service';
import { Medicacion } from '@historia-clinica/modules/ambulatoria/services/medicaciones-nueva-consulta.service';
import { MotivoConsulta } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { RiskFactorsValue } from '@historia-clinica/services/factores-de-riesgo-form.service';
import { Procedimiento } from '@historia-clinica/services/procedimientos.service';

@Injectable()
export class EvolutionNoteEditionService {

	constructor() { }

	loadFormByEvolutionNoteData(evolutionNoteForm: FormGroup, evolutionNoteInformation: EmergencyCareEvolutionNoteDto) {
		const controlsFormInfo = this.buildFormControlsValueMap(evolutionNoteInformation);
		controlsFormInfo.forEach((value, key) => evolutionNoteForm.get(key).setValue(value))
	}

	private buildFormControlsValueMap(evolutionNoteInformation: EmergencyCareEvolutionNoteDto): Map<FormControlKeys, FormValues> {
		const evolutionNoteDataMap = new Map<FormControlKeys, FormValues>();
		fieldAssignments.forEach(fieldAssignment => {
			const evolutionNoteDataByKey = evolutionNoteInformation[fieldAssignment.evolutionNoteKeys];
			if (evolutionNoteDataByKey)
				evolutionNoteDataMap.set(fieldAssignment.formControl, fieldAssignment.assignEvolutionNoteValue(evolutionNoteDataByKey));
		});
		return evolutionNoteDataMap;
	}
}

type FormControlKeys = 'riskFactors' | 'allergies' | 'procedures' | 'medications' | 'familyHistories' | 'reasons' | 'evolutionNote' | 'anthropometricData' | 'clinicalSpecialty';
type EvolutionNoteKeys =  'clinicalSpecialtyId' | FormControlKeys; 
type FormValues =
	{
		data: Alergia[]
		| AntecedenteFamiliar[]
		| Medicacion[]
		| Procedimiento[]
	}
	| { motivo: MotivoConsulta[] }
	| { evolucion: string }
	| { clinicalSpecialty: Object }
	| RiskFactorsValue
	| AnthropometricDataValues;

interface FieldAssignments {
	formControl: FormControlKeys,
	evolutionNoteKeys: EvolutionNoteKeys,
	assignEvolutionNoteValue: (value: any) => FormValues
}

const fieldAssignments: FieldAssignments[] = [{
		formControl: 'clinicalSpecialty',
		evolutionNoteKeys: 'clinicalSpecialtyId',
		assignEvolutionNoteValue: (value) => ({ clinicalSpecialty: { id: value } }),
	},
	{
		formControl: 'reasons',
		evolutionNoteKeys: 'reasons',
		assignEvolutionNoteValue: (value) => ({ motivo: toMotives(value) }),
	},
	{
		formControl: 'evolutionNote',
		evolutionNoteKeys: 'evolutionNote',
		assignEvolutionNoteValue: (value) => ({ evolucion: value }),
	},
	{
		formControl: 'anthropometricData',
		evolutionNoteKeys: 'anthropometricData',
		assignEvolutionNoteValue: (value) => toAnthropometricDataValues(value),
	},
	{
		formControl: 'familyHistories',
		evolutionNoteKeys: 'familyHistories',
		assignEvolutionNoteValue: (value) => ({ data: toFamilyHistories(value) }),
	},
	{
		formControl: 'medications',
		evolutionNoteKeys: 'medications',
		assignEvolutionNoteValue: (value) => ({ data: toMedications(value) }),
	},
	{
		formControl: 'procedures',
		evolutionNoteKeys: 'procedures',
		assignEvolutionNoteValue: (value) => ({ data: toProcedures(value) }),
	},
	{
		formControl: 'riskFactors',
		evolutionNoteKeys: 'riskFactors',
		assignEvolutionNoteValue: (value) => toRiskFactorsValue(value),
	},
	{
		formControl: 'allergies',
		evolutionNoteKeys: 'allergies',
		assignEvolutionNoteValue: (value) => ({ data: toAllergies(value) }),
	},
];