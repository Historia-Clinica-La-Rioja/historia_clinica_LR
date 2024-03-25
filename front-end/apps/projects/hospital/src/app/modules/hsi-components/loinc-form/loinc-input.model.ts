
export class LoincInput {
	observationValue: LoincObservationValue;
	key: string;
	label: string;
	required: boolean;
	order: number;
	controlType: string;
	type: string;
	options: {key: string, value: string}[];
	param: any;

	constructor(options: {
		observationValue?: LoincObservationValue;
		key?: string;
		label?: string;
		required?: boolean;
		order?: number;
		controlType?: string;
		type?: string;
		options?: {key: string, value: string}[];
		param?: any;
	  } = {}) {
	  this.observationValue = options.observationValue;
	  this.key = options.key || '';
	  this.label = options.label || '';
	  this.required = !!options.required;
	  this.order = options.order === undefined ? 1 : options.order;
	  this.controlType = options.controlType || '';
	  this.type = options.type || '';
	  this.options = options.options || [];
	  this.param = options.param;
	}
}

export class NumericLoincInput extends LoincInput {
	override controlType = 'numeric'; // typeId === 1 //NUMERIC
}

export class TextboxLoincInput extends LoincInput {
	override controlType = 'textbox'; //
}

export class DropdownLoincInput extends LoincInput {
	override controlType = 'dropdown'; //
}

export class SnomedLoincInput extends LoincInput {
	override controlType = 'snomed'; //
}

export interface LoincFormValues {
	hasChanged: boolean;
	values: LoincObservationValue[];
}

export interface LoincObservationValue {
	procedureParameterId: number;
	value: string;
	unitOfMeasureId: number;
}

