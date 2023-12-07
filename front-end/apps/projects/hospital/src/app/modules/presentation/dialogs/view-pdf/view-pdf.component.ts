import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ViewPdfBo } from './view-pdf.service';
import { PDFDocumentProxy, PDFProgressData } from 'ng2-pdf-viewer';
import { saveAs } from 'file-saver';

@Component({
	selector: 'app-view-pdf',
	templateUrl: './view-pdf.component.html',
	styleUrls: ['./view-pdf.component.scss']
})
export class ViewPdfComponent {
	public error: any = undefined;
	public progressValue = 0;
	public isLoading = true;
	public canShareFile = false;

	// private pdf: PDFDocumentProxy;
	private pdfFile: File;
	private metadataFilename = '';

	showShareButton = false;

	constructor(
		private dialogRef: MatDialogRef<ViewPdfComponent>,
		@Inject(MAT_DIALOG_DATA)
		public data: ViewPdfBo,
	) {

	}

	closeModal(): void {
		this.dialogRef.close();
	}

	onProgress(progressData: PDFProgressData) {
		this.progressValue = calcProgress(progressData);
		this.isLoading = this.progressValue < 100;
	}

	afterLoadComplete(pdf: PDFDocumentProxy): void {
		buildFile(pdf, this.data.filename + '.pdf').then(file => {
			this.pdfFile = file;
			this.canShareFile = canShare(file);
		});

		pdf.getMetadata().then(data => this.metadataFilename = data['contentDispositionFilename']);
	}

	onError(error: any) {
		this.error = error;
	}

	get filename(): string {
		return this.metadataFilename || this.pdfFile?.name || this.data.filename;
	}

	download() {
		saveAs(this.pdfFile, this.filename);
	}

	share() {
		shareFile(this.pdfFile, this.filename);
	}
}

const buildFile = (pdf: PDFDocumentProxy, filename: string): Promise<File> =>
	pdf.getData().then((u8) => {
		let blob = new Blob([u8.buffer], {
			type: 'application/pdf'
		});
		return new File([blob], filename);
	});

const calcProgress = (progressData: PDFProgressData): number => {
	if (!progressData) return 0;
	return (progressData.loaded / progressData.total) * 100;
}

const canShare = (file: File): boolean => {
	const navigator: any = window.navigator;
	return !!navigator?.share && !!navigator?.canShare && navigator?.canShare({ files: [file] })
}

const shareFile = (file: File, filename: string) => {
	const navigator: any = window.navigator;

	navigator.share({
        files: [file],
        title: filename,
        text: `Te comparto este archivo ${filename}`
	});
}
