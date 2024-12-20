import { Component, EventEmitter, Input, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ClinicalTermDto, DiagnosisDto, MasterDataDto } from '@api-rest/api-model';
import { EmergencyCareMasterDataService } from '@api-rest/services/emergency-care-master-data.service';
import { Subscription } from 'rxjs';
import { EpisodeDiagnosesService } from '@historia-clinica/services/episode-diagnoses.service';
import { SaveIsolationAlertService } from '@historia-clinica/services/save-isolation-alert.service';

const TYPE_REQUIRED_OBSERVATION = 6;

@Component({
	selector: 'app-isolation-alert-form',
	templateUrl: './isolation-alert-form.component.html',
	styleUrls: ['./isolation-alert-form.component.scss'],

})
export class IsolationAlertFormComponent implements OnInit, OnDestroy {

	readonly Validators = Validators;
	readonly today = new Date();
	form: FormGroup<IsolationAlertForm>;
	diagnoses: ClinicalTermDto[] = [];
	isolationTypes: MasterDataDto[] = [];
	markAsTouched = false;
	submitFormEvent = new EventEmitter<void>();
	submitSubscription: Subscription;

	@Input() set preloadedIsolationAlert(isolationAlert: IsolationAlert) {
		isolationAlert && this.preloadedIsolationAlertForm(isolationAlert);
	}

	constructor(
		private readonly emergencyCareMasterDataService: EmergencyCareMasterDataService,
		private readonly episodeDiagnosesService: EpisodeDiagnosesService,
		private readonly saveIsolationAlertService: SaveIsolationAlertService,
	) { }

	ngOnInit(): void {
		this.createForm();
		this.emergencyCareMasterDataService.getIsolationTypes().subscribe(isolationTypes => this.isolationTypes = isolationTypes);
		this.diagnoses = this.episodeDiagnosesService.getEpisodeDiagnoses();
		this.submitSubscription = this.saveIsolationAlertService.submit$.subscribe(submit => this.submitForm());
	}

	ngOnDestroy(): void {
		this.submitSubscription.unsubscribe();
	}

	createForm() {
		this.form = new FormGroup<IsolationAlertForm>({
			id: new FormControl(null),
			statusId: new FormControl(null),
			diagnosis: new FormControl(null, Validators.required),
			types: new FormControl(null, Validators.required),
			criticality: new FormControl(null, Validators.required),
			endDate: new FormControl(null, Validators.required),
			observations: new FormControl(null),
		});
	}

	verifyValidations() {
		const types = this.form.getRawValue().types;
		const selectedTypeWithRequiredObservations = types.some(type => type.id === TYPE_REQUIRED_OBSERVATION);
		const observationsControl = this.form.controls.observations;
		selectedTypeWithRequiredObservations ? this.addRequiredValidator(observationsControl) : this.removeRequiredValidator(observationsControl);

	}

	private addRequiredValidator(control: FormControl) {
		control.addValidators(Validators.required);
		control.updateValueAndValidity();
	}

	private removeRequiredValidator(control: FormControl) {
		control.removeValidators(Validators.required);
		control.updateValueAndValidity();
	}

	private submitForm() {
		this.emitSubmit();
		this.form.valid && this.saveIsolationAlertService.persistSubject.next(this.buildIsolationAlert());
	}

	private emitSubmit() {
		this.submitFormEvent.emit();
		this.form.markAllAsTouched();
		this.form.updateValueAndValidity();
		this.markAsTouched = true;
	}

	private buildIsolationAlert(): IsolationAlert {
		const isolationAlertForm = this.form.getRawValue();
		return {
			...isolationAlertForm,
			observations: isolationAlertForm.observations.observations,
			criticality: isolationAlertForm.criticality.criticality
		}
	}

	private preloadedIsolationAlertForm(isolationAlert: IsolationAlert) {
		this.diagnoses = [isolationAlert.diagnosis];
		const isolationAlertForm = {
			...isolationAlert,
			types: this.isolationTypes.filter(type => isolationAlert.types.some(selectedType => type.id === selectedType.id)),
			criticality: { criticality: isolationAlert.criticality },
			observations: { observations: isolationAlert.observations ? isolationAlert.observations : null },
		}
		this.form.setValue(isolationAlertForm);
		this.disableFormControls();
		this.verifyValidations();
	}

	private disableFormControls() {
		this.form.controls.diagnosis.disable();
		this.form.controls.types.disable();
	}

}

interface IsolationAlertForm {
	id: FormControl<number>,
	statusId: FormControl<number>,
	diagnosis: FormControl<DiagnosisDto>,
	types: FormControl<MasterDataDto[]>,
	criticality: FormControl<{ criticality: MasterDataDto }>,
	endDate: FormControl<Date>,
	observations: FormControl<{ observations: string }>,
}

export interface IsolationAlert {
	id: number,
	statusId: number,
	diagnosis: DiagnosisDto,
	types: MasterDataDto[],
	criticality: MasterDataDto,
	endDate: Date,
	observations: string,
}
