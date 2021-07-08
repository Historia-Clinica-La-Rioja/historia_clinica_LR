import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { OdontologyConceptDto, ToothDto, ToothSurfacesDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { map } from 'rxjs/internal/operators/map';
import { ConceptsService } from '../../api-rest/concepts.service';
import { OdontogramService } from '../../api-rest/odontogram.service';
import { getSurfaceShortName } from '../../utils/surfaces';
import { ToothTreatment, CommonActions } from '../tooth/tooth.component';

@Component({
	selector: 'app-tooth-dialog',
	templateUrl: './tooth-dialog.component.html',
	styleUrls: ['./tooth-dialog.component.scss']
})
export class ToothDialogComponent implements OnInit {

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly odontogramService: OdontogramService,
		@Inject(MAT_DIALOG_DATA) public data: { tooth: ToothDto, quadrantCode: number },
		private readonly conceptsService: ConceptsService
	) { }

	readonly toothTreatment = ToothTreatment.AS_FRACTIONAL_TOOTH;

	form: FormGroup;
	newHallazgoId: number;

	selectedSurfacesText: string;

	selectedSurfaces: string[] = [];

	private surfacesDto: ToothSurfacesDto;

	private diagnostics$: Observable<OdontologyConceptDto[]>;
	filteredDiagnostics$: Observable<OdontologyConceptDto[]>;

	private procedures$: Observable<OdontologyConceptDto[]>;
	filteredProcedures$: Observable<OdontologyConceptDto[]>;

	outputProcedures;

	ngOnInit(): void {

		this.form = this.formBuilder.group(
			{
				findingId: [undefined],
				procedures: this.formBuilder.group({
					firstProcedureId: [undefined],
					secondProcedureId: [undefined],
					thirdProcedureId: [undefined],
				})
			}
		);

		this.odontogramService.getToothSurfaces(this.data.tooth.snomed.sctid).subscribe(surfaces => this.surfacesDto = surfaces);
		this.diagnostics$ = this.conceptsService.getDiagnostics();
		this.procedures$ = this.conceptsService.getProcedures();
	}

	confirm() {
	}

	reciveSelectedSurfaces(surfaces: string[]) {
		this.selectedSurfaces = surfaces;
		this.concatNames();

		let filterFuncion = (diagnostic: OdontologyConceptDto) => { return diagnostic.applicableToTooth }
		if (surfaces.length) {
			filterFuncion = (diagnostic: OdontologyConceptDto) => { return diagnostic.applicableToSurface }
		}

		this.filteredDiagnostics$ = this.diagnostics$?.pipe(map(diagnostics => diagnostics.filter(filterFuncion)));
		this.filteredProcedures$ = this.procedures$?.pipe(map(procedures => procedures.filter(filterFuncion)));
	}

	findingChanged(hallazgoId) {
		this.newHallazgoId = hallazgoId.value;
	}

	reciveCommonActions(inCommon: CommonActions) {
		if (this.form) {
			if (inCommon?.findingId) {
				this.form.controls.findingId.setValue(inCommon.findingId);
			} else {
				this.form.controls.findingId.setValue(undefined);
				this.newHallazgoId = undefined;
			}
			const procedures = {
				firstProcedureId: inCommon.procedures?.firstProcedureId,
				secondProcedureId: inCommon.procedures?.secondProcedureId,
				thirdProcedureId: inCommon.procedures?.thirdProcedureId
			};
			this.form.patchValue({
				procedures
			});
		}
	}

	private concatNames() {
		this.selectedSurfacesText = '';
		if (this.selectedSurfaces.length) {
			this.selectedSurfacesText = this.selectedSurfaces.length === 1 ? 'Cara ' : 'Caras ';
			const mappedNames = this.selectedSurfaces.map(surface => this.findSutableName(surface));
			this.selectedSurfacesText += mappedNames.filter(Boolean).join(', ');
		}
	}

	private findSutableName(surface: string): string {
		const sctid = this.surfacesDto[surface].sctid;
		return getSurfaceShortName(sctid);
	}

	procedureChanged() {
		this.outputProcedures = this.form.value.procedures;
	}

}

