import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { HierarchicalUnitStaffDto, UserDataDto } from '@api-rest/api-model';
import { HierarchicalUnitStaffService } from '@api-rest/services/hierarchical-unit-staff.service';

@Component({
	selector: 'app-hierarchical-units',
	templateUrl: './hierarchical-units.component.html',
	styleUrls: ['./hierarchical-units.component.scss']
})
export class HierarchicalUnitsComponent implements OnChanges {

	personHU: HierarchicalUnitStaffDto[] = [];
	@Input() userData: UserDataDto;
	@Input() hasRoles = true;

	constructor(
		private readonly hierarchicalUnitStaffService: HierarchicalUnitStaffService,
	) { }

	ngOnChanges(changes: SimpleChanges) {
		if (changes.userData.currentValue && this.userData.id)
			this.hierarchicalUnitStaffService.getByUserId(this.userData.id).subscribe(hu => this.personHU = hu);
	}

}
