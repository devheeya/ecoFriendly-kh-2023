<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang='en'>
  <head>
    <meta charset='utf-8' />
 <!-- css -->
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" crossorigin="anonymous">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">

<!-- javascript -->
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.4/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<!-- overal -->
<style> 
    #wrapper{
		border : 1px solid teal;
        width: 1200px;
       
       margin: auto;
        background-color: white;
    }


    #search-area{
       border : 1px solid teal;
    }
    #header-area{
    	border : 1px solid yellow;
       width : 100%;
       height : 15%;
       display : flex;
       justify-content: space-around;
       align-items: center;
    }

    #content-area{
		
       border : 1px solid teal;
        height : 40%; 
        width : 100%;
        
       display : flex;
       justify-content: space-around;
       align-items: center;
    }
    
    #achievement-area{
        border : 1px solid red;
        height : 40%; 
        width : 100%;
    }

</style>

  </head>
  <body>
 	<jsp:include page="../common/header.jsp" />
 	
    <div id="wrapper">
    	<header id="header-area">
    		<h1>${ challenge.challengeTitle }</h1>
    		<h6>${ challenge.challengePlace }</h6>
    		<h6>${ categoryName }</h6>
    		<h6>${ userId }</h6>
    	</header>
    	
    	<section id="content-area">
    		<article id="content-text">
    			${ challenge.challengeContent }
    		</article>
    		<article id="content-img">
    			<img src="${ challenge.changeName }" />
    		</article>
    		
    		<article id="participants">
    			${ challenge.minParticipant }명 ~ ${ challenge.maxParticipant }명
    		</article>
    		
    		
    		<article id="likeCount"><!-- 로그인유저가 좋아요체크한 회원이면 💚로 보이기 -->
    			<c:choose>
	    			<c:when test="${ not empty sessionScope.loginUser }"> 
	    				<button id="like"></button>
	    			</c:when>
	    			<c:otherwise>
	    				<button disabled>🤍</button>
    				</c:otherwise>
    			</c:choose>
    				<div id="count">${ likeCount }</div> 
    		</article>
    		
    		
    		<script>
    		// 1. 전역함수 선언
    			var checkLikeCount = function(){
			    			var deferred = $.Deferred();
			    			$.ajax({
			    				url : 'checkLike.ch',
			    				data : {
			    					userNo : ${ sessionScope.loginUser.userNo },
			    					activityNo : ${ challenge.activityNo }
			    				},
			    				success : function(data){ 
			    					deferred.resolve(data);
			    					if(data == 'success'){ 
			    						$('#like').html('💚');
			    					} else{
			    						$('#like').html('🤍');
			    					}
			    				},
			    				error : function(err){
			    					deferred.reject(err);
			    				}
			    			});
		    			return deferred.promise();	
		    		};
		    		
		    // 2. 페이지 로드되자마자 실행될 것
    			$(function(){
    				
    				checkLikeCount();
    			
    			}); // jQuery끝
    			
    		
    			// Promise를 써야 ajax비동기를 동기로 사용할 수 있음 
    			// 만약 초기ajax success에 ajax를 쓴다면 동기로 처리되는 것이 아니라 그대로 비동기로 처리됨
    			//	$('#content-area').on('click', '#likeCount', function(){
    					
    		// jQuery시작
    		  $(function(){		
				$('#like').on('click',  function(){
					checkLikeCount()
		    		.done(function(checked){// 체크여부 확인 완료했을 때
		    			const count = parseInt($('#count').html()); 
					    			if(checked == 'success'){ 	
						    				$.ajax({
				    		    				url : 'deleteLike.ch',
				    		    				type : 'POST',
				    		    				data : {
				    		    					userNo : ${ sessionScope.loginUser.userNo },
				    		    					activityNo : ${ challenge.activityNo },
				    		    				},
				    		    				success : function(result){ 
				    		    					if(result == 'success' ){
				    		    						$('#count').html(count - 1);
				    		    						$('#like').html('🤍');		
				    		    					} else {
				    		    						console.log('좋아요 한행 삭제 실패');
				    		    					}
				    		    				},
				    		    				error : function(){
				    		    					console.log('decraese연결 실패');
				    		    				}
					    				})
					    			}//if 
					    			
					    			else { // 처음 체크하거나 취소후 재체크하는 회원이라면
						    				$.ajax({
							    				url : 'insertLike.ch',
							    				type : 'POST',
							    				data : {
							    					userNo : ${ sessionScope.loginUser.userNo },
							    					activityNo : ${ challenge.activityNo },
							    				},
							    				success : function(result){ 
							    					if(result == 'success'){ 
							    						$('#count').html(count + 1);
							    						$('#like').html('💚');
							    					} 
							    				},
							    				error : function(){					
							    					console.log('increase연결 실패'); 
							    				}
					    				})
					    			}// else	
					    		})// done
						    	.fail(function(message){
						    			console.log('좋아요수 체크 실패');
						    	});// fail
					});
    		  })
    		
    		
		    </script>
		    		
    		
    		<c:if test="${  sessionScope.loginUser.userNo eq challenge.userNo }">
    		
    			<!-- 수정 : 넘겨야할 파라미터가 객체라 그냥 input hidden으로 넘김 -->
    			<form  enctype="multipart/form-data"  action="updateForm.ch" method="post">
    				<input type="hidden" name="activityNo" value="${ challenge.activityNo }" />
    				<input type="hidden" name="challengeTitle" value="${ challenge.challengeTitle }" />
    				<input type="hidden" name="categoryNo" value="${ challenge.categoryNo }" />
    				<input type="hidden" name="challengePlace" value="${ challenge.challengePlace }" />
    				<input type="hidden" name="startDate" value="${ challenge.startDate }" />
    				<input type="hidden" name="endDate" value="${ challenge.endDate }" />
    				<input type="hidden" name="achievementCount" value="${ challenge.achievementCount }" />
    				<input type="hidden" name="successLimit" value="${ challenge.successLimit }" />
    				<input type="hidden" name="minParticipant" value="${ challenge.minParticipant }" />
    				<input type="hidden" name="maxParticipant" value="${ challenge.maxParticipant }" />
  					<input type="hidden" name="upfile" value="${ challenge.changeName }" />
    				<textarea style="display:none;" name="challengeContent">${ challenge.challengeContent }</textarea>
    			
    			<button type="submit">수정</button>
    			</form>
    			
    			<!-- 삭제 : 넘기는 파라미터가 1개라서 a태그 쿼리스트링으로 넘김 -->
    			<button><a href="delete.ch?activityNo=${ challenge.activityNo }">삭제</a></button>
    		</c:if>
    		
    	</section>
    
    	<button id="achievement-toggle-btn">인증글 보기</button>
    	
    	<section id="achievement-area">
    	
    	<c:if test="${ sessionScope.loginUser != null }">
    		<article id="achievement-enroll" >
    			<input id="achievementTitle" type="text" name="achievementTitle"/>
    			<textarea id="achievementContent" style="resize:none; width:100%; height:10%;"></textarea>
    			<input id="upfile" type="file" name="upfile" />첨부파일
    			<button id="insertAchievement">인증</button>
    			<button id="toggle-btn" onclick="selectMine()">내 인증글만 보기</button>
    		</article>
    	</c:if>
    		
    		<article id="achievement-list">

    				<div id="achievement-content">
    				</div>

    		</article>

			<button id="selectMore-btn">더보기</button>
    	</section>

