import { Component, Inject, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, DiagnosisDto, EProfessionType, HealthConditionDto, ProcedureTypeEnum, ProfessionalDto, SurgicalReportDto } from '@api-rest/api-model';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { SurgicalReportService } from '@api-rest/services/surgical-report.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { DocumentActionReasonComponent } from '@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/document-action-reason/document-action-reason.component';
import { InternmentFields } from '@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service';
import { ComponentEvaluationManagerService } from '@historia-clinica/modules/ambulatoria/services/component-evaluation-manager.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-surgical-report-dock-popup',
	templateUrl: './surgical-report-dock-popup.component.html',
	styleUrls: ['./surgical-report-dock-popup.component.scss'],
	providers: [ComponentEvaluationManagerService]
})
export class SurgicalReportDockPopupComponent implements OnInit{
	searchConceptsLocallyFF: boolean;
	mainDiagnosis: HealthConditionDto;
	diagnosis: DiagnosisDto[];
	professionals: ProfessionalDto[];
	isLoading = false;
	loadingReport = false;
	disabled = true;
	markAsTouched = false;
	validDate = false;
	validSurgicalTeam = false;
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
		modificationReason: undefined,
		mainDiagnosis: undefined,
		pathologist: undefined,
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
		surgeryProcedures: [],
		surgicalTeam: [],
		transfusionist: undefined,
	};

	PATHOLOGIST = EProfessionType.PATHOLOGIST;
	TRANSFUSIONIST = EProfessionType.TRANSFUSIONIST;
	DRAINAGE = ProcedureTypeEnum.DRAINAGE;
	CULTURE = ProcedureTypeEnum.CULTURE;
	FROZEN_SECTION_BIOPSY = ProcedureTypeEnum.FROZEN_SECTION_BIOPSY;

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly healthcareProfessionalService: HealthcareProfessionalService,
		private readonly surgicalReportService: SurgicalReportService,
		private readonly snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
		private readonly componentEvaluationManagerService: ComponentEvaluationManagerService,
		private readonly featureFlagService: FeatureFlagService,
	) {
		this.componentEvaluationManagerService.mainDiagnosis = this.mainDiagnosis;
		this.componentEvaluationManagerService.diagnosis = this.diagnosis;
		this.diagnosis = data.diagnosis;
		this.mainDiagnosis = data.mainDiagnosis;
		this.healthcareProfessionalService.getAllProfessionalsAndTechniciansByInstitution().subscribe(response => {
			if (response)
				this.professionals = response;
		});

		if (this.data.surgicalReportId) {
			this.loadingReport = true;
			this.surgicalReportService.getSurgicalReport(this.data.internmentEpisodeId, this.data.surgicalReportId).subscribe(response => {
				this.surgicalReport = response;
				this.diagnosis = response?.preoperativeDiagnosis;
				this.mainDiagnosis = response?.mainDiagnosis;
				this.loadingReport = false;
				if (this.surgicalReport.startDateTime && this.surgicalReport.endDateTime)
					this.disabled = false;
			})
		}
	}

	ngOnInit(): void {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFF = isOn;
		})
	}

	setDisabled(): void {
		this.disabled = !this.validDate || !this.validProsthesis || this.validSurgicalTeam === false;
	}

	setValidProsthesis(event: boolean): void {
		this.validProsthesis = event;
		this.setDisabled();
	}

	setValidDate(event: boolean): void {
		this.validDate = event;
		this.setDisabled();
	}

	setValidSurgicalTeam(event: boolean): void {
		this.validSurgicalTeam = event;
		this.setDisabled();
	}

	save(): void {
		if (!this.disabled) {
			this.surgicalReport.confirmed = true;
			this.isLoading = true;
			this.surgicalReport.preoperativeDiagnosis = this.filterCheckedDiagnosis(this.surgicalReport.preoperativeDiagnosis);
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
					this.snackBarService.showError("Error al generar parte quirúrgico. " + error.errors[0])
					this.isLoading = false;
					this.dockPopupRef.close();
				}
			);
		}
		else {
			this.markAsTouched = true;
			this.snackBarService.showError('Faltan completar campos en el formulario');
		}
	}

	private filterCheckedDiagnosis(diagnosis: DiagnosisDto[]): DiagnosisDto[] {
		return diagnosis.filter(d => d.isAdded);
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
