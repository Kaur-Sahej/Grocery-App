package ca.sheridancollege.groceryapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.groceryapp.security.DatabaseAccess;

@Controller
public class HomeController {
	
	@Autowired
	DatabaseAccess dao;
	
	@GetMapping("/")
	public String goHome(HttpSession session,@AuthenticationPrincipal User user) {
		
		for(GrantedAuthority role:user.getAuthorities())
		{
			System.out.println("Authority:"+role.getAuthority());
			if(role.getAuthority().toLowerCase().contains("admin"))
			{
				return "/Admin/index.html";
			}
		}
		
		return "redirect:/getAllproducts";
	}

	@GetMapping("/user")
	public String goHomeUser() {
		return "/user/index.html";
	}
	
	@GetMapping("/admin")
	public String goadminPage() {
		return "/Admin/index.html";
	}
	
	
	
	@GetMapping("/login")
	public String goLoginPage() {
		return "login.html";
	}
	
	
	@GetMapping("/access-denied")
	public String goAccessDenied() {
		return "/error/access-denied.html";
	}
	
	
	  @GetMapping("/register") public String registration() { return
	  "registration.html"; }
	  
	  @PostMapping("/register") public String processRegistration(@RequestParam
	  String name,@RequestParam String password) { 
		   dao.addUser(name, password);
		  return "redirect:/"; 
		 }
	 
	
	@GetMapping("/admin/dashoboard")
	public String adminDashoboard() {
		return "/admin/index.html";
	}
	
	@GetMapping("/admin/manageUsers")
	public String showUsers(Model model) {
		Map<String, String> resultMap =  dao.getUserDetails();
		model.addAttribute("resultMap", resultMap);	
		return "/admin/index.html";
	}
	
	
}
