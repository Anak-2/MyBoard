package com.myspring.pro30.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.myspring.pro30.member.*;

@Service("memberService")
public class MemberService {
	@Autowired
	private MemberDAO memberDAO;
	
	public MemberVO login(MemberVO memberVO) throws Exception{
		MemberVO vo = memberDAO.loginById(memberVO);
		return vo;
	}

	public List listMembers() {
		List<MemberVO> memberList = memberDAO.selectAllMemberList();
		return memberList;
	}

	public int addMember(MemberVO member) {
		return memberDAO.insertMember(member);
	}

	public int idDupCheck(String id) {
		if(memberDAO.selectById(id) != null) {
			return 1;
		}
		else{
			return 0;
		}
	}
	
	public void removeMember(String id) {
		memberDAO.deleteMember(id);
	}

	public void modMember(MemberVO member) {
		memberDAO.updateMember(member);
	}
}
