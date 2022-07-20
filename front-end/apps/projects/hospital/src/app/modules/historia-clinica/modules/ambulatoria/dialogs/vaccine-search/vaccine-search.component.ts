import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnomedDto, SnomedResponseDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { SnowstormService } from '@api-rest/services/snowstorm.service';
import { TranslateService } from '@ngx-translate/core';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
  selector: 'app-vaccine-search',
  templateUrl: './vaccine-search.component.html',
  styleUrls: ['./vaccine-search.component.scss']
})
export class VaccineSearchComponent {

  searching = false;
  conceptsResultsTable: TableModel<any>;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: {
      searchValue: string
    },
    private readonly snackBarService: SnackBarService,
    private readonly snowstormService: SnowstormService,
    private readonly translate: TranslateService,
    public dialogRef: MatDialogRef<VaccineSearchComponent>,
  ) { }

  onSearch(searchValue: string): void {
    if (searchValue) {
      this.searching = true;
      this.snowstormService.getSNOMEDConcepts({ term: searchValue, ecl: SnomedECL.VACCINE })
        .subscribe(
          (results: SnomedResponseDto) => {
            this.buildConceptsResultsTable(results.items);
          },
          _ => {
            this.snackBarService.showError('historia-clinica.snowstorm.CONCEPTS_COULD_NOT_BE_OBTAINED');
          },
          () => {
            this.searching = false;
          }
        );
    }
  }

  private buildConceptsResultsTable(data: SnomedDto[]): void {
    this.translate.get('ambulatoria.vaccine-search.COLUMN_VACCINE_HEADER').subscribe(
      (columnHeader: string) => {
        this.conceptsResultsTable = {
          columns: [
            {
              columnDef: '1',
              header: columnHeader,
              text: concept => concept.pt
            },
            {
              columnDef: 'select',
              action: {
                displayType: ActionDisplays.BUTTON,
                display: this.translate.instant('buttons.SELECT'),
                matColor: 'primary',
                do: concept => this.dialogRef.close(concept)
              }
            }
          ],
          data,
          enablePagination: true
        };
      }
    );
  }

}
