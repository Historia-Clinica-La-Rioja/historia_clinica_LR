import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ParametersService } from '@api-rest/services/parameters.service';
import { CompletedParameterizedFormParameterDto, EParameterType, ParameterCompleteDataDto, ParameterTextOptionDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { NumericalParameterData, NumericalParameterValues } from '../numerical-parameter/numerical-parameter.component';
import { OptionListParameter } from '../option-list-parameter/option-list-parameter.component';

const NOT_FOUND = -1;

@Component({
	selector: 'app-parameterized-form-parameters-section',
	templateUrl: './parameterized-form-parameters-section.component.html',
	styleUrls: ['./parameterized-form-parameters-section.component.scss']
})
export class ParameterizedFormParametersSectionComponent {

	readonly PARAMETER_TYPES = {
		optionList: EParameterType.OPTIONS_LIST,
		freeText: EParameterType.FREE_TEXT,
		snomedECL: EParameterType.SNOMED_ECL,
		numerical: EParameterType.NUMERIC,
	}

	parameterSectionMap = new Map<string, Parameter>();
	parameterSectionKeys: string[] = [];
	completedParameterizedFormParameterDto: CompletedParameterizedFormParameterDto[] = [];

	@Input()
	set parameterizedFormId(id: number) {
		this.parametersService.getParametersByFormId(id).subscribe((parameterizedFormParameters: ParameterCompleteDataDto[]) => {
			this.buildParameterSection(parameterizedFormParameters);
		});
	}

	@Output() parametersToSave = new EventEmitter<CompletedParameterizedFormParameterDto[]>();

	constructor(
		private readonly parametersService: ParametersService,
	) { }

	concatParametersToSave(basicParameters: CompletedParameterizedFormParameterDto[]) {
		this.completedParameterizedFormParameterDto.concat(basicParameters);
		this.emit();
	}

	setNumericParameter(numericData: NumericalParameterValues[]) {
		numericData.forEach(numeric => {
			const parameter = this.findOrCreateParameter(numeric.parameterId);
			parameter.numericValue = numeric.value;
			this.emit();
		});
	}

	setOptionListParameterData(optionSelectedId: number, parameterId: number) {
		const parameter = this.findOrCreateParameter(parameterId);
		parameter.optionId = optionSelectedId;
		this.emit();
	}

	setFreeTextParameterData(freeText: string, parameterId: number) {
		const parameter = this.findOrCreateParameter(parameterId);
		parameter.textValue = freeText;
		this.emit();
	}

	setSnomedData(snomedConcept: SnomedDto, parameterId: number) {
		const parameter = this.findOrCreateParameter(parameterId);
		parameter.conceptPt = snomedConcept.pt;
		parameter.conceptSctid = snomedConcept.sctid;
		this.emit();
	}

	private findOrCreateParameter(parameterId: number): CompletedParameterizedFormParameterDto {
		const parameterIndex = this.completedParameterizedFormParameterDto.findIndex(parameter => parameter.id === parameterId);
		if (parameterIndex !== NOT_FOUND) {
			return this.completedParameterizedFormParameterDto[parameterIndex];
		}

		const newParameter = { id: parameterId };
		this.completedParameterizedFormParameterDto.push(newParameter);
		return newParameter;
	}

	private emit() {
		this.parametersToSave.emit(this.completedParameterizedFormParameterDto);
	}

	private buildParameterSection(parameters: ParameterCompleteDataDto[]) {
		parameters.forEach(parameter => {
			if (!this.parameterSectionMap.has(parameter.description))
				this.initializeParameterSection(parameter);

			this.populateParameterSection(parameter);
		});

		this.parameterSectionKeys = Array.from(this.parameterSectionMap.keys());
	}

	private initializeParameterSection(parameter: ParameterCompleteDataDto) {
		const initializeParameterSection = { id: parameter.id, type: parameter.type };
		this.parameterSectionMap.set(parameter.description, initializeParameterSection);
	}

	private populateParameterSection(parameter: ParameterCompleteDataDto) {
		const parameterSection = this.parameterSectionMap.get(parameter.description);

		const buildSectionByType = {
			[EParameterType.SNOMED_ECL]: () => parameterSection.ecl = parameter.ecl,
			[EParameterType.OPTIONS_LIST]: () => parameterSection.options = this.toOptionListParameters(parameter.textOptions),
			[EParameterType.NUMERIC]: () => {
				if (!parameterSection.numericals) {
					parameterSection.numericals = [];
				}
				parameterSection.numericals.push(this.toNumericalParameterData(parameter));
			}
		};

		const hasToBuildSection = buildSectionByType[parameter.type];
		if (hasToBuildSection)
			hasToBuildSection();
	}

	private toOptionListParameters(options: ParameterTextOptionDto[]): OptionListParameter[] {
		return options.map(option => { return { id: option.id, description: option.description } });
	}

	private toNumericalParameterData(parameter: ParameterCompleteDataDto): NumericalParameterData {
		return {
			parameterId: parameter.id,
			inpuntCount: parameter.inputCount,
			unitOfMeasureCode: parameter.unitOfMeasure.unitOfMeasureCode
		}

	}

}

interface Parameter {
	id: number;
	type: EParameterType;
	ecl?: SnomedECL;
	options?: OptionListParameter[];
	numericals?: NumericalParameterData[];
}
