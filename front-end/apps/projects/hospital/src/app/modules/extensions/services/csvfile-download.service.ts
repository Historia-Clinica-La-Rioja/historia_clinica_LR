import { Injectable } from '@angular/core';
import { InstitutionDto } from '@api-rest/api-model';
import { InstitutionService } from '@api-rest/services/institution.service';
import { ContextService } from '@core/services/context.service';

const NEW_LINE = '\n';
@Injectable({
  providedIn: 'root'
})
export class CSVFileDownloadService {
  private _enabled = false;
  private data: TabData[] = [];

  constructor(
    private institutionService: InstitutionService,
    private contextService: ContextService,
  ) { }

  set enabledDownload(value: boolean) {
    this._enabled = value;
  }

  get enabledDownload() {
    return this._enabled;
  }

  addTableData(tabName: string, columnTitles: string[], tableData: any[]): void {

    const foundIndex = this.data.findIndex(tabInfo => tabInfo.label === tabName);

    const tabInfo = {
      label: tabName,
      content: {
        columnTitles: columnTitles,
        tableData: tableData
      }
    };

    foundIndex === -1 ? this.data.push(tabInfo) : this.data[foundIndex] = tabInfo;

  }

  donwload(): void {
    const csvContent = this.fileContent();
    this.institutionService.getInstitutions([this.contextService.institutionId]).subscribe(
      (institutions: InstitutionDto[]) => {
        const institutionName = institutions[0]?.name ? institutions[0].name.trim() : 'Institucion';
        const today = new Date();
        const fileName =
          "Ref_" + institutionName
          + "_" + today.toLocaleString('es-AR', { year: 'numeric' })
          + "_" + today.toLocaleString('es-AR', { month: '2-digit' })
          + "_" + today.toLocaleString('es-AR', { day: '2-digit' });
        this.saveFile(fileName, csvContent);
      }
    );
  }

  private fileContent(): string {
    if (!this.data?.length) {
      return '';
    }
    else {
      let tabDataToCSV = [];

      this.data.forEach(
        (tabInfo: TabData) => {
          const title = tabInfo.label.trim();
          const headers = tabInfo.content.columnTitles.join(',').trim();

          let rows = '';
          tabInfo.content.tableData.forEach(
            register => {
              let rowCellsData = [];
              Object.keys(register).forEach(
                (key: string) => {
                  rowCellsData.push(register[key] ? register[key].toString().replace(',', '').trim() : '');
                }
              );
              rows += rowCellsData.join(',') + NEW_LINE;
            }
          );

          tabDataToCSV.push(title + NEW_LINE + headers + NEW_LINE + rows);
        }
      );

      return tabDataToCSV.join(NEW_LINE);

    }
  }

  private saveFile(fileName: string, csvContent: string): void {
    let blob = new Blob(['' + csvContent], {
      type: 'text/csv;charset=utf-8;',
    });
    let dwldLink = document.createElement('a');
    let url = URL.createObjectURL(blob);
    const isSafariBrowser = navigator.userAgent.indexOf('Safari') != -1 && navigator.userAgent.indexOf('Chrome') == -1;
    if (isSafariBrowser) {
      dwldLink.setAttribute('target', '_blank');
    }
    dwldLink.setAttribute('href', url);
    dwldLink.setAttribute('download', fileName + '.csv');
    dwldLink.style.visibility = 'hidden';
    document.body.appendChild(dwldLink);
    dwldLink.click();
    document.body.removeChild(dwldLink);
  }

}

interface TabData {
  label: string;
  content: {
    columnTitles: string[],
    tableData: any[]
  }
}
