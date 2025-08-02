package cash_flow.service;

import cash_flow.controller.LoadingController;
import cash_flow.domain.Guardian;
import cash_flow.domain.Member;
import cash_flow.domain.PersonType;
import cash_flow.dto.incoming.GroupMemberCreationCommand;
import cash_flow.repository.GroupMembershipRepository;
import cash_flow.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final GuardianService guardianService;
    private final PersonService personService;
    private final LoadingController loadingController;
    private final GroupMemberShipService groupMemberShipService;

    @Autowired
    public MemberService(MemberRepository memberRepository,
                         ModelMapper modelMapper,
                         GuardianService guardianService,
                         PersonService personService,
                         LoadingController loadingController,
                         GroupMemberShipService groupMemberShipService) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
        this.guardianService = guardianService;
        this.personService = personService;
        this.loadingController = loadingController;
        this.groupMemberShipService = groupMemberShipService;
    }

    public void createMember(GroupMemberCreationCommand groupMemberCreationCommand) {
        if (groupMemberCreationCommand == null) {
            log.error("Group member creation command is null");
            return;
        }

        String currentStep = "Creating member %s %s"
                .formatted(groupMemberCreationCommand.getFirstName(), groupMemberCreationCommand.getLastName());
        loadingController.updateCurrentStepLabel(currentStep);
        log.info(currentStep);

        Member member = modelMapper.map(groupMemberCreationCommand, Member.class);
        member.setPersonType(PersonType.GROUP_MEMBER);
        member.setDateOfCreation(LocalDateTime.now());

        //
        log.info("Checking for Existing Guardian");
        Guardian guardian;
        if (groupMemberCreationCommand.getGuardianId() != null) {
            guardian = guardianService.getGuardian(groupMemberCreationCommand.getGuardianId());
            member.getGuardians().add(guardian);
        }

        String memberId = personService.createPersonId(member, memberRepository.count() + 1);
        member.setId(memberId);
        memberRepository.save(member);

        groupMemberShipService.createMembership(member, groupMemberCreationCommand.getStartDate());

    }
}
