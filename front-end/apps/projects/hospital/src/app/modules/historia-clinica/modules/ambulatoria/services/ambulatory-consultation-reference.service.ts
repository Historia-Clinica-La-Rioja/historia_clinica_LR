import { Inject, Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CareLineDto, ClinicalSpecialtyDto, HCEPersonalHistoryDto, ReferenceDto } from '@api-rest/api-model';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyCareLineService } from '@api-rest/services/clinical-specialty-care-line.service';
import { removeFrom } from '@core/utils/array.utils';
import { AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { HCEPersonalHistory, ReferenceComponent } from '../dialogs/reference/reference.component';

@Injectable({
	providedIn: 'root'
})
export class AmbulatoryConsultationReferenceService {

	specialties: ClinicalSpecialtyDto[] = [];
	careLines: CareLineDto[];
	outpatientReferences: ReferenceDto[] = [];
	references: Reference[] = [];
	referenceProblems: HCEPersonalHistoryDto[] = [];

	constructor(
		private readonly dialog: MatDialog,
		@Inject(OVERLAY_DATA) public informationData: any,
		private readonly ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService,
		private readonly clinicalSpecialtyCareLine: ClinicalSpecialtyCareLineService,
		private readonly careLineService: CareLineService,
	) {
		this.careLineService.getCareLines().subscribe(
			careLines => {
				this.careLines = careLines;
				this.careLines.forEach(careLine => {
					this.clinicalSpecialtyCareLine.getSpecialtyCareLine(careLine.id).subscribe((specialties: ClinicalSpecialtyDto[]) => {
						specialties.forEach((specialty: ClinicalSpecialtyDto) => this.specialties.push(specialty));
					});
				});
			}
		);
	}

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
				this.references.push(ref);
				this.outpatientReferences.push(reference.data);
			}
		});
	}

	remove(index: number): void {
		this.outpatientReferences = removeFrom<ReferenceDto>(this.outpatientReferences, index);
		this.references = removeFrom<Reference>(this.references, index);
	}

	getData(): any[] {
		return (this.outpatientReferences.map(
			(reference: ReferenceDto, index: number) => ({
				problems: reference.problems,
				consultation: reference.consultation,
				procedure: reference.procedure,
				careLine: this.careLines.find(careLine => careLine.id == reference.careLineId),
				clinicalSpecialty: this.specialties.find(specialty => specialty.id === reference.clinicalSpecialtyId),
				note: reference.note,
				index: index
			})
		));
	}

	getOutpatientReferences(): ReferenceDto[] {
		return this.outpatientReferences;
	}

	getReferences(): Reference[] {
		return this.references;
	}

	addFileIdAt(index: number, fileId: number): void {
		this.outpatientReferences[index].fileIds.push(fileId);
		this.references[index].referenceIds.push(fileId);
	}

	getReferenceFilesIds(): number[] {
		let referencesFilesIds: number[] = []
		this.references.forEach(reference => {
			referencesFilesIds = [...referencesFilesIds, ...reference.referenceIds]
		});

		return referencesFilesIds;
	}

	deleteReferenceFilesIds() {
		this.references.forEach(reference => {
			reference.referenceIds = [];
		});
	}

	isEmpty(): boolean {
		return (!this.outpatientReferences || this.outpatientReferences.length === 0);
	}

}

export interface Reference {
	referenceFiles: File[];
	referenceIds: number[];
	referenceProblems: HCEPersonalHistory[];
}
