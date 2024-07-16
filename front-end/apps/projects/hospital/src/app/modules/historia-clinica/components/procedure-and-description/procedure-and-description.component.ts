import { Component, Input, OnInit } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, HospitalizationProcedureDto, ProcedureTypeEnum, SurgicalReportDto } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { NewConsultationProcedureFormComponent } from '@historia-clinica/dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-procedure-and-description',
	templateUrl: './procedure-and-description.component.html',
	styleUrls: ['./procedure-and-description.component.scss']
})
export class ProcedureAndDescriptionComponent implements OnInit {

	@Input() title: string;
	@Input() tableTitle: string;
	@Input() buttonTitle: string;
	@Input() icon: string;
	@Input() surgicalReport: SurgicalReportDto;
	@Input() type: ProcedureTypeEnum;

	procedureService = new ProcedimientosService(this.formBuilder, this.snomedService, this.snackBarService, this.dateFormatPipe);
	searchConceptsLocallyFF = false;
	procedures: HospitalizationProcedureDto[];
	description: string;

	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
		private readonly dateFormatPipe: DateFormatPipe

	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFF = isOn;
		})
		this.procedureService.procedimientos$.subscribe(procedures => this.changeProcedure(procedures));
	}

    ngOnInit(): void {
        switch (this.type) {
            case ProcedureTypeEnum.DRAINAGE:
                this.procedures = this.surgicalReport.drainages;
                break;
            case ProcedureTypeEnum.CULTURE:
                this.procedures = this.surgicalReport.cultures;
                break;
            case ProcedureTypeEnum.FROZEN_SECTION_BIOPSY:
                this.procedures = this.surgicalReport.frozenSectionBiopsies;
                break;
        }
    }

    addProcedure() {
        this.dialog.open(NewConsultationProcedureFormComponent, {
            data: {
                procedureService: this.procedureService,
                searchConceptsLocallyFF: this.searchConceptsLocallyFF,
                hideDate: true
            },
            autoFocus: false,
            width: '35%',
            disableClose: true,
        });
    }

    private changeProcedure(procedures) {
        procedures.forEach(procedure => {
            this.procedures = pushIfNotExists(this.procedures, this.mapToHospitalizationProcedure(procedure, this.type), this.compare);
        });
        this.updateSurgicalReportProcedures();
    }

    private updateSurgicalReportProcedures() {
        switch (this.type) {
            case ProcedureTypeEnum.DRAINAGE:
                this.surgicalReport.drainages = this.procedures;
                break;
            case ProcedureTypeEnum.CULTURE:
                this.surgicalReport.cultures = this.procedures;
                break;
            case ProcedureTypeEnum.FROZEN_SECTION_BIOPSY:
                this.surgicalReport.frozenSectionBiopsies = this.procedures;
                break;
        }
    }

    private compare(first, second): boolean {
        return first.snomed.sctid === second.snomed.sctid;
    }

    deleteProcedure(index: number) {
        this.procedures = removeFrom(this.procedures, index);
        this.procedureService.remove(index);
        this.updateSurgicalReportProcedures();
    }

    private mapToHospitalizationProcedure(procedure, type: ProcedureTypeEnum): HospitalizationProcedureDto {
        return {
            snomed: procedure.snomed,
            type: type,
            note: this.description,
        }
    }

    isEmpty(): boolean {
        return !this.procedures.length && !this.description;
    }

    onDescriptionChange() {
        if (this.procedures.length > 0) {
            this.procedures[0].note = this.description;
            this.updateSurgicalReportProcedures();
        }
    }
}
