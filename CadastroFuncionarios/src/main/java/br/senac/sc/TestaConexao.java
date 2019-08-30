package br.senac.sc;

import javax.persistence.Persistence;

public class TestaConexao {
	public static void main(String[] args) {
		
		Persistence.createEntityManagerFactory("Funcionarios-PU");
		
	}
}
