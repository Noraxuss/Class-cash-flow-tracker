package cash_flow.service;

import cash_flow.common.ErrorUtilities;
import cash_flow.domain.Group;
import cash_flow.domain.LogsMessages;
import cash_flow.domain.Overseer;
import cash_flow.dto.incoming.GroupCreationCommand;
import cash_flow.dto.outgoing.GroupSelectionDetails;
import cash_flow.dto.outgoing.OverseerSelectionDetails;
import cash_flow.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class GroupService {

    private final GroupRepository groupRepository;
    private final ModelMapper modelMapper;
    private final OverseerService overseerService;
    private final ErrorUtilities errorUtilities;
    private final LogService logService;

    @Autowired
    public GroupService(GroupRepository groupRepository,
                        ModelMapper modelMapper,
                        OverseerService overseerService,
                        ErrorUtilities errorUtilities,
                        @Lazy LogService logService) {
        this.groupRepository = groupRepository;
        this.modelMapper = modelMapper;
        this.overseerService = overseerService;
        this.errorUtilities = errorUtilities;
        this.logService = logService;
    }

    public List<GroupSelectionDetails> getOverseerGroups(OverseerSelectionDetails selectedOverseer) {
        Overseer overseer = overseerService.getOverseerById(selectedOverseer.getId());

        List<Group> groups = groupRepository.findAllByOverseer(overseer);

        GroupSelectionDetails groupSelectionDetails = new GroupSelectionDetails();
        groupSelectionDetails.setGroupId(0L);
        groupSelectionDetails.setName("Új csoport");
        groupSelectionDetails.setDescription("Új csoport létrehozása");

        List<GroupSelectionDetails> groupSelectionDetailsList = new ArrayList<>();
        groupSelectionDetailsList.add(groupSelectionDetails);
        if (!groups.isEmpty()) {
            for (Group group : groups) {
                GroupSelectionDetails groupDetails = modelMapper.map(group, GroupSelectionDetails.class);
                groupSelectionDetailsList.add(groupDetails);
                log.info("Retrieved {} groups for overseer: {}", groupSelectionDetailsList.size(), selectedOverseer.getName());
            }
        } else {
            log.info("No groups found for overseer: {}", selectedOverseer.getName());
        }
        return groupSelectionDetailsList;
    }

    public Long createGroup(GroupCreationCommand groupCreationCommand) {
        if (groupCreationCommand == null || groupCreationCommand.getOverseerId() == null) {
            log.error("Group creation failed: GroupCreationCommand or Overseer is null");
            // TODO make systemResponse appear on various scenes
        }

        Overseer overseer = overseerService.getOverseerById(groupCreationCommand.getOverseerId());
        if (overseer == null) {
            log.error("Group creation failed: Overseer not found with ID {}",
                    groupCreationCommand.getOverseerId());
            // TODO make systemResponse appear on various scenes
        }

        Group group = modelMapper.map(groupCreationCommand, Group.class);
        group.setOverseer(overseer);
        groupRepository.save(group);

        log.info("Group created successfully with ID: {}", group.getId());
        logService.createLogEntry(LogsMessages.GROUP_CREATED, requiredPayment, member, currency, group);
        return group.getId();
    }

    public LocalDate getGroupStartDate(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("not good"));

        LocalDate startDate = group.getGroupCreationDate().toLocalDate();
        log.info("Retrieved start date for group ID {}: {}", groupId, startDate);
        return startDate;
    }

    public LocalDate getGroupEndDate(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("not good"));

        LocalDate endDate = group.getGroupEndDate().toLocalDate();
        log.info("Retrieved end date for group ID {}: {}", groupId, endDate);
        return endDate;
    }

    public Group getGroupFromRepository(Long groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group with ID " + groupId + " not found"));
    }

    public Group getGroupById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group with ID " + id + " not found"));
    }
}
