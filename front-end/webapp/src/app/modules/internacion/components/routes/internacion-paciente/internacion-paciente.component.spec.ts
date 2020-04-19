import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InternacionPacienteComponent } from './internacion-paciente.component';

describe('InternacionPacienteComponent', () => {
  let component: InternacionPacienteComponent;
  let fixture: ComponentFixture<InternacionPacienteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InternacionPacienteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InternacionPacienteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
