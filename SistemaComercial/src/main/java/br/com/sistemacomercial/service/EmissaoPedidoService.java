package br.com.sistemacomercial.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sistemacomercial.model.Pedido;
import br.com.sistemacomercial.model.StatusPedido;
import br.com.sistemacomercial.repository.Pedidos;
import br.com.sistemacomercial.util.jpa.Transactional;

public class EmissaoPedidoService implements Serializable { // ESTA CLASSE É UM
															// BEAN CDI

	private static final long serialVersionUID = 1L;

	@Inject
	private CadastroPedidoService cadastroPedidoService;

	@Inject
	private Pedidos pedidos;

	@Inject
	private EstoqueService estoqueService;

	@Transactional // anotação que cuida das operações de transação com o banco de dados.
	public Pedido emitir(Pedido pedido) {
		pedido = this.cadastroPedidoService.salvar(pedido);

		if (pedido.isNaoEmissivel()) {
			throw new NegocioException("Este pedido não poderá ser emitido, verifique o status "
					+ pedido.getStatus().getDescricao() + ".");
		}

		this.estoqueService.baixarItensEstoque(pedido);

		pedido.setStatus(StatusPedido.EMITIDO);

		pedido = this.pedidos.guardar(pedido);

		return pedido;
	}

}
