import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { FileRequest } from '@api-rest/services/binary/file-download.model';
import { ViewPdfComponent } from './view-pdf.component';

@Injectable({
	providedIn: 'root'
})
export class ViewPdfService {
	private openDialog: MatDialogRef<any, void>;

	constructor(
		public dialog: MatDialog,
	) {

	}

	showFile(fileRequest: FileRequest): Observable<void> {
		if (this.openDialog) {
			this.openDialog.componentInstance.data.push(fileRequest);
			return;
		} else {
			this.openDialog = this.dialog.open(ViewPdfComponent, {
				width: '100vw',
				height: '100vh',
				maxWidth: '100vw',
				maxHeight: '100vh',
				panelClass: 'view-pdf-panel',
				data: [fileRequest],
			});
			this.openDialog.afterClosed().subscribe(
				() => this.openDialog = undefined
			);
		}

		return this.openDialog.afterClosed();
	}
}

export interface ViewPdfBo {
	url: URL;
	filename: string;
}
