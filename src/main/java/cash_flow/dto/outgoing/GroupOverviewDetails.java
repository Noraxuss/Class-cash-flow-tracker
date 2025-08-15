package cash_flow.dto.outgoing;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GroupOverviewDetails {

    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String overseerName;
    private int numberOfMembers;
    private int totalPayment;
    private int RemainingMoney;

}
