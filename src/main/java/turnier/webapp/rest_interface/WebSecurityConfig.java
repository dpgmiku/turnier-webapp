package turnier.webapp.rest_interface;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**Configuration for securing the application.
 * @see <a href="https://spring.io/guides/gs/securing-web/">Spring: Getting Started Guide "Securing a Web Application"</a> 
 *      for defining users with basic authentication and for access control to URIs.
 * @see <a href="http://websystique.com/spring-security/spring-security-4-method-security-using-preauthorize-postauthorize-secured-el/">Spring Security 4 Method security using @PreAuthorize,@PostAuthorize, @Secured, EL</a> 
 *      for enabling securing methods by the @Secured annotation.
 * */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled=true) No success to get it work. Knabe 17-11-10
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ADMIN_ROLE = "ADMIN";
	private static final String NUTZER_ROLE = "NUTZER";


	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
             .antMatchers("/").permitAll()
            .antMatchers("/admin/**").hasRole(ADMIN_ROLE)
            .antMatchers("/nutzer/**").hasRole(NUTZER_ROLE)
            .anyRequest().authenticated()
            .and().httpBasic() //Authenticate with username and password.
            //For REST services disable CSRF protection. 
            //See https://docs.spring.io/spring-security/site/docs/current/reference/html/csrf.html#when-to-use-csrf-protection
            .and().csrf().disable()
            ;
    }
	
	//partnerklasse erzeugen user 
	//
	
	private static final List<String> predefinedUsernames = Arrays.asList("admin", "hans", "nina", "fritz", "lisa");
    
	/**Configures the {@link #predefinedUsernames} as known users with their password equal to the user name.*/
    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        final InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryAuthentication = auth.inMemoryAuthentication();
        for(final String username: predefinedUsernames) {
        	final String role = username.equalsIgnoreCase(ADMIN_ROLE) ? ADMIN_ROLE : NUTZER_ROLE;
			inMemoryAuthentication.withUser(username).password("yo").roles(role);
        }
    }
    
    public List<String> predefinedUsernames(){
    	return predefinedUsernames;
    }
    
}