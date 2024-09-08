package techclallenge5.fiap.com.msGestaoPreco.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import techclallenge5.fiap.com.msGestaoPreco.model.Preco;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Bean
    public Job processarPreco(JobRepository jobRepository, Step step) {
        return new JobBuilder("processarPreco", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager platformTransactionManager,
                     ItemReader<Preco> itemReader,
                     ItemWriter<Preco> itemWriter) {
        return new StepBuilder("step", jobRepository)
                .<Preco, Preco>chunk(20, platformTransactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public ItemReader<Preco> itemReader() {
        return new FlatFileItemReaderBuilder<Preco>()
                .name("precoProdutoReader")
                .resource(new ClassPathResource("preco.csv"))
                .delimited()
                .names("preco_id", "preco_normal", "preco_promocional", "data_inicio_promocao", "data_fim_promocao", "produto_id")
                .fieldSetMapper(new PrecoFieldSetMapper())
                .build();
    }


    @Bean
    public ItemWriter<Preco> itemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Preco>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .dataSource(dataSource).
                sql("""
                        INSERT INTO PRECO (preco_id, preco_normal, preco_promocional, data_inicio_promocao, data_fim_promocao, produto_id) VALUES
                        (:precoId, :precoNormal, :precoPromocional, :dataInicioPromocao, :dataFimPromocao, :produtoId);
                        """)
                .build();
    }

}
