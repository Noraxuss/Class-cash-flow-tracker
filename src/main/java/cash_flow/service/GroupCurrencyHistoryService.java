package cash_flow.service;

import cash_flow.domain.Currency;
import cash_flow.domain.Group;
import cash_flow.domain.GroupCurrencyHistory;
import cash_flow.domain.Logs;
import cash_flow.repository.GroupCurrencyHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class GroupCurrencyHistoryService {

    private GroupCurrencyHistoryRepository groupCurrencyHistoryRepository;
    private CurrencyService currencyService;

    @Autowired
    public GroupCurrencyHistoryService(GroupCurrencyHistoryRepository groupCurrencyHistoryRepository, CurrencyService currencyService) {
        this.groupCurrencyHistoryRepository = groupCurrencyHistoryRepository;
        this.currencyService = currencyService;
    }

    public void createGroupCurrencyHistory(Group group, String currencyCode, Logs logEntry) {
        GroupCurrencyHistory groupCurrencyHistory = new GroupCurrencyHistory();
        groupCurrencyHistory.setGroup(group);
        groupCurrencyHistory.setDate(LocalDateTime.now());
        groupCurrencyHistory.setLogs(logEntry);

        Currency currency = currencyService.getCurrencyByCode(currencyCode);
        groupCurrencyHistory.setCurrency(currency);

        groupCurrencyHistoryRepository.save(groupCurrencyHistory);
        log.info("Created group currency history for group ID {}: {}", group.getId(), groupCurrencyHistory
        );
    }
}


