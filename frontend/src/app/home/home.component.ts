import { Component, OnInit } from '@angular/core';
import { TenantService } from '../tenant/tenant.service';
import { NotificationService } from '../shared/notification.service';
import { Usuario } from '../usuario/usuario.model';
import { UsuarioService } from '../usuario/usuario.service';

@Component({
  selector: 'dml-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  public tenantName:string = '';
  public adminEmail:string = '';
  public adminPwd:string = '';

  constructor(protected service: TenantService, protected notificationService: NotificationService,
    protected usuarioService: UsuarioService) {
  }

  ngOnInit() {
    console.log('Hello Home');
  }

  addTenant() {
    console.log(this.tenantName);
    this.service.create({name: this.tenantName}).subscribe(
      () => {
        this.usuarioService.createOnTenant(new Usuario(
            undefined,
            this.adminEmail,
            'ADMIN',
            this.adminEmail,
            '',
            '',
            this.adminPwd
        ), this.tenantName).subscribe(
          () => {
            this.tenantName = '';
            this.adminEmail = '';
            this.adminPwd = '';
            this.notificationService.success('Nova loja salva com sucesso!');
          },
          error => {
            this.notificationService.error('Não foi possível inserir o usuário administrador!');
            this.rollbackTenant();
          }
        );
      },
      (error) => {
        this.notificationService.error('Não foi possível salvar a loja!');
        this.rollbackTenant();
      }
    );
  }

  rollbackTenant() {
    this.service.delete({name: this.tenantName}).subscribe(
      () => { },
      (error) => { }
    );
  }
}
