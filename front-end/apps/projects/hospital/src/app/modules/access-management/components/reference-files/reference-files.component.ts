
import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { ReferenceCounterReferenceFileDto } from '@api-rest/api-model';
import { ReferenceFileService } from '@api-rest/services/reference-file.service';

@Component({
	selector: 'app-reference-files',
	templateUrl: './reference-files.component.html',
	styleUrls: ['./reference-files.component.scss'],
	changeDetection: ChangeDetectionStrategy.OnPush

})
export class ReferenceFilesComponent {

	referenceFiles: ReferenceFiles;
	deletedFiles: number[] = [];

	@Input()
	set oldFiles(files: ReferenceCounterReferenceFileDto[]) {
		this.referenceFiles = { oldFiles: files, newFiles: [] }
		this.emit();
	}

	@Output() selectedFiles = new EventEmitter<ReferenceFiles>();
	@Output() deletedIdFiles = new EventEmitter<number[]>();

	constructor(
		private readonly referenceFileService: ReferenceFileService,
	) { }

	addNewFile(file: Event) {
		const fileTarget = file.target as HTMLInputElement;
		this.referenceFiles = {
			...this.referenceFiles,
			newFiles: this.referenceFiles.newFiles.concat(Array.from(fileTarget.files))
		};
		this.emit();
	}

	downloadFile(file: ReferenceCounterReferenceFileDto) {
		this.referenceFileService.downloadReferenceFiles(file.fileId, file.fileName);
	}

	removeFile(files: File[] | ReferenceCounterReferenceFileDto[], positionOfFileToDelete: number, removedFile: ReferenceCounterReferenceFileDto) {
		if (this.referenceFiles.oldFiles.find( file => file.fileId === removedFile.fileId))
			this.deletedFiles.push(removedFile.fileId);
		
		files.splice(positionOfFileToDelete, 1);
		this.emit();
	}

	private emit() {
		this.selectedFiles.emit(this.referenceFiles);
		this.deletedIdFiles.emit(this.deletedFiles)
	}

}

export interface ReferenceFiles {
	oldFiles: ReferenceCounterReferenceFileDto[];
	newFiles: File[];
}