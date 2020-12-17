import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AgregarPrescripcionItemComponent } from './agregar-prescripcion-item.component';

describe('AgregarPrescripcionItemComponent', () => {
  let component: AgregarPrescripcionItemComponent;
  let fixture: ComponentFixture<AgregarPrescripcionItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AgregarPrescripcionItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AgregarPrescripcionItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
