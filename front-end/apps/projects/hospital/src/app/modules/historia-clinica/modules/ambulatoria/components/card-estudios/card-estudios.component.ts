import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosticReportInfoDto, HCEPersonalHistoryDto } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { ESTUDIOS } from '@historia-clinica/constants/summaries';
import { STUDY_STATUS } from '../../constants/prescripciones-masterdata';
import { ConfirmarPrescripcionComponent } from '../../dialogs/ordenes-prescripciones/confirmar-prescripcion/confirmar-prescripcion.component';
import { StudyCategories } from '../../modules/estudio/constants/internment-studies';
import { TranslateService } from '@ngx-translate/core';
import { anyMatch, pushIfNotExists } from '@core/utils/array.utils';
import { PrescripcionesService, PrescriptionTypes } from '../../services/prescripciones.service';
import { CreateInternmentOrderComponent, NewInternmentOrder } from "@historia-clinica/modules/ambulatoria/dialogs/create-internment-order/create-internment-order.component";
import { InternmentPatientService } from "@api-rest/services/internment-patient.service";
import { OperationDeniedComponent } from "@historia-clinica/modules/ambulatoria/dialogs/diagnosis-required/operation-denied.component";
import { InternmentStateService } from "@api-rest/services/internment-state.service";
import { CreateOutpatientOrderComponent, NewOutpatientOrder } from "@historia-clinica/modules/ambulatoria/dialogs/create-outpatient-order/create-outpatient-order.component";
import { HceGeneralStateService } from "@api-rest/services/hce-general-state.service";
import { PermissionsService } from "@core/services/permissions.service";

@Component({
	selector: 'app-card-estudios',
	templateUrl: './card-estudios.component.html',
	styleUrls: ['./card-estudios.component.scss']
})
export class CardEstudiosComponent implements OnInit {

	public readonly estudios = ESTUDIOS;
	public readonly STUDY_STATUS = STUDY_STATUS;
	imageDiagnotics: DiagnosticReportInfoDto[] = [];
	laboratoryDiagnotics: DiagnosticReportInfoDto[] = [];
	pathologicAnatomyDiagnotics: DiagnosticReportInfoDto[] = [];
	hemotherapyDiagnotics: DiagnosticReportInfoDto[] = [];
	surgicalProceduresDiagnotics: DiagnosticReportInfoDto[] = [];
	otherProceduresAndPracticesDiagnotics: DiagnosticReportInfoDto[] = [];
	adviceDiagnotics: DiagnosticReportInfoDto[] = [];
	educationDiagnotics: DiagnosticReportInfoDto[] = [];
	public hideFilterPanel = false;
	public formFilter: FormGroup;
	private internmentEpisodeInProgressId;
	hasHealthRelatedRole = false;
	hasPicturesStaffRole = false;
	hasLaboratoryStaffRole = false;
	hasPharmacyStaffRole = false;

	@Input() patientId: number;
	
	@Input() epicrisisConfirmed: boolean;

	@Input()
	set categories(categories: any[]) {
		this._categories = categories;
	}
	get categories(): any[] {
		return this._categories;
	}
	private _categories: any[];

	@Input()
	set diagnosticReportsStatus(diagnosticReportsStatus: any[]) {
		this._diagnosticReportsStatus = diagnosticReportsStatus;
	}
	get diagnosticReportsStatus(): any[] {
		return this._diagnosticReportsStatus;
	}
	private _diagnosticReportsStatus: any[];

	constructor(
		private readonly dialog: MatDialog,
		private readonly requestMasterDataService: RequestMasterDataService,
		private prescripcionesService: PrescripcionesService,
		private readonly formBuilder: FormBuilder,
		private readonly internmentPatientService: InternmentPatientService,
		private readonly internmentStateService: InternmentStateService,
		private readonly translateService: TranslateService,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly permissionsService: PermissionsService,
	) { }

	ngOnInit(): void {
		this.formFilter = this.formBuilder.group({
			categoryId: [null],
			statusId: [null],
			healthCondition: [null],
			study: [null],
		});

		this.getStudy();

		this.setActionsLayout();

		this.requestMasterDataService.categories().subscribe(categories => {
			this.categories = categories;
		});

		this.internmentPatientService.internmentEpisodeIdInProcess(this.patientId).subscribe(internmentEpisodeInProgress => {
			this.internmentEpisodeInProgressId = internmentEpisodeInProgress.id;
		})
	}

