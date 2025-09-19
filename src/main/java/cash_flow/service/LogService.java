package cash_flow.service;

import cash_flow.domain.*;
import cash_flow.repository.LogRepository;
import cash_flow.service.utilities.LogPlaceholderEnum;
import cash_flow.service.utilities.LogTemplate;
import cash_flow.service.utilities.LogTemplateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class LogService {

    private LogRepository logRepository;
    private MemberService memberService;
    private GroupService groupService;
    private RequiredPaymentService requiredPaymentService;
    private PaymentService paymentService;
    private GroupCurrencyHistoryService groupCurrencyHistoryService;

    public static final String USER_LOG_TEMPLATES_PATH = "user_log_templates.json";

    @Autowired
    public LogService(LogRepository logRepository,
                      @Lazy MemberService memberService,
                      @Lazy GroupService groupService,
                      @Lazy RequiredPaymentService requiredPaymentService,
                      @Lazy PaymentService paymentService, GroupCurrencyHistoryService groupCurrencyHistoryService) {
        this.logRepository = logRepository;
        this.memberService = memberService;
        this.groupService = groupService;
        this.requiredPaymentService = requiredPaymentService;
        this.paymentService = paymentService;
        this.groupCurrencyHistoryService = groupCurrencyHistoryService;
    }

    public String fillTemplate(String template, Map<LogPlaceholderEnum, String> values) {
        String result = template;
        for (Map.Entry<LogPlaceholderEnum, String> entry : values.entrySet()) {
            result = result.replace(entry.getKey().getPlaceholder(), entry.getValue());
        }
        return result;
    }

    public Map<String, String> loadTemplates(String resourcePath) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> templates = new HashMap<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }

            LogTemplateWrapper wrapper = mapper.readValue(inputStream, LogTemplateWrapper.class);

            for (LogTemplate template : wrapper.getTemplates()) {
                templates.put(template.getTemplateKey(), template.getHu());
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load templates from " + resourcePath, e);
        }

        return templates;
    }

    private static Map<LogPlaceholderEnum, String> groupCreatedMap(Group group) {
        return Map.of(
                LogPlaceholderEnum.DATE, group.getGroupCreationDate().toString(),
                LogPlaceholderEnum.OVERSEER_NAME, group.getOverseer().getFirstName() + " " + group.getOverseer().getLastName(),
                LogPlaceholderEnum.GROUP_NAME, group.getName()
        );
    }

    private String getStringTemplate(LogsMessages logsMessages) {
        Map<String, String> templates = loadTemplates(USER_LOG_TEMPLATES_PATH);
        String template = templates.get(logsMessages.name());
        if (template == null) {
            throw new IllegalArgumentException("No template found for log message: " + logsMessages.name());
        }
        return template;
    }

    private void saveLogEntryToRepository(Group group, Logs logEntry, String template, LogsMessages logsMessages) {
        logEntry.setLogsMessages(logsMessages);
        logRepository.save(logEntry);
        log.info("Log entry created: {}", fillTemplate(template, groupCreatedMap(group)));
    }

    public void createGroupCreatedEntry(LogsMessages logsMessages, Group group, String currencyCode) {
        String template = getStringTemplate(logsMessages);

        Logs logEntry = new Logs();
        logEntry.setGroup(group);
        log.info("saved log entry to repository");
        saveLogEntryToRepository(group, logEntry, template, logsMessages);
        groupCurrencyHistoryService.createGroupCurrencyHistory(group, currencyCode, logEntry);

    }

    public void createMemberAddedToGroupEntry(LogsMessages logsMessages, Group group, Member member) {
        String template = getStringTemplate(logsMessages);

        Logs logEntry = new Logs();
        logEntry.setGroup(group);
        logEntry.setMember(member);
        saveLogEntryToRepository(group, logEntry, template, logsMessages);
    }

    public void createRequiredPaymentCreatedEntry(LogsMessages logsMessages, RequiredPayment requiredPayment, Member member, Currency currency, Group group) {
        String template = getStringTemplate(logsMessages);

        Logs logEntry = new Logs();
        logEntry.setGroup(group);
        logEntry.setMember(member);

        

    }

    public List<Logs> getLogsByMemberId(String memberId, Long groupId) {
        return logRepository.findAllByMemberId(memberId, groupId);
    }
}
