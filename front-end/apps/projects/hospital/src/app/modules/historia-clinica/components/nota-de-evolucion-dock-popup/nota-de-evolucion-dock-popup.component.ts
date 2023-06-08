import { ChangeDetectorRef, Component, Inject } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { DiagnosisDto, EmergencyCareEvolutionNoteDto, HealthConditionDto, OutpatientAnthropometricDataDto, OutpatientFamilyHistoryDto, OutpatientMedicationDto, OutpatientRiskFactorDto } from '@api-rest/api-model';
import { EmergencyCareEvolutionNoteService } from '@api-rest/services/emergency-care-evolution-note.service';
import { EmergencyCareStateService } from '@api-rest/services/emergency-care-state.service';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { NewEmergencyCareEvolutionNoteService } from '@historia-clinica/modules/guardia/services/new-emergency-care-evolution-note.service';
import { DockPopUpHeader } from '@presentation/components/dock-popup/dock-popup.component';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-nota-de-evolucion-dock-popup',
	templateUrl: './nota-de-evolucion-dock-popup.component.html',
	styleUrls: ['./nota-de-evolucion-dock-popup.component.scss'],
	providers: [
		ComponentEvaluationManagerService,
	]
})

export class NotaDeEvolucionDockPopupComponent {

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

	disableConfirmButton = false;
	diagnosis;
	constructor(
		public dockPopupRef: DockPopupRef,
		private formBuilder: FormBuilder,
		@Inject(OVERLAY_DATA) public data: { patientId: number, episodeId: number },
		private readonly emergencyCareStateService: EmergencyCareStateService,
		private readonly emergencyCareEvolutionNoteService: EmergencyCareEvolutionNoteService,
		private readonly snackBarService: SnackBarService,
		private readonly newEmergencyCareEvolutionNoteService: NewEmergencyCareEvolutionNoteService,
		readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
		private changeDetectorRef: ChangeDetectorRef
	) {
		this.emergencyCareStateService.getEmergencyCareEpisodeDiagnoses(this.data.episodeId).subscribe(
			diagnoses => {
				if (diagnoses.length) {
					this.diagnosis = {
						mainDiagnosis: diagnoses.find(d => d.main),
						diagnosticos: diagnoses.filter(d => !d.main) || [],
					}
					this.componentEvaluationManagerService.mainDiagnosis = this.diagnosis.mainDiagnosis;
					this.componentEvaluationManagerService.diagnosis = this.diagnosis.diagnosticos;
				}
			})
	}

	ngAfterViewInit() {
		this.changeDetectorRef.detectChanges();
	}

	save() {
		const value = this.form.value;
		const allDiagnosis = this.getDiagnosis(value.diagnosis);
		const medications = this.mapMedications(value.medications?.data);
		const anthropometricData = this.mapAnthropometricData(value.anthropometricData);
		const dto: EmergencyCareEvolutionNoteDto = {
			clinicalSpecialtyId: value.clinicalSpecialty?.clinicalSpecialty.id,
			reasons: value.reasons?.motivo || [],
			diagnosis: allDiagnosis.diagnosis,
			mainDiagnosis: allDiagnosis.mainDiagnosis,
			evolutionNote: value.evolutionNote?.evolucion,
			anthropometricData,
			familyHistories: this.mapFamilyHistories(value.familyHistories?.data),
			procedures: value.procedures?.data || [],
			medications,
			riskFactors: this.toRiskFactors(value.riskFactors),
			allergies: value.allergies?.data || [],
			patientId: this.data.patientId,
		}
		this.persist(dto)
	}

	persist(dto) {
		this.emergencyCareEvolutionNoteService.saveEmergencyCareEvolutionNote(this.data.episodeId, dto).subscribe(
			saved => {
				this.snackBarService.showSuccess('Nota de evolucion guardada correctamente');
				this.newEmergencyCareEvolutionNoteService.newEvolutionNote();
				this.dockPopupRef.close(true)
			},
			error => {
				this.snackBarService.showError(error.text)
			}
		);
	}

	private toRiskFactors(riskFactors: OutpatientRiskFactorDto): OutpatientRiskFactorDto {
		if (riskFactors) {
			let result;
			Object.keys(riskFactors).forEach(
				key => {
					if (riskFactors[key]?.value) {
						result = { ...result, [key]: riskFactors[key] }
					}
				}
			)
			return result;
		}
		return null;
	}

	private mapFamilyHistories(familyHistories: any[]): OutpatientFamilyHistoryDto[] {
		return familyHistories?.map(f => {
			return {
				snomed: f.snomed,
				startDate: f.fecha ? momentFormat(f.fecha, DateFormat.API_DATE) : null
			}
		}) || []
	}

	private mapAnthropometricData(anthropometricData): OutpatientAnthropometricDataDto {
		if (anthropometricData) {
			return {
				height: {
					value: anthropometricData.height?.toString()
				},
				weight: {
					value: anthropometricData.weight?.toString()
				},
				bloodType: anthropometricData.bloodType,
				headCircumference: {
					value: anthropometricData.headCircumference?.toString()
				},
				bmi: {
					value: null
				},
			}
		}
		return null;
	}

	private getDiagnosis(diagnosisFormValue): { diagnosis: DiagnosisDto[], mainDiagnosis: HealthConditionDto } {
		return {
			diagnosis: diagnosisFormValue?.otrosDiagnosticos.filter(d => d.isAdded) || [],
			mainDiagnosis: diagnosisFormValue?.mainDiagnostico
		}
	}

	private mapMedications(formData: any[]): OutpatientMedicationDto[] {
		return formData?.map(r => {
			return {
				note: r.observaciones,
				snomed: r.snomed,
				suspended: r.suspendido
			}
		}) || []
	}

}

