package com.myspring.pro30.board;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("boardDAO")
public class BoardDAO {
	@Autowired
	private SqlSession sqlSession;
	
	public List selectAllArticlesList() throws DataAccessException{
		List<BoardVO> articlesList = sqlSession.selectList("mapper.board.selectAllarticlesList");
		return articlesList;
	}

	public int insertNewArticle(Map articleMap) throws DataAccessException{
		int articleNO = selectNewArticleNO();
		articleMap.put("articleNO",articleNO);
		sqlSession.insert("mapper.board.insertNewArticle",articleMap);
		return articleNO;
	}

	private int selectNewArticleNO() throws DataAccessException{
		return sqlSession.selectOne("mapper.board.selectNewArticleNO");
	}

//	insert the image in t_image table
	public void insertNewImage(Map articleMap) throws DataAccessException{
		
//		*** Print articleMap keys ***
//		Set keySet = articleMap.keySet();
//		Iterator<String> iter = keySet.iterator();
//		while(iter.hasNext()) {
//			String key = iter.next();
//			System.out.println("key: "+key);
//			System.out.println("type of value: "+articleMap.get(key).getClass().getSimpleName());
//		}
		
		int articleNO;
//		여기서 계속 오류가 발생해서 catch로 이동되는 문제!
//		형변환 문제인가? yes
//		articleNO가 Integer로 들어올 때, String으로 들어올 때가 있음!
//		String으로 들어올 때 int x = (Integer) string 으로 할 경우 classCastException 발생
		if(articleMap.get("articleNO").getClass().getSimpleName().equals("String")) {
			articleNO = Integer.parseInt((String)articleMap.get("articleNO"));
		}
		else {
			articleNO = (Integer)articleMap.get("articleNO");
		}
		
		@SuppressWarnings("unchecked")
		List<ImageVO> imageFileList = (List<ImageVO>)articleMap.get("imageFileList");
//		get new imageFileNO to insert image
		int imageFileNO = selectNewImageFileNO();
//		java object substitution is a shallow copy(reference copy)
		for(ImageVO imageVO : imageFileList){
			imageVO.setImageFileNO(++imageFileNO);
			imageVO.setArticleNO(articleNO);
		}
		sqlSession.insert("mapper.board.insertNewImage",imageFileList);
	}
	
//	get max index of t_image table
	private int selectNewImageFileNO() throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectNewImageFileNO");
	}

	public BoardVO selectArticle(int articleNO) {
		BoardVO boardVO = sqlSession.selectOne("mapper.board.selectArticle",articleNO);
		return boardVO;
	}

	public List<ImageVO> selectImageFileList(int articleNO) {
		List<ImageVO> imageList = sqlSession.selectList("mapper.board.selectImageFileList",articleNO);
		return imageList;
	}
	
	public void updateArticle(Map articleMap) {
		sqlSession.update("mapper.board.updateArticle", articleMap);
	}
	
	public void updateImage(List<ImageVO> imageFileList) {
		sqlSession.update("mapper.board.updateImage", imageFileList);
	}
}
