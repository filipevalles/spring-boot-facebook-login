package br.com.pontosmultiplus.facebook.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Modificando as configurações padrão de segurança.
 * 
 * @author Fiipe Assaf
 *
 */
@Configurable
@EnableWebSecurity
public class FacebookConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	OAuth2ClientContext oauth2ClientContext;
	@Autowired
	AuthorizationCodeResourceDetails authorizationCodeResourceDetails;
	@Autowired
	ResourceServerProperties resourceServerProperties;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		super.configure(web);
	}

	private OAuth2ClientAuthenticationProcessingFilter filter() {
		
		// Criação do filtro para a url "/facebook/login"
		OAuth2ClientAuthenticationProcessingFilter oAuth2Filter = new OAuth2ClientAuthenticationProcessingFilter("/facebook/login");

		// Criação do rest template para buscar a conexão com o OAuth service.
		OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(authorizationCodeResourceDetails,oauth2ClientContext);
		oAuth2Filter.setRestTemplate(oAuth2RestTemplate);

		// Configurando o token service, será necessário para buscar o token e detalhes do usuário do OAuth Service
		oAuth2Filter.setTokenServices(new UserInfoTokenServices(resourceServerProperties.getUserInfoUri(),resourceServerProperties.getClientId()));

		return oAuth2Filter;
	}

	// Método usado para sobrepor o HttpSecurity da web Application.
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
				.authorizeRequests()
				.antMatchers("/", "/**.html", "/app/**.js").permitAll()
				.anyRequest().fullyAuthenticated()
				.and()
				.logout()
				.logoutSuccessUrl("/")
				.permitAll()
				.and()
				.addFilterAt(filter(), BasicAuthenticationFilter.class)
				.csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

}
