import { Component, EventEmitter, Input, Output } from '@angular/core';
import { removeAccents } from '@core/utils/core.utils';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
	selector: 'app-search-cases',
	templateUrl: './search-cases.component.html',
	styleUrls: ['./search-cases.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})

export class SearchCasesComponent {

	_filters: SEARCH_CASES[] = [];

	@Input() label: string;

	@Input()
	set filters(value: SEARCH_CASES[]) {
		this._filters = value;
	};

	@Output() descriptionFiltered: EventEmitter<string> = new EventEmitter();

	resultEmmiter(description: string) {
		if (description) {
			this._filters.forEach(filterType => {
				description = FILTER_CASES[filterType](description);
			})
		}
		this.descriptionFiltered.emit(description);
	}
}

const REMOVE_DOT = /[.]/g;

export enum SEARCH_CASES {
	LOWER_CASE = 'Eliminar mayusculas',
	REMOVE_DOT = 'Eliminar puntos',
	REMOVE_ACCENTS = 'Eliminar acentos',
}

export const FILTER_CASES = {
	[SEARCH_CASES.LOWER_CASE]: (str: string) => str.toLowerCase(),
	[SEARCH_CASES.REMOVE_DOT]: (str: string) => str.replace(REMOVE_DOT, ''),
	[SEARCH_CASES.REMOVE_ACCENTS]: (str: string) => removeAccents(str),
}
