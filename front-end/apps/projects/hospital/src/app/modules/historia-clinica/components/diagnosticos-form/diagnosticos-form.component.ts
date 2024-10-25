import { Component, forwardRef } from '@angular/core';
import { FormBuilder, FormControl, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { IsolationAlertDiagnosesService } from '@historia-clinica/services/isolation-alert-diagnoses.service';
import { Subscription } from 'rxjs';
import { EmergencyCareDiagnosis, EmergencyCareMainDiagnosis } from '../emergency-care-diagnoses/emergency-care-diagnoses.component';
import { BoxMessageInformation } from '@presentation/components/box-message/box-message.component';
import { ClinicalTermDto, DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';

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
		mainDiagnostico: new FormControl<EmergencyCareMainDiagnosis | null>(null),
		otrosDiagnosticos: new FormControl<EmergencyCareDiagnosis[] | null>([]),
	});

	onChangeSub: Subscription;

	existsADiagnosisAssociatedToIsolationAlerts = false;
	isolationAlertSubscription: Subscription;

	boxMesaggeInfo: BoxMessageInformation = {
		message: "historia-clinica.isolation-alert.DIAGNOSIS_ASSOCIATED_TO_ISOLATION_ALERT",
		showButtons: false
	}
	isolationAlertsDiagnoses: ClinicalTermDto[] = [];

	constructor(
		private formBuilder: FormBuilder,
		readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
		private readonly isolationAlertDiagnoses: IsolationAlertDiagnosesService,
	) {
		this.isolationAlertSubscription = this.isolationAlertDiagnoses.isolationAlertDiagnisis$.subscribe(isolationAlertDiagnoses => {
			this.isolationAlertsDiagnoses = isolationAlertDiagnoses;
			this.calculateDiagnosesAssociatedToIsolationAlerts();
		});
	}

	diagnosisChange(event: EmergencyCareDiagnosis[]) {
		this.formDiagnosticos.controls.otrosDiagnosticos.setValue(event);
	}

	mainDiagnosisChange(event: EmergencyCareMainDiagnosis) {
		this.formDiagnosticos.controls.mainDiagnostico.setValue(event);
	}

	onTouched = () => { };

	writeValue(obj: any): void {
		if (obj) {
			this.formDiagnosticos.setValue(obj);
			this.componentEvaluationManagerService.mainDiagnosis = obj.mainDiagnostico.main;
			this.componentEvaluationManagerService.diagnosis = obj.otrosDiagnosticos.diagnosis;
			this.calculateDiagnosesAssociatedToIsolationAlerts();
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
		this.isolationAlertSubscription && this.isolationAlertSubscription.unsubscribe();
	}

	private calculateDiagnosesAssociatedToIsolationAlerts() {
		this.resetDiagnosesAssociatedToIsolationAlerts();
		this.disableDiagnosesAssociatedToIsolationAlerts(this.isolationAlertsDiagnoses);
	}

	private resetDiagnosesAssociatedToIsolationAlerts() {
		if (this.formDiagnosticos.value.mainDiagnostico) {
			const main = this.formDiagnosticos.value.mainDiagnostico.main;
			this.formDiagnosticos.controls.mainDiagnostico.setValue(this.toEmergencyCareMainDiagnosis(main, false));
		}

		if (this.formDiagnosticos.value.otrosDiagnosticos.length) {
			const others = this.formDiagnosticos.value.otrosDiagnosticos.map(otherDiagnosis => this.toEmergencyCareDiagnosis(otherDiagnosis.diagnosis, false));
			this.formDiagnosticos.controls.otrosDiagnosticos.setValue(others);
		}

		this.existsADiagnosisAssociatedToIsolationAlerts = false;
	}

	private disableDiagnosesAssociatedToIsolationAlerts(isolationAlertDiagnoses: ClinicalTermDto[]) {
		isolationAlertDiagnoses.forEach(diagnosis => {
			const main = this.formDiagnosticos.value.mainDiagnostico?.main;
			if (isMainAssociated()) {
				this.existsADiagnosisAssociatedToIsolationAlerts = true;
				this.formDiagnosticos.controls.mainDiagnostico.setValue(this.toEmergencyCareMainDiagnosis(main, true));
			}

			const otherDiagnoses = this.formDiagnosticos.value.otrosDiagnosticos;
			if (isOtherDiagnosesAssociated()) {
				const elementIndex = otherDiagnoses.findIndex(otherDiagnosis => otherDiagnosis.diagnosis.snomed.sctid === diagnosis.snomed.sctid);
				otherDiagnoses[elementIndex].isAssociatedToIsolationAlert = true;
				this.existsADiagnosisAssociatedToIsolationAlerts = true;
			}

			function isMainAssociated() {
				return main && diagnosis.snomed.sctid === main.snomed.sctid
			}

			function isOtherDiagnosesAssociated() {
				return otherDiagnoses?.some(otherDiagnosis => otherDiagnosis.diagnosis.snomed.sctid === diagnosis.snomed.sctid);
			}
		});
	}


	private toEmergencyCareMainDiagnosis(mainDiagnosis: HealthConditionDto, isAssociatedToIsolationAlert: boolean): EmergencyCareMainDiagnosis {
		return { main: mainDiagnosis, isAssociatedToIsolationAlert }
	}

	private toEmergencyCareDiagnosis(diagnosis: DiagnosisDto, isAssociatedToIsolationAlert: boolean): EmergencyCareDiagnosis {
		return { diagnosis, isAssociatedToIsolationAlert }
	}

}