import { Component, forwardRef } from '@angular/core';
import { FormControl, FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { HealthConditionDto } from '@api-rest/api-model';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { HEALTH_CLINICAL_STATUS, HEALTH_VERIFICATIONS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';

@Component({
	selector: 'app-diagnosis-form-by-nurse',
	templateUrl: './diagnosis-form-by-nurse.component.html',
	styleUrls: ['./diagnosis-form-by-nurse.component.scss'],
	providers: [
		ComponentEvaluationManagerService,
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => DiagnosisFormByNurseComponent),
			multi: true,
		}
	]
})
export class DiagnosisFormByNurseComponent extends AbstractCustomForm {

	readonly ACTIVE = HEALTH_CLINICAL_STATUS.ACTIVO;
	readonly CONFIRMED = HEALTH_VERIFICATIONS.CONFIRMADO;

	form: FormGroup<DiagnosisForm>;

	constructor(
		readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
	) {
		super();
		this.createForm();
	}

	createForm() {
		this.form = new FormGroup<DiagnosisForm>({
			mainDiagnostico: new FormControl(null)
		});
	}
}

export interface DiagnosisForm {
	mainDiagnostico: FormControl<HealthConditionDto>;
}