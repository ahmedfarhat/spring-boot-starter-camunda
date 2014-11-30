package org.springframework.boot.autoconfigure.camunda;

import java.io.IOException;

import javax.sql.DataSource;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnMissingBean(ProcessEngineFactoryBean.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class CamundaAutoConfiguration {

	@Autowired
	private BeanFactory beanFactory;

	@Autowired
	private ApplicationContext appContext;

	@Bean
	public SpringProcessEngineConfiguration springProcessEngineConfig() throws IOException {
		SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
		config.setDataSource(beanFactory.getBean(DataSource.class));
		config.setProcessEngineName("processEngine");
		config.setTransactionManager(beanFactory.getBean(PlatformTransactionManager.class));
		config.setDatabaseSchemaUpdate(Boolean.TRUE.toString());
		config.setJobExecutorActivate(false);
		config.setDeploymentResources(appContext.getResources("classpath*:*.bpmn"));
		return config;
	}

	@Bean
	public ProcessEngineFactoryBean processEngine() throws IOException {
		ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
		processEngineFactoryBean.setProcessEngineConfiguration(springProcessEngineConfig());
		return processEngineFactoryBean;
	}

	@Bean
	public RepositoryService repositoryService() throws Exception {
		return processEngine().getObject().getRepositoryService();
	}

	@Bean
	public RuntimeService runtimeService() throws Exception {
		return processEngine().getObject().getRuntimeService();
	}

	@Bean
	public TaskService taskService() throws Exception {
		return processEngine().getObject().getTaskService();
	}

	@Bean
	public HistoryService historyService() throws Exception {
		return processEngine().getObject().getHistoryService();
	}

	@Bean
	public ManagementService managementService() throws Exception {
		return processEngine().getObject().getManagementService();
	}

}
