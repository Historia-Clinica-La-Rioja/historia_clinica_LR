import { Inject, Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CareLineDto, ClinicalSpecialtyDto, ReferenceDto } from '@api-rest/api-model';
import { removeFrom } from '@core/utils/array.utils';
import { Reference, ReferenceComponent } from '@historia-clinica/modules/ambulatoria/dialogs/reference/reference.component';
import { ReferenceInformation } from '@historia-clinica/modules/ambulatoria/services/ambulatory-consultation-reference.service';
import { ProblemasService } from '@historia-clinica/services/problemas.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';

@Injectable({
	providedIn: 'root'
})

export class OdontologyReferenceService {

	specialties: ClinicalSpecialtyDto[] = [];
	careLines: CareLineDto[];
	odontologyReferences: ReferenceDto[] = [];
	referencesInformation: ReferenceInformation[] = [];
	references: Reference[] = [];

	constructor(
		private readonly dialog: MatDialog,
		@Inject(OVERLAY_DATA) public data: any,
		private readonly problemasService: ProblemasService,
	) { }

	openReferenceDialog(): void {
		const dialogRef = this.dialog.open(ReferenceComponent, {
			autoFocus: false,
			disableClose: true,
			data: {
				consultationProblems: this.problemasService.getProblemas(),
				patientId: this.data.patientId,
			}
		});
		dialogRef.afterClosed().subscribe(reference => {
			if (reference.data) {
				let ref = {
					referenceFiles: reference.files.length ? reference.files : [],
					referenceIds: [],
					referenceProblems: reference.problems
				};
				this.referencesInformation.push(ref);
				this.references.push(reference.data);
				this.odontologyReferences.push(this.mapToReferenceDto(reference.data));
			}
		});
	}

	remove(index: number): void {
		this.odontologyReferences = removeFrom<ReferenceDto>(this.odontologyReferences, index);
		this.references = removeFrom<Reference>(this.references, index);
		this.referencesInformation = removeFrom<ReferenceInformation>(this.referencesInformation, index);
	}

	getData(): any[] {
		return (this.references.map((reference: Reference, index: number) => ({
			problems: reference.problems,
			consultation: reference.consultation,
			procedure: reference.procedure,
			careLine: reference.careLine ? reference.careLine : null,
			clinicalSpecialties: reference.clinicalSpecialties,
			note: reference.note,
			index: index
		})));
	}

	getOdontologyReferences(): ReferenceDto[] {
		return this.odontologyReferences;
	}

	getReferences(): ReferenceInformation[] {
		return this.referencesInformation;

	}

	addFileIdAt(index: number, fileId: number): void {
		this.odontologyReferences[index].fileIds.push(fileId);
		this.referencesInformation[index].referenceIds.push(fileId);
	}

	getReferenceFilesIds(): number[] {
		let referencesFilesIds: number[] = []
		this.referencesInformation.forEach(reference => {
			referencesFilesIds = [...referencesFilesIds, ...reference.referenceIds]
		});

		return referencesFilesIds;
	}

	deleteReferenceFilesIds() {
		this.referencesInformation.forEach(reference => {
			reference.referenceIds = [];
		});
	}

	isEmpty(): boolean {
		return (!this.odontologyReferences || this.odontologyReferences.length === 0);
	}

	private mapToReferenceDto(reference: Reference): ReferenceDto {
		return {
			careLineId: reference.careLine ? reference.careLine.id : null,
			clinicalSpecialtyIds: reference.clinicalSpecialties?.map(specialty => specialty.id),
			consultation: reference.consultation,
			note: reference.note,
			problems: reference.problems,
			procedure: reference.procedure,
			fileIds: reference.fileIds,
			destinationInstitutionId: reference.destinationInstitutionId,
			phoneNumber: reference.phoneNumber,
			phonePrefix: reference.phonePrefix,
			priority: reference.priority,
			study: reference.referenceStudy
		}
	}
}
