import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';
import { pushTo, removeFrom } from '@core/utils/array.utils';
import { SnomedECL } from '@api-rest/api-model';
import { SnomedDto } from '@api-rest/api-model';
import { ExternalCauseDto } from '@api-rest/api-model';
import { Injectable } from '@angular/core';
import { BasicTable } from '@material/model/table.model';
import { BehaviorSubject, Subject } from 'rxjs';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnomedSemanticSearch } from '@historia-clinica/services/snomed.service';
import { Observable } from 'rxjs';

@Injectable()

export class ExternalCauseService {
	ecl = SnomedECL.EVENT;
	snomedConceptEvent: SnomedDto;
	snomedConceptEvent$: Subject<SnomedDto> = new Subject<SnomedDto>();
	externalCause = new BehaviorSubject<ExternalCauseDto>(null);

	private tableSubject = new BehaviorSubject<BasicTable<SnomedDto>>(null);

	formEvent = new UntypedFormGroup({
		snomedEvent: new UntypedFormControl('', Validators.required)
	});

	table: BasicTable<SnomedDto> = {
		data: [],
		columns: [
			{
				def: 'eventType',
				header: 'internaciones.anamnesis.externalCause.EVENT',
				display: ap => ap.pt
			}
		],
		displayedColumns: ['pt', 'remove'],
	};

	table$ = this.tableSubject.asObservable();

	constructor(
		private readonly snomedService: SnomedService,
	) { }

	setValue(externalCause: ExternalCauseDto) {
		this.externalCause.next(externalCause);
		externalCause?.snomed && this.setConceptEventSnomed(externalCause?.snomed);
		this.addEvent();
	}

	getValue(): Observable<ExternalCauseDto> {
		return this.externalCause.asObservable();
	}

	remove(index: number) {
		this.table.data = removeFrom<SnomedDto>(this.table.data, index);
		this.tableSubject.next(this.table);
		this.formEvent.reset();
		this.snomedConceptEvent = null;
		this.snomedConceptEvent$.next(null);
		this.getTableInit();
	}

	openSearchDialogEvent(searchValue: string) {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: SnomedECL.EVENT
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConceptEventSnomed(selectedConcept));
		}
	}

	setConceptEventSnomed(selectedConcept: SnomedDto) {
		this.snomedConceptEvent = selectedConcept;
		this.snomedConceptEvent$.next(selectedConcept);
		const pt = selectedConcept ? selectedConcept.pt : '';
		this.formEvent.controls.snomedEvent.setValue(pt);
	}

	addEvent() {
		if (this.snomedConceptEvent) {
			this.table.data = pushTo<SnomedDto>(this.table?.data, this.snomedConceptEvent);
			if (this.formEvent.valid) {
				this.formEvent.reset();
			}
			this.tableSubject.next(this.getTable());
		}
	}

	resetForm() {
		this.formEvent.reset();
		this.snomedConceptEvent = null;
	}

	getTableInit() {
		this.tableSubject.next(this.getTable());
	}

	getTable(): BasicTable<SnomedDto> {
		return {
			data: this.table.data,
			columns: this.table.columns,
			displayedColumns: this.table.displayedColumns
		};
	}

	getSnomedConceptEvent() {
		return this.snomedConceptEvent$.asObservable();
	}
}
