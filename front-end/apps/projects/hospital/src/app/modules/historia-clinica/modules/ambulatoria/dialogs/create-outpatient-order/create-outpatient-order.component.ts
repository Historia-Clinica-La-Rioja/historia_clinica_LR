import { Component, Inject, OnInit } from '@angular/core';
import { ApiErrorDto, BasicPatientDto, PatientMedicalCoverageDto, PrescriptionDto } from "@api-rest/api-model";
import { SnomedECL } from "@api-rest/api-model";
import { AbstractControl, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { TemplateOrConceptOption, TemplateOrConceptType } from "@historia-clinica/components/template-concept-typeahead-search/template-concept-typeahead-search.component";
import { OrderStudiesService, Study } from "@historia-clinica/services/order-studies.service";
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from "@angular/material/dialog";
import { RequestMasterDataService } from "@api-rest/services/request-masterdata.service";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { ConceptsTypeaheadSearchDialogComponent } from "@historia-clinica/dialogs/concepts-typeahead-search-dialog/concepts-typeahead-search-dialog.component";
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { hasError } from '@core/utils/form.utils';
import { HceGeneralStateService } from "@api-rest/services/hce-general-state.service";
import { OutpatientOrderService } from "@api-rest/services/outpatient-order.service";
import { MedicalCoverageComponent, PatientMedicalCoverage } from "@pacientes/dialogs/medical-coverage/medical-coverage.component";
import { map } from "rxjs/operators";
import { PatientMedicalCoverageService } from "@api-rest/services/patient-medical-coverage.service";
import { MapperService } from "@core/services/mapper.service";
import { PatientService } from "@api-rest/services/patient.service";

@Component({
  selector: 'app-create-outpatient-order',
  templateUrl: './create-outpatient-order.component.html',
  styleUrls: ['./create-outpatient-order.component.scss']
})
export class CreateOutpatientOrderComponent implements OnInit {

	readonly ecl = SnomedECL.PROCEDURE;
	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	hasError = hasError;

	form: FormGroup;
	firstStepCompleted = false;

	private patientData: BasicPatientDto;
	patientMedicalCoverages: PatientMedicalCoverage[];
	studyCategoryOptions = [];
	healthProblemOptions = [];
	selectedStudy: TemplateOrConceptOption = null;

	orderStudiesService: OrderStudiesService;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { patientId: number, healthProblems },
		public dialogRef: MatDialogRef<CreateOutpatientOrderComponent>,
		private readonly formBuilder: FormBuilder,
		private readonly mapperService: MapperService,
		private readonly requestMasterDataService: RequestMasterDataService,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly outpatientOrderService: OutpatientOrderService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly patientService: PatientService,
		private readonly snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
	) {
		this.orderStudiesService = new OrderStudiesService();
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			patientMedicalCoverage: [null],
			studyCategory: [null, Validators.required],
			studySelection: [null, Validators.required],
			healthProblem: [null, Validators.required],
			notes: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]]
		});

		this.setMedicalCoverages();

		this.patientService.getPatientBasicData(Number(this.data.patientId)).subscribe((basicData: BasicPatientDto) => {
			this.patientData = basicData;
		});

		this.requestMasterDataService.categories().subscribe(categories => {
			this.studyCategoryOptions = categories;
		});

		this.healthProblemOptions = this.data.healthProblems;
	}

	private setMedicalCoverages(): void {
		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.data.patientId)
			.pipe(
				map(
					patientMedicalCoveragesDto =>
						patientMedicalCoveragesDto.map(s => this.mapperService.toPatientMedicalCoverage(s))
				)
			)
			.subscribe((patientMedicalCoverages: PatientMedicalCoverage[]) => this.patientMedicalCoverages = patientMedicalCoverages);
	}

	getFullMedicalCoverageText(patientMedicalCoverage): string {
		const condition = (patientMedicalCoverage.condition) ? patientMedicalCoverage.condition.toLowerCase() : null;
		const medicalCoverageText = [patientMedicalCoverage.medicalCoverage.acronym, patientMedicalCoverage.medicalCoverage.name]
			.filter(Boolean).join(' - ');
		return [medicalCoverageText, patientMedicalCoverage.affiliateNumber,condition].filter(Boolean).join(' / ');
	}

	openMedicalCoverageDialog(): void {
		const dialogRef = this.dialog.open(MedicalCoverageComponent, {
			data: {
				genderId: this.patientData.person.gender.id,
				identificationNumber: this.patientData.person.identificationNumber,
				identificationTypeId: this.patientData.person.identificationTypeId,
				initValues: this.patientMedicalCoverages,
				patientId: this.patientData.id
			}
		});

		dialogRef.afterClosed().subscribe(
			values => {
				if (values) {
					const patientCoverages: PatientMedicalCoverageDto[] =
						values.patientMedicalCoverages.map(s => this.mapperService.toPatientMedicalCoverageDto(s));

					this.patientMedicalCoverageService.addPatientMedicalCoverages(Number(this.data.patientId), patientCoverages).subscribe(
						_ => {
							this.setMedicalCoverages();
							this.snackBarService.showSuccess('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_UPDATE_COVERAGE_SUCCESS');
						},
						_ => this.snackBarService.showError('ambulatoria.paciente.ordenes_prescripciones.toast_messages.POST_UPDATE_COVERAGE_ERROR')
					);
				}
			}
		);
	}

	handleStudySelected(study) {
		this.selectedStudy = study;
		this.form.controls.studySelection.setValue(this.getStudyDisplayName());
	}

	resetStudySelector() {
		this.selectedStudy = null;
		this.form.controls.studySelection.setValue(null);
	}

	selectedStudyIsTemplate(): boolean {
		return this.selectedStudy.type === TemplateOrConceptType.TEMPLATE;
	}

	getStudyDisplayName(): string {
		return (this.selectedStudyIsTemplate()) ? this.selectedStudy?.data?.description : this.selectedStudy?.data?.pt.term;
	}

	getTemplateIncludedConceptsDisplayText(): string {
		return this.selectedStudy.data.concepts.map(c => c.pt.term).join(', ');
	}

	goToConfirmationStep() {
		this.firstStepCompleted = true;
		this.loadSelectedConceptsIntoOrderStudiesService();
	}

	private loadSelectedConceptsIntoOrderStudiesService() {
		let conceptsToLoad: Study[];
		if (this.selectedStudyIsTemplate()) {
			conceptsToLoad = this.selectedStudy.data.concepts.map(concept => ({
				snomed: {
					sctid: concept.conceptId,
					pt: concept.pt.term
				}
			}));
		}
		else {
			conceptsToLoad = [
				{
					snomed: {
						sctid: this.selectedStudy.data.conceptId,
						pt: this.selectedStudy.data.pt.term
					}
				}
			];
		}
		this.orderStudiesService.addAll(conceptsToLoad);
	}

	removeStudy(i) {
		this.orderStudiesService.remove(i);
	}

	getSelectedCategoryDisplayName() {
		return this.studyCategoryOptions.filter((c) => c.id === this.form.controls.studyCategory.value).pop()?.description;
	}

	getSelectedHealthProblemDisplayName() {
		return this.form.controls.healthProblem.value.description;
	}

	getNotesDisplayText() {
		return this.form.controls.notes.value;
	}

	confirmOrder() {
		const newOutpatientOrder: PrescriptionDto = {
			medicalCoverageId: this.form.controls.patientMedicalCoverage.value?.id,
			hasRecipe: true,
			items: this.orderStudiesService.getStudies().map(study => {
				return {
					healthConditionId: this.form.controls.healthProblem.value.id,
					observations: this.form.controls.notes.value,
					snomed: study.snomed,
					categoryId: this.form.controls.studyCategory.value,
				};
			})
		};
		this.saveOutpatientOrder(newOutpatientOrder);
	}

	private saveOutpatientOrder(newOutpatientOrder: PrescriptionDto) {
		this.outpatientOrderService.create(this.data.patientId, newOutpatientOrder).subscribe(prescriptionRequestResponse => {
				this.closeModal({ prescriptionDto: newOutpatientOrder, prescriptionRequestResponse: prescriptionRequestResponse });
			},
			(err: ApiErrorDto) => {
				this.snackBarService.showError(err.errors[0]);
			});
	}

	private closeModal(newOutpatientOrder: NewOutpatientOrder): void {
		this.dialogRef.close(newOutpatientOrder);
	}

	openAddAnotherStudyDialog() {
		const addStudy = this.dialog.open(ConceptsTypeaheadSearchDialogComponent, {
			width: '25%',
			data: {
				ecl: this.ecl,
				placeholder: 'ambulatoria.paciente.outpatient-order.create-order-dialog.STUDY',
				title: 'ambulatoria.paciente.outpatient-order.create-order-dialog.ADD_STUDY_DIALOG_TITLE'
			},
		});

		addStudy.afterClosed().subscribe((addStudyDialogData) => {
			if (addStudyDialogData?.selectedConcept) {
				let added = this.orderStudiesService.add({snomed: addStudyDialogData.selectedConcept});
				if (!added)
					this.snackBarService.showError('ambulatoria.paciente.outpatient-order.create-order-dialog.STUDY_REPEATED')
			}
		})
	}

	clear(control: AbstractControl): void {
		control.reset();
	}

}

export class NewOutpatientOrder {
	prescriptionDto: PrescriptionDto;
	prescriptionRequestResponse: number | number[];
}
