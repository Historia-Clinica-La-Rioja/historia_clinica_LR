
import {
	LoincInput,
	DropdownLoincInput,
	TextboxLoincInput,
	SnomedLoincInput,
	NumericLoincInput,
} from '../loinc-input.model';

import { ProcedureParameterFullSummaryDto } from '@api-rest/api-model';

export const dtoToLoincInput = (p: ProcedureParameterFullSummaryDto): LoincInput => {
	const key = `param_${p.id}`;
	const label = p.loincCode.customDisplayName || p.loincCode.description;
	const param = p;
	const order = p.orderNumber;

	if (p.typeId === 1) { //NUMERIC
		return 	  new NumericLoincInput({
			key,
			label,
			type: 'number',
			order,
			param
		  });
	}

	if (p.typeId === 2) { //FREE_TEXT
		return new TextboxLoincInput({
			key,
			label,
			type: 'text',
			order,
			param
		  });
	}

	if (p.typeId === 3) { //SNOMED_ECL
		return new SnomedLoincInput({
			key,
			label,
			type: 'snomed',
			order,
			param
		  });
	}

	if (p.typeId === 4) { // TEXT_OPTION
		const options: {key: string, value: string}[] = p.textOptions.map(t => ({key: `${t.procedureTextOptionId}`, value: t.description}));
		return new DropdownLoincInput({
			key,
			label,
			options: options,
			order,
			param
		})
	}

	return new TextboxLoincInput({
        key,
        label: `${label} [${p.typeId}]`,
        required: true,
        order,
		param
	});

};

