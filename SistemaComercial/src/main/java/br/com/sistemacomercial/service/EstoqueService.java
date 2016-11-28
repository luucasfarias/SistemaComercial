package br.com.sistemacomercial.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sistemacomercial.model.ItemPedido;
import br.com.sistemacomercial.model.Pedido;
import br.com.sistemacomercial.repository.Pedidos;
import br.com.sistemacomercial.util.jpa.Transactional;

public class EstoqueService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Pedidos pedidos;
	
	@Transactional
	public void baixarItensEstoque(Pedido pedido){
		pedido = this.pedidos.porId(pedido.getId());
		
		for(ItemPedido item : pedido.getItens()){
			item.getProduto().baixarEstoque(item.getQuantidade());
		}
	}

	//aqui retorna a quantidade do estoque quando o pedido for cancelado na emissão.
	public void retornarItensEstoque(Pedido pedido) {
		pedido = this.pedidos.porId(pedido.getId());
		
		for(ItemPedido item : pedido.getItens()){
			item.getProduto().adicionarEstoque(item.getQuantidade());
		}
		
	}

}
