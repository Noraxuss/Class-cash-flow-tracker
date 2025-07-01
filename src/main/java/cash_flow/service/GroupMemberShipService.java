package cash_flow.service;

import cash_flow.repository.GroupMembershipRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GroupMemberShipService {

    private final GroupMembershipRepository groupMembershipRepository;

    public GroupMemberShipService(GroupMembershipRepository groupMembershipRepository) {
        this.groupMembershipRepository = groupMembershipRepository;
    }
}
