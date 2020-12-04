import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OrdenesComponent } from './ordenes.component';

describe('OrdenesComponent', () => {
  let component: OrdenesComponent;
  let fixture: ComponentFixture<OrdenesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OrdenesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrdenesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
