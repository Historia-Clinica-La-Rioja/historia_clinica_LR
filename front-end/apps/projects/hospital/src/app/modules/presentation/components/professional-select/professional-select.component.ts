import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ProfessionalDto } from '@api-rest/api-model';
import { PatientNameService } from '@core/services/patient-name.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

@Component({
	selector: 'app-professional-select',
	templateUrl: './professional-select.component.html',
	styleUrls: ['./professional-select.component.scss']
})
export class ProfessionalSelectComponent {

	professionalsTypeahead: TypeaheadOption<ProfessionalDto>[];
	professionalInitValue: TypeaheadOption<ProfessionalDto>;

	@Input()
	set professionalSelected(professional: ProfessionalDto) {
		if (professional)
			this.loadProfessionalSelected(professional);
	}

	@Input()
	set professionals(professionals: ProfessionalDto[]) {
		if (professionals.length)
			this.loadProfessionalsTypeahead(professionals);
	}

	@Output() selectionChange = new EventEmitter();

	constructor(
		private readonly patientNameService: PatientNameService
	) { }


	setProfesional(result: ProfessionalDto) {
		if (!result) {
			this.selectionChange.emit(null);
			return;
		}
		this.selectionChange.next(result);
	}

	getFullName(profesional: ProfessionalDto): string {
		let fullName =profesional.lastName + ' ';
		if(profesional?.otherLastNames){
			fullName += profesional.otherLastNames + ' ';
		}
		fullName += `${this.patientNameService.getFullName(profesional.firstName, profesional.nameSelfDetermination,profesional?.middleNames)}`;
		return  fullName;
	}

	getFullNameLicence(profesional: ProfessionalDto): string {
		return `${this.getFullName(profesional)}`;
	}


	private loadProfessionalSelected(professional: ProfessionalDto) {
		this.professionalInitValue = this.toProfessionalTypeahead(professional);
	}

	private loadProfessionalsTypeahead(professionals: ProfessionalDto[]) {
		this.professionalsTypeahead = professionals.map(d => this.toProfessionalTypeahead(d));
	}

	private toProfessionalTypeahead(professionalDto: ProfessionalDto): TypeaheadOption<ProfessionalDto> {
		return {
			compareValue: this.getFullNameLicence(professionalDto),
			value: professionalDto
		};
	}
}
