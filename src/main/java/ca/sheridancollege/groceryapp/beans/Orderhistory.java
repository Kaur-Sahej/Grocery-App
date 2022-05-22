package ca.sheridancollege.groceryapp.beans;


import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Orderhistory {
	int orderId;
	String orderCode;
	Date orderDate;
	String userEmail;
}
