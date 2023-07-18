import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ApiErrorMessageDto, ERole, AppFeature, EpisodeDocumentTypeDto, ProfessionalDto } from '@api-rest/api-model';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import { InternmentEpisodeDocumentService } from '@api-rest/services/internment-episode-document.service';
import { PermissionsService } from '@core/services/permissions.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { ExtesionFile } from '@core/utils/extensionFile';
import { hasError, requiredFileType } from '@core/utils/form.utils';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

const ALLOWED_EXTENSIONS = ['.jpg', '.jpeg', '.png', '.pdf'];
const REGULAR_DOCUMENT: number = 1;

@Component({
	selector: 'app-attach-document-popup',
	templateUrl: './attach-document-popup.component.html',
	styleUrls: ['./attach-document-popup.component.scss']
})

export class AttachDocumentPopupComponent implements OnInit {

	hasError = hasError;
	form: UntypedFormGroup;
	surgicalForm: UntypedFormGroup;
	documentTypes: TypeaheadOption<any>[];
	consentDocumentTypes: EpisodeDocumentTypeDto[];
	professionals: TypeaheadOption<any>[];
	nameSelfDeterminationFF: boolean;
	required: boolean = true;
	file: File = null;
	showGenerateDocument = false;
	consentSelectedType: EpisodeDocumentTypeDto;
	showAttachFile = false;
	showSurgicalInfo = false;
	isAdministrative: boolean = false;
	hasConsentDocumentError: string;

	constructor(private fb: UntypedFormBuilder,
		private internmentEpisodeDocument: InternmentEpisodeDocumentService,
		private readonly healthcareProfessionalByInstitutionService: HealthcareProfessionalByInstitutionService,
		public dialogRef: MatDialogRef<AttachDocumentPopupComponent>,
		private readonly snackBarService: SnackBarService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly permissionService: PermissionsService,
		@Inject(MAT_DIALOG_DATA) public data
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
	}

	ngOnInit(): void {
		this.form = this.fb.group({
			fileName: new UntypedFormControl({ value: null, disabled: true }),
			file: new UntypedFormControl(null, requiredFileType(ExtesionFile.PDF) && Validators.required),
			type: new UntypedFormControl(null, Validators.required)
		});

		this.surgicalForm = this.fb.group({
			professional: new UntypedFormControl(null, Validators.required)
		});

		this.setDocumentTypesFilter();
		this.setProfessionalsFilter();
		this.setIsAdministrative();
	}

	setProfessionalsFilter() {
		this.healthcareProfessionalByInstitutionService.getAll().subscribe(professionals => {
			this.professionals = professionals.map( professional => {
					const professionalName = this.getFullNameByFF(professional);
					return {
						compareValue: professionalName,
						value: professionalName
					}
				})
		});
	}

	getFullNameByFF(professional: ProfessionalDto): string {
		const firstName = (professional.middleNames) ? professional.firstName + " " + professional.middleNames : professional.firstName;
		const nameSelfDetermination = (professional.nameSelfDetermination) ? professional.nameSelfDetermination : firstName;
		const lastName = (professional.otherLastNames) ? professional.lastName + " " + professional.otherLastNames : professional.lastName;
		return (this.nameSelfDeterminationFF) ? lastName + ", " + nameSelfDetermination : lastName + ", " + firstName;
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
		const consentId: number =  this.consentSelectedType?.consentId ? this.consentSelectedType.consentId : REGULAR_DOCUMENT;
		this.internmentEpisodeDocument.saveInternmentEpisodeDocument(formDataFile, this.data.internmentEpisodeId, this.form.get('type').value, consentId)
			.subscribe(resp => {
				if (resp)
					this.dialogRef.close()
				}, (error: ApiErrorMessageDto) => {
					this.form.controls.type.setErrors({invalid: true});
					this.hasConsentDocumentError = error.text;
				});
	}

	setDocumentType(type) {
		this.consentSelectedType = this.consentDocumentTypes.find(elem => elem.id === type);
		this.showGenerateDocument = this.consentSelectedType ? true : false;
		this.showAttachFile = true;
		this.form.get('type').setValue(type);
	}

	setProfessional(professional: any) {
		this.surgicalForm.get('professional').setValue(professional);
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
