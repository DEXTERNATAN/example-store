/*
 * Demoiselle Framework
 *
 * License: GNU Lesser General Public License (LGPL), version 3 or later.
 * See the lgpl.txt file in the root directory or <https://www.gnu.org/licenses/lgpl.html>.
 */
package org.demoiselle.jee7.example.store.user.business;

import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import org.demoiselle.jee.script.DynamicManager;
import org.demoiselle.jee7.example.store.user.crud.GenericCrudBusiness;
import org.demoiselle.jee7.example.store.user.dao.UserDAO;
import org.demoiselle.jee7.example.store.user.dao.multitenancy.MultiTenantContext;
import org.demoiselle.jee7.example.store.user.entity.User;

/**
 * O TransactionManagementType.BEAN significa que o ciclo de transação das
 * requisições será gerenciado por padrão pelo usuário utilizando o objeto
 * UserTransaction. Neste caso quando se quiser que o container trate as
 * requisições (begin, commit e rollback) basta anotar o método
 * com @Transaction.
 * 
 * @author SERPRO
 *
 */
@Stateless
@TransactionManagement(value = TransactionManagementType.BEAN)
public class UserBC extends GenericCrudBusiness<User> {

	@Inject
	private UserDAO dao;

	@Resource
	private UserTransaction userTransaction;

	@Inject
	private MultiTenantContext multiTenantContext;

	@Inject
	private DynamicManager scriptManager;

	@Override
	protected UserDAO getPersistenceDAO() {
		return dao;
	}

	@Transactional
	@Override
	public User create(User entity) {

		// Aplica o script no usuário do contexto de multitenancy
		// multiTenantContext
		try {
			if (multiTenantContext.getTenant().getScriptCreateUser() != null
					&& !multiTenantContext.getTenant().getScriptCreateUser().isEmpty()) {

				SimpleBindings vars = new SimpleBindings();
				vars.put("usuario", entity);
				vars.put("tenant", multiTenantContext.getTenant());

				String scriptId = "createUser-" + multiTenantContext.getTenant().getName();

				// Verifica se existe o script no cache
				if (scriptManager.getScript(scriptId) == null) {
					System.out.println("Criado o script [" + scriptId + "].");

					scriptManager.loadEngine("groovy");
					scriptManager.loadScript(scriptId, multiTenantContext.getTenant().getScriptCreateUser());
				}

				scriptManager.eval(scriptId, vars);
			}
		} catch (ScriptException e) {
			e.printStackTrace();
		}

		return getPersistenceDAO().create(entity);
	}

	public void createTesteTransacional1(User usuario)
			throws SecurityException, IllegalStateException, RollbackException, HeuristicMixedException,
			HeuristicRollbackException, SystemException, NotSupportedException {

		userTransaction.begin();
		getPersistenceDAO().create(usuario);
		userTransaction.commit();

		userTransaction.begin();

		UUID uuid = UUID.randomUUID();
		String emailUniqueTest = uuid.toString() + "@teste.com.br";
		String nomeUniqueTest = "Nome " + uuid.toString();

		usuario = new User();
		usuario.setId(null);
		usuario.setNome(nomeUniqueTest);
		usuario.setEmail(emailUniqueTest);
		getPersistenceDAO().create(usuario);

		usuario = new User();
		usuario.setId(null);
		usuario.setNome(nomeUniqueTest);
		usuario.setEmail(emailUniqueTest);
		getPersistenceDAO().create(usuario);

		userTransaction.commit();
	}

	public void createTesteTransacional2(User usuario) {
		getPersistenceDAO().create(usuario);
	}

	@Transactional
	public void createTesteTransacional3(User usuario) {

		getPersistenceDAO().create(usuario);

		UUID uuid = UUID.randomUUID();
		String emailUniqueTest = uuid.toString() + "@teste.com.br";
		String nomeUniqueTest = "Nome " + uuid.toString();

		usuario = new User();
		usuario.setId(null);
		usuario.setNome(nomeUniqueTest);
		usuario.setEmail(emailUniqueTest);
		getPersistenceDAO().create(usuario);

		usuario = new User();
		usuario.setId(null);
		usuario.setNome(nomeUniqueTest);
		usuario.setEmail(emailUniqueTest);
		getPersistenceDAO().create(usuario);

	}

	public User loadByEmailAndSenha(String email, String senha) {
		User u = getPersistenceDAO().loadByEmailAndSenha(email, senha);
		return u;
	}

}