package in.nit.raghu.model;

import lombok.Data;

@Data
public class Product {

	private Integer prodId;
	private String prodName;
	private Double prodCost;
	private Double prodGst;
	private Double prodDisc;
		
}
