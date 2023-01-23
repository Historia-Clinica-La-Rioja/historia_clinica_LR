import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PharmacoSummaryDto } from '@api-rest/api-model';

const PAGE_SIZE_OPTIONS = [5, 10, 25];
const PAGE_SIZE_MIN = [5];
const PAGE_MIN_SIZE = 5;

@Component({
	selector: 'app-pharmacos-frequent',
	templateUrl: './pharmacos-frequent.component.html',
	styleUrls: ['./pharmacos-frequent.component.scss']
})
export class PharmacosFrequentComponent implements OnInit {
	formFilter: FormGroup;
	pageSlice = [];
	pageSizeOptions: number[] = PAGE_SIZE_OPTIONS;
	numberOfPatients = 0;
	pharmacos: PharmacoSummaryDto[] = [];
	private sizePageSelect = PAGE_MIN_SIZE;
	private applySearchFilter = '';

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { pharmacos: PharmacoSummaryDto[] },

		private readonly dialogRef: MatDialogRef<PharmacosFrequentComponent>,
		private readonly formBuilder: FormBuilder,

	) {
		this.pharmacos = this.data.pharmacos;
	}
	ngOnInit() {
		this.pageSlice = this.pharmacos.slice(0, PAGE_MIN_SIZE);
		this.numberOfPatients = this.pharmacos?.length;
		this.formFilter = this.formBuilder.group({
			description: [null]
		});
	}

	applyFilter($event: any) {
		this.applySearchFilter = ($event.target as HTMLInputElement).value?.replace(' ', '');
		this.upDateFilters();
	}

	upDateFilters() {
		this.applyFilters();
		this.pageSlice = this.pageSlice.slice(0, this.sizePageSelect);
	}

	private setPageSizeOptions() {
		this.pageSizeOptions = (this.pageSlice.length < PAGE_MIN_SIZE) ? PAGE_SIZE_MIN : PAGE_SIZE_OPTIONS;
		this.numberOfPatients = this.pageSlice.length;
		this.pageSlice.length = this.numberOfPatients;
	}

	clearFilterField(control: AbstractControl) {
		control.reset();
		this.pageSlice = this.pharmacos.slice(0, PAGE_MIN_SIZE);
		this.numberOfPatients = this.pharmacos?.length;
	}

	onPageChange($event: any) {
		const page = $event;
		this.sizePageSelect = page.pageSize;
		const startPage = page.pageIndex * page.pageSize;
		this.applyFilters();
		this.pageSlice = this.pageSlice.slice(startPage, $event.pageSize + startPage);
	}

	private applyFilters() {
		this.pageSlice = this.filter();
		this.setPageSizeOptions();
	}

	private filter(): PharmacoSummaryDto[] {
		let listFilter = this.pharmacos;
		listFilter = listFilter.filter((e) => e?.snomed.pt.toLowerCase().includes(this.applySearchFilter.toLowerCase()));
		return listFilter;
	}

	close(openFormPharmaco: boolean, pharmaco?: PharmacoSummaryDto) {
		this.dialogRef.close({ openFormPharmaco, pharmaco });
	}
}
