import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAttackCycle, AttackCycle } from 'app/shared/model/attack-cycle.model';
import { AttackCycleService } from './attack-cycle.service';
import { AttackCycleComponent } from './attack-cycle.component';
import { AttackCycleDetailComponent } from './attack-cycle-detail.component';
import { AttackCycleUpdateComponent } from './attack-cycle-update.component';

@Injectable({ providedIn: 'root' })
export class AttackCycleResolve implements Resolve<IAttackCycle> {
  constructor(private service: AttackCycleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAttackCycle> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((attackCycle: HttpResponse<AttackCycle>) => {
          if (attackCycle.body) {
            return of(attackCycle.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AttackCycle());
  }
}

export const attackCycleRoute: Routes = [
  {
    path: '',
    component: AttackCycleComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AttackCycles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AttackCycleDetailComponent,
    resolve: {
      attackCycle: AttackCycleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AttackCycles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AttackCycleUpdateComponent,
    resolve: {
      attackCycle: AttackCycleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AttackCycles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AttackCycleUpdateComponent,
    resolve: {
      attackCycle: AttackCycleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'AttackCycles'
    },
    canActivate: [UserRouteAccessService]
  }
];
