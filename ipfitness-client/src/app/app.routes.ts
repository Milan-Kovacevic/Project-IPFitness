import { Routes } from '@angular/router';
import { NavigationLayoutComponent } from './layouts/navigation-layout/navigation-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { LoginComponent } from './components/auth-page/login/login.component';
import { RegisterComponent } from './components/auth-page/register/register.component';
import { HomeComponent } from './components/home-page/home/home.component';
import { ProgramsComponent } from './components/programs-page/programs/programs.component';
import { SettingsComponent } from './components/settings-page/settings/settings.component';
import { ProfileComponent } from './components/settings-page/profile/profile.component';
import { PurchaseHistoryComponent } from './components/settings-page/purchase-history/purchase-history.component';
import { NotFoundComponent } from './components/not-found-page/not-found/not-found.component';
import { ActivateAccountComponent } from './components/auth-page/activate-account/activate-account.component';
import { MessagesComponent } from './components/messages-page/messages/messages.component';
import { MessageChatComponent } from './components/messages-page/message-chat/message-chat.component';
import { ContactAdvisorComponent } from './components/messages-page/contact-advisor/contact-advisor.component';
import { userActivatedGuard } from './guards/user-activated.guard';
import { SingleFitnessProgramComponent } from './components/programs-page/single-fitness-program/single-fitness-program.component';
import { ActivitiesComponent } from './components/activities-page/activities/activities.component';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  {
    path: 'auth',
    title: 'IPFitness',
    component: AuthLayoutComponent,
    children: [
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      { path: 'login', title: 'IPFitness - Login', component: LoginComponent },
      {
        path: 'register',
        title: 'IPFitness - Register',
        component: RegisterComponent,
      },
      {
        path: 'activate',
        title: 'IPFitness - Activate Account',
        component: ActivateAccountComponent,
      },
    ],
  },
  {
    path: '',
    title: 'IPFitness',
    component: NavigationLayoutComponent,
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', title: 'IPFitness - Home', component: HomeComponent },
      {
        path: 'programs',
        title: 'IPFitness - Programs',
        component: ProgramsComponent,
      },
      {
        path: 'programs/:id',
        title: 'IPFitness - Program Details',
        component: SingleFitnessProgramComponent,
      },
      {
        path: 'settings',
        title: 'IPFitness - Settings',
        component: SettingsComponent,
        children: [
          { path: '', redirectTo: 'profile', pathMatch: 'full' },
          {
            path: 'profile',
            title: 'IPFitness - Profile',
            component: ProfileComponent,
          },
          {
            path: 'history',
            title: 'IPFitness - Purchase History',
            component: PurchaseHistoryComponent,
            canActivate: [userActivatedGuard],
          },
        ],
      },
      {
        path: 'messages',
        title: 'IPFitness - Messages',
        canActivate: [userActivatedGuard],
        component: MessagesComponent,
      },
      {
        path: 'activities',
        title: 'IPFitness - Activities',
        canActivate: [userActivatedGuard],
        component: ActivitiesComponent,
      },
    ],
  },
  {
    path: '**',
    title: 'IPFitness - Page Not Found',
    component: NotFoundComponent,
  },
];
