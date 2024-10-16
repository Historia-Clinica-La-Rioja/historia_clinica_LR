import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { EEmergencyCareEvolutionNoteType, EmergencyCareEvolutionNoteDto, HealthConditionDto } from '@api-rest/api-model';
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

@Component({
	selector: 'app-evolution-note-dock-popup-by-nurse',
	templateUrl: './evolution-note-dock-popup-by-nurse.component.html',
	styleUrls: ['./evolution-note-dock-popup-by-nurse.component.scss'],
	providers: [EvolutionNoteEditionService]
})
export class EvolutionNoteDockPopupByNurseComponent implements OnInit {

	readonly header: DockPopUpHeader = { title: 'guardia.actions.EVOLUTION_NOTE_BY_NURSE' };
	disableConfirmButton = false;
	isAllergyNoRefer = true;
	isFamilyHistoriesNoRefer = true;
	markAsTouched = false;
	form: FormGroup;

	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly emergencyCareEvolutionNoteService: EmergencyCareEvolutionNoteService,
		private readonly newEmergencyCareEvolutionNoteService: NewEmergencyCareEvolutionNoteService,
		private readonly newRiskFactorsService: NewRiskFactorsService,
		private readonly evolutionNoteEditionService: EvolutionNoteEditionService,
		readonly dockPopupRef: DockPopupRef,
		@Inject(OVERLAY_DATA) readonly data: NotaDeEvolucionData,
	) { }

	ngOnInit(): void {
		this.buildForm();
		if (this.data.editMode) {
			this.setEvolutionNoteDataToEdit();
			return;
		}
		this.setDefaultValue(NURSING_DIAGNOSIS);
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
		const {episodeId, documentId } = this.data;
		const saveDocument$ = this.data.editMode ? this.emergencyCareEvolutionNoteService.updateEmergencyCareEvolutionNote(episodeId, documentId, emergencyCareEvolutionNoteDto) : this.emergencyCareEvolutionNoteService.saveEmergencyCareEvolutionNote(this.data.episodeId, emergencyCareEvolutionNoteDto);
		saveDocument$.subscribe({
			next: saved => {
				this.snackBarService.showSuccess('guardia.nursing_evolution_note.SAVE_SUCCESS');
				this.newEmergencyCareEvolutionNoteService.newEvolutionNote();
				if (emergencyCareEvolutionNoteDto.riskFactors)
					this.newRiskFactorsService.newRiskFactors();
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
			allergies: new FormControl(null)
		});
	}

	private setDefaultValue(mainDiagnosis: HealthConditionDto) {
		this.form.controls.diagnosis.setValue({ mainDiagnostico: mainDiagnosis });
	}

	private setEvolutionNoteDataToEdit() {
		const evolutionNoteData = this.data.emergencyCareEvolutionNote;
		this.setDefaultValue(evolutionNoteData.mainDiagnosis);
		this.evolutionNoteEditionService.loadFormByEvolutionNoteData(this.form, evolutionNoteData);
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
