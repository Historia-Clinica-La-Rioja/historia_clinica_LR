import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MedicationDto, SnomedDto } from '@api-rest/api-model';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { SEMANTICS_CONFIG } from '../../../constants/snomed-semantics';
import { pushTo, removeFrom } from '@core/utils/array.utils';

export class MedicacionesNuevaConsultaService {

	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	private form: FormGroup;
	private snomedConcept: SnomedDto;
	private readonly columns: ColumnConfig[];
	private data: any[];

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly snomedService: SnomedService
	) {
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			observaciones: [null],
			suspendido: [false]
		});

		this.columns = [
			{
				def: 'medicacion',
				header: 'ambulatoria.paciente.nueva-consulta.medicaciones.NOMBRE_MEDICACION',
				text: v => v.snomed
			},
			{
				def: 'observaciones',
				header: 'ambulatoria.paciente.nueva-consulta.medicaciones.OBSERVACIONES',
				text: v => v.observaciones
			},
		];

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

	add(medicacion: MedicationDto): void {
		this.data = pushTo<MedicationDto>(this.data, medicacion);
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			this.add(this.form.value);
			this.resetForm();
		}
	}

	remove(index: number): void {
		this.data = removeFrom<MedicationDto>(this.data, index);
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: this.SEMANTICS_CONFIG.medicine
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

	getForm(): FormGroup {
		return this.form;
	}

	getSnomedConcept(): SnomedDto {
		return this.snomedConcept;
	}

	getColumns(): ColumnConfig[] {
		return this.columns;
	}

	getData(): any[] {
		return this.data;
	}
}
