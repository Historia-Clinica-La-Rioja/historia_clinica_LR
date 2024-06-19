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
import { Subject, take } from 'rxjs';
import { SurgicalReportDockPopupComponent } from '@historia-clinica/components/surgical-report-dock-popup/surgical-report-dock-popup.component';
import { AnestheticReportDockPopupComponent } from '@historia-clinica/components/anesthetic-report-dock-popup/anesthetic-report-dock-popup.component';

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

	surgicalReportSubject = new Subject<InternmentFields>();
	surgicalReport$ = this.surgicalReportSubject.asObservable();

	anestheticReportSubject = new Subject<InternmentFields>();
	anestheticReport$ = this.anestheticReportSubject.asObservable();

	popUpOpenSubject = new Subject<boolean>();
	popUpOpen$ = this.popUpOpenSubject.asObservable();

	dialogRefSubject = new Subject<DockPopupRef>();
	dialogRef$ = this.dialogRefSubject.asObservable();

	constructor(
		private readonly dockPopupService: DockPopupService,
		private readonly dialog: MatDialog
	) { }

	setInternmentInformation(patientId: number, internmentEpisodeId: number) {
		this.patientId = patientId;
		this.internmentEpisodeId = internmentEpisodeId;
		this.popUpOpenSubject.next(false)
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
			this.popUpOpenSubject.next(true);
			this.dialogRefSubject.next(this.dialogRef);
			this.dialogRef.afterClosed().pipe(take(1)).subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				this.anamnesisSubject.next(fieldsToUpdate);
				this.popUpOpenSubject.next(false);
				this.dialogRefSubject.next(this.dialogRef);
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
				documentType: documentType,
				patientId: this.patientId
			});
			this.popUpOpenSubject.next(true);
			this.dialogRefSubject.next(this.dialogRef);
			this.dialogRef.afterClosed().pipe(take(1)).subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				this.evolutionNoteSubject.next(fieldsToUpdate);
				this.popUpOpenSubject.next(false);
				this.dialogRefSubject.next(this.dialogRef);
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
			this.popUpOpenSubject.next(true);
			this.dialogRefSubject.next(this.dialogRef);
			this.dialogRef.afterClosed().pipe(take(1)).subscribe((epicrisisClose: EpicrisisClose) => {
				delete this.dialogRef;
				this.epicrisisSubject.next(epicrisisClose?.fieldsToUpdate);
				if (epicrisisClose?.openMedicalDischarge)
					this.openMedicalDischarge();
				this.popUpOpenSubject.next(false);
				this.dialogRefSubject.next(this.dialogRef);
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
		this.popUpOpenSubject.next(true);
		dialogRef.afterClosed().subscribe(medicalDischarge => {
			if (medicalDischarge)
				this.medicalDischargeSubject.next(medicalDischarge);
			this.popUpOpenSubject.next(false);
		});
	}

	openAnestheticReport(id?: number, isDraft?: boolean) {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(AnestheticReportDockPopupComponent, {
				autoFocus: false,
				disableClose: true,
				patientId: this.patientId,
				internmentEpisodeId: this.internmentEpisodeId,
				mainDiagnosis: this.mainDiagnosis,
				anestheticPartId: id,
				isDraft: !!isDraft
			});
			this.popUpOpenSubject.next(true);
			this.dialogRefSubject.next(this.dialogRef);
			this.dialogRef.afterClosed().pipe(take(1)).subscribe((fieldsToUpdate) => {
				delete this.dialogRef;
				this.popUpOpenSubject.next(false);
				this.anestheticReportSubject.next(fieldsToUpdate);
				this.dialogRefSubject.next(this.dialogRef);
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

	openSurgicalReport(id?: number): void {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(SurgicalReportDockPopupComponent, {
				patientId: this.patientId,
				internmentEpisodeId: this.internmentEpisodeId,
				surgicalReportId: id,
				autoFocus: false,
				disableClose: true,
				mainDiagnosis: this.mainDiagnosis,
				diagnosis: this.diagnosticos,
			});
			this.popUpOpenSubject.next(true);
			this.dialogRefSubject.next(this.dialogRef);
			this.dialogRef.afterClosed().pipe(take(1)).subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				this.surgicalReportSubject.next(fieldsToUpdate);
				this.popUpOpenSubject.next(false);
				this.dialogRefSubject.next(this.dialogRef);
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}
}

export interface EpicrisisClose { fieldsToUpdate: InternmentFields, openMedicalDischarge: boolean };
