import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { SnomedDto, SnomedECL } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { newDate } from '@core/utils/moment.utils';
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { Subject } from 'rxjs';

export interface AntecedenteFamiliar {
	snomed: SnomedDto;
	fecha: Date;
}

export class AntecedentesFamiliaresNuevaConsultaService {

	private form: UntypedFormGroup;
	private data: AntecedenteFamiliar[];
	private snomedConcept: SnomedDto;
	private readonly ECL = SnomedECL.FAMILY_RECORD;

	dataEmitter = new Subject<AntecedenteFamiliar[]>()
	data$ = this.dataEmitter.asObservable();

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService) {

		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			fecha: [null]
		});

		this.data = [];
	}

	getAntecedentes(): AntecedenteFamiliar[] {
		return this.data;
	}

	getSnomedConcept(): SnomedDto {
		return this.snomedConcept;
	}

	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	add(antecedente: AntecedenteFamiliar): boolean {
		const currentItems = this.data.length;
		this.data = pushIfNotExists<any>(this.data, antecedente, this.compareAntecedenteFamiliar);
		this.dataEmitter.next(this.data);
		return currentItems === this.data.length;
	}

	compareAntecedenteFamiliar(data: AntecedenteFamiliar, data1: AntecedenteFamiliar): boolean {
		return data.snomed.sctid === data1.snomed.sctid;
	}

	addToList(): boolean {
		if (this.form.valid && this.snomedConcept) {
			const antecedente: AntecedenteFamiliar = {
				snomed: this.snomedConcept,
				fecha: this.form.value.fecha
			};
			if (this.add(antecedente))
				this.snackBarService.showError("Antecedente familiar duplicado");
			this.resetForm();
			return true;
		}
		return false;
	}

	remove(index: number): void {
		this.data = removeFrom<AntecedenteFamiliar>(this.data, index);
		this.dataEmitter.next(this.data)
	}


	getForm(): UntypedFormGroup {
		return this.form;
	}


	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
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

	getMaxFecha(): Date {
		return newDate();
	}

	getECL(): SnomedECL {
		return this.ECL;
	}

	isEmpty(): boolean {
		return (!this.data || this.data.length === 0);
	}

	setFamilyHistories(familyHistories: AntecedenteFamiliar[]){
		familyHistories.forEach(familyHistory => {
			this.form.controls.fecha.setValue(familyHistory.fecha);
			this.setConcept(familyHistory.snomed);
			this.addToList();
		});
	}

}
