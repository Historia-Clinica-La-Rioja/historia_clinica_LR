import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { HierarchicalUnitStaffDto } from '@api-rest/api-model';
import { MatDialog } from '@angular/material/dialog';
import { HierarchicalUnitStaffService } from '@api-rest/services/hierarchical-unit-staff.service';
import { EditHierarchicalUnitsComponent } from '@pacientes/dialogs/edit-hierarchical-units/edit-hierarchical-units.component';

@Component({
	selector: 'app-hierarchical-units',
	templateUrl: './hierarchical-units.component.html',
	styleUrls: ['./hierarchical-units.component.scss']
})
export class HierarchicalUnitsComponent implements OnChanges {

	personHU: HierarchicalUnitStaffDto[] = [];
	@Input() enable = true;
	@Input() userId: number;
	@Input() hasRoles = true;

	constructor(
		private readonly hierarchicalUnitStaffService: HierarchicalUnitStaffService,
		private readonly dialog: MatDialog,
	) { }

	ngOnChanges(changes: SimpleChanges) {
		if (changes.userId?.currentValue || this.enable)
			this.hierarchicalUnitStaffService.getByUserId(this.userId).subscribe(hu => this.personHU = hu);
	}

	editHierarchicalUnits() {
		const dialog = this.dialog.open(EditHierarchicalUnitsComponent, {
			disableClose: true,
			data: {
				userId: this.userId,
				personHU: this.personHU,
				hasRoles: this.hasRoles
			}
		});

		dialog.afterClosed().subscribe(success => {
			if (success) {
				this.hierarchicalUnitStaffService.getByUserId(this.userId).subscribe(hu => this.personHU = hu);
			}
		})
	}
}
