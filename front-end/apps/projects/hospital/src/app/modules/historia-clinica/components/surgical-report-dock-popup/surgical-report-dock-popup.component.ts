import { Component, Inject, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosisDto, EProfessionType, ProcedureTypeEnum, ProfessionalDto, SurgicalReportDto } from '@api-rest/api-model';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { SurgicalReportService } from '@api-rest/services/surgical-report.service';
import { DocumentActionReasonComponent } from '@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/document-action-reason/document-action-reason.component';
import { InternmentFields } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-surgical-report-dock-popup',
	templateUrl: './surgical-report-dock-popup.component.html',
	styleUrls: ['./surgical-report-dock-popup.component.scss'],
})
export class SurgicalReportDockPopupComponent implements OnInit {

	diagnosis: DiagnosisDto[];
	professionals: ProfessionalDto[];
	isLoading = false;
	loadingReport = false;
	disabled = true;
	validDate = false;
	validProsthesis = true;

	surgicalReport: SurgicalReportDto = {
		confirmed: undefined,
		anesthesia: [],
		cultures: [],
		description: undefined,
		drainages: [],
		endDateTime: {
			date: {
				year: undefined,
				month: undefined,
				day: undefined
			},
			time: {
				hours: undefined,
				minutes: undefined
			}
		},
		frozenSectionBiopsies: [],
		healthcareProfessionals: [],
		modificationReason: undefined,
		postoperativeDiagnosis: [],
		preoperativeDiagnosis: [],
		procedures: [],
		prosthesisDescription: undefined,
		startDateTime: {
			date: {
				year: undefined,
				month: undefined,
				day: undefined
			},
			time: {
				hours: undefined,
				minutes: undefined
			}
		},
		surgeryProcedures: []
	};

	PATHOLOGIST = EProfessionType.PATHOLOGIST;
	TRANSFUSIONIST = EProfessionType.TRANSFUSIONIST;
	DRAINAGE = ProcedureTypeEnum.DRAINAGE;
	CULTURE = ProcedureTypeEnum.CULTURE;
	FROZEN_SECTION_BIOPSY = ProcedureTypeEnum.FROZEN_SECTION_BIOPSY;

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly internmentStateService: InternmentStateService,
		private readonly healthcareProfessionalService: HealthcareProfessionalService,
		private readonly surgicalReportService: SurgicalReportService,
		private readonly snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
	) {
		this.internmentStateService.getDiagnosesGeneralState(this.data.internmentEpisodeId).subscribe(response => {
			if (response)
				this.diagnosis = response;
		});

		this.healthcareProfessionalService.getAllProfessionalsAndTechnicians().subscribe(response => {
			if (response)
				this.professionals = response;
		});

		if (this.data.surgicalReportId) {
			this.loadingReport = true;
			this.surgicalReportService.getSurgicalReport(this.data.internmentEpisodeId, this.data.surgicalReportId).subscribe(response => {
				this.surgicalReport = response;
				this.loadingReport = false;
				if (this.surgicalReport.startDateTime && this.surgicalReport.endDateTime)
					this.disabled = false;
			})
		}
	}

	ngOnInit(): void {
	}

	setDisabled(): void {
		this.disabled = !this.validDate || !this.validProsthesis;
	}

	setValidProsthesis(event: boolean): void {
		this.validProsthesis = event;
		this.setDisabled();
	}

	setValidDate(event: boolean): void {
		this.validDate = event;
		this.setDisabled();
	}

	save(): void {
		this.surgicalReport.confirmed = true;
		this.isLoading = true;
		if (this.data.surgicalReportId) {
			this.openEditReason();
			this.isLoading = false;
			return;
		}

		this.surgicalReportService.saveSurgicalReport(this.data.internmentEpisodeId, this.surgicalReport).subscribe(
			saved => {
				this.snackBarService.showSuccess('Parte quirúrgico generado correctamente');
				this.dockPopupRef.close(this.setFieldsToUpdate());
			},
			error => {
				this.snackBarService.showError(error.text)
			}
		);

	}

	private openEditReason() {
		const dialogRef = this.dialog.open(DocumentActionReasonComponent, {
			data: {
				title: 'internaciones.dialogs.actions-document.EDIT_TITLE',
				subtitle: 'internaciones.dialogs.actions-document.SUBTITLE',
			},
			width: "50vh",
			autoFocus: false,
			disableClose: true
		});
		dialogRef.afterClosed().subscribe(reason => {
			if (reason) {
				this.surgicalReport.modificationReason = reason;
				this.isLoading = true;
				this.surgicalReportService.editSurgicalReport(this.data.internmentEpisodeId, this.data.surgicalReportId, this.surgicalReport).subscribe(
					success => {
						this.snackBarService.showSuccess('Parte quirúrgico editado correctamente');
						this.dockPopupRef.close(this.setFieldsToUpdate());
					},
					error => {
						this.snackBarService.showError(error.text)
					});
			}
		});
	}

	formControlChange(event, formControl: FormControl): void {
		formControl.setValue(event);
	}

	private setFieldsToUpdate(): InternmentFields {
		return {
			diagnosis: !!this.surgicalReport.preoperativeDiagnosis || !!this.surgicalReport.postoperativeDiagnosis,
			evolutionClinical: !!this.surgicalReport.confirmed
		}
	}

}