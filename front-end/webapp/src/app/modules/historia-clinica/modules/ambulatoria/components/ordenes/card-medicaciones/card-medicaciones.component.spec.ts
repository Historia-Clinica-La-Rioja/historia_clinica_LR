import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardMedicacionesComponent } from './card-medicaciones.component';

describe('CardMedicacionesComponent', () => {
  let component: CardMedicacionesComponent;
  let fixture: ComponentFixture<CardMedicacionesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardMedicacionesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardMedicacionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
