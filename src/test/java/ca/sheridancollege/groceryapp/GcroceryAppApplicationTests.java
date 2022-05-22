package ca.sheridancollege.groceryapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import ca.sheridancollege.groceryapp.beans.Product;
import ca.sheridancollege.groceryapp.security.DatabaseAccess;


@SpringBootTest
@AutoConfigureMockMvc
class GcroceryAppApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	DatabaseAccess dao;

	
	
	@Test 
	public void testAddProduct()
	{
		
		ArrayList<Product> prods = (ArrayList<Product>) dao.getAllProducts();
		System.out.println("Prod size:"+prods.size());
		int sizeAfterAddingAnElement = prods.size()+1;
		assertEquals(0,0);
		
		dao.addNewProduct("Test Prod",12,"Test");
		ArrayList<Product> newList= (ArrayList<Product>) dao.getAllProducts();
		int newSize = newList.size();
		assertEquals(sizeAfterAddingAnElement, newSize);	
	}
	@Test
	
	public void testLoadIndexPage()
	{
		try {
			this.mockMvc.perform(get("/register")).andExpect(status().isOk()).andExpect(view().name("registration.html"));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
