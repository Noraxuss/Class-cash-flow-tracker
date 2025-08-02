package cash_flow.service;

import cash_flow.context.AppContext;
import cash_flow.domain.Group;
import cash_flow.domain.GroupMembership;
import cash_flow.domain.Member;
import cash_flow.repository.GroupMembershipRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class GroupMemberShipService {

    private final GroupMembershipRepository groupMembershipRepository;
    private final AppContext appContext;
    private final GroupService groupService;

    public GroupMemberShipService(GroupMembershipRepository groupMembershipRepository, AppContext appContext, GroupService groupService) {
        this.groupMembershipRepository = groupMembershipRepository;
        this.appContext = appContext;
        this.groupService = groupService;
    }

    public void createMembership(Member member, LocalDate startDate) {
        GroupMembership groupMembership = new GroupMembership();
        groupMembership.setMember(member);
        groupMembership.setMembershipStartDate(startDate);

        Group group = groupService.getGroupFromRepository(appContext.getGroupContext().getGroupId());
        groupMembership.setGroup(group);

        groupMembershipRepository.save(groupMembership);
        log.info("Created membership for member {} in group {}", member.getId(), group.getId());

    }
}
