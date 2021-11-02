import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-img-uploader',
	templateUrl: './img-uploader.component.html',
	styleUrls: ['./img-uploader.component.scss']
})
export class ImgUploaderComponent {

	@Input() buttonLabel: string;
	@Input() icon: string;
	@Input() validations: ImgValidation;

	@Output() selectFiles = new EventEmitter<File>();

	@ViewChild('fileInput') fileInput: ElementRef;

	constructor() { }

	onSelectFile($event) {
		const imgFile = $event.target.files[0];
		this.imgLoading(imgFile).subscribe(data => {
				if (this.validations.height !== data.height || this.validations.width !== data.width) {
					this.fileInput.nativeElement.value = '';
					this.selectFiles.emit(null);
				} else {
					this.fileInput.nativeElement.value = '';
					this.selectFiles.emit(imgFile);
				}
		});
	}

	imgLoading(file: File): Observable<{width: number; height: number}> {
		return new Observable((observer) => {
				const img = new Image();
				img.onload = () => {
					observer.next({width: img.width, height: img.height});
					observer.complete();
				};
				img.src = URL.createObjectURL(file);
			});
	}

}

export interface ImgValidation {
	acceptFormat: string;
	width: number;
	height: number;
}
