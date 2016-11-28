package br.com.sistemacomercial.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sistemacomercial.model.Produto;
import br.com.sistemacomercial.repository.Produtos;
import br.com.sistemacomercial.util.jpa.Transactional;

public class CadastroProdutoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Produtos produtos;

	@Transactional
	public Produto salvar(Produto produto) {
		Produto produtoExistente = produtos.porSku(produto.getSku());
		if (produtoExistente != null && !produtoExistente.equals(produto)) {
			throw new NegocioException(
					"Já existe um produto com este SKU cadastrado no sistema.");
		}
		return produtos.guardar(produto);
	}
}
