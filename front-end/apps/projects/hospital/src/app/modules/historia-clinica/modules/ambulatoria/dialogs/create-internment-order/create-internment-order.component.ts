import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from "@angular/forms";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { ApiErrorDto, PrescriptionDto } from "@api-rest/api-model";
import { SnomedECL } from "@api-rest/api-model";
import { EmergencyCareServiceRequestService } from '@api-rest/services/emergency-care-serive-request.service';
import { InternmentOrderService } from "@api-rest/services/internment-order.service";
import { RequestMasterDataService } from "@api-rest/services/request-masterdata.service";
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { hasError } from '@core/utils/form.utils';
import { TemplateOrConceptOption, TemplateOrConceptType } from "@historia-clinica/components/template-concept-typeahead-search/template-concept-typeahead-search.component";
import { ConceptsTypeaheadSearchDialogComponent } from "@historia-clinica/dialogs/concepts-typeahead-search-dialog/concepts-typeahead-search-dialog.component";
import { OrderStudiesService, Study } from "@historia-clinica/services/order-studies.service";
import { SnackBarService } from "@presentation/services/snack-bar.service";

@Component({
	selector: 'app-create-order',
	templateUrl: './create-internment-order.component.html',
	styleUrls: ['./create-internment-order.component.scss']
})
export class CreateInternmentOrderComponent implements OnInit {

	readonly ecl = SnomedECL.PROCEDURE;
	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	hasError = hasError;

	form: UntypedFormGroup;
	firstStepCompleted = false;

	studyCategoryOptions = [];
	healthProblemOptions = [];
	selectedStudy: TemplateOrConceptOption = null;

	orderStudiesService: OrderStudiesService;
	title = this.data.emergencyCareId ? 'Nueva orden de Guardia': 'ambulatoria.paciente.internment-order.create-order-dialog.TITLE';
	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { diagnoses: any[], patientId: number, emergencyCareId?: number },
		public dialogRef: MatDialogRef<CreateInternmentOrderComponent>,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly requestMasterDataService: RequestMasterDataService,
		private readonly internmentOrderService: InternmentOrderService,
		private readonly snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
		private readonly emergencyCareServiceRequestService: EmergencyCareServiceRequestService
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

		this.healthProblemOptions = this.data.diagnoses;
		this.setMainDiagnosisAsDefaultHealthProblem();
	}

	private setMainDiagnosisAsDefaultHealthProblem() {
		const mainDiagnosis = this.healthProblemOptions.filter((d) => { return d.main }).pop();
		this.form.controls.healthProblem.setValue(mainDiagnosis);
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
		let prescriptionLineNumberAux = 0;
		const newInternmentOrder: PrescriptionDto = {
			medicalCoverageId: null,
			hasRecipe: true,
			items: this.orderStudiesService.getStudies().map(study => {
				return {
					healthConditionId: this.form.controls.healthProblem.value.id,
					observations: this.form.controls.notes.value,
					snomed: study.snomed,
					categoryId: this.form.controls.studyCategory.value,
					prescriptionLineNumber: ++prescriptionLineNumberAux,
				};
			})
		};
		if (this.data.emergencyCareId) {
			this.saveEmergencyCareOrder(newInternmentOrder);
		} else {
			this.saveInternmentOrder(newInternmentOrder);
		}
	}

	private saveEmergencyCareOrder(newInternmentOrder: PrescriptionDto) {
		this.emergencyCareServiceRequestService.create(this.data.patientId, this.data.emergencyCareId, newInternmentOrder).subscribe(
			{
				next: prescriptionRequestResponse => this.closeModal({ prescriptionDto: newInternmentOrder, prescriptionRequestResponse: prescriptionRequestResponse }),
				error: (err: ApiErrorDto) => this.snackBarService.showError(err.errors[0])
			}
		)
	}

	private saveInternmentOrder(newInternmentOrder: PrescriptionDto) {
		this.internmentOrderService.create(this.data.patientId, newInternmentOrder).subscribe(prescriptionRequestResponse => {
			this.closeModal({ prescriptionDto: newInternmentOrder, prescriptionRequestResponse: prescriptionRequestResponse });
		},
			(err: ApiErrorDto) => {
				this.snackBarService.showError(err.errors[0]);
			});
	}

	private closeModal(newInternmentOrder: NewInternmentOrder): void {
		this.dialogRef.close(newInternmentOrder);
	}

	openAddAnotherStudyDialog() {
		const addStudy = this.dialog.open(ConceptsTypeaheadSearchDialogComponent, {
			width: '25%',
			data: {
				ecl: this.ecl,
				placeholder: 'ambulatoria.paciente.internment-order.create-order-dialog.STUDY',
				title: 'ambulatoria.paciente.internment-order.create-order-dialog.ADD_STUDY_DIALOG_TITLE'
			},
		});

		addStudy.afterClosed().subscribe((addStudyDialogData) => {
			if (addStudyDialogData?.selectedConcept) {
				let added = this.orderStudiesService.add({ snomed: addStudyDialogData.selectedConcept });
				if (!added)
					this.snackBarService.showError('ambulatoria.paciente.internment-order.create-order-dialog.STUDY_REPEATED')
			}
		})
	}

}

export class NewInternmentOrder {
	prescriptionDto: PrescriptionDto;
	prescriptionRequestResponse: number | number[];
}
