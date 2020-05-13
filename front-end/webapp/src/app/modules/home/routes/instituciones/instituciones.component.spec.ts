import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstitucionesComponent } from './instituciones.component';

describe('InstitucionesComponent', () => {
	let component: InstitucionesComponent;
	let fixture: ComponentFixture<InstitucionesComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [InstitucionesComponent]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(InstitucionesComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
