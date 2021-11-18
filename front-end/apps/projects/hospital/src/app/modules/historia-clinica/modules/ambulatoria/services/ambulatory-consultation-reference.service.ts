import { Inject, Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CareLineDto, ClinicalSpecialtyDto } from '@api-rest/api-model';
import { CareLineService } from '@api-rest/services/care-line.service';
import { ClinicalSpecialtyCareLineService } from '@api-rest/services/clinical-specialty-care-line.service';
import { removeFrom } from '@core/utils/array.utils';
import { AmbulatoryConsultationProblemsService } from '@historia-clinica/services/ambulatory-consultation-problems.service';
import { CellTemplates } from '@presentation/components/cell-templates/cell-templates.component';
import { TableColumnConfig } from '@presentation/components/document-section-table/document-section-table.component';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { Reference, ReferenceComponent } from '../dialogs/reference/reference.component';

@Injectable({
	providedIn: 'root'
})
export class AmbulatoryConsultationReferenceService {

	specialties: ClinicalSpecialtyDto[];
	careLines: CareLineDto[];
	private readonly columns: TableColumnConfig[];
	references: Reference[];

	constructor(
		private readonly dialog: MatDialog,
		@Inject(OVERLAY_DATA) public informationData: any,
		private readonly ambulatoryConsultationProblemsService: AmbulatoryConsultationProblemsService,
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

		this.references = [];

		this.specialties = [];

		this.careLineService.getCareLines().subscribe(
			lineCare => {
				this.careLines = lineCare;
				this.careLines.forEach(careLine => {
					this.clinicalSpecialtyCareLine.getSpecialtyCareLine(careLine.id).subscribe((specialty: ClinicalSpecialtyDto[]) => {
						specialty.forEach((specialty: ClinicalSpecialtyDto) => this.specialties.push(specialty));
					});
				});
			});
	}

	openReferenceDialog(): void {
		const dialogRef = this.dialog.open(ReferenceComponent, {
			autoFocus: false,
			data: {
				newConsultationProblems: this.ambulatoryConsultationProblemsService.getProblemas(),
				idPatient: this.informationData.idPaciente,
			}
		});
		dialogRef.afterClosed().subscribe((reference: Reference) => {
			if (reference){
				this.references.push(reference);
			}	
		});
	}

	remove(index: number): void {
		this.references = removeFrom<Reference>(this.references, index);
	}

	getColumns(): TableColumnConfig[] {
		return this.columns;
	}

	getData(): any[] {
		return (this.references.map(
			(reference: Reference) => ({
				problems: reference.problems,
				consultation: reference.consultation,
				procedure: reference.procedure,
				careLine: this.careLines.find(careLine => careLine.id == reference.careLineId),
				clinicalSpecialty: this.specialties.find(specialty => specialty.id === reference.clinicalSpecialtyId),
				note: reference.note,
			})
		));
	}
}
