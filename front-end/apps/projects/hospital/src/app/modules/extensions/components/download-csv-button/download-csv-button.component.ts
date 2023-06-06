import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-download-csv-button',
	templateUrl: './download-csv-button.component.html',
	styleUrls: ['./download-csv-button.component.scss']
})
export class DownloadCsvButtonComponent {

	@Input() data: any[];

	constructor() { }

	exportToCsv() {
		if (!this.data || !this.data.length) {
			return;
		}
		const separator = ',';
		const keys = Object.keys(this.data[0]);
		const header = Object.keys(this.data[0]).map(attribute => attribute.split(".")[1]);
		const csvContent =
			header.join(separator) + '\n' +
			this.data.map(row => {
				return keys.map(k => {
				let cell = row[k] === null || row[k] === undefined ? '' : row[k];
				cell = cell instanceof Date
					? cell.toLocaleString()
					: cell.toString().replace(/"/g, '""');
				if (cell.search(/("|,|\n)/g) >= 0) {
					cell = `"${cell}"`;
				}
				return cell;
				}).join(separator);
			}).join('\n');
		this.downloadVariableAsCSV(csvContent);
	  }

	  private downloadVariableAsCSV(csv: string) {
		const blob = new Blob([csv], { type: 'text/csv' });
		const url = URL.createObjectURL(blob);

		const link = document.createElement('a');
		link.href = url;
		link.download = "reporte";

		document.body.appendChild(link);
		link.click();

		document.body.removeChild(link);
		URL.revokeObjectURL(url);
	  }

}
