import { OnInit } from '@angular/core';
import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-card-roles',
	templateUrl: './card-roles.component.html',
	styleUrls: ['./card-roles.component.scss']
})
export class CardRolesComponent implements OnInit {
	@Input() userRoles: string[];
	@Input() institutionName: string;
	constructor() { }
	ngOnInit(): void {
	}
	hasAssignedRoles(): boolean {
		return this.userRoles.length > 0 ? true : false;
	}

}
