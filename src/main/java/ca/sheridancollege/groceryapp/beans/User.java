package ca.sheridancollege.groceryapp.beans;
//any bean must implement serializable, should have no args constructor and must have encapsulated data

//objects of this class has to be serialized
import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId;
	private String userName;
	private String encryptedPassword;
}
