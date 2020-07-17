import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NuevaConsultaComponent } from './nueva-consulta.component';

describe('NuevaConsultaComponent', () => {
	let component: NuevaConsultaComponent;
	let fixture: ComponentFixture<NuevaConsultaComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [NuevaConsultaComponent]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(NuevaConsultaComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
