import { Component, Inject } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { DiagnosisDto, EmergencyCareEvolutionNoteDto, HealthConditionDto, OutpatientAnthropometricDataDto, OutpatientMedicationDto } from '@api-rest/api-model';
import { EmergencyCareEvolutionNoteService } from '@api-rest/services/emergency-care-evolution-note.service';
import { EmergencyCareStateService } from '@api-rest/services/emergency-care-state.service';
import { DockPopUpHeader } from '@presentation/components/dock-popup/dock-popup.component';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';

@Component({
	selector: 'app-nota-de-evolucion-dock-popup',
	templateUrl: './nota-de-evolucion-dock-popup.component.html',
	styleUrls: ['./nota-de-evolucion-dock-popup.component.scss']
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
		private readonly emergencyCareStateService: EmergencyCareStateService
	) {
		this.emergencyCareStateService.getEmergencyCareEpisodeDiagnoses(this.data.episodeId).subscribe(
			diagnoses => {
				if (diagnoses.length) {
					this.diagnosis = {
						mainDiagnosis: diagnoses.find(d => d.main) ,
						diagnosticos: diagnoses.filter(d => !d.main) || [],
					}
				}
			}
		)
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
			familyHistories: value.familyHistories?.data || [],
			procedures: value.procedures?.data || [],
			medications,
			riskFactors: value.riskFactors,
			allergies: value.allergies?.data || [],
			patientId: this.data.patientId,
		}
		this.dockPopupRef.close(dto);
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
			diagnosis: diagnosisFormValue?.otrosDiagnosticos || [],
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

