package ca.sheridancollege.groceryapp.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.tool.schema.extract.spi.ExtractionContext.DatabaseObjectAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.groceryapp.beans.Orderhistory;
import ca.sheridancollege.groceryapp.beans.Product;
import ca.sheridancollege.groceryapp.security.DatabaseAccess;
@Controller
public class ProductController {

	@Autowired
	DatabaseAccess dao;
    
	@GetMapping("/addNewProduct")
	public String addNewProd(Model model)
	{
		Product p1 = new Product();
		model.addAttribute("Product", p1);
		return "/admin/addNewProduct.html";
	}
	
	@PostMapping("/addNewProduct")
    public String addNewPrduct(@ModelAttribute Product product,Model model,HttpSession session) {
        
        model.addAttribute("user", product);
        System.out.println("Full name:" + product.getProductname());
        System.out.println("Full name:" + product.getCategory());
        System.out.println("Full name:" + product.getPrice());
        
		
        dao.addNewProduct(product.getProductname(), (product.getPrice()).floatValue(),product.getCategory());
        
		model.addAttribute("productDetails", "Product has been added successfully");
		
		return "/admin/newProductAdded.html";
    }
	
	@GetMapping("/getAllproducts")
    public String getProducts(Model model,HttpSession session) {
		Iterable<Product> prods = dao.getAllProducts();
		model.addAttribute("productDetails", prods);

		List<Product> totalProdList = null;
		
		if(session.getAttribute("currentBaset")!=null)
		{
			totalProdList= (List<Product>) session.getAttribute("currentBaset");
			
		}
		
		int totalProductsInCart = 0;
		
		if(totalProdList!=null && totalProdList.size()>0)
		{
			totalProductsInCart = totalProdList.size();
		}
		if(totalProductsInCart>0)
		{
			model.addAttribute("cartLink","You have total "+totalProductsInCart+" in your cart.  Please go to checkout page to finalise your order.");
		}
	
		return "showProducts.html";	
    }
	
