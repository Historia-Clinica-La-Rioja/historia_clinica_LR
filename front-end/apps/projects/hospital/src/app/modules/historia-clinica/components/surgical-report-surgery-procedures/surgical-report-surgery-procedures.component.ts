import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ProcedureTypeEnum, HospitalizationProcedureDto, AppFeature, SurgicalReportDto } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { removeFrom, pushIfNotExists } from '@core/utils/array.utils';
import { NewConsultationProcedureFormComponent } from '@historia-clinica/dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
  selector: 'app-surgical-report-surgery-procedures',
  templateUrl: './surgical-report-surgery-procedures.component.html',
  styleUrls: ['./surgical-report-surgery-procedures.component.scss']
})
export class SurgicalReportSurgeryProceduresComponent implements OnInit {
  procedureService = new ProcedimientosService(this.formBuilder, this.snomedService, this.snackBarService);
  searchConceptsLocallyFF = false;
  @Input() surgicalReport: SurgicalReportDto;

  constructor(private formBuilder: FormBuilder,
    public dialog: MatDialog,
    private readonly snomedService: SnomedService,
    private readonly snackBarService: SnackBarService,
    private readonly featureFlagService: FeatureFlagService
  ) { }

  ngOnInit(): void {
    this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
      this.searchConceptsLocallyFF = isOn;
    })
    this.procedureService.procedimientos$.subscribe(procedures => this.changeSurgeryProcedure(procedures));

  }

  addProcedure(): void {
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

  deleteProcedure(index: number): void {
    this.surgicalReport.surgeryProcedures = removeFrom(this.surgicalReport.surgeryProcedures, index);
    this.procedureService.remove(index);
  }

  isEmpty(): boolean {
    return !this.surgicalReport.surgeryProcedures?.length;
  }

  private compare(first, second): boolean {
    return first.snomed.sctid === second.snomed.sctid;
  }

  private changeSurgeryProcedure(procedures): void {
    procedures.forEach(procedure =>
      this.surgicalReport.surgeryProcedures = pushIfNotExists(this.surgicalReport.surgeryProcedures, this.mapToHospitalizationProcedure(procedure, ProcedureTypeEnum.SURGICAL_PROCEDURE), this.compare));
  }

  private mapToHospitalizationProcedure(procedure, type: ProcedureTypeEnum): HospitalizationProcedureDto {
    return {
      snomed: procedure.snomed,
      type: type,
    }
  }
}