import { NgModule } from '@angular/core';
import { ExtraOptions, RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { LoginComponent } from './component/login/login.component';
import { PageNotFoundComponent } from './component/page-not-found/page-not-found.component';
import { ListPensionerDetailComponent } from './component/pensioner/list-pensioner-detail/list-pensioner-detail.component';
import { SearchPensionerComponent } from './component/pensioner/search-pensioner/search-pensioner.component';
import { ViewPensionComponent } from './component/pensioner/view-pension/view-pension.component';
import { ViewPensionerDetailComponent } from './component/pensioner/view-pensioner-detail/view-pensioner-detail.component';
import { AdminAuthGuard } from './services/admin.auth.guard';
import { AuthGuard } from './services/auth.guard';

export const routingConfiguration: ExtraOptions = {
  paramsInheritanceStrategy: 'always',
};

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    pathMatch: 'full',
  },

  {
    path: 'login',
    component: LoginComponent,
    pathMatch: 'full',
  },

  {
    path: 'pensioner',
    children: [
      {
        path: 'list',
        component: ListPensionerDetailComponent,
        pathMatch: 'full',
        canActivate: [AdminAuthGuard],
      },
      {
        path: 'search',
        component: SearchPensionerComponent,
        pathMatch: 'full',
        canActivate: [AuthGuard],
      },
      {
        path: 'view/:id',
        component: ViewPensionerDetailComponent,
        canActivate: [AuthGuard],
      },
      {
        path: 'process',
        component: ViewPensionComponent,
        pathMatch: 'full',
        canActivate: [AuthGuard],
      },
    ],
  },

  {
    path: 'not-found',
    component: PageNotFoundComponent,
  },

  { path: '**', redirectTo: '/not-found' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, routingConfiguration)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
