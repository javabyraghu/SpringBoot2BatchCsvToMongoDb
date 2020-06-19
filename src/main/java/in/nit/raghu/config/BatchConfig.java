package in.nit.raghu.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import in.nit.raghu.model.Product;
import in.nit.raghu.process.ProductProcessor;

@EnableBatchProcessing
@Configuration
public class BatchConfig {

	@Autowired
	private MongoTemplate template;

	//1. Item Reader from CSV file
	@Bean
	public ItemReader<Product> reader(){
		FlatFileItemReader<Product> reader=new FlatFileItemReader<Product>();
		//loading file
		reader.setResource(new ClassPathResource("myprods.csv"));
		
		reader.setLineMapper(new DefaultLineMapper<Product>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
					setNames("prodId","prodName","prodCost");
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<Product>() {{
				setTargetType(Product.class);
			}});
		}});
		
		
		return reader;
	}
	
	//2. Item Processor
	@Bean
	public ItemProcessor<Product, Product> processor(){
		return new ProductProcessor();
	}
	
	
	//#. Item Writer
	@Bean
	public ItemWriter<Product> writer(){
		MongoItemWriter<Product> writer=new MongoItemWriter<>();
		writer.setTemplate(template);
		writer.setCollection("products");
		return writer;
	}
	
	
	
	//STEP
	@Autowired
	private StepBuilderFactory sf;
	
	@Bean
	public Step stepA() {
		return sf.get("stepA")
				.<Product,Product>chunk(3)
				.reader(reader())
				.processor(processor())
				.writer(writer()).build(); 
	}
	
	
	//JOB
	@Autowired
	private JobBuilderFactory jf;
	@Bean
	public Job jobA() {
		return jf.get("jobA")
				.incrementer(new RunIdIncrementer())
				.start(stepA())
				.build();
	}
	
}
