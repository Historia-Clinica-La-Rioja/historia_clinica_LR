import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

	constructor(private router: Router) {
	}

	ngOnInit(): void {
	}

	goToMockup(): void {
		console.log('asdfasfasdfasdfasfasdfasdfasd');
		this.router.navigate([`${this.router.url}/mock`]);
	}

}
