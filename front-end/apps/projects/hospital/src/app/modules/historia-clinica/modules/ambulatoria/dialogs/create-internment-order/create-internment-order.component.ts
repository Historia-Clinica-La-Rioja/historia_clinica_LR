import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { ApiErrorDto, BasicPatientDto, EStudyType, PatientMedicalCoverageDto, PrescriptionDto, PrescriptionItemDto } from "@api-rest/api-model";
import { SnomedECL } from "@api-rest/api-model";
import { dateToDateTimeDtoUTC } from '@api-rest/mapper/date-dto.mapper';
import { EmergencyCareServiceRequestService } from '@api-rest/services/emergency-care-serive-request.service';
import { InternmentOrderService } from "@api-rest/services/internment-order.service";
import { PatientMedicalCoverageService } from '@api-rest/services/patient-medical-coverage.service';
import { PatientService } from '@api-rest/services/patient.service';
import { RequestMasterDataService } from "@api-rest/services/request-masterdata.service";
import { MapperService } from '@core/services/mapper.service';
import { hasError } from '@core/utils/form.utils';
import { TemplateOrConceptOption, TemplateOrConceptType } from "@historia-clinica/components/template-concept-typeahead-search/template-concept-typeahead-search.component";
import { OrderStudiesService, Study } from "@historia-clinica/services/order-studies.service";
import { OrderTemplateService } from '@historia-clinica/services/order-template.service';
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { PatientBasicData } from '@presentation/utils/patient.utils';

const MAX_DATE = 45;

@Component({
	selector: 'app-create-order',
	templateUrl: './create-internment-order.component.html',
	styleUrls: ['./create-internment-order.component.scss'],
	providers: [OrderTemplateService]
})
export class CreateInternmentOrderComponent implements OnInit {

	readonly ecl = SnomedECL.PROCEDURE;
	hasError = hasError;
	patientMedicalCoverages: PatientMedicalCoverageDto[];
	patient: PatientBasicData;
	selectedCoverage: PatientMedicalCoverageDto;
	ROUTINE = EStudyType.ROUTINE
	URGENT = EStudyType.URGENT
	form: UntypedFormGroup;

	studyCategoryOptions = [];
	healthProblemOptions = [];
	selectedStudy: TemplateOrConceptOption = null;

	orderStudiesService: OrderStudiesService;
	title = this.data.emergencyCareId ? 'Nueva orden de Guardia' : 'ambulatoria.paciente.internment-order.create-order-dialog.TITLE';
	today = new Date();
	isSelectedDateInThePast = false;
	maxDate: Date = this.setMaxDay(new Date());
	setDateWithTime = false;
	private selectedDateTime: Date;

