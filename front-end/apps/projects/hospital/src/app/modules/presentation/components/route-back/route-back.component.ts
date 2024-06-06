import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
	selector: 'app-route-back',
	templateUrl: './route-back.component.html',
	styleUrls: ['./route-back.component.scss']
})
export class RouteBackComponent implements OnInit {
	parentData: any;
	currentData: any;

	constructor(
		private route: ActivatedRoute,
	) { }

	ngOnInit(): void {
		this.route.data.subscribe(
			data => this.currentData = data
		);

		// Obtener la ruta padre
		const parentRoute = this.route.parent;

		// Comprobar si existe una ruta padre
		if (parentRoute) {
		  // Acceder al Data del ActivatedRoute del componente padre
		  const parentData = parentRoute.snapshot.data;
		  this.parentData = parentData;
		}
	}

}
