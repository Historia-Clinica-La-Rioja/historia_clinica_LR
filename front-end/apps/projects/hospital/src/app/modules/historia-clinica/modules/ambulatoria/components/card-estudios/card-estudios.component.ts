import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { DiagnosticReportInfoDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { ESTUDIOS } from '@historia-clinica/constants/summaries';
import { STUDY_STATUS } from '../../constants/prescripciones-masterdata';
import { ConfirmarPrescripcionComponent } from '../../dialogs/ordenes-prescripciones/confirmar-prescripcion/confirmar-prescripcion.component';
import {
	NewPrescription,
	NuevaPrescripcionComponent
} from '../../modules/indicacion/dialogs/nueva-prescripcion/nueva-prescripcion.component';
import { PrescripcionesService, PrescriptionTypes } from '../../services/prescripciones.service';
import {
	CreateInternmentOrderComponent,
	NewInternmentOrder
} from "@historia-clinica/modules/ambulatoria/dialogs/create-internment-order/create-internment-order.component";
import { InternmentPatientService } from "@api-rest/services/internment-patient.service";
import { DiagnosisRequiredComponent } from "@historia-clinica/modules/ambulatoria/dialogs/diagnosis-required/diagnosis-required.component";
import { InternmentStateService } from "@api-rest/services/internment-state.service";
import { StudyCategories } from '../../modules/estudio/constants/internment-studies';
import { TranslateService } from '@ngx-translate/core';
import { pushIfNotExists } from '@core/utils/array.utils';

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

	@Input() patientId: number;

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
		private readonly translateService: TranslateService
	) { }

	ngOnInit(): void {
		this.formFilter = this.formBuilder.group({
			categoryId: [null],
			statusId: [null],
			healthCondition: [null],
			study: [null],
		});

		this.getStudy();

		this.requestMasterDataService.categories().subscribe(categories => {
			this.categories = categories;
		});

		this.internmentPatientService.internmentEpisodeIdInProcess(this.patientId).subscribe(internmentEpisodeInProgress => {
			this.internmentEpisodeInProgressId = internmentEpisodeInProgress.id;
		})
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

	patientHasInternmentEpisodeInProgress(): boolean {
		return this.internmentEpisodeInProgressId;
	}

	openNewInternmentOrderDialog() {
		this.internmentStateService.getDiagnosesGeneralState(this.internmentEpisodeInProgressId).subscribe(diagnoses => {
			if (diagnoses.length) {
				this.openCreateInternmentOrderDialog();
			}
			else {
				this.dialog.open(DiagnosisRequiredComponent, { width: '35%' });
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
				this.dialog.open(ConfirmarPrescripcionComponent,
					{
						disableClose: true,
						data: {
							titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.STUDY_TITLE',
							downloadButtonLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.DOWNLOAD_BUTTON_STUDY',
							successLabel: 'ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_STUDY_SUCCESS',
							prescriptionType: PrescriptionTypes.STUDY,
							patientId: this.patientId,
							prescriptionRequest: newInternmentOrder.prescriptionRequestResponse,
						},
						width: '35%',
					});
				this.getStudy();
			}
		});
	}

	openDialogNewStudy() {
		const newStudyDialog = this.dialog.open(NuevaPrescripcionComponent,
			{
				data: {
					patientId: this.patientId,
					titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.STUDY_TITLE',
					addLabel: 'ambulatoria.paciente.ordenes_prescripciones.new_prescription_dialog.ADD_STUDY_LABEL',
					prescriptionType: PrescriptionTypes.STUDY,
					prescriptionItemList: undefined,
					addPrescriptionItemDialogData: {
						titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.STUDY_TITLE',
						searchSnomedLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.STUDY',
						showDosage: false,
						showStudyCategory: true,
						eclTerm: SnomedECL.PROCEDURE,
					}
				},
				width: '35%',
			});

		newStudyDialog.afterClosed().subscribe((newPrescription: NewPrescription) => {
			if (newPrescription) {
				this.dialog.open(ConfirmarPrescripcionComponent,
					{
						disableClose: true,
						data: {
							titleLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.STUDY_TITLE',
							downloadButtonLabel: 'ambulatoria.paciente.ordenes_prescripciones.confirm_prescription_dialog.DOWNLOAD_BUTTON_STUDY',
							successLabel: 'ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_STUDY_SUCCESS',
							prescriptionType: PrescriptionTypes.STUDY,
							patientId: this.patientId,
							prescriptionRequest: newPrescription.prescriptionRequestResponse,
						},
						width: '35%',
					});
				this.getStudy();
			}
		});
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
