import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectConsultorioComponent } from './select-consultorio.component';

describe('SelectConsultorioComponent', () => {
	let component: SelectConsultorioComponent;
	let fixture: ComponentFixture<SelectConsultorioComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [ SelectConsultorioComponent ]
		})
			.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(SelectConsultorioComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