	setActionsLayout(): void {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.hasPicturesStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_IMAGENES]);
			this.hasLaboratoryStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_LABORATORIO]);
			this.hasPharmacyStaffRole = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_FARMACIA]);
			this.hasHealthRelatedRole = anyMatch<ERole>(userRoles, [ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ENFERMERO_ADULTO_MAYOR]);
		});
	}

	private getStudy(): void {
		const value = this.formFilter.value;
		this.clearLoadedReports();
		this.prescripcionesService.getPrescription(PrescriptionTypes.STUDY, this.patientId, value.statusId, null, value.healthCondition, value.study, value.categoryId)
			.subscribe((response: DiagnosticReportInfoDto[]) => {
				response.forEach(report => {
					report.creationDate = new Date(report.creationDate);
					this.classifyReport(report);
				});
			});
	}

	isNewInternmentOrderEnabled(): boolean {
		return this.internmentEpisodeInProgressId && !this.epicrisisConfirmed;
	}

	openNewInternmentOrderDialog() {
		this.internmentStateService.getDiagnosesGeneralState(this.internmentEpisodeInProgressId).subscribe(diagnoses => {
			if (diagnoses.length) {
				this.openCreateInternmentOrderDialog();
			}
			else {
				this.dialog.open(OperationDeniedComponent, {
					width: '35%',
					data: { message : 'ambulatoria.paciente.internment-order.diagnosis-required-dialog.MESSAGE' }
				});
			}
		})
	}

	openCreateInternmentOrderDialog() {
		const newOrderComponent = this.dialog.open(CreateInternmentOrderComponent,
			{
				width: '28%',
				data: { internmentEpisodeId: this.internmentEpisodeInProgressId, patientId: this.patientId },
			})

		newOrderComponent.afterClosed().subscribe((newInternmentOrder: NewInternmentOrder) => {
			if (newInternmentOrder) {
				this.openNewStudyConfirmationDialog(newInternmentOrder);
			}
		});
	}

	openNewOutpatientOrderDialog() {
		this.hceGeneralStateService.getActiveProblems(this.patientId).subscribe((activeProblems: HCEPersonalHistoryDto[]) => {
			const activeProblemsList = activeProblems.map(problem => ({id: problem.id, description: problem.snomed.pt, sctId: problem.snomed.sctid}));

			this.hceGeneralStateService.getChronicConditions(this.patientId).subscribe((chronicProblems: HCEPersonalHistoryDto[]) => {
				const chronicProblemsList = chronicProblems.map(problem => ({id: problem.id, description: problem.snomed.pt,  sctId: problem.snomed.sctid}));
				const healthProblems = activeProblemsList.concat(chronicProblemsList);

				if (healthProblems.length) {
					this.openCreateOutpatientOrderDialog(healthProblems);
				}
				else {
					this.dialog.open(OperationDeniedComponent, {
						width: '35%',
						data: { message : 'ambulatoria.paciente.outpatient-order.problem-required-dialog.MESSAGE' }
					});
				}

			});

		});

	}

	openCreateOutpatientOrderDialog(healthProblems) {
		const newOrderComponent = this.dialog.open(CreateOutpatientOrderComponent,
			{
				width: '28%',
				data: { patientId: this.patientId, healthProblems },
			});

		newOrderComponent.afterClosed().subscribe((newOutpatientOrder: NewOutpatientOrder) => {
			if (newOutpatientOrder) {
				this.openNewStudyConfirmationDialog(newOutpatientOrder);
			}
		});
	}

	private openNewStudyConfirmationDialog(newOutpatientOrder: NewOutpatientOrder) {
		this.dialog.open(ConfirmarPrescripcionComponent,
			{
				disableClose: true,
				data: {
					titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.STUDY_TITLE',
					downloadButtonLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.DOWNLOAD_BUTTON_STUDY',
					successLabel: 'ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_STUDY_SUCCESS',
					prescriptionType: PrescriptionTypes.STUDY,
					patientId: this.patientId,
					prescriptionRequest: newOutpatientOrder.prescriptionRequestResponse,
				},
				width: '35%',
			});
		this.getStudy();
	}

	hideFilters() {
		this.hideFilterPanel = !this.hideFilterPanel;
	}

	filter(): void {
		this.getStudy();
	}

	clearFilterField(control: AbstractControl): void {
		control.reset();
	}

	private clearLoadedReports() {
		this.imageDiagnotics = [];
		this.laboratoryDiagnotics = [];
		this.pathologicAnatomyDiagnotics = [];
		this.hemotherapyDiagnotics = [];
		this.surgicalProceduresDiagnotics = [];
		this.otherProceduresAndPracticesDiagnotics = [];
		this.adviceDiagnotics = [];
		this.educationDiagnotics = [];
	}

	private classifyReport(report: DiagnosticReportInfoDto) {
		switch (report.category) {
			case this.translateService.instant(StudyCategories.IMAGE):
				this.imageDiagnotics = pushIfNotExists<DiagnosticReportInfoDto>(this.imageDiagnotics, report, this.compareReports);
				break;
			case this.translateService.instant(StudyCategories.LABORATORY):
				this.laboratoryDiagnotics = pushIfNotExists<DiagnosticReportInfoDto>(this.laboratoryDiagnotics, report, this.compareReports);
				break;
			case this.translateService.instant(StudyCategories.PATHOLOGIC_ANATOMY):
				this.pathologicAnatomyDiagnotics = pushIfNotExists<DiagnosticReportInfoDto>(this.pathologicAnatomyDiagnotics, report, this.compareReports);
				break;
			case this.translateService.instant(StudyCategories.HEMOTHERAPY):
				this.hemotherapyDiagnotics = pushIfNotExists<DiagnosticReportInfoDto>(this.hemotherapyDiagnotics, report, this.compareReports);
				break;
			case this.translateService.instant(StudyCategories.SURGICAL_PROCEDURE):
				this.surgicalProceduresDiagnotics = pushIfNotExists<DiagnosticReportInfoDto>(this.surgicalProceduresDiagnotics, report, this.compareReports);
				break;
			case this.translateService.instant(StudyCategories.OTHER_PROCEDURES_OR_PRACTICE):
				this.otherProceduresAndPracticesDiagnotics = pushIfNotExists<DiagnosticReportInfoDto>(this.otherProceduresAndPracticesDiagnotics, report, this.compareReports);
				break;
			case this.translateService.instant(StudyCategories.ADVICE):
				this.adviceDiagnotics = pushIfNotExists<DiagnosticReportInfoDto>(this.adviceDiagnotics, report, this.compareReports);
				break;
			case this.translateService.instant(StudyCategories.EDUCATION):
				this.educationDiagnotics = pushIfNotExists<DiagnosticReportInfoDto>(this.educationDiagnotics, report, this.compareReports);
				break;
		}
	}

	compareReports(firstReport: DiagnosticReportInfoDto, secondReport: DiagnosticReportInfoDto): boolean {
		return firstReport.id === secondReport.id;
	}

	theresReports(): boolean {
		return !((this.imageDiagnotics.length == 0) && (this.laboratoryDiagnotics.length == 0) && (this.pathologicAnatomyDiagnotics.length == 0) &&
			(this.hemotherapyDiagnotics.length == 0) && (this.surgicalProceduresDiagnotics.length == 0) &&
			(this.otherProceduresAndPracticesDiagnotics.length == 0) && (this.adviceDiagnotics.length == 0) && (this.educationDiagnotics.length == 0));
	}

	updateModifiedStudyList(updatedCategoryId: string) {
		this.resetCategoryStudyList(updatedCategoryId);
		this.prescripcionesService.getPrescription(PrescriptionTypes.STUDY, this.patientId, null, null, null, null, updatedCategoryId)
			.subscribe((response: DiagnosticReportInfoDto[]) => {
				response.forEach(report => {
					report.creationDate = new Date(report.creationDate);
					this.classifyReport(report);
				})
			});
	}

	private resetCategoryStudyList(updatedCategoryId: string) {
		switch (updatedCategoryId) {
			case this.categories[0].id:
				this.imageDiagnotics = [];
				break;
			case this.categories[1].id:
				this.laboratoryDiagnotics = [];
				break;
			case this.categories[2].id:
				this.pathologicAnatomyDiagnotics = [];
				break;
			case this.categories[3].id:
				this.hemotherapyDiagnotics = [];
				break;
			case this.categories[4].id:
				this.surgicalProceduresDiagnotics = [];
				break;
			case this.categories[5].id:
				this.otherProceduresAndPracticesDiagnotics = [];
				break;
			case this.categories[6].id:
				this.adviceDiagnotics = [];
				break;
			case this.categories[7].id:
				this.educationDiagnotics = [];
				break;
		}
	}

}
