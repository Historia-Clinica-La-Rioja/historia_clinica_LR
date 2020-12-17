import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmarPrescripcionComponent } from './confirmar-prescripcion.component';

describe('ConfirmarPrescripcionComponent', () => {
  let component: ConfirmarPrescripcionComponent;
  let fixture: ComponentFixture<ConfirmarPrescripcionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConfirmarPrescripcionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmarPrescripcionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
