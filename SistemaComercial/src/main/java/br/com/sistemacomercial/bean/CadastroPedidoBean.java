package br.com.sistemacomercial.bean;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import br.com.sistemacomercial.model.Cliente;
import br.com.sistemacomercial.model.EnderecoEntrega;
import br.com.sistemacomercial.model.FormaPagamento;
import br.com.sistemacomercial.model.ItemPedido;
import br.com.sistemacomercial.model.Pedido;
import br.com.sistemacomercial.model.Produto;
import br.com.sistemacomercial.model.Usuario;
import br.com.sistemacomercial.repository.Clientes;
import br.com.sistemacomercial.repository.Produtos;
import br.com.sistemacomercial.repository.Usuarios;
import br.com.sistemacomercial.service.CadastroPedidoService;
import br.com.sistemacomercial.util.jsf.FacesUtil;
import br.com.sistemacomercial.validation.SKU;

@Named
@ViewScoped
public class CadastroPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Usuarios usuarios;
	@Inject
	private Clientes clientes;
	@Inject
	private CadastroPedidoService cadastroPedidoService;
	@Inject
	private Produtos produtos;

	private String sku;

	private Produto produtoLinhaEditavel;

	@Produces
	@PedidoEdicao
	private Pedido pedido;
	
	private List<Usuario> vendedores;

	public CadastroPedidoBean() {
		limpar();
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			this.vendedores = this.usuarios.vendedores();

			this.pedido.adicionarItemVazio();

			this.recalcularPedido();
		}
	}

	public List<Cliente> completarCliente(String nome) {
		return this.clientes.porNome(nome);

	}

	public void carregarProdutoLinhaEditavel() {
		ItemPedido item = this.pedido.getItens().get(0);

		if (this.produtoLinhaEditavel != null) {
			if (this.existeItemComProduto(this.produtoLinhaEditavel)) {
				FacesUtil.addErrorMessage("Já existe um item no pedido com o produto informado.");
			} else {
				item.setProduto(this.produtoLinhaEditavel);
				item.setValorUnitario(this.produtoLinhaEditavel.getValorUnitario());

				this.pedido.adicionarItemVazio();
				this.produtoLinhaEditavel = null;
				this.sku = null;
				this.pedido.recalcularValorTotal();
			}
		}
	}

	public void atualizarQuantidade(ItemPedido item, int linha) {
		if (item.getQuantidade() < 1) {
			if (linha == 0) {
				item.setQuantidade(1);
			}else{
				this.getPedido().getItens().remove(linha);
			}
		}

		this.pedido.recalcularValorTotal();
	}

	private boolean existeItemComProduto(Produto produto) {
		boolean existeItem = false;

		for (ItemPedido item : this.getPedido().getItens()) {
			if (produto.equals(item.getProduto())) {
				existeItem = true;
				break;
			}
		}

		return existeItem;
	}

	public List<Produto> completarProduto(String nome) {
		return this.produtos.porNome(nome);
	}

	public void carregarProdutoPorSku() {
		if (StringUtils.isNotEmpty(this.sku)) {
			this.produtoLinhaEditavel = this.produtos.porSku(this.sku);
			this.carregarProdutoLinhaEditavel();
		}
	}

	private void limpar() {
		pedido = new Pedido();
		pedido.setEnderecoEntrega(new EnderecoEntrega());
	}
	
	public void pedidoAlterado(@Observes PedidoAlteradoEvent event){
		this.pedido = event.getPedido();
	}
	

	public void salvar() {
		this.pedido.removerItemVazio();
		
		try {
			this.pedido = this.cadastroPedidoService.salvar(this.pedido);

			FacesUtil.addInfoMessage("Pedido salvo com sucesso");
		} finally {
			this.pedido.adicionarItemVazio();
		}
		
	}

	public void recalcularPedido() {
		if (this.pedido != null) {
			this.pedido.recalcularValorTotal();
		}
	}

	public FormaPagamento[] getFormasPagamento() {
		return FormaPagamento.values();
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public List<Usuario> getVendedores() {
		return vendedores;
	}

	public boolean isEditando() {
		return this.pedido.getId() != null;
	}

	public Produto getProdutoLinhaEditavel() {
		return produtoLinhaEditavel;
	}

	public void setProdutoLinhaEditavel(Produto produtoLinhaEditavel) {
		this.produtoLinhaEditavel = produtoLinhaEditavel;
	}

	@SKU
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

}
