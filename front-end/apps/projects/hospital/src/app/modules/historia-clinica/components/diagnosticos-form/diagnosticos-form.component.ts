import { Component, Input, OnChanges, SimpleChanges, forwardRef } from '@angular/core';
import { FormBuilder, NG_VALUE_ACCESSOR } from '@angular/forms';
import { DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-diagnosticos-form',
	templateUrl: './diagnosticos-form.component.html',
	styleUrls: ['./diagnosticos-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => DiagnosticosFormComponent),
			multi: true,
		}
	]
})
export class DiagnosticosFormComponent implements OnChanges {

	@Input() diagnosis: {
		mainDiagnosis: DiagnosisDto[],
		diagnosticos: HealthConditionDto
	}



	ngOnChanges(changes: SimpleChanges): void {
		this.formDiagnosticos.controls.otrosDiagnosticos.setValue(this.diagnosis?.diagnosticos || []);
		this.formDiagnosticos.controls.mainDiagnostico.setValue(this.diagnosis?.mainDiagnosis);
	}

	formDiagnosticos = this.formBuilder.group({
		mainDiagnostico: [[]],
		otrosDiagnosticos: [[]]
	});

	onChangeSub: Subscription;

	constructor(
		private formBuilder: FormBuilder,
	) { }

	diagnosisChange(event) {
		this.formDiagnosticos.controls.otrosDiagnosticos.setValue(event)
	}

	mainDiagnosisChange(event) {
		this.formDiagnosticos.controls.mainDiagnostico.setValue(event)
	}

	onTouched = () => { };

	writeValue(obj: any): void {
		if (obj)
			this.formDiagnosticos.setValue(obj);
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
