import { Inject, Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CareLineDto, ClinicalSpecialtyDto, HCEHealthConditionDto, ReferenceDto } from '@api-rest/api-model';
import { removeFrom } from '@core/utils/array.utils';
import { AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { HCEPersonalHistory, Reference, ReferenceComponent } from '../dialogs/reference/reference.component';

@Injectable({
	providedIn: 'root'
})
export class AmbulatoryConsultationReferenceService {

	specialties: ClinicalSpecialtyDto[] = [];
	careLines: CareLineDto[];
	outpatientReferences: ReferenceDto[] = [];
	referencesInformation: ReferenceInformation[] = [];
	references: Reference[] = [];
	referenceProblems: HCEHealthConditionDto[] = [];

	constructor(
		private readonly dialog: MatDialog,
		@Inject(OVERLAY_DATA) public informationData: any,
		private readonly ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService,
	) { }

	openReferenceDialog(): void {
		const dialogRef = this.dialog.open(ReferenceComponent, {
			autoFocus: false,
			disableClose: true,
			data: {
				consultationProblems: this.ambulatoryConsultationProblemsService.getProblemas(),
				patientId: this.informationData.idPaciente,
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
				this.outpatientReferences.push(this.mapToReferenceDto(reference.data));
			}
		});
	}

	remove(index: number): void {
		this.outpatientReferences = removeFrom<ReferenceDto>(this.outpatientReferences, index);
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

	getOutpatientReferences(): ReferenceDto[] {
		return this.outpatientReferences;
	}

	getReferences(): ReferenceInformation[] {
		return this.referencesInformation;
	}

	addFileIdAt(index: number, fileId: number): void {
		this.outpatientReferences[index].fileIds.push(fileId);
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
		return (!this.outpatientReferences || this.outpatientReferences.length === 0);
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
			study: reference?.referenceStudy
		}
	}
}

export interface ReferenceInformation {
	referenceFiles: File[];
	referenceIds: number[];
	referenceProblems: HCEPersonalHistory[];
}
