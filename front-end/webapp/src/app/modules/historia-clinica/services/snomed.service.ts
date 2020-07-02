import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConceptsSearchDialogComponent } from '../dialogs/concepts-search-dialog/concepts-search-dialog.component';
import { Observable } from 'rxjs';
import { SnomedDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class SnomedService {

	constructor(
		public dialog: MatDialog
	) {
	}

	public openConceptsSearchDialog(data: SnomedSemanticSearch): Observable<SnomedDto> {
		const dialogRef = this.dialog.open(ConceptsSearchDialogComponent, {
			disableClose: true,
			width: '70%',
			data
		});

		return dialogRef.afterClosed();
	}
}

export interface SnomedSemanticSearch {
	eclFilter: string;
	searchValue: string;
}
