package br.com.sistemacomercial.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.sistemacomercial.model.Cliente;
import br.com.sistemacomercial.repository.Clientes;
import br.com.sistemacomercial.util.jpa.Transactional;

public class CadastroClienteService implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private Clientes clientes;
	
	
	@Transactional
	public Cliente salvar(Cliente cliente) {
		List<Cliente> clienteExistente = clientes.porNome(cliente.getNome());
		if (clienteExistente == null && !clienteExistente.equals(cliente)) {
			throw new NegocioException(
					"Já existe um cliente com este ID cadastrado no sistema.");
		}
		return clientes.guardar(cliente);
	}

}
