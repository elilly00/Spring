package com.kh.spring.member.model.service;

import java.util.HashMap;

import com.kh.spring.member.model.vo.Member;

public interface MemberService { // 추상 메소드 추가

	Member memberLogin(Member m);

	int insertMember(Member m);

	int updateMember(Member m);

	int updatePassword(HashMap<String, String> map);

	int duplicateId(String id);


	
	
}
// @Controller,@Service,@Repository -> @Component: 단순히 bean(객체)를 생성(등록)하기 위한 어노테이션 (12.15일 4교시 수업)