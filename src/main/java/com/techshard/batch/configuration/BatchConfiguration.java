package com.techshard.batch.configuration;

import com.techshard.batch.dao.entity.Voltage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.util.Calendar;
import java.util.Date;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


//    @Autowired
//    JobLauncher jobLauncher;
//
//    @Autowired
//    Job job;

    @Value("${batch.file}")
    String batchFile;

//    @Bean
//    public FlatFileItemReader<Voltage> reader() {
//        return new FlatFileItemReaderBuilder<Voltage>()
//                .name("voltItemReader")
//                //.resource(new ClassPathResource("Volts.csv"))
//                .resource(new ClassPathResource("volts1.csv"))
//                .delimited()
//                .names(new String[]{"volt", "time"})
//                .lineMapper(lineMapper())
//                .linesToSkip(1)
//                .fieldSetMapper(new BeanWrapperFieldSetMapper<Voltage>() {{
//                    setTargetType(Voltage.class);
//                }})
//                .build();
//    }








    @Bean
    public FlatFileItemReader<Voltage> reader() {
        FlatFileItemReader<Voltage> itemReader = new FlatFileItemReader<Voltage>();
        itemReader.setLineMapper(lineMapper());
        itemReader.setLinesToSkip(1);
        //itemReader.setResource(new FileSystemResource("src/main/resources/TESTFILE.xlsx"));
        itemReader.setResource(new FileSystemResource(batchFile));
        return itemReader;
    }


    @Bean
    public LineMapper<Voltage> lineMapper() {

        final DefaultLineMapper<Voltage> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        //lineTokenizer.setDelimiter(";");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"volt","time"});

        final VoltageFieldSetMapper fieldSetMapper = new VoltageFieldSetMapper();
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }


    @Bean
    public VoltageProcessor processor() {
        return new VoltageProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Voltage> writer(final DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Voltage>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO voltage (volt, time) VALUES (:volt, :time)")
                .dataSource(dataSource)
                .build();
    }



    @Bean
    public Job importVoltageJob(NotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importVoltageJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Voltage> writer) {
        return stepBuilderFactory.get("step1")
                .<Voltage, Voltage> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }
}
