import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PowerattackSharedModule } from 'app/shared/shared.module';
import { AttackCycleComponent } from './attack-cycle.component';
import { AttackCycleDetailComponent } from './attack-cycle-detail.component';
import { AttackCycleUpdateComponent } from './attack-cycle-update.component';
import { AttackCycleDeleteDialogComponent } from './attack-cycle-delete-dialog.component';
import { attackCycleRoute } from './attack-cycle.route';

@NgModule({
  imports: [PowerattackSharedModule, RouterModule.forChild(attackCycleRoute)],
  declarations: [AttackCycleComponent, AttackCycleDetailComponent, AttackCycleUpdateComponent, AttackCycleDeleteDialogComponent],
  entryComponents: [AttackCycleDeleteDialogComponent]
})
export class PowerattackAttackCycleModule {}
