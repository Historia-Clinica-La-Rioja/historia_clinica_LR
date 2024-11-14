import { ChangeDetectorRef, Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { AppFeature, DiagnosisDto, EEmergencyCareEvolutionNoteType, EmergencyCareEvolutionNoteDto, HealthConditionDto } from '@api-rest/api-model';
import { EmergencyCareEvolutionNoteService } from '@api-rest/services/emergency-care-evolution-note.service';
import { EmergencyCareStateService } from '@api-rest/services/emergency-care-state.service';
import { NewEmergencyCareEvolutionNoteService } from '@historia-clinica/modules/guardia/services/new-emergency-care-evolution-note.service';
import { NewRiskFactorsService } from '@historia-clinica/modules/guardia/services/new-risk-factors.service';
import { DockPopUpHeader } from '@presentation/components/dock-popup/dock-popup.component';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { buildEmergencyCareEvolutionNoteDto } from '@historia-clinica/mappers/emergency-care-evolution-note.mapper';
import { EvolutionNoteEditionService } from '@historia-clinica/modules/guardia/services/evolution-note-edition.service';
import { EpisodeDiagnosesService } from '@historia-clinica/services/episode-diagnoses.service';
import { IsolationAlertDiagnosesService } from '@historia-clinica/services/isolation-alert-diagnoses.service';
import { IsolationAlert } from '../isolation-alert-form/isolation-alert-form.component';
import { Subscription } from 'rxjs';
import { PatientIsolationAlertsService } from '@historia-clinica/services/patient-isolation-alerts.service';

@Component({
	selector: 'app-nota-de-evolucion-dock-popup',
	templateUrl: './nota-de-evolucion-dock-popup.component.html',
	styleUrls: ['./nota-de-evolucion-dock-popup.component.scss'],
	providers: [EvolutionNoteEditionService]
})

export class NotaDeEvolucionDockPopupComponent implements OnInit, OnDestroy {

	disableConfirmButton = false;
	readonly header: DockPopUpHeader = {
		title: 'Nota de evolución'
	}

	form = this.formBuilder.group({
		clinicalSpecialty: [],
		reasons: [],
		diagnosis: [],
		evolutionNote: [],
		anthropometricData: [],
		familyHistories: [],
		medications: [],
		procedures: [],
		riskFactors: [],
		allergies: [],
		isolationAlerts: [],
	});

	diagnosis;
	isAllergyNoRefer: boolean = true;
	isFamilyHistoriesNoRefer: boolean = true;
	disabled = true;
	markAsTouched = false;

	formsSubscription: Subscription[] = [];
	readonly HABILITAR_PACIENTES_COLONIZADOS = AppFeature.HABILITAR_PACIENTES_COLONIZADOS_EN_DESARROLLO;

	constructor(
		public dockPopupRef: DockPopupRef,
		private formBuilder: FormBuilder,
		@Inject(OVERLAY_DATA) public data: NotaDeEvolucionData,
		private readonly emergencyCareStateService: EmergencyCareStateService,
		private readonly emergencyCareEvolutionNoteService: EmergencyCareEvolutionNoteService,
		private readonly snackBarService: SnackBarService,
		private readonly newEmergencyCareEvolutionNoteService: NewEmergencyCareEvolutionNoteService,
		private changeDetectorRef: ChangeDetectorRef,
		private readonly newRiskFactorsService: NewRiskFactorsService,
		private readonly evolutionNoteEditionService: EvolutionNoteEditionService,
		private readonly episodeDiagnosesService: EpisodeDiagnosesService,
		private readonly isolationAlertDiagnosesService: IsolationAlertDiagnosesService,
		private readonly patientIsolationAlertService: PatientIsolationAlertsService,
	) { }

	ngOnInit(): void {
		this.subscribeToDiagnosesForm();
		this.subscribeIsolationAlertsForm();

		if (this.data.editMode) {
			this.setEvolutionNoteDataToEdit();
			return;
		}

		this.emergencyCareStateService.getEmergencyCareEpisodeDiagnosesWithoutNursingAttentionDiagnostic(this.data.episodeId).subscribe(
			diagnoses => {
				if (diagnoses.length) {
					const mainDiagnosis = diagnoses.find(d => d.main);
					const otherDiagnoses = diagnoses.filter(d => !d.main) || [];
					this.setDiagnosis(mainDiagnosis, otherDiagnoses);
				}
			});
	}

	ngOnDestroy(): void {
		if (this.formsSubscription.length) {
			this.formsSubscription.forEach(subscription => subscription.unsubscribe());
		}
		this.episodeDiagnosesService.resetDiagnoses();
	}

	ngAfterViewInit() {
		this.changeDetectorRef.detectChanges();
	}

	save() {
		if (this.form.value.evolutionNote) {
			this.disableConfirmButton = true;
			const emergencyCareEvolutionNoteDto = buildEmergencyCareEvolutionNoteDto(this.form, this.isFamilyHistoriesNoRefer, this.isAllergyNoRefer, this.data.patientId, EEmergencyCareEvolutionNoteType.DOCTOR);
			this.persist(emergencyCareEvolutionNoteDto)
		}
		else {
			this.markAsTouched = true;
			this.snackBarService.showError('La nota de evolución de guardia debe tener una evolución');
		}
	}

	private persist(emergencyCareEvolutionNoteDto: EmergencyCareEvolutionNoteDto) {
		const saveEmergencyCareEvolutionNote$ = this.data.editMode ? this.emergencyCareEvolutionNoteService.updateEmergencyCareEvolutionNote(this.data.episodeId, this.data.documentId, emergencyCareEvolutionNoteDto) : this.emergencyCareEvolutionNoteService.saveEmergencyCareEvolutionNote(this.data.episodeId, emergencyCareEvolutionNoteDto)
		saveEmergencyCareEvolutionNote$.subscribe(
			saved => {
				this.snackBarService.showSuccess('Nota de evolución guardada correctamente');
				this.newEmergencyCareEvolutionNoteService.newEvolutionNote();
				emergencyCareEvolutionNoteDto.riskFactors && this.newRiskFactorsService.newRiskFactors();
				emergencyCareEvolutionNoteDto.isolationAlerts.length && this.patientIsolationAlertService.newIsolationAlert(this.data.patientId);
				this.dockPopupRef.close(true)
			},
			error => {
				this.snackBarService.showError(error.text)
				this.disableConfirmButton = false;
			}
		);
	}

	setIsAllergyNoRefer = ($event) => {
		this.isAllergyNoRefer = $event;
	}

	setIsFamilyHistoriesNoRefer = ($event) => {
		this.isFamilyHistoriesNoRefer = $event;
	}

	private setDiagnosis(mainDiagnosis: HealthConditionDto, otherDiagnoses: DiagnosisDto[]) {
		mainDiagnosis.isAdded = !!mainDiagnosis;
		const othersDiagnosis = otherDiagnoses.map(otherDiagnosis => { return { diagnosis: otherDiagnosis } });
		const main = { main: mainDiagnosis };
		this.form.controls.diagnosis.setValue({ mainDiagnostico: main, otrosDiagnosticos: othersDiagnosis });
	}

	private setEvolutionNoteDataToEdit() {
		const evolutionNoteData = this.data.emergencyCareEvolutionNote;
		this.setDiagnosis(evolutionNoteData.mainDiagnosis, evolutionNoteData.diagnosis);
		this.evolutionNoteEditionService.loadFormByEvolutionNoteData(this.form, evolutionNoteData);
	}

	private subscribeToDiagnosesForm() {
		const subscription = this.form.controls.diagnosis.valueChanges.subscribe(diagnosis => {
			const { mainDiagnostico, otrosDiagnosticos } = diagnosis;
			this.episodeDiagnosesService.setEpisodeDiagnoses({ 
				main: mainDiagnostico?.main, 
				others: otrosDiagnosticos.filter(otherDiagnosis => otherDiagnosis.diagnosis.isAdded).map(otherDiagnosis => otherDiagnosis.diagnosis) });
		});

		this.formsSubscription.push(subscription);
	}

	private subscribeIsolationAlertsForm() {
		const subscription =  this.form.controls.isolationAlerts.valueChanges.subscribe((isolationAlerts: { isolationAlerts: IsolationAlert[] }) => {
			this.isolationAlertDiagnosesService.setIsolationAlertDiagnosis(isolationAlerts.isolationAlerts.map(isolationAlert => isolationAlert.diagnosis))
		});

		this.formsSubscription.push(subscription);
	}
}

export interface NotaDeEvolucionData {
	patientId: number,
	episodeId: number,
	editMode: boolean,
	emergencyCareEvolutionNote: EmergencyCareEvolutionNoteDto,
	documentId: number,
}
