import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TriageAdultGynecologicalDto } from '@api-rest/api-model';
import { getError, hasError } from '@core/utils/form.utils';
import { VitalSignsValue, VitalSignsFormService } from '../../../../services/vital-signs-form.service';
import { GuardiaMapperService } from '../../services/guardia-mapper.service';

@Component({
	selector: 'app-adult-gynecological-triage',
	templateUrl: './adult-gynecological-triage.component.html',
	styleUrls: ['./adult-gynecological-triage.component.scss']
})
export class AdultGynecologicalTriageComponent implements OnInit {

	@Input() confirmLabel = 'Confirmar episodio';
	@Input() cancelLabel = 'Volver';
	@Input() disableConfirmButton: boolean;
	@Output() confirm = new EventEmitter();
	@Output() cancel = new EventEmitter();

	hasError = hasError;
	getError = getError;

	private triageCategoryId: number;
	private doctorsOfficeId: number;

	adultGynecologicalForm: FormGroup;
	vitalSignsForm: FormGroup;

	constructor(
		private formBuilder: FormBuilder,
		private guardiaMapperService: GuardiaMapperService,
		public vitalSignsFormService: VitalSignsFormService,
	) { }

	ngOnInit(): void {
		this.adultGynecologicalForm = this.formBuilder.group({
			evaluation: [null]
		});
		this.vitalSignsForm = this.vitalSignsFormService.buildForm();
	}

	setTriageCategoryId(triageCategoryId: number): void {
		this.triageCategoryId = triageCategoryId;
	}

	setDoctorsOfficeId(doctorsOfficeId: number): void {
		this.doctorsOfficeId = doctorsOfficeId;
	}

	confirmAdultGynecologicalTriage(): void {
		const formValue = this.adultGynecologicalForm.value;
		if (this.adultGynecologicalForm.valid && this.vitalSignsForm.valid) {
			const vitalSignsValue: VitalSignsValue = this.vitalSignsFormService.buildVitalSignsValue(this.vitalSignsForm);
			const triage: TriageAdultGynecologicalDto = {
				categoryId: this.triageCategoryId,
				doctorsOfficeId: this.doctorsOfficeId,
				notes: formValue.evaluation,
				vitalSigns: this.guardiaMapperService.vitalSignsValuetoNewVitalSignsObservationDto(vitalSignsValue)
			};
			this.confirm.emit(triage);
		}
	}

	back(): void {
		this.cancel.emit();
	}
}
