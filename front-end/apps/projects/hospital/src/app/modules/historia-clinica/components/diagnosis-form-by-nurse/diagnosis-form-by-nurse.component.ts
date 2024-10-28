import { Component, forwardRef, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, NG_VALUE_ACCESSOR } from '@angular/forms';
import { AppFeature, DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { HEALTH_CLINICAL_STATUS, HEALTH_VERIFICATIONS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { IsolationAlertDiagnosesService } from '@historia-clinica/services/isolation-alert-diagnoses.service';
import { BoxMessageInformation } from '@presentation/components/box-message/box-message.component';
import { of, Subscription, switchMap, take } from 'rxjs';

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
export class DiagnosisFormByNurseComponent extends AbstractCustomForm implements OnDestroy {

	readonly ACTIVE = HEALTH_CLINICAL_STATUS.ACTIVO;
	readonly CONFIRMED = HEALTH_VERIFICATIONS.CONFIRMADO;
	readonly boxMesaggeInfo: BoxMessageInformation = {
		message: "historia-clinica.isolation-alert.DIAGNOSIS_ASSOCIATED_TO_ISOLATION_ALERT",
		showButtons: false
	}

	form: FormGroup<DiagnosisForm>;
	isolationAlertSubscription: Subscription;

	constructor(
		readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly isolationAlertDiagnosesService: IsolationAlertDiagnosesService,
	) {
		super();
		this.createForm();
		this.subscribeToIsolationAlertDiagnoses()
	}

	ngOnDestroy(): void {
		this.isolationAlertSubscription && this.isolationAlertSubscription.unsubscribe();
	}

	createForm() {
		this.form = new FormGroup<DiagnosisForm>({
			mainDiagnostico: new FormControl(null),
			otrosDiagnosticos: new FormControl([])
		});
	}

	private addIsolationAlertDiagnosis(isolationAlertDiagnoses: DiagnosisDto[]) {
		this.resetIsolationAlertDiagnoses();
		this.form.controls.otrosDiagnosticos.setValue(isolationAlertDiagnoses);
	}

	private resetIsolationAlertDiagnoses() {
		this.form.controls.otrosDiagnosticos.setValue([]);
	}

	private subscribeToIsolationAlertDiagnoses() {
		this.isolationAlertSubscription = this.featureFlagService.isActive(AppFeature.HABILITAR_PACIENTES_COLONIZADOS_EN_DESARROLLO).
			pipe(take(1), switchMap(isActive => isActive ? this.isolationAlertDiagnosesService.isolationAlertDiagnoses$ : of()))
			.subscribe((isolationAlertDiagnosis: DiagnosisDto[]) =>
				isolationAlertDiagnosis && this.addIsolationAlertDiagnosis(isolationAlertDiagnosis)
			);
	}
}

export interface DiagnosisForm {
	mainDiagnostico: FormControl<HealthConditionDto>;
	otrosDiagnosticos: FormControl<DiagnosisDto[]>;
}