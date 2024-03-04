import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControlStatus, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { CareLineDto, ClinicalSpecialtyDto, EVirtualConsultationPriority, PersonPhotoDto } from '@api-rest/api-model';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ContextService } from '@core/services/context.service';
import { MotivoConsulta } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { AmbulatoryConsultationProblem } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { PatientBasicData } from '@presentation/utils/patient.utils';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { Observable } from 'rxjs';

const REASON_LIMIT = 1;
const VALID = "VALID";

@Component({
	selector: 'app-information-request-form',
	templateUrl: './information-request-form.component.html',
	styleUrls: ['./information-request-form.component.scss']
})
export class InformationRequestFormComponent implements OnInit {
	informationForm: UntypedFormGroup;
	@Input() patient: PatientBasicData;
	@Input() patientPhoto: PersonPhotoDto;
	@Input() set confirmAndValidateForm(isConfirmAndValidateForm: Observable<boolean>) {
		isConfirmAndValidateForm.subscribe(change => {
			this.validateAndDisplayErrors();
		})
	}
	@Input() set resetForm(reset: boolean) {
		if (reset) {
			this.showCareLineError = false;
			this.showMotiveError = false;
			this.showPriorityError = false;
			this.showSpecialtyError = false;
		}
	}
	@Output() requestInformationData = new EventEmitter<any>();

	careLines: CareLineDto[];
	careLinesTypeahead: TypeaheadOption<CareLineDto>[] = [];
	showCareLineError = false;
	specialties: ClinicalSpecialtyDto[];
	specialtyTypeaheadOptions: TypeaheadOption<ClinicalSpecialtyDto>[] = [];
	showSpecialtyError = false;
	showMotiveError = false;
	showPriorityError = false;
	reasonLimit = REASON_LIMIT;

	constructor(private carelineService: CareLineService, private contextService: ContextService,
		private readonly formBuilder: UntypedFormBuilder) { }

	ngOnInit(): void {
		this.initForm();
		this.carelineService.getCareLinesAttachedToInstitution(this.contextService.institutionId).subscribe(data => {
			this.careLines = data;
			this.careLinesTypeahead = this.careLines.map(c => this.toCareLinesDtoTypeahead(c));
		})
		this.informationForm.statusChanges.subscribe((isValid: FormControlStatus) => {
			if (isValid === VALID) {
				this.requestInformationData.emit(this.informationForm.value);
			} else {
				this.requestInformationData.emit(null);
			}
		})
	}

	private initForm(): void {
		this.informationForm = this.formBuilder.group({
			careLine: [null, Validators.required],
			specialty: [null, Validators.required],
			priority: [null, Validators.required],
			motive: [null, Validators.required],
			problem: [null],
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

	setMotive(motive: MotivoConsulta) {
		this.informationForm.controls.motive.setValue(motive);
		this.showMotiveError = motive ? false : true;
	}

	setProblem(problem: AmbulatoryConsultationProblem) {
		this.informationForm.controls.problem.setValue(problem);
	}

	validateAndDisplayErrors() {
		this.showCareLineError = !this.informationForm.controls.careLine.valid;
		this.showPriorityError = !this.informationForm.controls.priority.valid
		this.showMotiveError = !this.informationForm.controls.motive.valid
		this.showSpecialtyError = !this.informationForm.controls.specialty.valid
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
