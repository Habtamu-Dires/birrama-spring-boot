package com.hab.birrama.members;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/members")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public List<Member> getAllMember(){
        return memberService.getAllMembers();
    }

    @GetMapping("{memberId}")
    public Member getMember(@PathVariable("memberId") Long id){
        return memberService.getMember(id);
    }

    @PostMapping
    public void addMember(@RequestBody Member member){
        memberService.addMember(member);
    }

    @DeleteMapping(path = "{memberId}")
    public void deleteMember(@PathVariable("memberId") Long id){
        memberService.deleteMember(id);
    }

    @PutMapping("{memberId}")
    public void updateMember(
            @PathVariable("memberId") Long id,
            @RequestBody Member member
    ){
        memberService.updateMember(id, member);
    }


}
