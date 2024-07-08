package com.kh.eco.challenge.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.eco.challenge.model.vo.Challenge;
import com.kh.eco.common.model.vo.Activity;
import com.kh.eco.common.model.vo.PageInfo;

@Repository
public class ChallengeDao {

	// 전체리스트 조회
	public int countChallengeList(SqlSessionTemplate sqlSession) {
		
		return sqlSession.selectOne("activityMapper.countChallengeList");
	}

	public ArrayList<Challenge> selectChallengeList(SqlSessionTemplate sqlSession, RowBounds rowBounds) {
		
		return (ArrayList)sqlSession.selectList("activityMapper.selectChallengeList", null, rowBounds);
	}
	
	// 검색결과 조회
	public int countSearchList(SqlSessionTemplate sqlSession, HashMap<String, String> map) {
		
		return sqlSession.selectOne("activityMapper.countSearchList", map);
	}

	public ArrayList<Challenge> selectSearchList(SqlSessionTemplate sqlSession, HashMap<String, String> map,
			RowBounds rowBounds) {
		
		return (ArrayList)sqlSession.selectList("activityMapper.selectSearchList", map, rowBounds);
	}
	
	
	// 정렬 조회

	public int countChallengeStatus(SqlSessionTemplate sqlSession, HashMap<String, String> map) {
		return sqlSession.selectOne("activityMapper.countChallengeStatus", map);
	}
	
	public ArrayList<Challenge> selectChallengeStatus(SqlSessionTemplate sqlSession, 
																			  HashMap<String, String> map, RowBounds rowBounds){
		return (ArrayList)sqlSession.selectList("activityMapper.selectChallengeStatus", map, rowBounds);
	}
	

	// 상세페이지 조회
	public int increaseViewCount(SqlSessionTemplate sqlSession, int activityNo) {
		
		return sqlSession.update("activityMapper.increaseViewCount", activityNo);
	}

	public Challenge selectChallengeDetail(SqlSessionTemplate sqlSession, int activityNo) {
		
		return sqlSession.selectOne("activityMapper.selectChallengeDetail", activityNo);
	}
	
	public String selectUserId(SqlSessionTemplate sqlSession, int activityNo) {
	
		return sqlSession.selectOne("activityMapper.selectUserId", activityNo);
	}

	public String selectCategoryName(SqlSessionTemplate sqlSession, int activityNo) {
		
		return sqlSession.selectOne("activityMapper.selectCategoryName", activityNo);
	}


	

	
	// 좋아요
	public int checkLikeCount(SqlSessionTemplate sqlSession, HashMap<String, Integer> map) {
	
		return sqlSession.selectOne("activityMapper.checkLikeCount", map);
	}

	/*
	 * public int selectLikedUser(SqlSessionTemplate sqlSession, HashMap<String,
	 * Integer> map) {
	 * 
	 * return sqlSession.selectOne("challengeMapper.selectLikedUser", map); }
	 */
	
	public int selectLikeCount(SqlSessionTemplate sqlSession, int activityNo) {
		
		return  sqlSession.selectOne("activityMapper.selectLikeCount", activityNo);
	}


	public int insertLike(SqlSessionTemplate sqlSession, HashMap<String, Integer> map) {
		
		return sqlSession.insert("activityMapper.insertLike", map);
	}


	public int deleteLike(SqlSessionTemplate sqlSession, HashMap<String, Integer> map) {
		
		return sqlSession.delete("activityMapper.deleteLike", map);
	}

	// 게시글 작성 수정 삭제
	public int insertChallenge(SqlSessionTemplate sqlSession, Challenge c) {
		
		return sqlSession.insert("activityMapper.insertChallenge", c);
	}

	public int updateChallenge(SqlSessionTemplate sqlSession, HashMap<String, Challenge> map) {
		
		return sqlSession.update("activityMapper.updateChallenge", map);
	}
	
	public int updateActivity(SqlSessionTemplate sqlSession, HashMap<String, Challenge> map) {
		
		return sqlSession.update("activityMapper.updateActivity", map);
	}

	public int deleteChallenge(SqlSessionTemplate sqlSession, int activityNo) {
		
		return sqlSession.update("activityMapper.deleteChallenge", activityNo);
	}









}
