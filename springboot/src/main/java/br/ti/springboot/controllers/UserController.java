package br.ti.springboot.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ti.springboot.model.User;
import br.ti.springboot.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
	
	
	private List<User> users = new ArrayList<>();

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/{id}")
	public ResponseEntity<?> user(@PathVariable("id") Long id) {
//		Optional<User> UserFind = users.stream().filter(user -> user.getId() == id).findFirst();
		Optional<User> UserFind = this.userRepository.findById(id);
		
		if (UserFind.isPresent()) {
			return ResponseEntity.ok().body(UserFind.get());
			
		}
		
		return ResponseEntity.status(404).body("ID não encontrado");
	}
	
	
	@GetMapping("/")
	public User user(){
		User user = new User();
		user.setId(1L);
		user.setName("lucas");
		user.setUsername("lucasvitor");		
		return user;
	}
	
	@PostMapping("/saveUsers")
	public User user(@RequestBody User user){
		this.userRepository.save(user);
//		users.add(user);
		return user;
	}
	
	@GetMapping("/list")
	public List<User> list(){
//	return users;
		return this.userRepository.findAll();
	
		
	}
	
	@GetMapping("/list/{id}")
	public List<User> list1(@PathVariable("id") Long id){
		return this.userRepository.findAllMoreThan(id);	
		
	}
	
	@GetMapping("/findByName/{name}")
	public List<User> fndByName(@PathVariable("name") String name){
		return this.userRepository.findByNameIgnoreCase(name);	
		
	}
}


