package com.kh.eco.challenge.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.kh.eco.challenge.model.dao.ChallengeDao;
import com.kh.eco.challenge.model.vo.Challenge;
import com.kh.eco.common.model.vo.Activity;
import com.kh.eco.common.model.vo.PageInfo;

import lombok.RequiredArgsConstructor;

@EnableTransactionManagement
@RequiredArgsConstructor
@Service
public class ChallengeServiceImpl implements ChallengeService{ // 잊지말자 implements

	
	private final ChallengeDao challengeDao;
	
	private final SqlSessionTemplate sqlSession;
	
	
	// 게시글 전체조회
	@Override
	public int countChallengeList() {
		
		return challengeDao.countChallengeList(sqlSession);
	}

	@Override
	public ArrayList<Challenge> selectChallengeList(PageInfo pi) {
		
		// offset(상쇄)이용해 rowBounds 만들기
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		// 만들어진 rowBounds가지고 challengeList 조회
		return challengeDao.selectChallengeList(sqlSession, rowBounds);
	}


	// 게시글 검색결과 조회
	
	@Override
	public int countSearchList(HashMap<String, String> map) {
		return challengeDao.countSearchList(sqlSession, map);
	}
	@Override
	public ArrayList<Challenge> selectSearchList(HashMap<String, String> map, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return challengeDao.selectSearchList(sqlSession, map, rowBounds);
	}

	
	
	
	// 게시글 정렬 조회
	
	@Override
	public int countChallengeStatus(HashMap<String, String> map) {
		return challengeDao.countChallengeStatus(sqlSession, map);
	}

	@Override
	public ArrayList<Challenge> selectChallengeStatus(HashMap<String, String> map, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return challengeDao.selectChallengeStatus(sqlSession, map, rowBounds);
	}
	
	
	
	
	// 상세조회 
	@Override
	public int increaseViewCount(int activityNo) {
		
		return challengeDao.increaseViewCount(sqlSession, activityNo);
	}

	@Override
	public Challenge selectChallengeDetail(int activityNo) {
		
		return challengeDao.selectChallengeDetail(sqlSession, activityNo);
	}

	@Override
	public String selectUserId(int activityNo) {
		
		return challengeDao.selectUserId(sqlSession, activityNo);
	}

	@Override
	public String selectCategoryName(int activityNo) {
		
		return challengeDao.selectCategoryName(sqlSession, activityNo);
	}

	
	
	
	// 좋아요
	@Override
	public int checkLikeCount(HashMap<String, Integer> map) {
		
		return challengeDao.checkLikeCount(sqlSession, map);
	}
	
	/*
	 * @Override public int selectLikedUser(HashMap<String, Integer> map) {
	 * 
	 * return challengeDao.selectLikedUser(sqlSession, map); }
	 */

	
	@Override
	public int selectLikeCount(int activityNo) {
	
		return challengeDao.selectLikeCount(sqlSession, activityNo);
	}

	@Override
	public int insertLike(HashMap<String, Integer> map) {
	
		return challengeDao.insertLike(sqlSession, map);
	}

	@Override
	public int deleteLike(HashMap<String, Integer> map) {
		
		return challengeDao.deleteLike(sqlSession, map);
	}

	/*
	 * @Override public int increaseLikeCount(HashMap<String, Integer> map) {
	 * 
	 * int update = challengeDao.increaseLikeCount(sqlSession, map); int insert =
	 * challengeDao.insertLike(sqlSession, map);
	 * 
	 * return update*insert; }
	 * 
	 * 
	 * 
	 * @Override public int decreaseLikeCount(HashMap<String, Integer> map) {
	 * 
	 * int update = challengeDao.decreaseLikeCount(sqlSession, map); int delete =
	 * challengeDao.deleteLike(sqlSession, map);
	 * 
	 * return update*delete; }
	 */


	 
	
	
	// 게시글 등록 수정 삭제
	@Override
	public int insertChallenge(Challenge c) {
		
		return challengeDao.insertChallenge(sqlSession, c);
	}

	@Transactional("transactionManager")
	@Override
	public int updateChallenge(HashMap<String, Challenge>map) {
		
		int result1 = challengeDao.updateChallenge(sqlSession, map);
		int result2 = challengeDao.updateActivity(sqlSession, map);
		
		return result1 + result2;
	}

	@Override
	public int deleteChallenge(int activityNo) {
		
		return challengeDao.deleteChallenge(sqlSession, activityNo);
	}
















	
}
