package br.com.paap.dto.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import br.com.paap.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	public Long id;
	public String email;
	public String siape;
	public String cpf;
	public String name;
	public Integer workload;
	public String departament;
	public String telephone;
	public LocalDate entryDate;
	public String avatar;
	@Builder.Default
	public List<EventUserDTO> events = new ArrayList<>();
	
	public UserDTO(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.siape = user.getSiape();
		this.cpf = user.getCpf();
		this.email = user.getEmail();
		this.workload = user.getWorkload();
		this.departament = user.getDepartament();
		this.telephone = user.getPhone();
		this.setEntryDate(user.getEntryDate());
		this.setAvatar(user.getAvatar());
		var collect = user.getEvents().stream().map(eu -> new EventUserDTO(eu)).collect(Collectors.toList());
		this.events = collect;
	}
	
	public static Page<UserDTO> parse(Page<User> usuarios) {
		return usuarios.map(UserDTO::new);
	}
	
	public static UserDTO parse(User usuario) {
		return new UserDTO(usuario);
	}	
}
