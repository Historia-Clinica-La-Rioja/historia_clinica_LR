import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { PersonFileDto } from '@api-rest/api-model';
import { PersonFileService } from '@api-rest/services/person-file.service';
import { Observable, of } from 'rxjs';

@Component({
	selector: 'app-files-uploader',
	templateUrl: './files-uploader.component.html',
	styleUrls: ['./files-uploader.component.scss']
})
export class FilesUploaderComponent implements OnInit {
	@Input() personFiles: PersonFileDto;
	@Input() personId: number;
	@Input()
	set hasToSaveFiles(value: boolean) {
		if (value) {
			this.save();
		}
	}
	selectedFiles: File[] = [];
	selectedFilesShow: any[] = [];
	@Output() filesId = new EventEmitter<Observable<number[]>>();

	constructor(private personFileService: PersonFileService) { }

	ngOnInit() {
		this.filesId.emit();
	}

	onSelectFileFormData($event: any) {
		Array.from($event.target.files).forEach((file: File) => {
			this.selectedFiles.push(file);
			this.selectedFilesShow.push(file.name);
		});
	}

	removeSelectedFile(index: number) {
		this.selectedFiles.splice(index, 1);
		this.selectedFilesShow.splice(index, 1);
	}

	save() {
		if (this.selectedFiles.length) {
			this.personFileService.saveFiles(this.personId, this.selectedFiles).subscribe((filesIds: number[]) => {
				this.filesId.emit(of(filesIds));
			})
		} else {
			this.filesId.emit(of([0]));
		}
	}

	download(fileId: number, fileName: string): void {
		this.personFileService.downloadFile(fileId, this.personId, fileName);
	}
}
