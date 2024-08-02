import { FormControl, FormGroup, UntypedFormGroup, Validators } from "@angular/forms";
import { BehaviorSubject, forkJoin, map, merge, Subject, switchMap } from "rxjs";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { pushIfNotExists, removeFrom } from "@core/utils/array.utils";
import { AddDiagnosticReportObservationsCommandDto, CreateOutpatientProcedureDto, OdontologyProcedureDto, ProcedureTemplateFullSummaryDto, SnomedDto } from "@api-rest/api-model";
import { toApiFormat } from "@api-rest/mapper/date.mapper";
import { Templates } from "@historia-clinica/modules/ambulatoria/components/control-select-template/control-select-template.component";
import { ProcedureTemplatesService } from "@api-rest/services/procedure-templates.service";
import { STUDY_STATUS_ENUM } from "@historia-clinica/modules/ambulatoria/constants/prescripciones-masterdata";

export const COMLETE_NOW = "COMLETE_NOW";

export class CreateOrderService {
	emitter = new Subject();
	customForms$ = new Subject<Templates[] | []>();
	study_status = STUDY_STATUS_ENUM;
	firstPartAreCompleted = false;
	templates = [];
	private form: FormGroup<ProcedureForm>;
	private data: Procedure[] = [];
	private snomedConcept = null;
	private orders = [];
	private hasProcedure = new BehaviorSubject<boolean>(true);
	private procedure = new BehaviorSubject<SnomedDto>(null);
	readonly procedure$ = this.procedure.asObservable();

