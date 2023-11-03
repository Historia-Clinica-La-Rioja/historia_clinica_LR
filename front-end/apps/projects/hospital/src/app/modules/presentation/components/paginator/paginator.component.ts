import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

const PAGE_SIZE_OPTIONS = [5, 10, 25];
const PAGE_MIN_SIZE = 5;

@Component({
	selector: 'app-paginator',
	templateUrl: './paginator.component.html',
	styleUrls: ['./paginator.component.scss']
})
export class PaginatorComponent<T> implements OnInit {

	pageSlice = [];

	@Input() list: T[];
	@Input() pageSizeOptions: number[] = PAGE_SIZE_OPTIONS;
	@Input() pageMinSize: number = PAGE_MIN_SIZE;
	@Output() result = new EventEmitter<T[]>;

	constructor() { }

	ngOnInit(): void {
		const pageSlice = this.list.slice(0, PAGE_MIN_SIZE);
		this.result.emit(pageSlice);
	}

	onPageChange($event: any) {
		const page = $event;
		const startPage = page.pageIndex * page.pageSize;
		const pageSlice = this.list.slice(startPage, $event.pageSize + startPage);
		this.result.emit(pageSlice);
	}

}
