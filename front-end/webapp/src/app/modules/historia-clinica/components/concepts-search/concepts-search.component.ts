import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';

@Component({
	selector: 'app-concepts-search',
	templateUrl: './concepts-search.component.html',
	styleUrls: ['./concepts-search.component.scss']
})
export class ConceptsSearchComponent implements OnInit {

	@Input() label = '';
	@Output() search = new EventEmitter<string>();

	searchValue = '';
	translatedLabel = '';

	constructor(
		private readonly translateService: TranslateService,
		public dialog: MatDialog,
	) { }

	ngOnInit(): void {
		this.translateService.get(this.label).subscribe(
			translatedText => this.translatedLabel = translatedText.toLowerCase()
		);
	}

	emitSearch(): void {
		this.search.emit(this.searchValue);
	}

	clear(): void {
		this.searchValue = '';
	}

}
