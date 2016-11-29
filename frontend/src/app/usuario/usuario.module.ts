import { NgModule } from '@angular/core';
import {SharedModule} from '../shared';

import {ConfirmationPopoverModule} from 'angular-confirmation-popover';
import { PaginationModule } from 'ng2-bootstrap/ng2-bootstrap';

import { UsuarioService } from './usuario.service';
import { UsuarioComponent } from './usuario.component';
import { UsuarioEditComponent } from './usuario-edit.component';



@NgModule({
    imports: [
        SharedModule,
        ConfirmationPopoverModule.forRoot({
            confirmText: 'Sim',
            cancelText: 'Não',
            appendToBody: true
        })
    ],
    declarations: [
        UsuarioComponent, UsuarioEditComponent
    ],
    providers: [UsuarioService]
})
export class UsuarioModule {}