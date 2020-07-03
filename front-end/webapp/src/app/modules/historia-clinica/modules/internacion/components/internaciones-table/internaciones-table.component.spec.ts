import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InternacionesTableComponent } from './internaciones-table.component';

describe('PacientesInternadosTableComponent', () => {
  let component: InternacionesTableComponent;
  let fixture: ComponentFixture<InternacionesTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InternacionesTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InternacionesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
