import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FiltrosCamasComponent } from './filtros-camas.component';

describe('FiltrosCamasComponent', () => {
  let component: FiltrosCamasComponent;
  let fixture: ComponentFixture<FiltrosCamasComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FiltrosCamasComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FiltrosCamasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
