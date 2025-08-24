package cash_flow.service;

import cash_flow.context.AppContext;
import cash_flow.controller.LoadingController;
import cash_flow.domain.GroupMembership;
import cash_flow.domain.Guardian;
import cash_flow.domain.Member;
import cash_flow.domain.PersonType;
import cash_flow.dto.incoming.GroupMemberCreationCommand;
import cash_flow.dto.mappers.MemberMapper;
import cash_flow.dto.outgoing.MemberExemptionDetails;
import cash_flow.dto.outgoing.MemberOverviewDetails;
import cash_flow.repository.GroupMembershipRepository;
import cash_flow.repository.MemberRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MemberService {

    private final ModelMapper modelMapper;
    private final AppContext appContext;
    private final LoadingController loadingController;

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    private final GuardianService guardianService;
    private final PersonService personService;
    private final GroupMemberShipService groupMemberShipService;
    private final PaymentService paymentService;

    @Autowired
    public MemberService(MemberRepository memberRepository,
                         ModelMapper modelMapper,
                         GuardianService guardianService,
                         PersonService personService,
                         LoadingController loadingController,
                         GroupMemberShipService groupMemberShipService,
                         AppContext appContext,
                         MemberMapper memberMapper,
                         PaymentService paymentService) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
        this.guardianService = guardianService;
        this.personService = personService;
        this.loadingController = loadingController;
        this.groupMemberShipService = groupMemberShipService;
        this.appContext = appContext;
        this.memberMapper = memberMapper;
        this.paymentService = paymentService;
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

    public ObservableList<MemberOverviewDetails> getMemberOverviewDetails() {
        log.info("Fetching member overview details");
        List<Member> members =
                memberRepository.findAllByGroupId(appContext.getGroupContext().getGroupId());
        log.info("Fetched {} members from repository", members.size());
        ObservableList<MemberOverviewDetails> memberOverviewDetails = FXCollections.observableArrayList();
        log.info("Mapping members to MemberOverviewDetails");
        for (Member member : members) {
            log.debug("Mapping member {} to MemberOverviewDetails", member.toString());
            GroupMembership groupMembership = groupMemberShipService.getGroupMembershipByMemberId(member.getId());
            int totalPayments = paymentService.getTotalPaymentsByMemberId(member.getId());
            MemberOverviewDetails details =
                    memberMapper.memberTopMemberOverviewDetails(member, groupMembership, totalPayments);
            memberOverviewDetails.add(details);
            log.debug("Mapped member {} to MemberOverviewDetails: {}", member.getId(), details);
        }
        log.info("Fetched {} member overview details", memberOverviewDetails.size());
        return memberOverviewDetails;
    }

    public List<MemberExemptionDetails> getMemberList() {
        List<Member> members =
                memberRepository.findAllByGroupId(appContext.getGroupContext().getGroupId());
        log.info("Fetched {} members from repository for exemptions", members.size());
        List<MemberExemptionDetails> memberExemptionDetails = new ArrayList<>();
        for (Member member : members) {
            log.debug("Mapping member {} to MemberExemptionDetails", member.toString());
            MemberExemptionDetails details = memberMapper.memberToMemberExceptionDetails(member);
                    memberExemptionDetails.add(details);
        }
        log.info("Fetched {} member exemption details", memberExemptionDetails.size());
        return memberExemptionDetails;
    }
}
