import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstitucionComponent } from './institucion.component';

describe('InstitucionComponent', () => {
	let component: InstitucionComponent;
	let fixture: ComponentFixture<InstitucionComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [InstitucionComponent]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(InstitucionComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
