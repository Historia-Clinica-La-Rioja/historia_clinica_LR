import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { CareLineDto, ClinicalSpecialtyDto, EVirtualConsultationPriority } from '@api-rest/api-model';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ContextService } from '@core/services/context.service';
import { Patient } from '@pacientes/component/search-patient/search-patient.component';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

@Component({
	selector: 'app-information-request-form',
	templateUrl: './information-request-form.component.html',
	styleUrls: ['./information-request-form.component.scss']
})
export class InformationRequestFormComponent implements OnInit {
	patient: Patient;
	informationForm: UntypedFormGroup;
	careLines: CareLineDto[];
	careLinesTypeahead: TypeaheadOption<CareLineDto>[] = [];
	showCareLineError = false;
	specialties: ClinicalSpecialtyDto[];
	specialtyTypeaheadOptions: TypeaheadOption<ClinicalSpecialtyDto>[] = [];
	showSpecialtyError = false;

	showPriorityError = false;

	constructor(private carelineService: CareLineService, private contextService: ContextService,
		private readonly formBuilder: UntypedFormBuilder) { }

	ngOnInit(): void {
		this.initForm();
		this.carelineService.getCareLinesAttachedToInstitution(this.contextService.institutionId).subscribe(data => {
			this.careLines = data;
			this.careLinesTypeahead = this.careLines.map(c => this.toCareLinesDtoTypeahead(c));
		})
	}

	private initForm(): void {
		this.informationForm = this.formBuilder.group({
			careLine: [null, Validators.required],
			specialty: [null, Validators.required],
			priority: [null, Validators.required],
		});
	}

	setCareLine(careLine: CareLineDto) {
		this.informationForm.controls.careLine.setValue(careLine);
		this.showCareLineError = false;
		if (careLine) {
			this.specialties = careLine.clinicalSpecialties;
			this.specialtyTypeaheadOptions = this.specialties.map(s => this.specialtyToTypeaheadOption(s));
		} else {
			this.specialties = [];
			this.specialtyTypeaheadOptions = [];
		}
	}

	setClinicalSpecialty(clinicalSpecialty: ClinicalSpecialtyDto) {
		this.informationForm.controls.specialty.setValue(clinicalSpecialty);
		this.showSpecialtyError = false;
	}

	setPriority(priorization: EVirtualConsultationPriority) {
		this.informationForm.controls.priority.setValue(priorization);
		this.showPriorityError = false;
	}

	private toCareLinesDtoTypeahead(careLine: CareLineDto): TypeaheadOption<CareLineDto> {
		return {
			compareValue: careLine.description,
			value: careLine,
			viewValue: careLine.description
		};
	}

	private specialtyToTypeaheadOption(specialty: ClinicalSpecialtyDto): TypeaheadOption<ClinicalSpecialtyDto> {
		return {
			compareValue: specialty.name,
			value: specialty,
			viewValue: specialty.name
		};
	}

}
