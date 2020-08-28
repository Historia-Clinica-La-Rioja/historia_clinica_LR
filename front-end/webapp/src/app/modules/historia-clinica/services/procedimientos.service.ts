import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnomedSemanticSearch, SnomedService } from './snomed.service';
import { SnomedDto } from '@api-rest/api-model';
import { ColumnConfig } from '@presentation/components/document-section/document-section.component';
import { SEMANTICS_CONFIG } from '../constants/snomed-semantics';
import { pushTo } from '@core/utils/array.utils';
import { DateFormat, momentFormat, newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';

export interface Procedimiento {
	snomed: SnomedDto;
	fecha?: Moment;
}

export class ProcedimientosService {

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
				header: 'historia-clinica.procedimientos.PROCEDIMIENTO',
				text: (row) => row.snomed.pt
			},
			{
				def: 'fecha',
				header: 'historia-clinica.procedimientos.FECHA',
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

	add(procedimiento: Procedimiento): void {
		this.data = pushTo<Procedimiento>(this.data, procedimiento);
	}

	addToList() {
		if (this.form.valid && this.snomedConcept) {
			const nuevoProcedimiento: Procedimiento = {
				snomed: this.snomedConcept,
				fecha: this.form.value.fecha
			};
			this.add(nuevoProcedimiento);
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

	getProcedimientos(): Procedimiento[] {
		return this.data;
	}

	getSnomedConcept(): SnomedDto {
		return this.snomedConcept;
	}

	getFechaMax(): Moment {
		return newMoment();
	}
}
