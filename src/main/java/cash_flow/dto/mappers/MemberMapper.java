package cash_flow.dto.mappers;

import cash_flow.domain.GroupMembership;
import cash_flow.domain.Member;
import cash_flow.dto.outgoing.MemberOverviewDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MemberMapper {

    public MemberOverviewDetails memberTopMemberOverviewDetails(Member member, GroupMembership groupMembership, int totalPayments) {
        log.info("Mapping member {} to memberOverviewDetails", member.toString());
        log.info("Mapping groupMembership {} to memberOverviewDetails", groupMembership.toString());
        log.info("Mapping totalPayments {} to memberOverviewDetails", totalPayments);
        MemberOverviewDetails memberOverviewDetails = new MemberOverviewDetails();
        memberOverviewDetails.setId(member.getId());
        memberOverviewDetails.setName(member.getFirstName() + " " + member.getLastName());
        memberOverviewDetails.setEmail(member.getEmail());
        memberOverviewDetails.setGroupJoinDate(groupMembership.getMembershipStartDate());
        memberOverviewDetails.setGroupLeaveDate(groupMembership.getMembershipEndDate());
        memberOverviewDetails.setTotalPayment(totalPayments);
        return memberOverviewDetails;
    }
}