	@GetMapping("/showBill")
    public String showCartBill(Model model,HttpSession session) {
		Iterable<Product> prods = dao.getAllProducts();
		List<Product> productBasket = (List<Product>) session.getAttribute("currentBaset");
		
		if(productBasket==null)
		{
			model.addAttribute("productAddedMessage}","Currently no items to display. Add something first in your cart and then come back on this page.");
			return "showCartWithBill.html";	
		}
		
		float cartPrice = 0;
		
		for(Product pb:productBasket)
		{
			cartPrice = cartPrice+pb.getPrice().floatValue();	
		}
		
		float discountedPrice = (float) (cartPrice - (cartPrice*0.10));
		
		model.addAttribute("totalPrice",cartPrice);
		model.addAttribute("discountedPrice",discountedPrice);
		
		model.addAttribute("cart",productBasket);
		return "showCartWithBill.html";	
    }
	
	
	@GetMapping("/reOrderbyHistory")
    public String reOrderbyHistory(@RequestParam String orderCode,Model model,HttpSession session) {
		
		//Iterable<Product> prods = dao.getAllProducts();
		
		List<Product> prodlist = new ArrayList<Product>();
		
		String[] prodArray = orderCode.split(",");
		
		for(String prodID:prodArray)
		{
			prodlist.add(dao.getProductDetailByProdID(Integer.valueOf(prodID)));
		}
			
		
		List<Product> productBasket = prodlist;
		
		if(productBasket==null)
		{
			model.addAttribute("productAddedMessage}","Currently no items to display. Add something first in your cart and then come back on this page.");
			return "showCartWithBill.html";	
		}
		
		float cartPrice = 0;
		
		for(Product pb:productBasket)
		{
			cartPrice = cartPrice+pb.getPrice().floatValue();	
		}
		
		float discountedPrice = (float) (cartPrice - (cartPrice*0.10));
		
		session.setAttribute("currentBaset",productBasket);
		
		model.addAttribute("totalPrice",cartPrice);
		model.addAttribute("discountedPrice",discountedPrice);
		
		model.addAttribute("cart",productBasket);
		return "showCartWithBill.html";	
    }
	
	
	@GetMapping("/addProduct")
    public String addToShoppinList(@RequestParam int prodid,Model model,HttpSession session) {
		System.out.println("productID is:"+prodid);
		
		Product p = dao.getProductDetailByProdID(prodid);
		if(session.getAttribute("currentBaset")==null)
		{
			List<Product> productBasket = new ArrayList<>();	
			productBasket.add(p);
			session.setAttribute("currentBaset", productBasket);
		}
		else
		{
			List<Product> productBasket = (List<Product>) session.getAttribute("currentBaset");
			productBasket.add(p);
			session.setAttribute("currentBaset", productBasket);
		}
		
		List<Product> totalProdList = (List<Product>) session.getAttribute("currentBaset");
		int totalProductsInCart = totalProdList.size();
		
		if(totalProductsInCart>0)
		{
			model.addAttribute("cartLink","You have total "+totalProductsInCart+" in your cart.  Please go to checkout page to finalise your order.");
		}
		
		model.addAttribute("productAddedMessage","Product "+p.getProductname()+" has been successfully added");
		Iterable<Product> prods = dao.getAllProducts();
		model.addAttribute("productDetails", prods);
		
		return "showProducts.html";
    }
	
	
	@GetMapping("/completeOrder")
    public String getProducts(HttpSession session,@AuthenticationPrincipal User user) {
		user.getUsername();
		System.out.println("UserNAme:"+user.getUsername());
		
		List<Product> pArr =  (List<Product>) session.getAttribute("currentBaset");
		String pIDCode = "";
		for(Product p1: pArr)
		{
			pIDCode = pIDCode+","+p1.getProductid();
		}
		
		if(pIDCode.contains(","))
		{
			pIDCode =pIDCode.substring(1);
		}
		
		dao.saveOrder(user.getUsername(),pIDCode );
		session.setAttribute("currentBaset", null);
		return "ordersuccess.html";	
    }
	
	@GetMapping("/admin/showProducts")
	public String showProducts(Model model) {
		Iterable<Product> prods = dao.getAllProducts();
		model.addAttribute("productDetails", prods);
		return "/Admin/index.html";
	}
	
	@GetMapping("/getOrderHistory")
	public String showOrderHistory(Model model,@AuthenticationPrincipal User user) {	
		String userName = user.getUsername();
		List<Orderhistory> orders = dao.getOrderHistory(userName);
		model.addAttribute("orders",orders);
		return "/orderHistory.html";
	}
	
	
	
	@GetMapping("/admin/deleteProduct")
	public String deleteProduct(@RequestParam int prodid,Model model,HttpSession session) {
		dao.deleteFromProducts(prodid);
		
		model.addAttribute("deletedsuccess", "Product Hasbeen deleted successfully");
		
		Iterable<Product> prods = dao.getAllProducts();
		model.addAttribute("productDetails", prods);
		return "/Admin/index.html";
	}
	
	@GetMapping("/admin/updatePrice")
	public String updateProductPrice(@RequestParam int prodid,Model model,HttpSession session) {
		//dao.deleteFromProducts(prodid);
		session.setAttribute("prodId", prodid);
		//Iterable<Product> prods = dao.getAllProducts();
		//model.addAttribute("productDetails", prods);
		return "/Admin/updateprice.html";
	}
	
	@PostMapping("/admin/updatePrice")
	public String updateProductPrices(@RequestParam("prodprices") float prodprices,Model model,HttpSession session) {
		
		int prodid = (int) session.getAttribute("prodId");
	    dao.updateProductPrices(prodid, prodprices);	
	    Iterable<Product> prods = dao.getAllProducts();
		model.addAttribute("productDetails", prods);
	    
	    model.addAttribute("deletedsuccess", "Prices Has been updated successfully");
		return "/Admin/index.html";
	}
	
}
