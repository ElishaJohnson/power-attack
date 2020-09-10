import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PowerattackTestModule } from '../../../test.module';
import { AttackCycleDetailComponent } from 'app/entities/attack-cycle/attack-cycle-detail.component';
import { AttackCycle } from 'app/shared/model/attack-cycle.model';

describe('Component Tests', () => {
  describe('AttackCycle Management Detail Component', () => {
    let comp: AttackCycleDetailComponent;
    let fixture: ComponentFixture<AttackCycleDetailComponent>;
    const route = ({ data: of({ attackCycle: new AttackCycle(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PowerattackTestModule],
        declarations: [AttackCycleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AttackCycleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AttackCycleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load attackCycle on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.attackCycle).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
