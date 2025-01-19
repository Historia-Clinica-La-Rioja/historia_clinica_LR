import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ChangeEvent } from 'react';

@Component({
	selector: 'app-download-button',
	templateUrl: './download-button.component.html',
	styleUrls: ['./download-button.component.scss']
})
export class DownloadButtonComponent {

	selectedFiles: File[] = [];
	selectedFilesShow = [];
	formReferenceClosure: FormGroup;

	@Input() description: string;
	@Output() selectedFilesEmiit: EventEmitter<File[]> = new EventEmitter<File[]>();

	onSelectFileFormData($event: ChangeEvent<HTMLInputElement>) {
		Array.from($event.target.files).forEach((file: File) => {
			this.selectedFiles.push(file);
			this.selectedFilesShow.push(file.name);
			this.selectedFilesEmiit.emit(this.selectedFiles);
		});
	}

	removeSelectedFile(index: number) {
		this.selectedFiles.splice(index, 1);
		this.selectedFilesShow.splice(index, 1);
		this.selectedFilesEmiit.emit(this.selectedFiles);
	}

}
