package br.com.paap.model;

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
public class EventUserID implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long event;
	
	private Long user;
		
}
