package br.com.sistemacomercial.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sistemacomercial.model.Pedido;
import br.com.sistemacomercial.model.StatusPedido;
import br.com.sistemacomercial.repository.Pedidos;
import br.com.sistemacomercial.util.jpa.Transactional;

public class CancelamentoPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Pedidos pedidos;

	@Inject
	private EstoqueService estoqueService;

	@Transactional
	public Pedido cancelar(Pedido pedido) {
		pedido = this.pedidos.porId(pedido.getId());
		if (pedido.isNaoCancelavel()) {
			throw new NegocioException("Ops, verifique. Pois o pedido não pode ser cancelado no status de "
					+ pedido.getStatus().getDescricao() + ".");
		}
		if (pedido.isEmitido()) { //este metedo retorna a quantidade anterior do numero de estoque se o pedido for cancelado na fase de emitido.
			this.estoqueService.retornarItensEstoque(pedido);
		}
		
		pedido.setStatus(StatusPedido.CANCELADO);
		pedido = this.pedidos.guardar(pedido);
		
		return pedido;
	}

}
