package com.ypdchurch.roundleafcafe.token.service;

import com.ypdchurch.roundleafcafe.common.auth.jwt.JwtProvider;
import com.ypdchurch.roundleafcafe.common.exception.TokenCustomException;
import com.ypdchurch.roundleafcafe.common.exception.code.TokenErrorCode;
import com.ypdchurch.roundleafcafe.member.domain.Member;
import com.ypdchurch.roundleafcafe.member.service.MemberService;
import com.ypdchurch.roundleafcafe.token.domain.Token;
import com.ypdchurch.roundleafcafe.token.enums.TokenStatus;
import com.ypdchurch.roundleafcafe.token.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public Token findByRefreshToken(String token) {
        return tokenRepository.findByRefreshToken(token)
                .orElseThrow(() -> new TokenCustomException(TokenErrorCode.TOKEN_IS_NOT_FOUND));
    }

    public Token findByEmail(String email) {
        return tokenRepository.findByEmail(email)
                .orElseThrow(() -> new TokenCustomException(TokenErrorCode.TOKEN_IS_NOT_FOUND));
    }

    public Token findByMemberId(Long memberId) {
        return tokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new TokenCustomException(TokenErrorCode.TOKEN_IS_NOT_FOUND));
    }

    public Token registerRefreshToken(String token) {
        if (!jwtProvider.isValidToken(token)) {
            throw new TokenCustomException(TokenErrorCode.INVALID_TOKEN);
        }

        String memberIdText = jwtProvider.findMemberId(token);
        Long memberId = Long.valueOf(memberIdText);
        Member foundMember = memberService.findById(memberId);

        Token refreshToken = Token.builder()
                .refreshToken(token)
                .memberId(memberId)
                .email(foundMember.getEmail())
                .status(TokenStatus.ACTIVE)
                .build();
        return refreshToken;
//        return tokenRepository.save(refreshToken);
    }

    public Token updateRefreshToken(String token) {
        if (!jwtProvider.isValidToken(token)) {
            throw new TokenCustomException(TokenErrorCode.INVALID_TOKEN);
        }

        Token refreshToken = findByRefreshToken(token);
        return refreshToken.updateRefreshToken(token);
    }

    public void deleteByRefreshToken(String token) {
        Token refreshToken = findByRefreshToken(token);
        tokenRepository.delete(refreshToken);
    }

    public void deleteByEmail(String email) {
        Token refreshToken = findByEmail(email);
        this.deleteByRefreshToken(refreshToken.getRefreshToken());
    }

    public void deleteByMemberId(Long memberId) {
        Token refreshToken = findByMemberId(memberId);
        this.deleteByRefreshToken(refreshToken.getRefreshToken());
    }


}