	private hasTemplate = new BehaviorSubject<boolean>(false);
	readonly hasTemplate$ = this.hasTemplate.asObservable();
	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly procedureTemplatesService: ProcedureTemplatesService,
	) {

		this.form = new FormGroup<ProcedureForm>({
			snomed: new FormControl<SnomedDto | null>(null, [Validators.required]),
			performedDate: new FormControl<string | null>(null),
			serviceRequest: new FormGroup<OrderForm>({
				healthConditionPt: new FormControl<string | null>(null, [Validators.required]),
				healthConditionSctid: new FormControl<string | null>(null, [Validators.required]),
				categoryId: new FormControl<number | null>(null, [Validators.required]),
				creationStatus: new FormControl<string | null>(null, [Validators.required]),
				observations: new FormControl<AddDiagnosticReportObservationsCommandDto | null>(null, [Validators.required]),
			})
		});

		this.subscribeFirstPartTheForm();

	}

	setConcept(selectedConcept: SnomedDto) {

		this.form.controls.snomed.setValue(selectedConcept);

		this.snomedConcept = selectedConcept;

		if (selectedConcept) {
			this.getProcedureTemplates(selectedConcept);
		}
		else {
			this.hasTemplate.next(false);
			this.customForms$.next(null);
			delete this.snomedConcept;
			this.form.patchValue({ snomed: null });
			this.form.controls.serviceRequest.controls.observations.setValue(null);
		}
		this.procedure.next(selectedConcept);
	}

	setProblem(healthProblem: SnomedDto) {
		this.hasProcedure.next(this.isEmpty());
		this.form.patchValue({
			serviceRequest: {
				healthConditionPt: healthProblem.pt,
				healthConditionSctid: healthProblem.sctid
			}
		})
	}

	setStudyCategory(studyCategoryId: number | string) {
		this.form.patchValue({
			serviceRequest: {
				categoryId: studyCategoryId
			}
		})
	}

	setCreationStatus(CreationStatus: string) {
		this.form.patchValue({
			serviceRequest: {
				creationStatus: ((CreationStatus === COMLETE_NOW) || (CreationStatus === STUDY_STATUS_ENUM.FINAL)) ? "FINAL" : "REGISTERED"
			}
		})
	}

	isEmpty(): boolean {
		return (!this.data || this.data.length === 0);
	}

	addToList() {
		const newProcedure: Procedure = {
			snomed: this.snomedConcept,
			performedDate: this.form.value.performedDate || undefined
		};
		this.addControl(newProcedure);
		this.resetForm();
		this.templates = [];
		this.hasTemplate.next(false);
	}

	addControl(procedimiento: Procedure) {
		if (this.add(procedimiento)) {
			this.snackBarService.showError("Procedimiento duplicado");
		} else {
			this.orders.push(this.form.value);
		}
	}

	resetForm() {
		this.procedure.next(null);
		delete this.snomedConcept;
		this.form.reset();
	}

	getForm(): UntypedFormGroup {
		return this.form;
	}

	isValidForm(): boolean {
		return this.form.valid;
	}

	getOrderForNewConsultation(): CreateOutpatientProcedureDto[] {
		return this.orders;
	}

	getOrderForOdontologyConsultation(): OdontologyProcedureDto[] {
		return this.orders;
	}

	getProcedimientos(): Procedure[] {
		return this.data;
	}

	remove(index: number) {
		this.hasTemplate.next(false);
		this.customForms$.next(null);
		this.orders = removeFrom<CreateOutpatientProcedureDto>(this.orders, index);
		this.data = removeFrom<Procedure>(this.data, index);
		this.emitter.next(this.data)
	}

	setDate(date: Date) {
		this.form.patchValue({
			performedDate: toApiFormat(date)
		})
	}

	setObservations($event, procedureTemplateId: number) {
		const observations = {
			isPartialUpload: false,
			procedureTemplateId: procedureTemplateId,
			referenceClosure: null,
			values: $event.form
		}

		this.form.patchValue({
			serviceRequest: {
				observations: observations
			}
		})
	}

	getProcedureTemplates(procedureSelected: SnomedDto) {
		this.getProcedureTemplatesService(procedureSelected).pipe(
			map((templates: ProcedureTemplateFullSummaryDto[]) => {
				this.hasTemplate.next(templates.length > 0);
				return {
					name: procedureSelected.pt,
					id: procedureSelected.sctid,
					form: templates,
					preloadValues: null,
					preloadedSelectedTemplate: null
				};
			})
		).subscribe(templates => {
			this.customForms$.next(templates);
			this.templates = templates;
		});
	}

	getTemplates() {
		return this.templates
	}

	hasConcept(): boolean {
		return this.form.controls.snomed.valid;
	}

	private subscribeFirstPartTheForm() {
		const snomedControl = this.form.get('snomed');
		const healthConditionSctidControl = this.form.get('serviceRequest.healthConditionSctid');
		const categoryIdControl = this.form.get('serviceRequest.categoryId');

		merge(
			snomedControl.valueChanges,
			healthConditionSctidControl.valueChanges,
			categoryIdControl.valueChanges
		).subscribe(() => {
			this.firstPartAreCompleted = this.areRequiredFieldsCompleted()
		});
	}


	private areRequiredFieldsCompleted(): boolean {

		const snomedCompleted = this.form.get('snomed').valid;
		const healthConditionIdCompleted = this.form.get('serviceRequest').get('healthConditionSctid').valid;
		const categoryIdCompleted = this.form.get('serviceRequest').get('categoryId').valid;
		return snomedCompleted && healthConditionIdCompleted && categoryIdCompleted;
	}


	private getProcedureTemplatesService(procedureSelected: SnomedDto): any {
		return this.procedureTemplatesService.findAvailableForSnomedConcept(procedureSelected).pipe(
			switchMap((practiceTemplates: ProcedureTemplateFullSummaryDto[]) => {
				let observables = practiceTemplates.map(t => this.procedureTemplatesService.findById(t.id));
				return forkJoin(observables);
			})
		);
	}

	private add(procedimiento: Procedure): boolean {
		const currentItems = this.data.length;
		this.data = pushIfNotExists<Procedure>(this.data, procedimiento, this.compareSpeciality);
		this.emitter.next(this.data);
		return currentItems === this.data.length;
	}

	private compareSpeciality(data: Procedure, data1: Procedure): boolean {
		return data.snomed.sctid === data1.snomed.sctid;
	}

}


interface ProcedureForm {
	snomed: FormControl<SnomedDto>;
	performedDate: FormControl<string>;
	serviceRequest: FormGroup<OrderForm>;
}

interface OrderForm {
	healthConditionPt: FormControl<string | null>;
	healthConditionSctid: FormControl<string | null>;
	categoryId: FormControl<number | string | null>;
	creationStatus: FormControl<string | null>;
	observations: FormControl<AddDiagnosticReportObservationsCommandDto | null>;
}

interface Procedure {
	snomed: SnomedDto;
	performedDate?: string;
}
