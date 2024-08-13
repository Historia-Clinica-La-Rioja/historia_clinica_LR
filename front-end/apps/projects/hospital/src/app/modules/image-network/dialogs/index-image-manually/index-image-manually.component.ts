import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, UntypedFormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { hasError } from '@core/utils/form.utils';
import { ButtonType } from '@presentation/components/button/button.component';
import { IndexingImageStatusComponent } from '../indexing-image-status/indexing-image-status.component';
import { ImageQueueService } from '@api-rest/services/image-queue.service';

@Component({
	selector: 'app-index-image-manually',
	templateUrl: './index-image-manually.component.html',
	styleUrls: ['./index-image-manually.component.scss']
})
export class IndexImageManuallyComponent implements OnInit {

	form: FormGroup;
	errorMessage: string;
	readonly ButtonType = ButtonType;
	readonly hasError = hasError;

	constructor(
		@Inject(MAT_DIALOG_DATA) public readonly data: number,
		private readonly dialogRef: MatDialogRef<IndexImageManuallyComponent>,
		private readonly imageQueueService: ImageQueueService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly dialog: MatDialog,
	) { }

	indexImage() {
		if (this.form.value && this.form.valid) {
			this.indexImageManually();
		}
	}

	ngOnInit() {
		this.form = this.formBuilder.group({
			ImageUID: [null, Validators.required]
		})
	}

	private indexImageManually() {
		this.imageQueueService.indexImageManually(this.data, this.form.value.ImageUID).subscribe({
			next: (success) => this.handleIndexingSuccess(success),
			error: () => this.handleIndexingError()
		});
	}

	private handleIndexingSuccess(success: boolean) {
		if (success) {
			const indexingDialogRef = this.openIndexingImageManually();
			indexingDialogRef.afterClosed().subscribe(() => {
				this.dialogRef.close(true);
			});
		}
	}

	private handleIndexingError() {
		this.errorMessage = 'image-network.queue_list.image_indexing.INDEX_IMAGE_MANUALLY_ERROR_INDEXING';
		this.form.controls.ImageUID.setErrors({ 'indexingError': true });
	}

	private openIndexingImageManually(): MatDialogRef<IndexingImageStatusComponent> {
		return this.dialog.open(IndexingImageStatusComponent, {
			width: '30%',
			autoFocus: false,
			data: {
				icon: 'watch_later',
				iconColor: 'primary',
				popUpMessage: 'image-network.queue_list.image_indexing.INDEX_IMAGE_MANUALLY_VERIFY',
				popUpTitle: 'image-network.queue_list.image_indexing.INDEX_IMAGE_MANUALLY_VERIFY_INDEXED',
				acceptBtn: true,
			}
		});
	}
}
