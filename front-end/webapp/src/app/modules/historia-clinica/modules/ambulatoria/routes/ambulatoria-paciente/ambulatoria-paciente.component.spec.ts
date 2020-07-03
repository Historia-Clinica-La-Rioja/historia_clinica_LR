import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AmbulatoriaPacienteComponent } from './ambulatoria-paciente.component';

describe('AmbulatoriaPacienteComponent', () => {
	let component: AmbulatoriaPacienteComponent;
	let fixture: ComponentFixture<AmbulatoriaPacienteComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [AmbulatoriaPacienteComponent]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(AmbulatoriaPacienteComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
