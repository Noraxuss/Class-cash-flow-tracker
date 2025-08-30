package cash_flow.service;

import cash_flow.context.AppContext;
import cash_flow.domain.*;
import cash_flow.dto.incoming.RequiredPaymentCommand;
import cash_flow.dto.outgoing.MemberExemptionDetails;
import cash_flow.repository.RequiredPaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class RequiredPaymentService {

    private final AppContext appContext;

    private final RequiredPaymentRepository requiredPaymentRepository;
    private final CurrencyService currencyService;
    private final MemberService memberService;
    private final LogService logService;
    private final GroupService groupService;

    public RequiredPaymentService(AppContext appContext, RequiredPaymentRepository requiredPaymentRepository, CurrencyService currencyService, MemberService memberService, LogService logService, GroupService groupService) {
        this.appContext = appContext;
        this.requiredPaymentRepository = requiredPaymentRepository;
        this.currencyService = currencyService;
        this.memberService = memberService;
        this.logService = logService;
        this.groupService = groupService;
    }

    public void saveRequiredPayments(List<RequiredPaymentCommand> requiredPaymentCommandList) {
        requiredPaymentCommandList.forEach(requiredPaymentCommand -> {
            requiredPaymentCommand.getMemberExemptionDetails().forEach(exemptionDetail -> {
                log.info("Exemption details for MemberExemptionDetails {}", exemptionDetail);
                if (!exemptionDetail.isExempted()) {
                    createRequiredPayment(requiredPaymentCommand, exemptionDetail);
                }
            });
            log.info("Saved required payment: {}", requiredPaymentCommand);
        });
    }

    private void createRequiredPayment(RequiredPaymentCommand requiredPaymentCommand, MemberExemptionDetails exemptionDetail) {
        RequiredPayment requiredPayment = new RequiredPayment();
        requiredPayment.setName(requiredPaymentCommand.getName());
        requiredPayment.setAmount(requiredPaymentCommand.getAmount());
        requiredPayment.setDueDate(requiredPaymentCommand.getDueDate().atStartOfDay());
        requiredPayment.setCreatedDate(LocalDateTime.now());
        requiredPayment.setPaid(false);
        Member member = memberService.getMember(exemptionDetail.getId());
        requiredPayment.setMember(member);
        Currency currency = currencyService.getCurrencyByCode(requiredPaymentCommand.getCurrency());
        requiredPayment.setCurrency(currency);
        requiredPaymentRepository.save(requiredPayment);
        log.info("Created required payment for member ID {}: {}", exemptionDetail.getId(), requiredPayment
        );
        Group group = groupService.getGroupById(appContext.getGroupContext().getGroupId());
        
        logService.createLogEntry(LogsMessages.REQUIRED_PAYMENT_CREATED, requiredPayment, member, currency, group);
        
        
        



    }
}
