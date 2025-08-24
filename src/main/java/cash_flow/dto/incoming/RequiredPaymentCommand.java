package cash_flow.dto.incoming;

import cash_flow.dto.outgoing.MemberExemptionDetails;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RequiredPaymentCommand {

    private String name;
    private double amount;
    private LocalDate dueDate;
    private List<MemberExemptionDetails> memberExemptionDetails;
    private String currency;

}
