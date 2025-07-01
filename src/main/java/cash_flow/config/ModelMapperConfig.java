package cash_flow.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // Configure the model mapper if needed
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);



        return modelMapper;
    }

}
