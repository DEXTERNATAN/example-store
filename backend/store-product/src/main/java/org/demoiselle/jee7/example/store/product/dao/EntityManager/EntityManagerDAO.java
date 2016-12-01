package org.demoiselle.jee7.example.store.product.dao.EntityManager;

/*
 * Demoiselle Framework
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later.
 * See the lgpl.txt file in the root directory or <https://www.gnu.org/licenses/lgpl.html>.
 */

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.demoiselle.jee.persistence.crud.AbstractDAO;

public abstract class EntityManagerDAO<T> extends AbstractDAO<T, Long> {

	@PersistenceContext(unitName = "ProductTenantsPU")
	protected EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}


