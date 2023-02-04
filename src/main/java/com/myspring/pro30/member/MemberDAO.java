package com.myspring.pro30.member;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository("memberDAO")
public class MemberDAO {
	@Autowired
	private SqlSession sqlSession;
	
	public MemberVO loginById(MemberVO memberVO) throws DataAccessException{
		MemberVO vo = sqlSession.selectOne("mapper.member.loginById", memberVO);
		return vo;
	}

	public List<MemberVO> selectAllMemberList() throws DataAccessException{
		List<MemberVO> membersList = sqlSession.selectList("mapper.member.selectAllMemberList");
		return membersList;
	}

	public int insertMember(MemberVO member) throws DataAccessException{
//		몇 개 넣었는지 반환
		int result = sqlSession.insert("mapper.member.insertMember", member);
		return result;
	}

	public String selectById(String id) {
//		selectById 쿼리로 얻은 정보가 반환된 것이 result
		String result = sqlSession.selectOne("mapper.member.selectById", id);
		return result;
	}
	
	public void deleteMember(String id) {
		sqlSession.delete("mapper.member.deleteMember", id);
	}

	public void updateMember(MemberVO member) {
		sqlSession.update("mapper.member.updateMember", member);
	}
}