</div><!-- wrapper -->
<script>
			$('#insertAchievement').on('click', function(){
				
				const formData = new FormData();
				/** fileUpload 및 저장경로 공부 */
				formData.append('upfile', $('#upfile')[0].files[0]);
				formData.append('activityNo', ${ challenge.activityNo });
				formData.append('userNo', ${ sessionScope.loginUser.userNo });
				formData.append('achievementTitle', $('#achievementTitle').val());
				formData.append('achievementContent', $('#achievementContent').val());

				$.ajax({
					url : 'insert.ac',
					type : 'POST',
					processData: false,
				    contentType: false,
				    enctype: 'multipart/form-data',
				    data: formData,
					success : function(data){
						if(data == 'success'){
							currentPage = 1;
							selectMore(currentPage);
							$('input').val('');
							$('textarea').val('');
							
							$.ajax({
								url : 'progress.ac',
								data : {
									userNo : ${ sessionScope.loginUser.userNo },
									activityNo : ${ challenge.activityNo }
								},
								success : function(result){
									let progress = (result)/${challenge.achievementCount};
									if(0.25 <= progress && progress < 0.5){
										alert('축하합니다 25% 이상 달성하셨습니다!');
									}else if(0.5 <= progress && progress < 0.75){
										alert('축하합니다 50% 이상 달성하셨습니다!');
									}else if(0.75 <= progress && progress < 1){
										alert('축하합니다 75% 이상 달성하셨습니다!');
									}else if(1 <= progress) {
										alert('축하합니다 100% 이상 달성하셨습니다! 마이페이지에서 뱃지를 확인하실 수 있습니다!');
										//여기에 ajax또 쓰기 to 마이페이지
									}else{
										alert('잘하셨어요! 앞으로 조금만 더 힘내세요~~');
									}
								},
								error : function(){
									
								}	
							});

						}else{
							alert('인증 실패ㅠㅠ');
						}
					},
					error : function(){
						console.log('작성 실패!');
					}
				})
			});//insertA 
