import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EvaluacionClinicaDiagnosticosComponent } from './evaluacion-clinica-diagnosticos.component';

describe('EvaluacionClinicaDiagnosticosComponent', () => {
  let component: EvaluacionClinicaDiagnosticosComponent;
  let fixture: ComponentFixture<EvaluacionClinicaDiagnosticosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EvaluacionClinicaDiagnosticosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EvaluacionClinicaDiagnosticosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
