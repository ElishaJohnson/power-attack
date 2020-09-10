import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PowerattackSharedModule } from 'app/shared/shared.module';
import { AttackComponent } from './attack.component';
import { AttackDetailComponent } from './attack-detail.component';
import { AttackUpdateComponent } from './attack-update.component';
import { AttackDeleteDialogComponent } from './attack-delete-dialog.component';
import { attackRoute } from './attack.route';

@NgModule({
  imports: [PowerattackSharedModule, RouterModule.forChild(attackRoute)],
  declarations: [AttackComponent, AttackDetailComponent, AttackUpdateComponent, AttackDeleteDialogComponent],
  entryComponents: [AttackDeleteDialogComponent]
})
export class PowerattackAttackModule {}
