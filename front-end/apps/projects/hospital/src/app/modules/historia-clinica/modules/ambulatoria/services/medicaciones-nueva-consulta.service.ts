import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MedicationDto, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { TEXT_AREA_MAX_LENGTH } from '@core/constants/validation-constants';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { BehaviorSubject, Subject } from 'rxjs';
export interface Medicacion {
	snomed: SnomedDto;
	observaciones?: string;
	suspendido?: boolean;
}

export class MedicacionesNuevaConsultaService {

	private form: UntypedFormGroup;
	private snomedConcept: SnomedDto;
	private data: Medicacion[];
	public readonly TEXT_AREA_MAX_LENGTH = TEXT_AREA_MAX_LENGTH;
	private readonly ECL = SnomedECL.MEDICINE;

	medicacionEmitter = new Subject()
	medicaciones$ = this.medicacionEmitter.asObservable();

	private isEmptySource = new BehaviorSubject<boolean>(true);
	isEmpty$ = this.isEmptySource.asObservable();


	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,

	) {
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			observaciones: [null, [Validators.maxLength(this.TEXT_AREA_MAX_LENGTH)]],
			suspendido: [false]
		});

		this.data = [];
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
	}

	add(medicacion: Medicacion): boolean {
		const currentItems = this.data.length;
		this.data = pushIfNotExists<Medicacion>(this.data, medicacion, this.compareSpeciality);
		this.medicacionEmitter.next(this.data);
		this.isEmptySource.next(this.isEmpty());
		return currentItems === this.data.length;
	}

	addControl(medicacion: Medicacion): void {
		if (this.add(medicacion))
			this.snackBarService.showError("Medicación duplicada");
	}

	compareSpeciality(data: Medicacion, data1: Medicacion): boolean {
		return data.snomed.sctid === data1.snomed.sctid;
	}

	addToList(): boolean {
		if (this.form.valid && this.snomedConcept) {
			const nuevaMedicacion: Medicacion = {
				snomed: this.snomedConcept,
				observaciones: this.form.value.observaciones,
				suspendido: this.form.value.suspendido
			};
			this.addControl(nuevaMedicacion);
			this.resetForm();
			return true;
		}
		return false;
	}

	remove(index: number): void {
		this.data = removeFrom<Medicacion>(this.data, index);
		this.medicacionEmitter.next(this.data);
		this.isEmptySource.next(this.isEmpty());
	}


	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: this.ECL
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

	getForm(): UntypedFormGroup {
		return this.form;
	}

	getSnomedConcept(): SnomedDto {
		return this.snomedConcept;
	}

	getMedicaciones(): Medicacion[] {
		return this.data;
	}

	getMedicationsAsMedicationDto(): MedicationDto[] {
		return this.mapToMedicationDto();
	}

	private mapToMedicationDto(): MedicationDto[] {
		return this.data.map(medication => {
			return {
				snomed: medication.snomed,
				note: medication.observaciones,
				suspended: medication.suspendido
			}
		})
	}

	getState(suspendido: boolean): string {
		return suspendido ? 'Suspendido' : 'Activo'
	}

	getECL(): SnomedECL {
		return this.ECL;
	}

	isEmpty(): boolean {
		return (!this.data || this.data.length === 0);
	}
}
