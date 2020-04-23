import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConceptsSearchDialogComponent } from '../../dialogs/concepts-search-dialog/concepts-search-dialog.component';
import { SnomedDto } from '@api-rest/api-model';

@Component({
	selector: 'app-concepts-search',
	templateUrl: './concepts-search.component.html',
	styleUrls: ['./concepts-search.component.scss']
})
export class ConceptsSearchComponent implements OnInit {

	@Output() onSelect = new EventEmitter<SnomedDto>();

	searchValue: string = '';

	constructor(
		public dialog: MatDialog
	) { }

	ngOnInit(): void { }

	openDialog(): void {
		if (!this.searchValue) return;
		const dialogRef = this.dialog.open(ConceptsSearchDialogComponent, {
			width: '70%',
			data: { searchValue: this.searchValue }
		});

		dialogRef.afterClosed().subscribe(
			(selectedConcept: SnomedDto) => {
				this.onSelect.emit(selectedConcept);
				this.searchValue = selectedConcept.fsn;
			}
		);
	}

}
