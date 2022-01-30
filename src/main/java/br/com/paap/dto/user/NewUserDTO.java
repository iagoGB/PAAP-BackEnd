package br.com.paap.dto.user;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NewUserDTO {
	@NotNull @NotEmpty @Email(message = "Não é um endereço de email válido")
	private String email;
	@NotNull
	private String cpf;
	@NotNull @NotEmpty
	private String name;
	@NotNull
	private String password;
	@NotNull @NotEmpty
	private String departament;
	private int workload;
	@NotNull @NotEmpty
	private String phone;
	@NotNull @PastOrPresent
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd" ,timezone = "GMT-3")
	private LocalDate entryDate;
}
