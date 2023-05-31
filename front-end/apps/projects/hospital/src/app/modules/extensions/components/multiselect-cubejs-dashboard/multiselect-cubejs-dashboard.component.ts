import { Component, Input, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { UIComponentDto } from "@extensions/extensions-model";


@Component({
	selector: 'app-multiselect-cubejs-dashboard',
	templateUrl: './multiselect-cubejs-dashboard.component.html',
	styleUrls: ['./multiselect-cubejs-dashboard.component.scss']
})
export class MultiselectCubejsDashboardComponent implements OnInit{

	@Input() content: UIComponentDto[];
	public selectedDashboards: UIComponentDto[] = [];
	options: UIComponentDto[];

	constructor() {
	}

	ngOnInit(): void {
		this.options = [...this.content];
	}

	onChange(category: UIComponentDto) {
		this.selectedDashboards.push(category);

		const index = this.options.findIndex( elem => elem.args.label === category.args.label)
		if (index >= 0) {
			this.options.splice(index, 1);
		}
	}

	removeDashboard(dashboard) {
		const index = this.selectedDashboards.findIndex( elem => elem.args.label === dashboard.args.label)
		if (index >= 0) {
			this.selectedDashboards.splice(index, 1);
		}
		this.options.push(dashboard);
		this.options.sort((a, b) => a.args.label.localeCompare(b.args.label));
	}
}
