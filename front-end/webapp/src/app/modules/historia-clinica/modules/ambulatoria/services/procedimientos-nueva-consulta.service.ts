import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from '../../../services/snomed.service';
import { SnomedDto } from '@api-rest/api-model';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { SEMANTICS_CONFIG } from '../../../constants/snomed-semantics';
import { pushTo } from '@core/utils/array.utils';
import { DateFormat, momentFormat, newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';

export class ProcedimientosNuevaConsultaService {

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
			fecha: [null],
		});

		this.columns = [
			{
				def: 'procedimiento',
				header: 'ambulatoria.paciente.nueva-consulta.procedimientos.PROCEDIMIENTO',
				text: (row) => row.snomed
			},
			{
				def: 'fecha',
				header: 'ambulatoria.paciente.nueva-consulta.procedimientos.FECHA',
				text: (row) => row.fecha ? momentFormat(row.fecha, DateFormat.VIEW_DATE) : ''
			}

		];
		this.data = [];
	}


	setConcept(selectedConcept: SnomedDto): void {
		this.snomedConcept = selectedConcept;
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
	}

	add(procedimiento: any): void {
		this.data = pushTo<any>(this.data, procedimiento);
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			this.add(this.form.value);
			this.resetForm();
		}
	}

	resetForm(): void {
		delete this.snomedConcept;
		this.form.reset();
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: this.SEMANTICS_CONFIG.procedure
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

	getForm(): FormGroup {
		return this.form;
	}

	getColumns(): ColumnConfig[] {
		return this.columns;
	}

	getData(): any[] {
		return this.data;
	}

	getSnomedConcept(): SnomedDto {
		return this.snomedConcept;
	}

	getFechaMax(): Moment {
		return newMoment();
	}
}
