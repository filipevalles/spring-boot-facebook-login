package br.com.pontosmultiplus.facebook.web;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller para buscar os detalhes do usuário.
 * 
 * @author Filipe Assaf
 *
 */
@RestController
public class FacebookController {
	@RequestMapping("/user")
	public Principal usuario(Principal principal) {
		return principal;
	}

}
