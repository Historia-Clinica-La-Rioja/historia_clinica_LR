import { Component, Input } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { HospitalizationProcedureDto, ProcedureTypeEnum, SnomedECL, SurgicalReportDto } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { AnesthesiaFormComponent } from '@historia-clinica/dialogs/anesthesia-form/anesthesia-form.component';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
  selector: 'app-surgical-report-anesthesia',
  templateUrl: './surgical-report-anesthesia.component.html',
  styleUrls: ['./surgical-report-anesthesia.component.scss']
})
export class SurgicalReportAnesthesiaComponent {
  anesthesiaService = new ProcedimientosService(this.formBuilder, this.snomedService, this.snackBarService, this.dateFormatPipe);
  @Input() searchConceptsLocallyFF: boolean;
  @Input() surgicalReport: SurgicalReportDto;

  constructor(private readonly snackBarService: SnackBarService,
    private readonly snomedService: SnomedService,
    private readonly formBuilder: UntypedFormBuilder,
    private readonly dialog: MatDialog,
    private readonly dateFormatPipe: DateFormatPipe,) {
    this.anesthesiaService.setECL(SnomedECL.ANESTHESIA);
    this.anesthesiaService.procedimientos$.subscribe(anesthesias => this.changeAnesthesia(anesthesias));

  }

  addAnesthesia() {
    this.dialog.open(AnesthesiaFormComponent, {
      data: {
        anesthesiaService: this.anesthesiaService,
        searchConceptsLocallyFF: this.searchConceptsLocallyFF,
        hideDate: true
      },
      autoFocus: false,
      width: '35%',
      disableClose: true,
    });
  }

  deleteAnesthesia(index: number): void {
    this.surgicalReport.anesthesia = removeFrom(this.surgicalReport.anesthesia, index);
    this.anesthesiaService.remove(index);
  }

  isEmpty(): boolean {
    return !this.surgicalReport.anesthesia?.length;
  }
  
  private changeAnesthesia(anesthesias): void {
    anesthesias.forEach(procedure =>
      this.surgicalReport.anesthesia = pushIfNotExists(this.surgicalReport.anesthesia, this.mapToHospitalizationProcedure(procedure, ProcedureTypeEnum.ANESTHESIA_PROCEDURE), this.compareByEqual));
  }

  private compareByEqual(first, second): boolean {
    return first.snomed.sctid === second.snomed.sctid;
  }

  private mapToHospitalizationProcedure(procedure, type: ProcedureTypeEnum): HospitalizationProcedureDto {
    return {
      snomed: procedure.snomed,
      type: type
    }
  }
}