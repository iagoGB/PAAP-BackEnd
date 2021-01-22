package br.com.casamovel.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class EventoUsuarioID implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long eventoID;
	
	private Long usuarioID;
		
}
