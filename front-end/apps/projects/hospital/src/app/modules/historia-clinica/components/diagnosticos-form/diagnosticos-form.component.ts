import { Component, forwardRef } from '@angular/core';
import { FormBuilder, FormControl, NG_VALUE_ACCESSOR } from '@angular/forms';
import { DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-diagnosticos-form',
	templateUrl: './diagnosticos-form.component.html',
	styleUrls: ['./diagnosticos-form.component.scss'],
	providers: [
		ComponentEvaluationManagerService,
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => DiagnosticosFormComponent),
			multi: true,
		}
	]
})
export class DiagnosticosFormComponent {

	formDiagnosticos = this.formBuilder.group({
		mainDiagnostico: new FormControl<HealthConditionDto | null>(null),
		otrosDiagnosticos: new FormControl<DiagnosisDto[] | null>([]),
	});

	onChangeSub: Subscription;

	constructor(
		private formBuilder: FormBuilder,
		readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
	) { }

	diagnosisChange(event: DiagnosisDto[]) {
		this.formDiagnosticos.controls.otrosDiagnosticos.setValue(event);
	}

	mainDiagnosisChange(event: HealthConditionDto) {
		this.formDiagnosticos.controls.mainDiagnostico.setValue(event);
	}

	onTouched = () => { };

	writeValue(obj: any): void {
		if (obj) {
			this.formDiagnosticos.setValue(obj);
			this.componentEvaluationManagerService.mainDiagnosis = obj.mainDiagnostico;
			this.componentEvaluationManagerService.diagnosis = obj.otrosDiagnosticos;
		}
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.formDiagnosticos.valueChanges
			.subscribe(value => {
				const toEmit = this.formDiagnosticos.valid ? value : null;
				fn(toEmit);
			})
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	setDisabledState?(isDisabled: boolean): void {
		isDisabled ? this.formDiagnosticos.disable() : this.formDiagnosticos.enable();
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}

}
