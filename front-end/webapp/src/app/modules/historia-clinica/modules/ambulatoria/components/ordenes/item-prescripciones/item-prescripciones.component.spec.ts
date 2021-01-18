import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ItemPrescripcionesComponent } from './item-prescripciones.component';

describe('ItemPrescripcionesComponent', () => {
  let component: ItemPrescripcionesComponent;
  let fixture: ComponentFixture<ItemPrescripcionesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ItemPrescripcionesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ItemPrescripcionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
