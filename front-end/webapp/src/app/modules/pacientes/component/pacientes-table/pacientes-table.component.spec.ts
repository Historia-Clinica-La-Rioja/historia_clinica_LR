import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PacientesTableComponent } from './pacientes-table.component';

describe('PacientesTableComponent', () => {
  let component: PacientesTableComponent;
  let fixture: ComponentFixture<PacientesTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PacientesTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PacientesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
