package cash_flow.config;

import cash_flow.domain.Group;
import cash_flow.domain.Member;
import cash_flow.domain.Overseer;
import cash_flow.dto.incoming.GroupCreationCommand;
import cash_flow.dto.outgoing.GroupSelectionDetails;
import cash_flow.dto.outgoing.MemberOverviewDetails;
import cash_flow.dto.outgoing.OverseerSelectionDetails;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Mapping Overseer to OverseerSelectionDetails
        modelMapper.typeMap(Overseer.class, OverseerSelectionDetails.class)
                .addMappings(mapper -> mapper.map(
                        overseer -> overseer.getFirstName() + " " + overseer.getLastName(),
                        OverseerSelectionDetails::setName
                ));

        // Mapping GroupCreationCommand to Group with string-to-date conversion
        modelMapper.typeMap(GroupCreationCommand.class, Group.class)
                .addMappings(mapper -> {
                    mapper.using(stringToLocalDateTime())
                            .map(GroupCreationCommand::getCreationDateString, Group::setGroupCreationDate);
                    mapper.using(stringToLocalDateTime())
                            .map(GroupCreationCommand::getEndDateString, Group::setGroupEndDate);
                });

        modelMapper.typeMap(Group.class, GroupSelectionDetails.class)
                .addMappings(mapper ->
                        mapper.map(Group::getId, GroupSelectionDetails::setGroupId));



        return modelMapper;
    }

    private Converter<String, LocalDateTime> stringToLocalDateTime() {
        return context -> {
            String source = context.getSource();
            if (source == null || source.isBlank()) return null;

            try {
                return LocalDateTime.parse(source, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException e) {
                return LocalDate.parse(source, DateTimeFormatter.ISO_LOCAL_DATE)
                        .atStartOfDay();
            }
        };
    }

}
