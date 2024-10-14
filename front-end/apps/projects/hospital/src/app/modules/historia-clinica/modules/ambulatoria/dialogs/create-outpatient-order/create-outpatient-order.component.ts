import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup, Validators } from "@angular/forms";
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { ApiErrorDto, BasicPatientDto, EStudyType, PatientMedicalCoverageDto, PrescriptionDto, SnomedECL } from "@api-rest/api-model";
import { OutpatientOrderService } from "@api-rest/services/outpatient-order.service";
import { PatientMedicalCoverageService } from "@api-rest/services/patient-medical-coverage.service";
import { PatientService } from "@api-rest/services/patient.service";
import { RequestMasterDataService } from "@api-rest/services/request-masterdata.service";
import { MapperService } from "@core/services/mapper.service";
import { PatientNameService } from '@core/services/patient-name.service';
import { hasError } from '@core/utils/form.utils';
import { TemplateOrConceptOption, TemplateOrConceptType } from "@historia-clinica/components/template-concept-typeahead-search/template-concept-typeahead-search.component";
import { OrderStudiesService, Study } from "@historia-clinica/services/order-studies.service";
import { PatientSummary } from '@hsi-components/patient-summary/patient-summary.component';
import { MedicalCoverageComponent, PatientMedicalCoverage } from "@pacientes/dialogs/medical-coverage/medical-coverage.component";
import { ButtonType } from '@presentation/components/button/button.component';
import { Size } from '@presentation/components/item-summary/item-summary.component';
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { map } from "rxjs/operators";

@Component({
	selector: 'app-create-outpatient-order',
	templateUrl: './create-outpatient-order.component.html',
	styleUrls: ['./create-outpatient-order.component.scss']
})
export class CreateOutpatientOrderComponent implements OnInit {
	eStudyType: EStudyType;
	ROUTINE = EStudyType.ROUTINE
	URGENT = EStudyType.URGENT
	patient: PatientSummary;
	readonly ecl = SnomedECL.PROCEDURE;
	hasError = hasError;
	form: UntypedFormGroup;
	private patientData: BasicPatientDto;
	patientMedicalCoverages: PatientMedicalCoverage[];
	studyCategoryOptions = [];
	healthProblemOptions = [];
	selectedStudy: TemplateOrConceptOption = null;
	orderStudiesService: OrderStudiesService;
	size = Size.SMALL;
	ButtonType = ButtonType;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { patientId: number, healthProblems },
		public dialogRef: MatDialogRef<CreateOutpatientOrderComponent>,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly mapperService: MapperService,
		private readonly requestMasterDataService: RequestMasterDataService,
		private readonly outpatientOrderService: OutpatientOrderService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly patientService: PatientService,
		private readonly snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
		private readonly patientNameService: PatientNameService
	) {

		this.patientService.getPatientBasicData<BasicPatientDto>(this.data.patientId).subscribe(
			patient => {
				this.patientService.getPatientPhoto(patient.id).subscribe(photo => {
					this.patient = this.mapperService.toPatientSummary(patient,photo, this.patientNameService);
				})
			});

		this.orderStudiesService = new OrderStudiesService();
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			patientMedicalCoverage: [null],
			studyCategory: [null, Validators.required],
			studySelection: [null, Validators.required],
			studyType: [EStudyType.ROUTINE, Validators.required],
			requiresTechnical: [false, Validators.required],
			healthProblem: [null, Validators.required],
			notes: [null]
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
		this.loadSelectedConceptsIntoOrderStudiesService();
		this.resetStudySelector();
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

	private loadSelectedConceptsIntoOrderStudiesService() {
		const addStudy = (study: Study) => {
			const added = this.orderStudiesService.add(study);
			if (!added) {
				this.snackBarService.showError('ambulatoria.paciente.outpatient-order.create-order-dialog.STUDY_REPEATED');
			}
		};

		const mapConceptToStudy = (concept: any): Study => ({
			snomed: {
				sctid: concept.conceptId,
				pt: concept.pt.term,
			}
		});

		if (this.selectedStudyIsTemplate()) {
			const conceptsToLoad = this.selectedStudy.data.concepts.map(mapConceptToStudy);
			conceptsToLoad.forEach(addStudy);
		} else {
			const conceptToLoad = mapConceptToStudy(this.selectedStudy.data);
			addStudy(conceptToLoad);
		}
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
		const newOutpatientOrder: PrescriptionDto = {
			medicalCoverageId: this.form.controls.patientMedicalCoverage.value?.id,
			hasRecipe: true,
			observations: this.form.controls.notes.value,
			studyType: this.form.controls.studyType.value,
			requiresTransfer: this.form.controls.requiresTechnical.value,
			items: this.orderStudiesService.getStudies().map(study => {
				return {
					healthConditionId: this.form.controls.healthProblem.value.id,
					snomed: study.snomed,
					categoryId: this.form.controls.studyCategory.value,
					prescriptionLineNumber: ++prescriptionLineNumberAux,
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

	private closeModal(newOutpatientOrder: NewOutpatientOrder) {
		this.dialogRef.close(newOutpatientOrder);
	}

	validateForm(){
		this.form.markAllAsTouched();
	}
	clear(control: AbstractControl): void {
		control.reset();
	}

  }

export class NewOutpatientOrder {
	prescriptionDto: PrescriptionDto;
	prescriptionRequestResponse: number | number[];
}
