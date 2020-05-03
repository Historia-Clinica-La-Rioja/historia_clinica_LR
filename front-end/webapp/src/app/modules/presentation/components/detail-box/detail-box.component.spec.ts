import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailBoxComponent } from './detail-box.component';

describe('DetailBoxComponent', () => {
  let component: DetailBoxComponent;
  let fixture: ComponentFixture<DetailBoxComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetailBoxComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
