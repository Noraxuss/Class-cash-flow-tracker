package cash_flow.service;

import cash_flow.dto.outgoing.OverseerSelectionDetails;
import cash_flow.dto.outgoing.SelectionParentClass;
import cash_flow.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }
}
