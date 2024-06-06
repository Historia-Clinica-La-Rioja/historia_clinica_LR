import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { Label } from '../label/label.component';

@Component({
	selector: 'app-route-menu',
	templateUrl: './route-menu.component.html',
	styleUrls: ['./route-menu.component.scss']
})
export class RouteMenuComponent implements OnInit {

	label: Label;
	routeItems: any[];
	childActive = false;
	data$: Observable<RouteMenuData>;

	constructor(
		private router: Router,
		private route: ActivatedRoute,
		private cdr: ChangeDetectorRef,
	) {
		this.data$ = route.data.pipe(map(data => data as RouteMenuData));
		this.data$.pipe(
			map(data => data.label),
		).subscribe(
			label => this.label = label as Label
		);
	}

	ngOnInit(): void {

		this.routeItems = (this.route.routeConfig.children || []).map(
			childRoute => {
				console.log('ngOnInit', childRoute);
				return {
					route: childRoute,
				}
			}
		);
	}

	navigate(route: Route): void {
		this.router.navigate([route.path || '.'], { relativeTo: this.route });
	}

	get icon$(): Observable<string> {
		return this.data$.pipe(map(data => data.icon));
	}

	onActivate(event): void {
		console.log('onActivate', event);
		// childActive se inicializa como false, el cambio puede estar sucediendo inmediatamente luego de inicializar
		this.childActive = true;
		// Forzar una nueva detección de cambios
		this.cdr.detectChanges();
	}

	onDeactivate(event): void {
		this.childActive = false;
	}

}

export interface RouteMenuData {
	label: Label;
	icon: string;
}
