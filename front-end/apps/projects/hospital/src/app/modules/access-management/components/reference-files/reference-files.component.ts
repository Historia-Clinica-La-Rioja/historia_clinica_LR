
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

	@Input()
	set oldFiles(files: ReferenceCounterReferenceFileDto[]) {
		this.referenceFiles = { oldFiles: files, newFiles: [] }
		this.emit();
	}

	@Output() selectedFiles = new EventEmitter<ReferenceFiles>();
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

	removeFile(files: File[] | ReferenceCounterReferenceFileDto[], positionOfFileToDelete: number) {
		files.splice(positionOfFileToDelete, 1);
		this.emit();
	}

	private emit() {
		this.selectedFiles.emit(this.referenceFiles);
	}

}

export interface ReferenceFiles {
	oldFiles: ReferenceCounterReferenceFileDto[];
	newFiles: File[];
}