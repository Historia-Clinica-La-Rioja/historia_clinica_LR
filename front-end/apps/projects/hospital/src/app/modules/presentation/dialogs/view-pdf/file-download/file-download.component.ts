import { Component, EventEmitter, Input, Output } from '@angular/core';
import { EMPTY, catchError } from 'rxjs';

import { ApiErrorMessageDto } from '@api-rest/api-model';

import {
	FileRequest,
	FileResponse,
	FileDownloadStatus,
} from '@api-rest/services/binary/file-download.model';

@Component({
	selector: 'app-file-download',
	templateUrl: './file-download.component.html',
	styleUrls: ['./file-download.component.scss']
})
export class FileDownloadComponent {

	@Output() onFinish = new EventEmitter<FileResponse | ApiErrorMessageDto>();

	fileDownloadStatus: FileDownloadStatus;

	constructor(

	) { }

	@Input() set viewFile(fileRequest: FileRequest) {
		this.fileDownloadStatus = undefined;
		fileRequest.downloadStatus.pipe(
			catchError(error => {
				this.onFinish.emit(error);
				return EMPTY;
			}),
		).subscribe(
			f => {
				this.fileDownloadStatus = f;
				if (f.state === 'DONE') {
					this.onFinish.emit(f.response);
				}
			}
		);
	}

}


