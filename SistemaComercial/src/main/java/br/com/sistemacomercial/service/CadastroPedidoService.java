package br.com.sistemacomercial.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import br.com.sistemacomercial.model.Pedido;
import br.com.sistemacomercial.model.StatusPedido;
import br.com.sistemacomercial.repository.Pedidos;
import br.com.sistemacomercial.util.jpa.Transactional;

public class CadastroPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Pedidos pedidos;

	@Transactional
	public Pedido salvar(Pedido pedido) {
		if (pedido.isNovo()) {
			pedido.setDataCriacao(new Date());
			pedido.setStatus(StatusPedido.ORCAMENTO);
		}

		pedido.recalcularValorTotal();
		
		
		if(pedido.isNaoAlteravel()){
			throw new NegocioException("Ops, o pedido não pode ser alterado no status " + pedido.getStatus().getDescricao() + ".");
		}

		if (pedido.getItens().isEmpty()) {
			throw new NegocioException("O cadastro de pedido deve obrigatoriamente conter um item, para que a operação"
					+ " ocorra com sucesso, verifique!");
		}

		if (pedido.isValorTotalNegativo()) {
			throw new NegocioException(
					"O cadastro de pedido obrigatoriamente não pode ter o valor total de pedido negativo, verifique!");
		}

		pedido = this.pedidos.guardar(pedido);
		return pedido;
	}

}