</script>

<script>
			let currentPage = 1;

			$('#selectMore-btn').on('click', function(){
				selectMore(currentPage);
				currentPage++;
			});
			
			function selectMore(currentPage){

				 $.ajax({
						url : 'achievement',
						data : { 
							activityNo : ${ challenge.activityNo },
							currentPage : currentPage
						},
						success : function(result){
							console.log("난 result : " + result);
							if(result != ''){
									let resultStr = '';
									for(let i in result){
										resultStr +=
														'<div>'
														+'<img src="' + result[i].changeName + '"/>'
														+'<span><b>'+ result[i].achievementTitle + '</b></span>'
														+'<span>'+ result[i].userNo + '</span>'
														+'<div>'+ result[i].achievementContent + '</div>'
														+'</div>'
					 			
										if(result[i].userNo == ${loginUser.userNo}){		
											resultStr += '<div>'
															+'<button class="update-btn">수정하기</button>'
														 	+'<button class="delete-btn">삭제하기</button>'
														 	+'<p>' + result[i].achievementNo + '</p>'
														 	+'</div>'
										}; 
								
									};//for
								
									/** 현재 페이지에 따라 더하거나 보여주기 */
									if(currentPage != 1){ 
										$('#achievement-content').append(resultStr);
									} else{
										$('#achievement-content').html(resultStr);
									};
							}else{
								$('#achievement-content').html(	'인증글이 없습니다.');
							}
						},
						error : function(){
							console.log('인증글 보기 실패');
						}
				}); //ajax
			};//selectMore
</script>

<script>

		$('#achievement-content').on('click', '.delete-btn', function(){
			
			$.ajax({
				url : 'delete/'+$(this).next().html(),
				type : 'DELETE',
				data : { achievementNo : $(this).next().html() },
				success : function(data){
					if(data == 'success'){
						currentPage = 1;
						selectMore(currentPage);
					}else{
						alert('인증글 삭제 실패! 다음에 다시 시도해주세요');
					}
				},
				error : function(){
					console.log('삭제 실패');
				}
			});//ajax
			
	
		});

</script>
<script>
	let isToggled = false;
	
	$('#toggle-btn').on('click', function() {
	  isToggled = !isToggled;
	  if (isToggled) {
	    // 버튼이 켜진 상태일 때 수행할 작업
	    selectMine();
	  } else {
	    // 버튼이 꺼진 상태일 때 수행할 작업
	    let currentPage = 1;
	    selectMore(currentPage);
	  }
	});

	function selectMine(){
		$.ajax({
			url : 'mine.ac',
			data : {
				userNo : ${ sessionScope.loginUser.userNo },
				activityNo : ${ challenge.activityNo }
			},
			success : function(result){
			
				let resultStr = '';
				for(let i in result){
					resultStr +=
									'<div>'
									+'<img src="' + result[i].changeName + '"/>'
									+'<span><b>'+ result[i].achievementTitle + '</b></span>'
									+'<span>'+ result[i].userNo + '</span>'
									+'<div>'+ result[i].achievementContent + '</div>'
									+'</div>'
 			
					if(result[i].userNo == ${loginUser.userNo}){		
						resultStr += '<div>'
										+'<button class="update-btn">수정하기</button>'
									 	+'<button class="delete-btn">삭제하기</button>'
									 	+'<p>' + result[i].achievementNo + '</p>'
									 	+'</div>'
					}; 
			
				};//for
			
				/** 자기글만 보여줄 때는 html() */
					$('#achievement-content').html(resultStr);
	
			},
			error : function(){
				
			}
		});
	};
</script>
    	
    <!-- content -->
    <style>
    #likeCount{
    	border : 1px solid black;
    }
    </style>
    
    <!-- achievement  -->
	<style>
	#achievement-area{
		/*visibility:hidden; visibility:visible*/ 
	
	}
	#achievement-list{
      	display : grid;
      	justify-content : center;
      	align-items:center;
      	
        grid-template-columns : 400px 400px;
        grid-row: auto auto;
        
      	grid-column-gap: 100px;
  		grid-row-gap: 50px;

	}
	#no-item{
		display : flex;
	 	align-items:center;
	    justify-content:center; 
	    border : 1px solid black;
	}
	#achievement-content{
	    align-items:center;
	    justify-content:center; 

	}
	#achievement-content img{
		float : left;
		width : 100%;
		height : 200px;
	}
	#achievement-content div{
		width : 100%;
	}
	#achievement-content p{
		display : none;
	}
	</style>
    
    

    </body>
    </html>