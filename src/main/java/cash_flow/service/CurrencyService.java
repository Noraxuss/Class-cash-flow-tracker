package cash_flow.service;


import cash_flow.domain.Currency;
import cash_flow.repository.CurrencyRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<String> getCurrencyCodeList(){
        List<String> currencyCodes = currencyRepository.findAll()
                .stream().map(Currency::getCode).toList();
        log.info("Retrieved {} currency codes.", currencyCodes.size());
        return currencyCodes;
    }

}
