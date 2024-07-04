import { ChangeDetectorRef, Component, Inject, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { DiagnosisDto, EmergencyCareEvolutionNoteDto, HealthConditionDto } from '@api-rest/api-model';
import { EmergencyCareEvolutionNoteService } from '@api-rest/services/emergency-care-evolution-note.service';
import { EmergencyCareStateService } from '@api-rest/services/emergency-care-state.service';
import { NewEmergencyCareEvolutionNoteService } from '@historia-clinica/modules/guardia/services/new-emergency-care-evolution-note.service';
import { NewRiskFactorsService } from '@historia-clinica/modules/guardia/services/new-risk-factors.service';
import { DockPopUpHeader } from '@presentation/components/dock-popup/dock-popup.component';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { toOutpatientMedicationDto, toOutpatientAnthropometricDataDto, toOutpatientFamilyHistoryDto, toOutpatientRiskFactorDto } from '@historia-clinica/mappers/emergency-care-evolution-note.mapper';
import { EvolutionNoteEditionService } from '@historia-clinica/modules/guardia/services/evolution-note-edition.service';
import { toApiFormat } from '@api-rest/mapper/date.mapper';

@Component({
	selector: 'app-nota-de-evolucion-dock-popup',
	templateUrl: './nota-de-evolucion-dock-popup.component.html',
	styleUrls: ['./nota-de-evolucion-dock-popup.component.scss'],
	providers: [EvolutionNoteEditionService]
})

export class NotaDeEvolucionDockPopupComponent implements OnInit {

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
		allergies: []
	});

	diagnosis;
	isAllergyNoRefer: boolean = true;
	isFamilyHistoriesNoRefer: boolean = true;
	disabled = true;
	markAsTouched = false;

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
	) { }

	ngOnInit(): void {
		if (this.data.editMode) {
			this.setEvolutionNoteDataToEdit();
			return;
		}

		this.emergencyCareStateService.getEmergencyCareEpisodeDiagnoses(this.data.episodeId).subscribe(
			diagnoses => {
				if (diagnoses.length) {
					const mainDiagnosis = diagnoses.find(d => d.main);
					const otherDiagnoses = diagnoses.filter(d => !d.main) || [];
					this.setDiagnosis(mainDiagnosis, otherDiagnoses);
				}
			});
	}

	ngAfterViewInit() {
		this.changeDetectorRef.detectChanges();
	}

	save() {
		if (this.form.value.evolutionNote) {
			this.disableConfirmButton = true;
			const emergencyCareEvolutionNoteDto = this.buildEmergencyCareEvolutionNoteDto();
			this.persist(emergencyCareEvolutionNoteDto)
		}
		else {
			this.markAsTouched = true;
			this.snackBarService.showError('La nota de evolución de guardia debe tener una evolución');
		}
	}

	private buildEmergencyCareEvolutionNoteDto(): EmergencyCareEvolutionNoteDto {
		const value = this.form.value;
		const allDiagnosis = this.getDiagnosis(value.diagnosis);
		const medications = toOutpatientMedicationDto(value.medications?.data);
		const anthropometricData = toOutpatientAnthropometricDataDto(value.anthropometricData);
		const familyHistories = toOutpatientFamilyHistoryDto(value.familyHistories?.data);
		const riskFactors = toOutpatientRiskFactorDto(value.riskFactors);
		return {
			clinicalSpecialtyId: value.clinicalSpecialty?.clinicalSpecialty.id,
			reasons: value.reasons?.motivo || [],
			diagnosis: allDiagnosis.diagnosis,
			mainDiagnosis: allDiagnosis.mainDiagnosis,
			evolutionNote: value.evolutionNote?.evolucion,
			anthropometricData,
			familyHistories: {
				isReferred: (this.isFamilyHistoriesNoRefer && (value.familyHistories?.data || []).length === 0) ? null: this.isFamilyHistoriesNoRefer,
				content: familyHistories,
			},
			procedures: value.procedures?.data.map(p => {return {...p, performedDate: p.performedDate ? toApiFormat(p.performedDate) : null}}) || [],
			medications,
			riskFactors,
			allergies: {
				isReferred: (this.isAllergyNoRefer && (value.allergies?.data || []).length === 0) ? null: this.isAllergyNoRefer,
				content: value.allergies?.data || []
			},
			patientId: this.data.patientId,
		}
	}

	private persist(emergencyCareEvolutionNoteDto: EmergencyCareEvolutionNoteDto) {
		const saveEmergencyCareEvolutionNote$ = this.data.editMode ? this.emergencyCareEvolutionNoteService.updateEmergencyCareEvolutionNote(this.data.episodeId, this.data.documentId, emergencyCareEvolutionNoteDto) : this.emergencyCareEvolutionNoteService.saveEmergencyCareEvolutionNote(this.data.episodeId, emergencyCareEvolutionNoteDto)
		saveEmergencyCareEvolutionNote$.subscribe(
			saved => {
				this.snackBarService.showSuccess('Nota de evolución guardada correctamente');
				this.newEmergencyCareEvolutionNoteService.newEvolutionNote();
				if (emergencyCareEvolutionNoteDto.riskFactors)
					this.newRiskFactorsService.newRiskFactors();
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

	private getDiagnosis(diagnosisFormValue): { diagnosis: DiagnosisDto[], mainDiagnosis: HealthConditionDto } {
		return {
			diagnosis: diagnosisFormValue?.otrosDiagnosticos?.filter(d => d.isAdded) || [],
			mainDiagnosis: diagnosisFormValue?.mainDiagnostico
		}
	}

	private setDiagnosis(mainDiagnosis: HealthConditionDto, otherDiagnoses: DiagnosisDto[]) {
		mainDiagnosis.isAdded = !!mainDiagnosis;
		this.form.controls.diagnosis.setValue({ mainDiagnostico: mainDiagnosis, otrosDiagnosticos: otherDiagnoses });
	}

	private setEvolutionNoteDataToEdit() {
		const evolutionNoteData = this.data.emergencyCareEvolutionNote;
		this.setDiagnosis(evolutionNoteData.mainDiagnosis, evolutionNoteData.diagnosis);
		this.evolutionNoteEditionService.loadFormByEvolutionNoteData(this.form, evolutionNoteData);
	}
}

export interface NotaDeEvolucionData {
	patientId: number,
	episodeId: number,
	editMode: boolean,
	emergencyCareEvolutionNote: EmergencyCareEvolutionNoteDto,
	documentId: number,
}
