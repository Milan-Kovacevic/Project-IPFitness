import { NgModule } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { CheckboxModule } from 'primeng/checkbox';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { FileUploadModule } from 'primeng/fileupload';
import { ToastModule } from 'primeng/toast';
import { RippleModule } from 'primeng/ripple';
import { DividerModule } from 'primeng/divider';
import { TooltipModule } from 'primeng/tooltip';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { ImageModule } from 'primeng/image';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { BlockUIModule } from 'primeng/blockui';
import { PanelModule } from 'primeng/panel';
import { CardModule } from 'primeng/card';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { ToggleButtonModule } from 'primeng/togglebutton';
import { BadgeModule } from 'primeng/badge';
import { MenuModule } from 'primeng/menu';
import { DropdownModule } from 'primeng/dropdown';
import { GalleriaModule } from 'primeng/galleria';
import { PaginatorModule } from 'primeng/paginator';
import { AvatarModule } from 'primeng/avatar';
import { AvatarGroupModule } from 'primeng/avatargroup';
import { StepsModule } from 'primeng/steps';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { CalendarModule } from 'primeng/calendar';
import { TableModule } from 'primeng/table';
import { MultiSelectModule } from 'primeng/multiselect';
import { SkeletonModule } from 'primeng/skeleton';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { ListboxModule } from 'primeng/listbox';
import { TabViewModule } from 'primeng/tabview';
import { InputMaskModule } from 'primeng/inputmask';
import { ChartModule } from 'primeng/chart';
import { KnobModule } from 'primeng/knob';

const primeNgModules = [
  ButtonModule,
  CheckboxModule,
  InputTextModule,
  PasswordModule,
  FileUploadModule,
  ToastModule,
  RippleModule,
  DividerModule,
  TooltipModule,
  InputTextareaModule,
  ImageModule,
  DialogModule,
  ConfirmDialogModule,
  BlockUIModule,
  PanelModule,
  CardModule,
  ProgressSpinnerModule,
  ToggleButtonModule,
  BadgeModule,
  MenuModule,
  DropdownModule,
  GalleriaModule,
  PaginatorModule,
  AvatarModule,
  AvatarGroupModule,
  StepsModule,
  InputGroupModule,
  InputGroupAddonModule,
  CalendarModule,
  TableModule,
  MultiSelectModule,
  SkeletonModule,
  OverlayPanelModule,
  ListboxModule,
  TabViewModule,
  InputMaskModule,
  ChartModule,
  KnobModule,
];

@NgModule({
  imports: [...primeNgModules],
  exports: [...primeNgModules],
})
export class PrimeNgModule {}
