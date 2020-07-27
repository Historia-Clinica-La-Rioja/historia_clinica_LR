import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewAttentionComponent } from './new-attention.component';

describe('NewAttentionComponent', () => {
  let component: NewAttentionComponent;
  let fixture: ComponentFixture<NewAttentionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewAttentionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewAttentionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
