package com.kh.eco.challenge.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.kh.eco.challenge.model.vo.Challenge;
import com.kh.eco.common.model.vo.Activity;
import com.kh.eco.common.model.vo.PageInfo;

public interface ChallengeService {


	// 게시글 전체조회

	int countChallengeList();
	
	ArrayList<Challenge> selectChallengeList(PageInfo pi);
	


	// 검색결과 조회
	int countSearchList(HashMap<String, String> map);
	
	ArrayList<Challenge> selectSearchList(HashMap<String, String> map, PageInfo pi);

	
	// 정렬 조회
	
	int countChallengeStatus(HashMap<String, String> map);
	ArrayList<Challenge> selectChallengeStatus(HashMap<String, String> map, PageInfo pi);



	
	// 상세글 조회
	int increaseViewCount(int activityNo);
	
	Challenge selectChallengeDetail(int activityNo);
	
	String selectUserId(int activityNo);
	
	String selectCategoryName(int activityNo);

	
	
	// 좋아요 기능
	int checkLikeCount(HashMap<String, Integer> map);
	//int selectLikedUser(HashMap<String, Integer> map);
	int selectLikeCount(int activityNo);
	
	int insertLike(HashMap<String, Integer> map);
	int deleteLike(HashMap<String, Integer> map); //loginUser.userNo


	// 게시글 등록 수정 삭제
	int insertChallenge(Challenge c);
	
	int updateChallenge(HashMap<String, Challenge>map);
	
	int deleteChallenge(int activityNo);

	
}
