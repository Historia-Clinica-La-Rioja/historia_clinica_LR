import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { saveAs } from 'file-saver';

import { FileResponse, FileRequest } from '@api-rest/services/binary/file-download.model';
import { ApiErrorMessageDto } from '@api-rest/api-model';
import { DownloadMessageArgs } from './file-download-card/file-download-card.component';

const DM_START: DownloadMessageArgs = {
	icon: 'downloading',
	text: 'download.content.IN_PROGRESS',
};

const DM_READY: DownloadMessageArgs = {
	icon: 'send',
	text: 'download.content.READY',
};

const DM_CANT_PREVIEW: DownloadMessageArgs = {
	icon: 'warning',
	text: 'download.content.CANT_PREVIEW',
};

const GENERIC_CODE = 'GENERIC_ERROR';
const TRANSLATED_CODES = ['SAVE_IOEXCEPTION', 'NON_EXIST'];

const translationKey = (code: string = GENERIC_CODE) => `download.content.${code}`;

const validCode = (code: string): string =>
	TRANSLATED_CODES.includes(code) ? code : GENERIC_CODE;

const buildDownloadMessageError = (error: ApiErrorMessageDto): DownloadMessageArgs => ({
	icon: 'report',
	text: translationKey(validCode(error.code)),
});

interface ActionButton {
	name: 'buttons.DOWNLOAD' | 'buttons.SHARE';
	icon: string;
	disabled: boolean;
	color?: string;
};

const buildActions = (downloadedFileBinary: File, enableSharing: boolean): ActionButton[] => {
	const shareAction: ActionButton = {
		name: 'buttons.SHARE',
		icon: 'share',
		disabled: !navigatorShare(downloadedFileBinary),
	};

	const downloadAction: ActionButton = {
		name: 'buttons.DOWNLOAD',
		icon: 'download',
		disabled: !downloadedFileBinary,
		color: 'primary'
	};

	if (enableSharing) return [
		downloadAction,
		shareAction, // no funciona bien el compartir
	]

	return [
		downloadAction,
	];
}

@Component({
	selector: 'app-view-pdf',
	templateUrl: './view-pdf.component.html',
	styleUrls: ['./view-pdf.component.scss']
})
export class ViewPdfComponent {
	viewFile: FileRequest;
	actions: ActionButton[];
	fileResponse: FileResponse;
	private downloadedFileBinary: File;
	dataIndex = 0;
	canPreview = false;
	dialogData: DownloadMessageArgs = DM_START;
	enableSharing = false;

	constructor(
		private dialogRef: MatDialogRef<ViewPdfComponent>,
		@Inject(MAT_DIALOG_DATA)
		public data: FileRequest[],
	) {
		this.dialogData = DM_START;
		this.viewFile = data[this.dataIndex];
	}

	nextFile(): void {
		const nextIndex = this.dataIndex + 1;
		if (nextIndex < this.data.length) {
			this.dataIndex = nextIndex;
			this.viewFile = this.data[this.dataIndex];
			// clean state
			this.fileResponse = undefined;
			this.downloadedFileBinary = undefined;
			this.actions = undefined;
			this.canPreview = false;
			this.dialogData = DM_START;
		} else {
			this.dialogRef.close();
		}
	}

	finishDownloading(result: FileResponse | ApiErrorMessageDto) {
		if ('content' in result) {
			const downloadedFile = result;
			this.fileResponse = downloadedFile;
			this.downloadedFileBinary = new File([downloadedFile.content], this.filename);
			this.canPreview = isPDF(downloadedFile.contentType);
			this.dialogData = DM_READY;
		} else {
			this.dialogData = buildDownloadMessageError(result);
		}

		this.actions = buildActions(this.downloadedFileBinary, this.enableSharing);
	}

	disablePreview($error: any): void {
		console.error('Error visualizando PDF', $error);
		this.canPreview = false;
		this.dialogData = DM_CANT_PREVIEW;
	}

	get filename(): string {
		return this.fileResponse?.filename || this.viewFile?.filename || 'HSI_NEW_DOCUMENT';
	}

	get position(): string {
		const humanIndex = this.dataIndex + 1;
		return humanIndex < this.data.length ? `${humanIndex}/${this.data.length}` : '';
	}

	get cardActions(): ActionButton[] {
		const resultActions = this.actions ? [...this.actions] : [];
		return resultActions.reverse();
	}

	runAction(action: ActionButton): void {
		if (action.name === 'buttons.DOWNLOAD') {
			saveAs(this.downloadedFileBinary, this.filename);
		} else if (action.name === 'buttons.SHARE') {
			shareFile(this.downloadedFileBinary, this.filename);
		}
	}
}

const isPDF = (contentType: string): boolean => contentType === 'application/pdf';

const navigatorShare = (file: File): any => {
	if (!file) {
		return undefined;
	}

	const navigator: any = window.navigator;
	if (!navigator?.canShare || !navigator?.canShare({ files: [file] })) {
		return undefined;
	}

	return navigator?.share;
}

const shareFile = (file: File, filename: string) => {
	const shareFnc = navigatorShare(file);

	if (!shareFnc) {
		return;
	}

	shareFnc({
        files: [file],
        title: filename,
        text: `Te comparto este archivo ${filename}`
	})
	.then(() => console.debug(`Archivo ${filename} compartido`))
	.catch((error) => console.warn(`Compartiendo archivo ${filename}`, error));
}

