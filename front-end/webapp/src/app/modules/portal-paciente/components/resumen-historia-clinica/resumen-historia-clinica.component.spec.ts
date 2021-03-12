import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResumenHistoriaClinicaComponent } from './resumen-historia-clinica.component';

describe('MiHistoriaClinicaComponent', () => {
  let component: ResumenHistoriaClinicaComponent;
  let fixture: ComponentFixture<ResumenHistoriaClinicaComponent>;

  beforeEach(async(() => {
	TestBed.configureTestingModule({
		declarations: [ ResumenHistoriaClinicaComponent ]
	})
	.compileComponents();
  }));

  beforeEach(() => {
	fixture = TestBed.createComponent(ResumenHistoriaClinicaComponent);
	component = fixture.componentInstance;
	fixture.detectChanges();
  });

  it('should create', () => {
	expect(component).toBeTruthy();
  });
});
