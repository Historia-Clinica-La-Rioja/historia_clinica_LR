import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DialogoInconsistenciaComponent } from './dialogo-inconsistencia.component';

describe('DialogoInconsistenciaComponent', () => {
  let component: DialogoInconsistenciaComponent;
  let fixture: ComponentFixture<DialogoInconsistenciaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DialogoInconsistenciaComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DialogoInconsistenciaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
