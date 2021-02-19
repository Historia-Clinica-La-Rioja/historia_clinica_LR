import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

const flat = (route: ActivatedRoute) => route ? [...flat(route.parent), ...route.snapshot.url.map(urlSegment => urlSegment.path)] : [];

@Component({
	selector: 'app-mock',
	templateUrl: './mock.component.html',
	styleUrls: ['./mock.component.scss']
})
export class MockComponent implements OnInit {
	urlSegments: string[];
	navigate: (path: string) => void;
	screenshot: string;
	loads: [];
	actions: [];

	constructor(
		router: Router,
		route: ActivatedRoute,
		private readonly location: Location,
	) {
		this.urlSegments = flat(route);
		this.navigate = (relPath) => router.navigate([relPath], { relativeTo: route });
		route.data.subscribe(data => {
			console.log('MockComponent route.data', data);
			this.loads = data.loads;
			this.actions = data.actions;
			this.screenshot = data.screenshot;
		});
		console.log('MockComponent route.snapshot', route.snapshot);
	}

	ngOnInit(): void {
	}

	goTo(navigate): void {
		if (navigate === 'back') {
			this.location.back();
		} else {
			this.navigate(navigate);
		}

	}
}
