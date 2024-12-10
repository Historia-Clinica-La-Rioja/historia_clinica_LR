import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AppFeature, DiagnosisDto, EEmergencyCareEvolutionNoteType, EmergencyCareEvolutionNoteDto, HealthConditionDto } from '@api-rest/api-model';
import { EmergencyCareEvolutionNoteService } from '@api-rest/services/emergency-care-evolution-note.service';
import { buildEmergencyCareEvolutionNoteDto } from '@historia-clinica/mappers/emergency-care-evolution-note.mapper';
import { HEALTH_CLINICAL_STATUS, HEALTH_VERIFICATIONS } from '@historia-clinica/modules/ambulatoria/modules/internacion/constants/ids';
import { NewEmergencyCareEvolutionNoteService } from '@historia-clinica/modules/guardia/services/new-emergency-care-evolution-note.service';
import { NewRiskFactorsService } from '@historia-clinica/modules/guardia/services/new-risk-factors.service';
import { DockPopUpHeader } from '@presentation/components/dock-popup/dock-popup.component';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { NotaDeEvolucionData } from '../nota-de-evolucion-dock-popup/nota-de-evolucion-dock-popup.component';
import { EvolutionNoteEditionService } from '@historia-clinica/modules/guardia/services/evolution-note-edition.service';
import { EmergencyCareStateService } from '@api-rest/services/emergency-care-state.service';
import { EpisodeDiagnosesService } from '@historia-clinica/services/episode-diagnoses.service';
import { Subscription } from 'rxjs';
import { IsolationAlert } from '../isolation-alert-form/isolation-alert-form.component';
import { IsolationAlertDiagnosesService } from '@historia-clinica/services/isolation-alert-diagnoses.service';
import { pushIfNotExists } from '@core/utils/array.utils';
import { PatientIsolationAlertsService } from '@historia-clinica/services/patient-isolation-alerts.service';

@Component({
	selector: 'app-evolution-note-dock-popup-by-nurse',
	templateUrl: './evolution-note-dock-popup-by-nurse.component.html',
	styleUrls: ['./evolution-note-dock-popup-by-nurse.component.scss'],
	providers: [EvolutionNoteEditionService]
})
export class EvolutionNoteDockPopupByNurseComponent implements OnInit, OnDestroy {

