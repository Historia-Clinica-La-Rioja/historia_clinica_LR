import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ERole, EpisodeDocumentTypeDto } from '@api-rest/api-model';
import { InternmentEpisodeDocumentService } from '@api-rest/services/internment-episode-document.service';
import { PermissionsService } from '@core/services/permissions.service';
import { ExtesionFile } from '@core/utils/extensionFile';
import { hasError, requiredFileType } from '@core/utils/form.utils';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

const ALLOWED_EXTENSIONS = ['.jpg', '.jpeg', '.png', '.pdf'];

@Component({
	selector: 'app-attach-document-popup',
	templateUrl: './attach-document-popup.component.html',
	styleUrls: ['./attach-document-popup.component.scss']
})

export class AttachDocumentPopupComponent implements OnInit {

	hasError = hasError;
	form: UntypedFormGroup;
	documentTypes: TypeaheadOption<any>[];
	consentDocumentTypes: EpisodeDocumentTypeDto[];
	required: boolean = true;
	file: File = null;
	showGenerateDocument = false;
	consentSelectedType: EpisodeDocumentTypeDto;
	isAdministrative: boolean = false;

	constructor(private fb: UntypedFormBuilder,
		private internmentEpisodeDocument: InternmentEpisodeDocumentService,
		public dialogRef: MatDialogRef<AttachDocumentPopupComponent>,
		private readonly snackBarService: SnackBarService,
		private readonly permissionService: PermissionsService,
		@Inject(MAT_DIALOG_DATA) public data) { }

	ngOnInit(): void {
		this.form = this.fb.group({
			fileName: new UntypedFormControl({ value: null, disabled: true }),
			file: new UntypedFormControl(null, requiredFileType(ExtesionFile.PDF) && Validators.required),
			type: new UntypedFormControl(null, Validators.required)
		});
		this.setDocumentTypesFilter();
		this.setIsAdministrative();
	}

	setDocumentTypesFilter() {
		this.internmentEpisodeDocument.getDocumentTypes().subscribe(response => {
			if (response.length > 0) {
				const options: TypeaheadOption<any>[] = this.setFilterValues(response);
				this.documentTypes = options;
			}
		})

		this.internmentEpisodeDocument.getConsentDocumentTypes().subscribe(response => {
			if (response.length > 0)
				this.consentDocumentTypes = response;
		})

	}

	setFilterValues(response) {
		const opt: TypeaheadOption<any>[] = [];
		response.map(value => {
			opt.push({
				value: value.id,
				compareValue: value.description,
				viewValue: value.description,
			});
		})
		return opt;
	}

	save() {
		if (!this.form.valid) return;

		const formDataFile: FormData = new FormData();
		formDataFile.append('file', this.file);
		this.internmentEpisodeDocument.saveInternmentEpisodeDocument(formDataFile, this.data.internmentEpisodeId, this.form.get('type').value)
			.subscribe(resp => {
				if (resp)
					this.dialogRef.close()
			});
	}

	setDocumentType(type) {
		this.consentSelectedType = this.consentDocumentTypes.find(elem => elem.id === type);
		this.showGenerateDocument = this.consentSelectedType ? true : false;
		this.form.get('type').setValue(type);
	}

	onFileSelected(event) {
		const file: File = event.target.files[0];

		if (file) {
			const fileExtension = file.name.substring(file.name.lastIndexOf('.')).toLowerCase();

			if (ALLOWED_EXTENSIONS.includes(fileExtension)) {
				this.file = file;
				this.form.get('file').setValue(file);
			} else {
				this.snackBarService.showError('Solo se permiten archivos de imagen y PDF.');
				event.target.value = null;
			}
		}
	}


	deleteFile() {
		this.file = null;
		this.form.get('file').setValue(null);
	}

	openFile(file) {
		const fileUrl = URL.createObjectURL(file);
		window.open(fileUrl, '_blank');
	}

	generateDocument() {
		this.internmentEpisodeDocument.generateConsentDocument(this.data.internmentEpisodeId,this.consentSelectedType.consentId);
	}

	private setIsAdministrative() {
		this.permissionService.hasContextAssignments$([ERole.ADMINISTRATIVO])
			.subscribe((isAdministrative: boolean) => this.isAdministrative = isAdministrative);
	}

}
