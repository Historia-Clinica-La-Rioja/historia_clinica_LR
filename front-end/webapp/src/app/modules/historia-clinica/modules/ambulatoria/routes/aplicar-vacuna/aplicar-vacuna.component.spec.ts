import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AplicarVacunaComponent } from './aplicar-vacuna.component';

describe('AplicarVacunaComponent', () => {
	let component: AplicarVacunaComponent;
	let fixture: ComponentFixture<AplicarVacunaComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [AplicarVacunaComponent]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(AplicarVacunaComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