	private setMaxDay(today: Date): Date {
		const maxDate = new Date(today);
		maxDate.setDate(maxDate.getDate() + MAX_DATE);
		return maxDate;
	}

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { diagnoses: any[], patientId: number, emergencyCareId?: number, patientInternmentEpisodeMedicalCoverageId?: number, patientEmergencyCareMedicalCoverageId?: number },
		public dialogRef: MatDialogRef<CreateInternmentOrderComponent>,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly requestMasterDataService: RequestMasterDataService,
		private readonly internmentOrderService: InternmentOrderService,
		private readonly snackBarService: SnackBarService,
		private readonly mapperService: MapperService,
		private readonly patientService: PatientService,
		private readonly patientMedicalCoverageService: PatientMedicalCoverageService,
		private readonly emergencyCareServiceRequestService: EmergencyCareServiceRequestService,
		private readonly orderTemplate: OrderTemplateService

	) {
		this.patientService.getPatientBasicData<BasicPatientDto>(this.data.patientId).subscribe(
			(patient: BasicPatientDto) => {
				this.patient = this.mapperService.toPatientBasicData(patient);
			});

		this.patientMedicalCoverageService.getActivePatientMedicalCoverages(this.data.patientId).subscribe(
			(medicalCoverage: PatientMedicalCoverageDto[]) => {
				this.patientMedicalCoverages = medicalCoverage;

				if (data?.patientInternmentEpisodeMedicalCoverageId || data?.patientEmergencyCareMedicalCoverageId) {

					const medicalCoverageSelected = data.patientInternmentEpisodeMedicalCoverageId ?? data.patientEmergencyCareMedicalCoverageId;

					this.selectedCoverage = medicalCoverage.find(mc => mc.id === medicalCoverageSelected);
				}
			})

		this.orderStudiesService = new OrderStudiesService();
	}

	ngOnInit(): void {

		this.form = this.formBuilder.group({
			patientMedicalCoverage: [null],
			studyCategory: [null, Validators.required],
			studySelection: [null, Validators.required],
			healthProblem: [null, Validators.required],
			studyType: [EStudyType.ROUTINE, Validators.required],
			requiresTechnical: [false, Validators.required],
			notes: [null],
			selectSectionDeferredDate: [false],
			date: [null],
		});

		this.requestMasterDataService.categories().subscribe(categories => {
			this.studyCategoryOptions = categories;
		});

		this.healthProblemOptions = this.data.diagnoses;
		this.setMainDiagnosisAsDefaultHealthProblem();

		this.form.controls.selectSectionDeferredDate.valueChanges.subscribe((selectSectionDeferredDate: boolean) => {
			if (!selectSectionDeferredDate)
				this.form.controls.date.setValue(null);
		}
		)

	}

	private setMainDiagnosisAsDefaultHealthProblem() {
		const mainDiagnosis = this.healthProblemOptions.filter((d) => { return d.main }).pop();
		this.form.controls.healthProblem.setValue(mainDiagnosis);
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
			this.orderTemplate.addTemplate(this.orderStudiesService.getStudies());
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
			this.addTemplateId();
		} else {
			const conceptToLoad = mapConceptToStudy(this.selectedStudy.data);
			addStudy(conceptToLoad);
		}
	}

	removeStudy(i) {
		const study = this.orderStudiesService.getStudyByIndex(i);
		this.orderTemplate.updateStudiesFromTemplate(study.snomed.sctid);
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
		const selectedDate = this.form.controls.date?.value;

		if (this.form.controls.selectSectionDeferredDate.value) {
			if (selectedDate && this.setDateWithTime) {
				this.isSelectedDateInThePast = this.isDateInThePast(selectedDate);
				if (!this.isSelectedDateInThePast)
					this.saveOrder();
			}
		} else {
			this.saveOrder();
		}

	}

	handleDateChange(selectedDate: Date) {
		this.selectedDateTime = selectedDate;
		this.form.controls.date.setValue(this.selectedDateTime);
		this.isSelectedDateInThePast = this.isDateInThePast(this.selectedDateTime);
	}

	handleTimeChange(selectedTime: { hours: number; minutes: number }) {
		this.isSelectedDateInThePast = false;

		if (this.selectedDateTime) {
			this.setDateWithTime = true;
			const dateWithTime = new Date(this.selectedDateTime);
			dateWithTime.setHours(selectedTime.hours);
			dateWithTime.setMinutes(selectedTime.minutes);
			this.selectedDateTime = dateWithTime;
			this.form.controls.date.setValue(this.selectedDateTime);
		}
	}

	handleDeferredSectionToggle(isSelected: boolean) {
		this.form.controls.selectSectionDeferredDate.setValue(isSelected);
	}

	private addTemplateId = () => {
		this.selectedStudy.data.concepts.forEach(concept => {
			const templateId = this.selectedStudy.data.id;
			const sctid = concept.conceptId;
			this.orderTemplate.addStudy(sctid, templateId)
		});
	}

	private isDateInThePast(selectedDate: Date): boolean {
		return this.today > selectedDate;
	}

	private saveOrder() {
		const newInternmentOrder = this.createNewInternmentOrder();
		if (this.data.emergencyCareId) {
			this.saveEmergencyCareOrder(newInternmentOrder);
		} else {
			this.saveInternmentOrder(newInternmentOrder);
		}
	}

	private createNewInternmentOrder(): PrescriptionDto {
		const { patientMedicalCoverage, notes, studyType, requiresTechnical, healthProblem, studyCategory } = this.form.controls;
		const studies = this.orderStudiesService.getStudies();

		return {
			medicalCoverageId: patientMedicalCoverage.value?.id,
			hasRecipe: true,
			observations: notes.value,
			studyType: studyType.value,
			requiresTransfer: requiresTechnical.value,
			...(this.form.controls.date?.value && {
				deferredDate: dateToDateTimeDtoUTC(this.form.controls.date.value)
			}),
			items: this.createPrescriptionItems(studies, healthProblem.value.id, studyCategory.value),
			templateIds: this.orderTemplate.getTemplatesIds()
		};
	}

	private createPrescriptionItems(studies: Study[], healthConditionId: number, categoryId: string): PrescriptionItemDto[] {
		let prescriptionLineNumber = 0;
		return studies.map(study => ({
			healthConditionId,
			snomed: study.snomed,
			categoryId,
			prescriptionLineNumber: ++prescriptionLineNumber,
		}));
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


}

export class NewInternmentOrder {
	prescriptionDto: PrescriptionDto;
	prescriptionRequestResponse: number | number[];
}
