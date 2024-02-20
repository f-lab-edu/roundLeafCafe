package com.ypdchurch.roundleafcafe.member.service;

import com.ypdchurch.roundleafcafe.common.exception.CustomApiException;
import com.ypdchurch.roundleafcafe.member.controller.dto.SigninRequest;
import com.ypdchurch.roundleafcafe.member.domain.Member;
import com.ypdchurch.roundleafcafe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member registerMember(Member requestMember) throws CustomApiException {

        // 1. 동일 유저가 있는지 검사
        if (isExistMember(requestMember)) {
            throw new CustomApiException("등록된 이메일이 존재합니다.");
        }
        // 2. password encoding
        return memberRepository.save(requestMember);
    }

    @Transactional
    public Long findEmailAndPassword(SigninRequest signinRequest) {
        Member member = memberRepository.findByEmailAndPassword(signinRequest.getEmail(), signinRequest.getPassword())
                .orElseThrow(() -> new IllegalArgumentException("이메일과 패스워드가 잘못되었습니다."));
        return member.getId();
    }

    private boolean isExistMember(Member member) {
        return memberRepository.findByEmail(member.getEmail())
                .isPresent();
    }
}
