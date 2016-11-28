package br.com.sistemacomercial.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Embeddable
public class EnderecoEntrega implements Serializable {

	private static final long serialVersionUID = 1L;

	private String logradoura;
	private String numero;
	private String complemento;
	private String cidade;
	private String cep;
	private String uf;

	@NotBlank
	@Size(max = 80)
	@Column(name = "entrega_logradoura", nullable = false, length = 100)
	public String getLogradoura() {
		return logradoura;
	}

	public void setLogradoura(String logradoura) {
		this.logradoura = logradoura;
	}

	@NotBlank
	@Size(max = 20)
	@Column(name = "entrega_numero", nullable = false, length = 10)
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Size(max = 100)
	@Column(name = "entrega_complemento", length = 100)
	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@NotBlank
	@Size(max = 60)
	@Column(name = "entrega_cidade", nullable = false, length = 60)
	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	@NotBlank
	@Size(max = 9)
	@Column(name = "entrega_cep", nullable = false, length = 9)
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@NotBlank
	@Size(max = 60)
	@Column(name = "entrega_estado", nullable = false, length = 2)
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

}
