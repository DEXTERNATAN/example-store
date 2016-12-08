

import { Injectable } from '@angular/core';
import { Http} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import { Produto } from './produto.model';

@Injectable()
export class ProdutoService {
  
    constructor(private http: Http) {

    }
    list () {

        return this.http.get('~livraria/api/user')
                    .map(
                      res => <Produto[]> res.json()
                      
                    )
                    .catch( function (error){

                        return Observable.throw(<Produto[]>[
                          {
                            id: 1,
                            name: 'Demoiselle1',
                            description: 'Produto 1111111111111111111111'
                          },
                          {
                            id: 2,
                            name: 'Demoiselle 2',
                            description: 'Produto com data 12/12/1081'
                          }
                        ]);
                    });
    }
}
