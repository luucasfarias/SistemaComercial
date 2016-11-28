package br.com.sistemacomercial.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sistemacomercial.model.Cliente;
import br.com.sistemacomercial.model.Endereco;
import br.com.sistemacomercial.service.CadastroClienteService;
import br.com.sistemacomercial.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CadastroClienteService clienteService;

	private Cliente cliente;

	private List<Endereco> enderecos;

	public CadastroClienteBean() {
		limpar();
	}

	public void limpar() {
		cliente = new Cliente();
		enderecos = new ArrayList<>();
	}

	public void salvar() {
		this.cliente = clienteService.salvar(this.cliente);// apagar
		// depois
		limpar();

		FacesUtil.addInfoMessage("Cliente cadastrado com sucesso!");
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

}
