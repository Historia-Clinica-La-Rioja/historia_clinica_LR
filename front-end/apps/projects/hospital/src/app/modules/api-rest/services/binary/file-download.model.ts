import { Observable } from 'rxjs';

export interface FileRequest {
	filename: string;
	downloadStatus: Observable<FileDownloadStatus>;
}

export interface FileDownloadStatus {
	state: 'PENDING' | 'IN_PROGRESS' | 'DONE' | 'ERROR';
	progress: FileDownloadProgress;
	response: FileResponse;
};

export interface FileResponse {
	status: number;
	filename: string;
	contentType: string;
	content: Blob | null;
}

export interface FileDownloadProgress {
	value: number;
	currentBytes: number;
	totalBytes: number;
}
