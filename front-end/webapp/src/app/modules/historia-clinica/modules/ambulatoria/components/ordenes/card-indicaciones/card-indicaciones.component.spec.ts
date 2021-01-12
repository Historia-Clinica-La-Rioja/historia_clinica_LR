import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardIndicacionesComponent } from './card-indicaciones.component';

describe('CardIndicacionesComponent', () => {
  let component: CardIndicacionesComponent;
  let fixture: ComponentFixture<CardIndicacionesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardIndicacionesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardIndicacionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
