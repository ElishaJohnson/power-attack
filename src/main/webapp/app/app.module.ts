import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import './vendor';
import { PowerattackSharedModule } from 'app/shared/shared.module';
import { PowerattackCoreModule } from 'app/core/core.module';
import { PowerattackAppRoutingModule } from './app-routing.module';
import { PowerattackHomeModule } from './home/home.module';
import { PowerattackEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    PowerattackSharedModule,
    PowerattackCoreModule,
    PowerattackHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    PowerattackEntityModule,
    PowerattackAppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class PowerattackAppModule {}
