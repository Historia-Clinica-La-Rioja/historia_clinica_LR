import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MasterDataDto } from '@api-rest/api-model';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { Item } from '@presentation/pipes/paginate.pipe';
import { PAGE_MIN_SIZE, PAGE_SIZE_OPTIONS } from '../../constants/internment-indications';

@Component({
	selector: 'app-most-frequent',
	templateUrl: './most-frequent.component.html',
	styleUrls: ['./most-frequent.component.scss']
})
export class MostFrequentComponent<T> implements OnInit {
	totalItems = 0;
	formFilter: UntypedFormGroup;
	filterSearch = ' ';
	pageSize = PAGE_MIN_SIZE;
	currentPage = 1;
	pageSizeOptions: number[] = PAGE_SIZE_OPTIONS;
	vias: MasterDataDto[] = [];
	constructor(@Inject(MAT_DIALOG_DATA) public data: MostFrequentIndication<T>,
		private readonly dialogRef: MatDialogRef<T>,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly internacionMasterdataService: InternacionMasterDataService,

	) {
		this.internacionMasterdataService.getVias().subscribe(v => this.vias = v);
	}

	ngOnInit(): void {
		this.totalItems = this.data?.items.length;
		this.formFilter = this.formBuilder.group({
			description: [null]
		});
	}

	clearFilterField(control: AbstractControl) {
		control.reset();
		this.filterSearch = ' ';
	}

	applyFilter($event: any) {
		this.filterSearch = ($event.target as HTMLInputElement).value;
	}

	setPage($event: any) {
		const page = $event;
		this.pageSize = page.pageSize;
		this.currentPage = page.pageIndex + 1;
	}

	close(openFormPharmaco: boolean, pharmaco?: T) {
		this.dialogRef.close({ openFormPharmaco, pharmaco });
	}

}

export interface MostFrequentIndication<T> {
	items: Item<T>[],
	title: string,
}
