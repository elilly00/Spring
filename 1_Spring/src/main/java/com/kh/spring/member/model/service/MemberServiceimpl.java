package com.kh.spring.member.model.service;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDAO;
import com.kh.spring.member.model.vo.Member;

@Service("mService") 
	// mService를 작성하지 않아도 되지만 작성하면 좀 더 정확성이 높아져 빠르게 찾을 수 있음 -> null이 출력됨 
	// -> 객체는 생성되었지만 객체를 controller에 집어넣는 과정이 없었기 때문에 나오는 null임(즉, DI(의존성 주입)일 일어나지 않음)
	// -> controller의 private MemberService mService;에 @Autowired를 추가해 DI가 일어나도록 함
public class MemberServiceimpl implements MemberService {
	
	@Autowired // 의존성 주입을 시키기 위한 annotation
	private MemberDAO mDAO;
	
	@Autowired // 프레임워크가 만든  주소을 자동 주입시킴 (Template주소 값 가져옴)
	private SqlSessionTemplate sqlSession; 
	
	@Override
	public Member memberLogin(Member m) { // 요즘엔 중간에 interface없이 진행하는 추세로 넘어가는 중(실무에선 반반으로 사용하는 중)
		//SqlSession 받아오기 (getSqlSession메소드를 작성한 Template만들기  -> root-context에서 DB와 연결함)
		
		// System.out.println(sqlSession);
		// [Error]
		// java.lang.TypeNotPresentException: Type org.apache.ibatis.session.SqlSessionFactory not present
		// sqlSession이 존재하지 않는다는 에러 -> MyBatis 라이브러리를 붙여넣어야함
		
		return mDAO.memberLogin(sqlSession, m);
	}

	@Override
	public int insertMember(Member m) {
		return mDAO.insertMember(sqlSession, m);
	}

	@Override
	public int updateMember(Member m) {
		return mDAO.updateMember(sqlSession, m);
	}

	@Override
	public int updatePassword(HashMap<String, String> map) {
		return mDAO.updatePassword(sqlSession, map);
	}

	@Override
	public int duplicateId(String id) {
		return mDAO.duplicateId(sqlSession, id);
	}

	@Override
	public int deleteMember(String id) {
		return mDAO.deleteMember(sqlSession, id);
	}

	
	
}
