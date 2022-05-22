package ca.sheridancollege.groceryapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//user name and password will be stored in this file
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private LoginAccessDeniedHandler accessdeniedhandler;
	
	protected void configure(HttpSecurity http)throws Exception{
		
		  http.csrf().disable();
	      http.headers().frameOptions().disable();
		
		//restricting urls not html pages
		http
		.authorizeRequests()
		.antMatchers("/register").permitAll()
		.antMatchers("/admin/**","/h2-console/**").hasAuthority("ADMIN")//""
		.antMatchers(
				"/js/**",
				"/css/**",
				"/images/**"
				).permitAll()// no user credentials required to login because it gives permission to all
		
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.permitAll()
		.and()
		.logout()
		.invalidateHttpSession(true)
		.clearAuthentication(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/login?logout")
		.permitAll()
		.and()
		.exceptionHandling()
		.accessDeniedHandler(accessdeniedhandler);
		//here no need to write Tom and all because we are getting information from database
		
	}

	@Autowired 
	UserDetailedServiceImple userDetailsService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)throws Exception{
		
		auth.userDetailsService(userDetailsService).
		passwordEncoder(passwordEncoder());
	}

}
