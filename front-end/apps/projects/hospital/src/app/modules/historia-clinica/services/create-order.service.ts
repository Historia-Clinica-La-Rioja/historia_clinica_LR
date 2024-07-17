import { FormControl, FormGroup, UntypedFormGroup, Validators } from "@angular/forms";
import { BehaviorSubject, Subject } from "rxjs";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { pushIfNotExists, removeFrom } from "@core/utils/array.utils";
import { CreateOutpatientProcedureDto, OdontologyProcedureDto, SnomedDto } from "@api-rest/api-model";
import { toApiFormat } from "@api-rest/mapper/date.mapper";

export class CreateOrderService {

	private form: FormGroup<ProcedureForm>;;
	private data: Procedure[] = [];
	private snomedConcept = null;
	private orders = [];
	private hasProcedure = new BehaviorSubject<boolean>(true);
	emitter = new Subject();

	constructor(
		private readonly snackBarService: SnackBarService,

	) {

		this.form = new FormGroup<ProcedureForm>({
			snomed: new FormControl<SnomedDto | null>(null, [Validators.required]),
			performedDate: new FormControl<string | null>(null),
			serviceRequest: new FormGroup<OrderForm>({
				healthConditionPt: new FormControl<string | null>(null, [Validators.required]),
				healthConditionSctid: new FormControl<string | null>(null, [Validators.required]),
				categoryId: new FormControl<number | null>(null, [Validators.required]),
				creationStatus: new FormControl<string | null>(null, [Validators.required])
			})
		});
	}

	setConcept(selectedConcept: SnomedDto) {
		this.snomedConcept = selectedConcept;
		this.form.controls.snomed.setValue(selectedConcept);
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
				creationStatus: CreationStatus
			}
		})
	}

	isEmpty(): boolean {
		return (!this.data || this.data.length === 0);
	}

	addToList(): boolean {
		if (this.form.valid && this.snomedConcept) {
			const newProcedure: Procedure = {
				snomed: this.snomedConcept,
				performedDate: this.form.value.performedDate || undefined
			};
			this.addControl(newProcedure);
			this.resetForm();
			return true;
		}
		return false;
	}

	addControl(procedimiento: Procedure) {
		this.hasProcedure.next(this.isEmpty());
		if (this.add(procedimiento)) {
			this.snackBarService.showError("Procedimiento duplicado");
		} else {
			this.orders.push(this.form.value);
		}
	}

	resetForm() {
		this.hasProcedure.next(this.isEmpty());
		delete this.snomedConcept;
		this.form.reset();
	}

	getForm(): UntypedFormGroup {
		return this.form;
	}

	isValidForm(): boolean {
		return this.form.valid;
	}

	areRequiredFieldsCompleted(): boolean {
		const snomedCompleted = this.form.get('snomed').valid;
		const healthConditionIdCompleted = this.form.get('serviceRequest').get('healthConditionSctid').valid;
		const categoryIdCompleted = this.form.get('serviceRequest').get('categoryId').valid;
		return snomedCompleted && healthConditionIdCompleted && categoryIdCompleted;
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
		this.hasProcedure.next(false);
		this.orders = removeFrom<CreateOutpatientProcedureDto>(this.orders, index);
		this.data = removeFrom<Procedure>(this.data, index);
		this.emitter.next(this.data)
	}

	setDate(date: Date) {
		this.form.patchValue({
			performedDate: toApiFormat(date)
		})
	}


	private add(procedimiento: Procedure): boolean {
		this.hasProcedure.next(this.isEmpty());
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
}


interface Procedure {
	snomed: SnomedDto;
	performedDate?: string;
}
