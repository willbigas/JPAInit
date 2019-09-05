package br.senac.sc;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import br.senac.sc.model.Departamento;
import br.senac.sc.model.Endereco;
import br.senac.sc.model.Funcionario;

public class Exemplos {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("Funcionarios-PU");

		EntityManager entityManager = factory.createEntityManager();

//		salvaFuncionario(entityManager);
//		alteraFuncionario(entityManager);
//		removeFuncionario(entityManager);
//		buscaFuncionarioPorID(entityManager);
//		buscarFuncionarios(entityManager);
//		buscarFuncionariosOrdenadosPorNome(entityManager);
//		buscarFuncionariosPorNome(entityManager);
//		testaRelacionamentoOneToOne(entityManager);
//		testaRelacionamentoManyToOne(entityManager);
		testaRelacionamentoOneToMany(entityManager);
	}

	private static void salvaFuncionario(EntityManager entityManager) {
		Funcionario funcionario = new Funcionario("Alice", "Alice.Silva@alunos.sc.senac.br");

		entityManager.getTransaction().begin();
		entityManager.persist(funcionario);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	public static void alteraFuncionario(EntityManager entityManager) {
		Funcionario funcionario = new Funcionario();
		funcionario.setCodigo(1L);
		funcionario.setNome("Nome alterado");
		funcionario.setEmail("william.mauro@alunos.sc.senac.br");

		entityManager.getTransaction().begin();
		entityManager.merge(funcionario);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	private static void removeFuncionario(EntityManager entityManager) {
		Funcionario funcionario = new Funcionario();
		funcionario.setCodigo(1L);
		entityManager.getTransaction().begin();
		funcionario = entityManager.find(Funcionario.class, funcionario.getCodigo());
		entityManager.remove(funcionario);
		entityManager.getTransaction().commit();
		entityManager.close();

	}

	private static void buscaFuncionarioPorID(EntityManager entityManager) {
		Funcionario funcionario = new Funcionario();
		funcionario.setCodigo(2L);
		funcionario = entityManager.find(Funcionario.class, funcionario.getCodigo());
		System.out.println(funcionario);
		entityManager.close();

	}

	private static void buscarFuncionarios(EntityManager entityManager) {
//		.createQuery("select f from Funcionario f", Funcionario.class); // 
		// SELECT * FROM FUNCIONARIO - SQL
		// SELECT f from Funcionario - JPQL
		// from Funcionario - JQPL REDUZIDO

		TypedQuery<Funcionario> query = entityManager.createQuery("from Funcionario", Funcionario.class);

		List<Funcionario> funcionarios = query.getResultList();

		entityManager.close();

		for (Funcionario funcionario : funcionarios) {
			System.out.println(funcionario);
		}
	}

	private static void buscarFuncionariosOrdenadosPorNome(EntityManager entityManager) {
		String jpql = "select f from Funcionario f order by f.nome";

		TypedQuery<Funcionario> query = entityManager.createQuery(jpql, Funcionario.class);

		List<Funcionario> funcionarios = query.getResultList();

		entityManager.close();

		for (Funcionario funcionario : funcionarios) {
			System.out.println(funcionario);
		}
	}

	private static void buscarFuncionariosPorNome(EntityManager entityManager) {

//		String jpql = "select f from Funcionario f where f.nome = :batata";
		String jpql = "select f from Funcionario f where f.nome like concat('%', :nome , '%')";

		TypedQuery<Funcionario> query = entityManager.createQuery(jpql, Funcionario.class);

		query.setParameter("nome", "João");
		List<Funcionario> funcionarios = query.getResultList();

		entityManager.close();

		for (Funcionario funcionario : funcionarios) {
			System.out.println(funcionario);
		}
	}

	private static void testaRelacionamentoOneToOne(EntityManager entityManager) {
		Endereco endereco = new Endereco();
		endereco.setNumero(456);
		endereco.setCep("000000");
		endereco.setRua("Rua asdasdsada");
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("asdsadsa");
		funcionario.setEmail("zezasdadainho@gmail.com");
		funcionario.setEndereco(endereco);

		entityManager.getTransaction().begin();
		entityManager.persist(funcionario);
		entityManager.getTransaction().commit();

		Funcionario funcionarioSalvo = entityManager.find(Funcionario.class, funcionario.getCodigo());

		System.out.println(funcionarioSalvo.getEndereco());
		entityManager.close();
	}
	
	private static void testaRelacionamentoManyToOne(EntityManager entityManager) {
		Departamento departamento = new Departamento();
		departamento.setNome("Vendas");
		
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("Henrique");
		funcionario.setEmail("henrique@email.com");
		funcionario.setDepartamento(departamento);
		entityManager.getTransaction().begin();
		entityManager.persist(departamento);
		entityManager.persist(funcionario);
		entityManager.getTransaction().commit();
		
		entityManager.clear();
		
		Funcionario funcionarioSalvo = entityManager.find(Funcionario.class, funcionario.getCodigo());
		
		System.out.println(funcionarioSalvo);
		
	}
	
	private static void testaRelacionamentoOneToMany(EntityManager entityManager) {
		Departamento departamento = new Departamento();
		departamento.setNome("RH");
		
		Funcionario funcionario1 = new Funcionario();
		funcionario1.setNome("Maycon");
		funcionario1.setEmail("mayckon@gmail.com");
		funcionario1.setDepartamento(departamento);
		
		Funcionario funcionario2 = new Funcionario();
		funcionario2.setNome("ABCD");
		funcionario2.setEmail("asasasa@gmail.com");
		funcionario2.setDepartamento(departamento);
		
		entityManager.getTransaction().begin();
		entityManager.persist(departamento);
		entityManager.persist(funcionario1);
		entityManager.persist(funcionario2);
		entityManager.getTransaction().commit();
		
		// Limpa a memoria para o find funcionar corretamente indo pro banco pesquisar
		entityManager.clear();
		
		Departamento departamentoSalvo = entityManager.find(Departamento.class, departamento.getCodigo());
		
		for (Funcionario  funcionario : departamentoSalvo.getFuncionarios()) {
			System.out.println(funcionario);
		}
		
		// Funcionalidades do Java 8 (Stream API)
//		departamentoSalvo.getFuncionarios().forEach(f -> System.out.println(f));
		departamento.getFuncionarios().forEach(System::out::println);
		
	}

}
