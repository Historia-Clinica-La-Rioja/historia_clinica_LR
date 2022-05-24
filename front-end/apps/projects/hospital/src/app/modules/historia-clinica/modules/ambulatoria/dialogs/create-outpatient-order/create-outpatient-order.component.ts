import { Component, Inject, OnInit } from '@angular/core';
import { ApiErrorDto, HCEPersonalHistoryDto, PrescriptionDto } from "@api-rest/api-model";
import { SnomedECL } from "@api-rest/api-model";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
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

	studyCategoryOptions = [];
	healthProblemOptions = [];
	selectedStudy: TemplateOrConceptOption = null;

	orderStudiesService: OrderStudiesService;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { patientId: number },
		public dialogRef: MatDialogRef<CreateOutpatientOrderComponent>,
		private readonly formBuilder: FormBuilder,
		private readonly requestMasterDataService: RequestMasterDataService,
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly outpatientOrderService: OutpatientOrderService,
		private readonly snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
	) {
		this.orderStudiesService = new OrderStudiesService();
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			studyCategory: [null, Validators.required],
			studySelection: [null, Validators.required],
			healthProblem: [null, Validators.required],
			notes: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]]
		});

		this.requestMasterDataService.categories().subscribe(categories => {
			this.studyCategoryOptions = categories;
		});

		this.hceGeneralStateService.getActiveProblems(this.data.patientId).subscribe((activeProblems: HCEPersonalHistoryDto[]) => {
			const activeProblemsList = activeProblems.map(problem => ({id: problem.id, description: problem.snomed.pt, sctId: problem.snomed.sctid}));

			this.hceGeneralStateService.getChronicConditions(this.data.patientId).subscribe((chronicProblems: HCEPersonalHistoryDto[]) => {
				const chronicProblemsList = chronicProblems.map(problem => ({id: problem.id, description: problem.snomed.pt,  sctId: problem.snomed.sctid}));
				this.healthProblemOptions = activeProblemsList.concat(chronicProblemsList);
			});

		});
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
		const newInternmentOrder: PrescriptionDto = {
			medicalCoverageId: null,
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
		this.saveInternmentOrder(newInternmentOrder);
	}

	private saveInternmentOrder(newInternmentOrder: PrescriptionDto) {
		this.outpatientOrderService.create(this.data.patientId, newInternmentOrder).subscribe(prescriptionRequestResponse => {
				this.closeModal({ prescriptionDto: newInternmentOrder, prescriptionRequestResponse: prescriptionRequestResponse });
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

}

export class NewOutpatientOrder {
	prescriptionDto: PrescriptionDto;
	prescriptionRequestResponse: number | number[];
}
