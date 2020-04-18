package br.com.casamovel.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@IdClass(EventoUsuarioID.class)
public class EventoUsuario implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
        @JoinColumn(
		name = "fk_evento_id",
		referencedColumnName = "id",
		foreignKey = @ForeignKey(name="fk_evento_id")
	)
	private Evento evento_id;
	
	@Id
	@ManyToOne
	@JoinColumn(
		name = "fk_usuario_id",
		referencedColumnName = "id",
		foreignKey = @ForeignKey(name="fk_usuario_id")
	)
	private Usuario usuario_id;
	
	private boolean isSubscribed;
	
	private boolean isPresent;
	
	public Evento getEvento() {
		return evento_id;
	}

	public void setEvento(Evento evento) {
		this.evento_id = evento;
	}

	public Usuario getUsuario() {
		return usuario_id;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario_id = usuario;
	}

	public boolean isSubscribed() {
		return isSubscribed;
	}

	public void setSubscribed(boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}	
}
