# SpringBoot2BatchCsvToMongoDb
Spring Boot Batch Procecssing CSV to MongoDB

##To Get Data From MongoDB
#3 From MongoDB
  @Bean _
  public MongoItemReader<Employee> reader() { _
    MongoItemReader<Employee> reader = new MongoItemReader<>(); _
    reader.setTemplate(mongoTemplate);
    reader.setSort(new HashMap<String, Sort.Direction>() {{
      put("_id", Direction.DESC);
    }});
    reader.setTargetType(Employee.class);
    reader.setQuery("{}");
    return reader;
  }
