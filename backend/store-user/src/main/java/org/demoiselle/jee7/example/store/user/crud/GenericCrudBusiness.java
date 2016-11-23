/*
 * Demoiselle Framework
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later.
 * See the lgpl.txt file in the root directory or <https://www.gnu.org/licenses/lgpl.html>.
 */
package org.demoiselle.jee7.example.store.user.crud;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;

import org.demoiselle.jee.core.message.DemoiselleMessage;
import org.demoiselle.jee.persistence.jpa.crud.GenericDataPage;
import org.demoiselle.jee7.example.store.user.dao.context.PersistenceContextDAO;

/**
 * O métodos marcados com @Transaction são obrigatoriamente transacionais.
 * 
 * @author SERPRO
 *
 * @param <T>
 */
public abstract class GenericCrudBusiness<T> {

	protected abstract PersistenceContextDAO<T> getPersistenceDAO();

	@Inject
	private DemoiselleMessage messages;
	
	@Transactional
	public T create(T entity) {
		return getPersistenceDAO().create(entity);
	}

	@Transactional
	public T edit(int id, T entity) {
		try {
			// é necessário setar o ID do objeto para que ele possa ser alterado
			// esta ação é feita por reflexão por não termos o tipo do objeto
			Method m = entity.getClass().getMethod("setId", new Class[] { Integer.class });
			m.invoke(entity, id);
		} catch (InvocationTargetException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}

		return getPersistenceDAO().edit(entity);
	}

	@Transactional
	public void remove(Object id) {
		T obj = find(id);		
		getPersistenceDAO().remove(obj);
	}

	public T find(Object id) {
		T obj = getPersistenceDAO().find(id);
		if (obj == null) {
			throw new NotFoundException(messages.keyNotFound(id.toString()));
		}
		return obj;
	}

	public List<T> findAll() {
		return getPersistenceDAO().findAll();
	}

	public List<T> findRange(int[] range) {
		return getPersistenceDAO().findRange(range);
	}

	public GenericDataPage list() {
		return getPersistenceDAO().list();
	}

	public List<T> find(String whereField, String whereValue, String fieldOrder, String order, int init, int qtde) {
		return getPersistenceDAO().find(whereField, whereValue, fieldOrder, order, init, qtde);
	}

	public Long count() {
		return getPersistenceDAO().count();
	}

}