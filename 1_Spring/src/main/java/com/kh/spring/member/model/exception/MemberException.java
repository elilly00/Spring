package com.kh.spring.member.model.exception;

public class MemberException extends RuntimeException {
	public MemberException() {}
	public MemberException(String msg) {
		super(msg);
	// 에러가 발생 중 try~catch를 하지 않아도 되는 에러(잡아주지 않아도 되는 에러) : Unchecked Exception
	// Unchecked Exception를 표시하는 가장 최상위 에러 : RuntimeException
	
	// try~catch를 하지 않아도 되도록 Exception이 아닌 RuntimeException으로 바꿔줌
	}
}
