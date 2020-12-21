package com.techshard.batch;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.management.NotificationListener;
import javax.sql.DataSource;

/**
 * @author Petri Kainulainen
 */
@Configuration
@EnableBatchProcessing
public class ExcelFileToDatabaseJobConfig {

    private static final String PROPERTY_EXCEL_SOURCE_FILE_PATH = "excel.to.database.job.source.file.path";

    @Value("${batch.file}")
    String batchFile;


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

//    @Bean
//    ItemReader<StudentDTO> excelStudentReader() {
//        PoiItemReader<StudentDTO> reader = new PoiItemReader<StudentDTO>();
//        reader.setLinesToSkip(1);
//        reader.setResource(new ClassPathResource(batchFile));
//        //reader.setResource(new FileSystemResource(batchFile));
//        reader.setRowMapper(excelRowMapper());
//        return reader;
//    }



    @Bean
    ItemReader<StudentDTO> excelStudentReader() {
        PoiItemReader<StudentDTO> reader = new PoiItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new ClassPathResource("data/students.xlsx"));
        reader.setRowMapper(excelRowMapper());
        return reader;
    }

    private RowMapper<StudentDTO> excelRowMapper() {
        BeanWrapperRowMapper<StudentDTO> rowMapper = new BeanWrapperRowMapper<>();
        rowMapper.setTargetType(StudentDTO.class);
        return rowMapper;
    }

    /**
     * If your Excel document has no header, you have to create a custom
     * row mapper and configure it here.
     */
    /*private RowMapper<StudentDTO> excelRowMapper() {
       return new StudentExcelRowMapper();
    }*/

//    @Bean
//    ItemProcessor<StudentDTO, StudentDTO> excelStudentProcessor() {
//        return new LoggingStudentProcessor();
//    }
//
//    @Bean
//    ItemWriter<StudentDTO> excelStudentWriter() {
//        return new LoggingStudentWriter();
//    }


    @Bean
    public VoltageProcessor processor() {
        return new VoltageProcessor();
    }


    @Bean
    public JdbcBatchItemWriter<StudentDTO> writer(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<StudentDTO>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO Student (emailAddress, name,purchasedPackage) VALUES (:emailAddress, :name ,:purchasedPackage)")
                .dataSource(dataSource)
                .build();
    }





    @Bean
  //  public Job importVoltageJob(NotificationListener listener, Step step1) {
    public Job importVoltageJob( Step step1) {
        return jobBuilderFactory.get("importVoltageJob")
                .incrementer(new RunIdIncrementer())
               // .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<StudentDTO> writer) {
        return stepBuilderFactory.get("step1")
                .<StudentDTO, StudentDTO> chunk(10)
                .reader(excelStudentReader())
                .processor(processor())
                .writer(writer)
                .build();
    }




}