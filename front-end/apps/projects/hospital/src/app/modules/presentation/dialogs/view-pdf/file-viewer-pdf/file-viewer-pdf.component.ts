import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PDFProgressData } from 'ng2-pdf-viewer';
import { FileResponse } from '@api-rest/services/binary/file-download.model';

@Component({
	selector: 'app-file-viewer-pdf',
	templateUrl: './file-viewer-pdf.component.html',
	styleUrls: ['./file-viewer-pdf.component.scss']
})
export class FileViewerPdfComponent implements OnInit {
	@Input() fileResponse: FileResponse;
	@Output() onError = new EventEmitter<any>();
	pdfProgress = 0;
	pdfIsLoading = true;
	pdfSrc: any;

	constructor() { }

	ngOnInit(): void {
		if (typeof (FileReader) !== 'undefined') {
			let reader = new FileReader();

			reader.onload = (e: any) => {
			  this.pdfSrc = e.target.result;
			};

			reader.readAsArrayBuffer(this.fileResponse.content);
		  }
	}

	onProgress(progressData: PDFProgressData) {
		this.pdfProgress = calcProgress(progressData);
		this.pdfIsLoading = this.pdfProgress < 100;
	}

	afterLoadComplete(): void {
		this.pdfIsLoading = false;
	}

}

const calcProgress = (progressData: PDFProgressData): number => {
	if (!progressData) return 0;
	return (progressData.loaded / progressData.total) * 100;
}
