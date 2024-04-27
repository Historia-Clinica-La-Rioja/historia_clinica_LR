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
import { mapMedications, mapAnthropometricData, mapFamilyHistories, toRiskFactors } from '@historia-clinica/mappers/emergency-care-evolution-note.mapper';

@Component({
	selector: 'app-nota-de-evolucion-dock-popup',
	templateUrl: './nota-de-evolucion-dock-popup.component.html',
	styleUrls: ['./nota-de-evolucion-dock-popup.component.scss']
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
	) { }

	ngOnInit(): void {
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
		this.disableConfirmButton = true;
		const emergencyCareEvolutionNoteDto = this.buildEmergencyCareEvolutionNoteDto();
		this.persist(emergencyCareEvolutionNoteDto)
	}

	private buildEmergencyCareEvolutionNoteDto(): EmergencyCareEvolutionNoteDto {
		const value = this.form.value;
		const allDiagnosis = this.getDiagnosis(value.diagnosis);
		const medications = mapMedications(value.medications?.data);
		const anthropometricData = mapAnthropometricData(value.anthropometricData);
		const familyHistories = mapFamilyHistories(value.familyHistories?.data);
		const riskFactors = toRiskFactors(value.riskFactors);

		return {
			clinicalSpecialtyId: value.clinicalSpecialty?.clinicalSpecialty.id,
			reasons: value.reasons?.motivo || [],
			diagnosis: allDiagnosis.diagnosis,
			mainDiagnosis: allDiagnosis.mainDiagnosis,
			evolutionNote: value.evolutionNote?.evolucion,
			anthropometricData,
			familyHistories,
			procedures: value.procedures?.data || [],
			medications,
			riskFactors,
			allergies: value.allergies?.data || [],
			patientId: this.data.patientId,
		}
	}

	private persist(emergencyCareEvolutionNoteDto: EmergencyCareEvolutionNoteDto) {
		this.emergencyCareEvolutionNoteService.saveEmergencyCareEvolutionNote(this.data.episodeId, emergencyCareEvolutionNoteDto).subscribe(
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
}

export interface NotaDeEvolucionData {
	patientId: number,
	episodeId: number,
}
