package ca.sheridancollege.groceryapp.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailedServiceImple implements UserDetailsService {

	@Autowired
	DatabaseAccess da;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//Find the user based on the user name
				ca.sheridancollege.groceryapp.beans.User user = da.findUserAccount(username);//user we created in beans package
				//If the user doesn't exist throw an exception
				if (user == null) {
					System.out.println("User not found:" + username);
					throw new UsernameNotFoundException
						("User " + username + " was not found in the database");
				}
				//Get a list of roles
				List<String> roleNames = da.getRolesById(user.getUserId());
				//Change the list of roles into a list of GrantedAuthority
				List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
				if (roleNames != null) {
					for(String role: roleNames) {
						grantList.add(new SimpleGrantedAuthority(role));
					}
				}
				//Create a user based on the information above.
				UserDetails userDetails = (UserDetails)new User(user.getUserName(),user.getEncryptedPassword(), grantList);	//this belongs
				//to security not user class
				
				return userDetails;

	}
	

}
