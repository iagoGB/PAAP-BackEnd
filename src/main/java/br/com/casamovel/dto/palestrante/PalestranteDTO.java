package br.com.casamovel.dto.palestrante;

import br.com.casamovel.model.Palestrante;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class PalestranteDTO {
	Long id;
	String nome;
	String descricao;
	String foto;
	
	public PalestranteDTO(Palestrante palestrante) {
		this.id = palestrante.getId();
		this.foto = palestrante.getFoto();
		this.nome = palestrante.getNome();
		this.descricao = palestrante.getDescricao();
	}
	
	public static PalestranteDTO parse(Palestrante palestrante) {
		return new PalestranteDTO(palestrante);
	}
	
	
	
}
