import { Injectable } from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { ViewPdfComponent } from "./view-pdf.component";

@Injectable({
	providedIn: 'root'
})
export class ViewPdfService {
	private openDialog: MatDialogRef<any, void>;

	constructor(
		public dialog: MatDialog,
	) {

	}

	showDialog(url: string, filename: string, params: Record<string, string> = {}): void {

		if (this.openDialog) {
			console.error('Abriendo un segundo archivo', params);
			return;
		}
		const data = newViewPdfBo(url + searchParamsString(params), filename);
		this.openDialog = this.dialog.open(ViewPdfComponent, {
			width: '100vw',
			height: '100vh',
			maxWidth: '100vw',
			maxHeight: '100vh',
			panelClass: 'view-pdf-panel',
			data,
		});
		// dialog.close();
		this.openDialog.afterClosed().subscribe(
			() => this.openDialog = undefined
		);
	}

	// showDemo() {
	// 	this.showDialog(
	// 		'/assets/ejemplos.pdf',
	// 		// url: newURL('https://hsi.pladema.net/wp-content/uploads/2021/10/Backoffice_V-1-11-0-OK.docx.pdf'),
	// 		'HSI Documentación',
	// 	);
	// }
}

export interface ViewPdfBo {
	url: URL;
	filename: string;
}

export const newViewPdfBo = (url: string, filename: string): ViewPdfBo => ({
	url: newURL(url),
	filename,
});

const searchParamsString = (params: Record<string, string>): string =>
	!params? '' : '?' + new URLSearchParams(params).toString();

const newURL = (url: string):URL => new URL(url, window.location.href);
