import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { SnomedDto, SnomedECL } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';

export interface Alergia {
	snomed: SnomedDto;
	criticalityId: number;
}

export class AlergiasNuevaConsultaService {

	private form: FormGroup;
	private data: Alergia[] = [];
	private snomedConcept: SnomedDto;
	private criticalityTypes: any[];
	private readonly ECL = SnomedECL.ALLERGY;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService
	) {

		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			criticality: [null, Validators.required],
		});

	}

	getDisplayName(criticalityId) {
		return this.criticalityTypes.find(criticalityType => criticalityType.id === criticalityId)?.display;
	}

	setCriticalityTypes(criticalityTypes): void {
		this.criticalityTypes = criticalityTypes;
	}

	getCriticalityTypes() {
		return this.criticalityTypes;
	}

	getAlergias(): Alergia[] {
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

	add(alergia: Alergia): boolean {
		const currentItems = this.data.length;
		this.data = pushIfNotExists<Alergia>(this.data, alergia, this.compareSpeciality);
		return currentItems === this.data.length;
	}

	addControl(alergia: Alergia): void {
		if (this.add(alergia))
			this.snackBarService.showError("Alergia duplicada");
	}

	compareSpeciality(data: Alergia, data1: Alergia): boolean {
		return data.snomed.sctid === data1.snomed.sctid;
	}

	removeAlergia(index: number): void {
		this.data = removeFrom<Alergia>(this.data, index);
	}

	addToList(): boolean {
		if (this.form.valid && this.snomedConcept) {
			const alergia: Alergia = {
				snomed: this.snomedConcept,
				criticalityId: this.form.value.criticality,
			};
			this.addControl(alergia);
			this.resetForm();
			return true;
		}
		return false;
	}

	getForm(): FormGroup {
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

	getECL(): SnomedECL {
		return this.ECL;
	}

	isEmpty(): boolean {
		return (!this.data || this.data.length === 0);
	}

}
