import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InternacionesHomeComponent } from './internaciones-home.component';

describe('InternacionesHomeComponent', () => {
  let component: InternacionesHomeComponent;
  let fixture: ComponentFixture<InternacionesHomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InternacionesHomeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InternacionesHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
