import { Injectable } from '@angular/core';
import { AnamnesisDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/anamnesis-dock-popup/anamnesis-dock-popup.component";
import {
	InternmentFields,
	InternmentSummaryFacadeService
} from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { EvolutionNoteDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/evolution-note-dock-popup/evolution-note-dock-popup.component";
import { EpicrisisDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/epicrisis-dock-popup/epicrisis-dock-popup.component";
import { MedicalDischargeComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/medical-discharge/medical-discharge.component";
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { DockPopupService } from "@presentation/services/dock-popup.service";
import { MatDialog } from "@angular/material/dialog";
import { PatientAllergiesService } from "@historia-clinica/modules/ambulatoria/services/patient-allergies.service";
import { DiagnosisDto, HealthConditionDto } from "@api-rest/api-model";

@Injectable({
	providedIn: 'root'
})
export class InternmentActionsService {

	patientId: number;
	internmentEpisodeId: number;
	dialogRef: DockPopupRef;
	mainDiagnosis: HealthConditionDto;
	diagnosticos: DiagnosisDto[];

	constructor(
		private readonly dockPopupService: DockPopupService,
		private readonly dialog: MatDialog,
		private readonly patientAllergies: PatientAllergiesService,
		readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
	) { }

	setInternmentInformation(patientId: number, internmentEpisodeId: number) {
		this.patientId = patientId;
		this.internmentEpisodeId = internmentEpisodeId;
		delete this.mainDiagnosis;
		delete this.diagnosticos;
	}

	openAnamnesis(anamnesisId?: number): void {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(AnamnesisDockPopupComponent, {
				patientInfo: {
					patientId: this.patientId,
					internmentEpisodeId: this.internmentEpisodeId,
					anamnesisId: anamnesisId
				},
				autoFocus: false,
				disableClose: true,
				mainDiagnosis: this.mainDiagnosis,
				diagnosticos: this.diagnosticos
			});
			this.dialogRef.afterClosed().subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				if (fieldsToUpdate)
					this.updateInternmentSummary(fieldsToUpdate);
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

	openEvolutionNote(documentId?: number, documentType?: string): void {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(EvolutionNoteDockPopupComponent, {
				internmentEpisodeId: this.internmentEpisodeId,
				autoFocus: false,
				disableClose: true,
				mainDiagnosis: this.mainDiagnosis,
				diagnosticos: this.diagnosticos,
				evolutionNoteId: documentId,
				documentType: documentType
			});
			this.dialogRef.afterClosed().subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				if (fieldsToUpdate)
					this.updateInternmentSummary(fieldsToUpdate);
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

	openEpicrisis(id?: number): void {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(EpicrisisDockPopupComponent, {
				patientInfo: {
					patientId: this.patientId,
					internmentEpisodeId: this.internmentEpisodeId,
					epicrisisId: id,
				},
				autoFocus: false,
				disableClose: true,
			});
			this.dialogRef.afterClosed().subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				if (fieldsToUpdate)
					this.updateInternmentSummary(fieldsToUpdate);
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

	updateInternmentSummary(fieldsToUpdate: InternmentFields): void {
		const fields = {
			personalHistories: fieldsToUpdate?.personalHistories,
			riskFactors: fieldsToUpdate?.riskFactors,
			medications: fieldsToUpdate?.medications,
			heightAndWeight: fieldsToUpdate?.heightAndWeight,
			bloodType: fieldsToUpdate?.bloodType,
			immunizations: fieldsToUpdate?.immunizations,
			mainDiagnosis: fieldsToUpdate?.mainDiagnosis,
			diagnosis: fieldsToUpdate?.diagnosis,
			evolutionClinical: fieldsToUpdate?.evolutionClinical
		}
		this.internmentSummaryFacadeService.setFieldsToUpdate(fields);
		if (fieldsToUpdate?.familyHistories)
			this.internmentSummaryFacadeService.unifyFamilyHistories(this.patientId);
		if (fieldsToUpdate?.allergies) {
			this.patientAllergies.updateCriticalAllergies(this.patientId);
			this.internmentSummaryFacadeService.unifyAllergies(this.patientId);
		}
		this.internmentSummaryFacadeService.updateInternmentEpisode();
	}

	openMedicalDischarge(): void {
		const dialogRef = this.dialog.open(MedicalDischargeComponent, {
			data: {
				patientId: this.patientId,
				internmentEpisodeId: this.internmentEpisodeId,
			},
			autoFocus: false,
			disableClose: true,
		});
		dialogRef.afterClosed().subscribe(medicalDischarge => {
			if (medicalDischarge) {
				this.internmentSummaryFacadeService.updateInternmentEpisode();
			}
		});
	}
}