	readonly header: DockPopUpHeader = { title: 'guardia.actions.EVOLUTION_NOTE_BY_NURSE' };
	readonly HABILITAR_PACIENTES_COLONIZADOS = AppFeature.HABILITAR_PACIENTES_COLONIZADOS_EN_DESARROLLO;
	disableConfirmButton = false;
	isAllergyNoRefer = true;
	isFamilyHistoriesNoRefer = true;
	markAsTouched = false;
	form: FormGroup;
	formSubscription: Subscription;

	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly emergencyCareEvolutionNoteService: EmergencyCareEvolutionNoteService,
		private readonly newEmergencyCareEvolutionNoteService: NewEmergencyCareEvolutionNoteService,
		private readonly newRiskFactorsService: NewRiskFactorsService,
		private readonly evolutionNoteEditionService: EvolutionNoteEditionService,
		private readonly emergencyCareStateService: EmergencyCareStateService,
		private readonly episodeDiagnosesService: EpisodeDiagnosesService,
		private readonly isolationAlertDiagnosesService: IsolationAlertDiagnosesService,
		readonly dockPopupRef: DockPopupRef,
		private readonly patientIsolationAlertService: PatientIsolationAlertsService,
		@Inject(OVERLAY_DATA) readonly data: NotaDeEvolucionData,
	) { }

	ngOnInit(): void {
		this.buildForm();
		this.subscribeToIsolationAlertsForm();
		this.setEpisodeDiagnoses();

		if (this.data.editMode) {
			this.setEvolutionNoteDataToEdit();
			return;
		}

		this.setDefaultValue(NURSING_DIAGNOSIS, []);
	}

	ngOnDestroy(): void {
		this.formSubscription && this.formSubscription.unsubscribe();
		this.episodeDiagnosesService.resetDiagnoses();
	}

	persist() {
		if (this.form.valid) {
			this.disableConfirmButton = true;
			const emergencyCareEvolutionNoteDto = buildEmergencyCareEvolutionNoteDto(this.form, this.isFamilyHistoriesNoRefer, this.isAllergyNoRefer, this.data.patientId, EEmergencyCareEvolutionNoteType.NURSE);
			this.save(emergencyCareEvolutionNoteDto);
		}
		else {
			this.markAsTouched = true;
			this.snackBarService.showError('guardia.nursing_evolution_note.SAVE_ERROR');
		}
	}

	private save(emergencyCareEvolutionNoteDto: EmergencyCareEvolutionNoteDto) {
		const { episodeId, documentId } = this.data;
		const saveDocument$ = this.data.editMode ? this.emergencyCareEvolutionNoteService.updateEmergencyCareEvolutionNote(episodeId, documentId, emergencyCareEvolutionNoteDto) : this.emergencyCareEvolutionNoteService.saveEmergencyCareEvolutionNote(this.data.episodeId, emergencyCareEvolutionNoteDto);
		saveDocument$.subscribe({
			next: saved => {
				this.snackBarService.showSuccess('guardia.nursing_evolution_note.SAVE_SUCCESS');
				this.newEmergencyCareEvolutionNoteService.newEvolutionNote();
				emergencyCareEvolutionNoteDto.riskFactors && this.newRiskFactorsService.newRiskFactors();
				const hasPatientUpdatedIsolationAlerts = emergencyCareEvolutionNoteDto.isolationAlerts.length || this.data.emergencyCareEvolutionNote?.isolationAlerts.length;
				hasPatientUpdatedIsolationAlerts && this.patientIsolationAlertService.updatedIsolationAlertsSubject.next(true);
				this.dockPopupRef.close(true);
			},
			error: error => {
				this.snackBarService.showError(error.text);
				this.disableConfirmButton = false;
			}
		});
	}

	private buildForm() {
		this.form = new FormGroup({
			clinicalSpecialty: new FormControl(null),
			reasons: new FormControl(null),
			diagnosis: new FormControl(null),
			evolutionNote: new FormControl(null, Validators.required),
			anthropometricData: new FormControl(null),
			familyHistories: new FormControl(null),
			medications: new FormControl(null),
			procedures: new FormControl(null),
			riskFactors: new FormControl(null),
			allergies: new FormControl(null),
			isolationAlerts: new FormControl(null),
		});
	}

	private setDefaultValue(mainDiagnosis: HealthConditionDto, otherDiagnoses: DiagnosisDto[]) {
		this.form.controls.diagnosis.setValue({ mainDiagnostico: mainDiagnosis, otrosDiagnosticos: otherDiagnoses });
	}

	private setEvolutionNoteDataToEdit() {
		const evolutionNoteData = this.data.emergencyCareEvolutionNote;
		this.setDefaultValue(evolutionNoteData.mainDiagnosis, evolutionNoteData.diagnosis);
		this.evolutionNoteEditionService.loadFormByEvolutionNoteData(this.form, evolutionNoteData);
	}

	private subscribeToIsolationAlertsForm() {
		this.formSubscription = this.form.controls.isolationAlerts.valueChanges.subscribe((isolationAlerts: { isolationAlerts: IsolationAlert[] }) => {
			if (isolationAlerts) {
				const diagnosis: DiagnosisDto[] = isolationAlerts.isolationAlerts.map(isolationAlerts => isolationAlerts.diagnosis);
				const uniqueDiagnosis = this.filterDuplicateDiagnoses(diagnosis);
				this.isolationAlertDiagnosesService.setIsolationAlertDiagnosis(uniqueDiagnosis);
			}
		})
	}

	private filterDuplicateDiagnoses(diagnosis: DiagnosisDto[]): DiagnosisDto[] {
		return diagnosis.reduce((diagnosis, diagnoses) => {
			diagnosis = pushIfNotExists<DiagnosisDto>(diagnosis, diagnoses, compareDiagnosis)
			return diagnosis;
		}, []);

		function compareDiagnosis(diagnosis1: DiagnosisDto, diagnosis2: DiagnosisDto) {
			return diagnosis1.snomed.sctid === diagnosis2.snomed.sctid;
		}
	}

	private setEpisodeDiagnoses() {
		this.emergencyCareStateService.getEmergencyCareEpisodeDiagnosesWithoutNursingAttentionDiagnostic(this.data.episodeId).subscribe(
			diagnoses => {
				if (diagnoses.length) {
					const main = diagnoses.find(d => d.main);
					const others = diagnoses.filter(d => !d.main) || [];
					diagnoses.length && this.episodeDiagnosesService.setEpisodeDiagnoses({ main, others });
				}
			});
	}

}

const NURSING_DIAGNOSIS: HealthConditionDto = {
	snomed: {
		pt: 'Atención de enfermería',
		sctid: '9632001',
	},
	isAdded: true,
	verificationId: HEALTH_VERIFICATIONS.CONFIRMADO,
	statusId: HEALTH_CLINICAL_STATUS.ACTIVO
}
