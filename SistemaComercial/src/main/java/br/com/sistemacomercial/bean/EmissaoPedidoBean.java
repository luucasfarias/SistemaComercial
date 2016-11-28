package br.com.sistemacomercial.bean;

import java.io.Serializable;

import javax.enterprise.event.Event;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sistemacomercial.model.Pedido;
import br.com.sistemacomercial.service.EmissaoPedidoService;
import br.com.sistemacomercial.util.jsf.FacesUtil;

@Named
@ViewScoped
public class EmissaoPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EmissaoPedidoService emissaoPedidoService;

	@Inject
	@PedidoEdicao
	private Pedido pedido;
	
	@Inject
	private Event<PedidoAlteradoEvent> pedidoAlteradoEvent;

	public void emitirPedido() {
		this.pedido.removerItemVazio();

		try {
			this.pedido = this.emissaoPedidoService.emitir(this.pedido);

			// ABAIXO SERA LANÇADA UM EVENTO CDI PARA ATUALIAZR O ESTOQUE
			this.pedidoAlteradoEvent.fire(new PedidoAlteradoEvent(this.pedido));

			FacesUtil.addInfoMessage("Ótimo! Seu pedido foi aprovado com sucesso.");
		} finally {
			this.pedido.adicionarItemVazio();
		}
	}
}
