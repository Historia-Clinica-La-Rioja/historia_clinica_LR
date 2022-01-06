import { Inject, Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CareLineDto, ClinicalSpecialtyDto, ReferenceDto } from '@api-rest/api-model';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyCareLineService } from '@api-rest/services/clinical-specialty-care-line.service';
import { removeFrom } from '@core/utils/array.utils';
import { ReferenceComponent } from '@historia-clinica/modules/ambulatoria/dialogs/reference/reference.component';
import { ProblemasService } from '@historia-clinica/services/problemas.service';
import { CellTemplates } from '@presentation/components/cell-templates/cell-templates.component';
import { TableColumnConfig } from '@presentation/components/document-section-table/document-section-table.component';
import { OVERLAY_DATA } from '@presentation/presentation-model';

@Injectable({
	providedIn: 'root'
})


export class OdontologyReferenceService {

	specialties: ClinicalSpecialtyDto[] = [];
	careLines: CareLineDto[];
	private readonly columns: TableColumnConfig[];
	odontologyReferences: ReferenceDto[] = [];
	references: Reference[] = [];


	constructor(
		private readonly dialog: MatDialog,
		@Inject(OVERLAY_DATA) public data: any,
		private readonly problemasService: ProblemasService,
		private readonly clinicalSpecialtyCareLine: ClinicalSpecialtyCareLineService,
		private readonly careLineService: CareLineService,
	) {
		this.columns = [
			{
				def: 'references',
				header: 'ambulatoria.paciente.nueva-consulta.solicitud-referencia.table.columns.REFERENCES',
				template: CellTemplates.REFERENCE,
			},
		];

		this.careLineService.getCareLines().subscribe(
			careLines => {
				this.careLines = careLines;
				this.careLines.forEach(careLine => {
					this.clinicalSpecialtyCareLine.getSpecialtyCareLine(careLine.id).subscribe((specialties: ClinicalSpecialtyDto[]) => {
						specialties.forEach((specialty: ClinicalSpecialtyDto) => this.specialties.push(specialty));
					});
				});
			});

	}

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
				let ref = { referenceNumber: this.references.length, referenceFiles: [], referenceIds: [] }
				if (reference.files.length) {
					let referenceIds: number[] = [];
					ref = { referenceNumber: this.references.length, referenceFiles: reference.files, referenceIds: referenceIds }
				}
				this.references.push(ref);
				this.odontologyReferences.push(reference.data);
			}
		});
	}

	remove(index: number): void {
		this.odontologyReferences = removeFrom<ReferenceDto>(this.odontologyReferences, index);
	}

	getColumns(): TableColumnConfig[] {
		return this.columns;
	}

	getData(): any[] {
		return (this.odontologyReferences.map(
			(reference: ReferenceDto) => ({
				problems: reference.problems,
				consultation: reference.consultation,
				procedure: reference.procedure,
				careLine: this.careLines.find(careLine => careLine.id === reference.careLineId),
				clinicalSpecialty: this.specialties.find(specialty => specialty.id === reference.clinicalSpecialtyId),
				note: reference.note,
			})
		));
	}

	getOdontologyReferences(): ReferenceDto[] {
		return this.odontologyReferences;
	}

	getReferences(): Reference[] {
		return this.references;

	}

	addFileIdAt(index: number, fileId: number): void {
		this.odontologyReferences[index].fileIds.push(fileId);
		this.references[index].referenceIds.push(fileId);
	}

	getReferenceFilesIds(): number[] {
		let referencesFilesIds: number[] = []
		this.references.forEach(reference => {
			referencesFilesIds = [...referencesFilesIds, ...reference.referenceIds]
		});

		return referencesFilesIds;
	}

	setReferenceFilesIds(referenceFilesIds: number[]) {
		this.references.forEach(reference => {
			reference.referenceIds = referenceFilesIds;
		});
	}
}

export interface Reference {
	referenceNumber: number;
	referenceFiles: File[];
	referenceIds: number[];
}
