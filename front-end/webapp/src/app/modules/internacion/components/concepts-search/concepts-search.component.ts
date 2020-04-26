import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConceptsSearchDialogComponent } from '../../dialogs/concepts-search-dialog/concepts-search-dialog.component';
import { SnomedDto } from '@api-rest/api-model';
import { TranslateService } from '@ngx-translate/core';

@Component({
	selector: 'app-concepts-search',
	templateUrl: './concepts-search.component.html',
	styleUrls: ['./concepts-search.component.scss']
})
export class ConceptsSearchComponent implements OnInit {

	@Input() label: string = '';
	@Output() onSelect = new EventEmitter<SnomedDto>();

	searchValue: string = '';
	translatedLabel: string = '';

	constructor(
		private translateService: TranslateService,
		public dialog: MatDialog,
	) { }

	ngOnInit(): void {
		this.translateService.get(this.label).subscribe(
			translatedText => this.translatedLabel = translatedText.toLowerCase()
		);
	}

	openDialog(): void {
		if (!this.searchValue) return;
		const dialogRef = this.dialog.open(ConceptsSearchDialogComponent, {
			width: '70%',
			data: { searchValue: this.searchValue }
		});

		dialogRef.afterClosed().subscribe(
			(selectedConcept: SnomedDto) => {
				if (selectedConcept) this.clear();
				this.onSelect.emit(selectedConcept);
			}
		);
	}

	clear(): void {
		this.searchValue = '';
	}

}
