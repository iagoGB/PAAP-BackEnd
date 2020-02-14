package br.com.casamovel.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NovoPalestranteDTO {
	
	@NotNull @NotEmpty
	private String nome;
	@NotNull @NotEmpty
	private String descricao;
	
	public NovoPalestranteDTO(String nome, String desc) {
		this.nome = nome;
		this.descricao = desc;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
}
