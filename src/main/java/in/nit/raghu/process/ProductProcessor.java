package in.nit.raghu.process;

import org.springframework.batch.item.ItemProcessor;

import in.nit.raghu.model.Product;

public class ProductProcessor implements ItemProcessor<Product, Product>{

	@Override
	public Product process(Product item) throws Exception {
		item.setProdGst(item.getProdCost()*12/100.0);
		item.setProdDisc(item.getProdCost()*25/100.0);
		return item;
	}
	

}
