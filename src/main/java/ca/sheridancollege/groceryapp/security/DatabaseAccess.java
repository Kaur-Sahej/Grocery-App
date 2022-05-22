package ca.sheridancollege.groceryapp.security;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.groceryapp.beans.Orderhistory;
import ca.sheridancollege.groceryapp.beans.Product;
import ca.sheridancollege.groceryapp.beans.User;

@Repository
public class DatabaseAccess {
	
	//we want to use named parameters
	@Autowired
	protected NamedParameterJdbcTemplate jdbc;

	
	//to find collection of users that match
	public User findUserAccount(String userName) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM sec_user where userName=:userName";
		parameters.addValue("userName", userName);
		ArrayList<User> users = (ArrayList<User>)jdbc.query(query, parameters,
				new BeanPropertyRowMapper<User>(User.class));// store user names in array list users
		if (users.size()>0)
			return users.get(0);
		else
			return null;
	}
	
	//to find collection of users that match
		public void saveOrder(String userEmail,String orderIdCode) {
			MapSqlParameterSource parameters = new MapSqlParameterSource();
		 
			String query =
			 "insert into ORDERHISTORY " + "(orderCode, userEmail, orderDate)"
			 +" values(:orderCode, :userEmail ,:orderDate)"; 
			
			java.util.Date date=new java.util.Date();  
			System.out.println(date);  			 
			parameters.addValue("orderCode",orderIdCode);
			parameters.addValue("userEmail",userEmail);
			parameters.addValue("orderDate", date);
			 jdbc.update(query, parameters); 
			
		}
		
		
		//to find collection of users that match
				public void addNewProduct(String productName,float prices,String category) {
					MapSqlParameterSource parameters = new MapSqlParameterSource();
				 
					String query =
					 "insert into PRODUCT " + "(productName, price, category)"
					 +" values(:productName, :prices , :category)"; 
					
					java.util.Date date=new java.util.Date();  
					System.out.println(date);  			 
					parameters.addValue("productName",productName);
					parameters.addValue("prices",prices);
					parameters.addValue("category", category);
					 jdbc.update(query, parameters); 
					
				}
		
		//to find collection of users that match
				public List<Orderhistory> getOrderHistory(String userEmail) {
					MapSqlParameterSource parameters = new MapSqlParameterSource();
					String query = "select * from ORDERHISTORY where USEREMAIL = :userEmail"; 
								 
					parameters.addValue("userEmail",userEmail);
					
					List<Orderhistory> resultObject = new ArrayList<Orderhistory>();
					
					 List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
						for (Map<String, Object> row : rows) {
							System.out.println("-----"+row);
							long id = (long)row.get("ORDERID");
							int ordid = (int)id;
							resultObject.add(new Orderhistory(ordid, (String)row.get("ORDERCODE"), (Date)row.get("ORDERDATE"), (String)row.get("USEREMAIL")));
						}
						return resultObject;
				}
	
	
	//to find collection of users that match
		public Map<String,String> getUserDetails() {
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			String query = "SELECT * FROM sec_user";
			ArrayList<User> users = (ArrayList<User>)jdbc.query(query, parameters,
					new BeanPropertyRowMapper<User>(User.class));// store user names in array list users
			Map<String,String> resultMap = new HashMap<String, String>();
			
			for(User user1: users)
			{
				System.out.println("UserID:"+user1.getUserName());
				System.out.println();
				
				String roles="";
				MapSqlParameterSource parameters1 = new MapSqlParameterSource();
				String query1 = "select user_role.userId, sec_role.roleName "
						+ "FROM user_role, sec_role "
						+ "WHERE user_role.roleId=sec_role.roleId "
						+ "and userId=:userId";//to match userId with roleId
				parameters1.addValue("userId", user1.getUserId());
				List<Map<String, Object>> rows = jdbc.queryForList(query1, parameters1);
				
				for (Map<String, Object> row : rows) {		
					roles = roles+","+(String)row.get("roleName");
				}
				
				if(roles.contains(","))
				{
					roles = roles.substring(1);
				}
				
				
				System.out.println("Roles:"+roles);
				resultMap.put(user1.getUserName(), roles);
			}
			return resultMap;
		}

	
	//to find collection of users that match
		public Product getProductDetailByProdID(int productId) {
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			String query = "SELECT * FROM PRODUCT where PRODUCTID=:productId";
			parameters.addValue("productId", productId);
			ArrayList<Product> prodlist = (ArrayList<Product>)jdbc.query(query, parameters,
					new BeanPropertyRowMapper<Product>(Product.class));// store user names in array list users
			if (prodlist.size()>0)
				return prodlist.get(0);
			else
				return null;
		}

		
		 public void addUser(String userName,String password) { 
		 BCryptPasswordEncoder passswordEncoder = new BCryptPasswordEncoder();	 
			 
		 MapSqlParameterSource
		 parameters = new MapSqlParameterSource(); String query =
		 "insert into SEC_User " + "(userName, encryptedPassword,ENABLED)"
		 +" values(:userName, :encryptedPassword,1)"; parameters.addValue("userName",
		 userName); parameters.addValue("encryptedPassword",
		 passswordEncoder.encode(password)); parameters.addValue("userName",
		 userName); jdbc.update(query, parameters); 
		 }
		 	
		
	public List<String> getRolesById(long userId) {
		ArrayList<String> roles = new ArrayList<String>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "select user_role.userId, sec_role.roleName "
				+ "FROM user_role, sec_role "
				+ "WHERE user_role.roleId=sec_role.roleId "
				+ "and userId=:userId";//to match userId with roleId
		parameters.addValue("userId", userId);
		List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
		for (Map<String, Object> row : rows) {
			roles.add((String)row.get("roleName"));
		}
		return roles;
	}
	
	public Iterable<Product> getAllProducts() {
		ArrayList<String> roles = new ArrayList<String>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "select * from PRODUCT";//to match userId with roleId
		List<Product> prodIterable = new ArrayList<Product>();
		
		List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
		for (Map<String, Object> row : rows) {
			roles.add((String)row.get("productName "));
			Product p1 = new Product((Long)row.get("PRODUCTID"),(String) row.get("PRODUCTNAME"), (BigDecimal)row.get("PRICE"), (String)row.get("CATEGORY"));
			prodIterable.add(p1);		
		}
		
		return prodIterable;
	}
	
	public String deleteFromProducts(int productId) {
		ArrayList<String> roles = new ArrayList<String>();
		SqlParameterSource namedParameters = new MapSqlParameterSource("productId", productId);
		String query = "delete from PRODUCT where PRODUCTID=:productId";//to match userId with roleId
		int status = jdbc.update(query, namedParameters);
		
		
		if(status != 0){
		      return "data updated for ID ";
		    }else{
		      return "Could no delete for prod ID ";
		    }
	}

	public String updateProductPrices(int prodid, float prices) {
		ArrayList<String> roles = new ArrayList<String>();

		String query = "UPDATE PRODUCT SET PRICE = :prices WHERE PRODUCTID = :prodid;";//to match userId with roleId
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("prices", prices).addValue("prodid", prodid);
	   		
		int status = jdbc.update(query, namedParameters);
		
		if(status != 0){
		      return "data updated for ID ";
		    }else{
		      return "Could no delete for prod ID ";
		    }
	}

	
}
