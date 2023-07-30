package com.hab.birrama.members;

import com.hab.birrama.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member getMember(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        if(member.isPresent()){
            return member.get();
        } else
            return null;
    }

    public List<Member> getAllMembers(){
        return memberRepository.findAll();
    }

    public void addMember(Member member){
        memberRepository.save(member);
    }

    public void deleteMember(Long memberId){
        //check if member exists
        if(!memberRepository.existsById(memberId)){
            throw new NotFoundException(
                    "Member with id " + memberId + " does not exists"
            );
        }
        memberRepository.deleteById(memberId);
    }
    
    public void updateMember(Long memberId, Member request){
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isPresent()){
            Member updatedMember = member.get();
            updatedMember.setFirstName(request.getFirstName());
            updatedMember.setLastName(request.getLastName());
            updatedMember.setAddress(request.getAddress());
            updatedMember.setLocation(request.getLocation());

            memberRepository.save(updatedMember);
        } else {
            throw new NotFoundException(
                    "Member with id " + memberId + " does not exists"
            );
        }


    }

}
