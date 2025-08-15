package cash_flow.dto.outgoing;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MemberOverviewDetails {

    // Overview
    private String id;
    private String name;
    private String email;
    private LocalDate groupJoinDate;
    private LocalDate groupLeaveDate;
    private List<String> groupMemberships;
    private int totalPayment;


}
