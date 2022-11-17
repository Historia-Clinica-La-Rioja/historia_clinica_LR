import { Injectable } from '@angular/core';
import { AnamnesisDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/anamnesis-dock-popup/anamnesis-dock-popup.component";
import { InternmentFields } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { EvolutionNoteDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/evolution-note-dock-popup/evolution-note-dock-popup.component";
import { EpicrisisDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/epicrisis-dock-popup/epicrisis-dock-popup.component";
import { MedicalDischargeComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/medical-discharge/medical-discharge.component";
import { DockPopupRef } from "@presentation/services/dock-popup-ref";
import { DockPopupService } from "@presentation/services/dock-popup.service";
import { MatDialog } from "@angular/material/dialog";
import { DiagnosisDto, HealthConditionDto } from "@api-rest/api-model";
import { Subject } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class InternmentActionsService {

	patientId: number;
	internmentEpisodeId: number;
	dialogRef: DockPopupRef;
	mainDiagnosis: HealthConditionDto;
	diagnosticos: DiagnosisDto[];
	anamnesisSubject = new Subject<InternmentFields>();
	anamnesis$ = this.anamnesisSubject.asObservable();

	evolutionNoteSubject = new Subject<InternmentFields>();
	evolutionNote$ = this.evolutionNoteSubject.asObservable();

	epicrisisSubject = new Subject<InternmentFields>();
	epicrisis$ = this.epicrisisSubject.asObservable();

	medicalDischargeSubject = new Subject<InternmentFields>();
	medicalDischarge$ = this.medicalDischargeSubject.asObservable();

	constructor(
		private readonly dockPopupService: DockPopupService,
		private readonly dialog: MatDialog
	) { }

	setInternmentInformation(patientId: number, internmentEpisodeId: number) {
		this.patientId = patientId;
		this.internmentEpisodeId = internmentEpisodeId;
		delete this.mainDiagnosis;
		delete this.diagnosticos;
	}

	openAnamnesis(anamnesisId?: number) {
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
				this.anamnesisSubject.next(fieldsToUpdate);
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
				this.evolutionNoteSubject.next(fieldsToUpdate);
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

	openEpicrisis(id?: number, isDraft?: boolean): void {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(EpicrisisDockPopupComponent, {
				patientInfo: {
					patientId: this.patientId,
					internmentEpisodeId: this.internmentEpisodeId,
					epicrisisId: id,
					isDraft: !!isDraft
				},
				autoFocus: false,
				disableClose: true,
			});
			this.dialogRef.afterClosed().subscribe((epicrisisClose: EpicrisisClose) => {
				delete this.dialogRef;
				this.epicrisisSubject.next(epicrisisClose?.fieldsToUpdate);
				if (epicrisisClose?.openMedicalDischarge)
					this.openMedicalDischarge();
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
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
			if (medicalDischarge)
				this.medicalDischargeSubject.next(medicalDischarge);
		});
	}
}

export interface EpicrisisClose { fieldsToUpdate: InternmentFields, openMedicalDischarge: boolean };
