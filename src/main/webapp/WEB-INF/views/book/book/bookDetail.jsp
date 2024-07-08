<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <title>도서상세</title>
    <style>
        /* 겉을 감싸는 div */
        .outer{
            margin: auto;
            width: 1200px;
            overflow:visible;
        }

        /* 그리드영역 */
        #bookform-grid{
            display: grid;
            grid-template-columns: 1fr 1fr;
            grid-template-rows: 100px 1fr;
        }
        #bookform-grid>img:nth-child(1){
            grid-column: 1 / 2;
	        grid-row: 1 / 3;
            height: 97%;
            width: 95%;
        }
        #bookform-grid>span:nth-child(2){
            grid-column: 2 / 3;
	        grid-row: 1 / 2;
        }
        #bookform-grid>table:nth-child(3){
            grid-column: 2 / 3;
	        grid-row: 2 / 3;
        }
        
        .bookmark{
        	content: url(resources/images/book/book-empty.png);
        }
        img[class$=abled]{
        	content: url(resources/images/book/book-full.png);
        }
    </style>
    
    	<c:if test="${ not empty sessionScope.loginUser }">
    		<script>
    			var loginNo = ${ sessionScope.loginUser.userNo };
    			
    			$(function(){
	        		$.ajax({
	        			url : 'bookMark.bk',
	        			type : 'get',
	        			data : {
	        				ISBN13 : '${ b.ISBN13 }',
	        				userNo : loginNo
	        			},
	        			success : result => {
	        				$('.bookmark').attr('class', result);
	        			},
	        			error : () => {
	        				alert("북마크 조회 실패");
	        			}
	        		});
	        	})
    			
	    		function bookmark(){ // 북마크 등록/삭제 ajax
	        		$.ajax({
	        			url : 'bookMark.bk',
	        			type : 'post',
	        			data : {
	        				className: $('.bookmark').attr('class'),
	        				ISBN13 : '${ b.ISBN13 }',
	        				userNo : loginNo
	        			},
	        			success : result => {
	        				$('.bookmark').attr('class', result);
	        			},
	        			error : () => {
	        				console.log($('.bookmark').attr('class'));
	        				alert("북마크 조작 실패");
	        			}
	        		});
	        	}
	    		
	    		function insertReply(){ // 한줄평 등록 ajax
	        		if($('#bookReplyContent').val().trim() != ''){
	    	    		$.ajax({
	    	    			url : 'bookReply.bk',
	    	    			type : 'post',
	    	    			data : {
	    	    				ISBN13 : '${ b.ISBN13 }',
	    	    				userNo : loginNo,
	    	    				content : $('#bookReplyContent').val()
	    	    			},
	    	    			success : result => {
	    	    				console.log(result);
	    	    				
	    	    				if(result === 'success'){
	    	    					$('#bookReplyContent').val('');
	    	    					selectBookReply();
	    	    				} else{
	    	    					alert('댓글 등록 실패');
	    	    				}
	    	    			},
	    	    			error : () => {
	    	    				console.log("댓글 통신 실패");
	    	    			}
	    	    		});
	        		}
	        	}
	    		
	    		function deleteReply(){ // 한줄평 삭제 ajax
	        		$.ajax({
	        			url : 'bookReply.bk',
	        			type : 'DELETE',
	        			data : {
	        				ecoNo : $('#bookReply-area').find('input[name=ecoNo]').val(),
	        				ISBN13 : '${ b.ISBN13 }',
	        				userNo : loginNo
	        			},
	        			success : result => {
	        				if(result === 'success'){
    	    					$('#bookReplyContent').val('');
    	    					selectBookReply();
    	    				} else{
    	    					alert('삭제 실패');
    	    				}
	        			},
	        			error : () => {
	        				console.log("댓글 통신 실패");
	        			}
	        		})
	        	}
    		</script>
    	</c:if>
    	<script>
    	//문자열이 빈 문자열인지 체크하여 결과값을 리턴.
    	function isEmpty(str){
    		if(typeof str == "undefined" || str == null || str == "")
    			return true;
    		else
    			return false ;
    	}
    	
    	function selectBookReply(nowPage){
    		// 한줄평 조회 ajax
    		$.ajax({
    			url : 'bookReply.bk',
    			type : 'get',
    			data : {
    				ISBN13 : '${ b.ISBN13 }',
					cPage : nowPage    				
    			},
    			success : result => {
    				
    				let replyArr = result.replyList;
    				let replyPi = result.replyPi;
    				let replyPiValue = '';
    				
    				$('#bookReply-count').html(result.replyCount + '개');
    				if(isEmpty(result.replyCount)){
    					$('#bookReply-area').html("한줄평이 없습니다.");
    					$('#bookReply-pagination').remove();
    				} else {
        				let replyValue = '';
        				for(let i in replyArr){
        					if(!isEmpty('${ sessionScope.loginUser.userId }')){
        						if(replyArr[i].userId === '${ sessionScope.loginUser.userId }' || '${ sessionScope.loginUser.userStatus }' === 'A'){
            						replyValue += '<button type="button" class="btn btn-secondary" onclick="deleteReply();">삭제</button>'
            									+ '<input type="hidden" value="' + replyArr[i].ecoNo + '" name="ecoNo">';
            					}
        						replyValue += '<p style="margin-bottom: 0px;">' + replyArr[i].userId  + '</p>'
     						   + '<p style="margin-bottom: 0px;">' + replyArr[i].bookReplyDate + '</p>'
     						   + '<p>' + replyArr[i].bookReplyContent + '</p>';
        					}
        				}
        				
        				let previous = replyPi['currentPage'] - 1;
        				let next = replyPi['currentPage'] + 1
        				
        				
						if(replyPi['currentPage'] == 1){
		 					replyPiValue += '<li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>';
		 				} else{
		 					replyPiValue += '<li class="page-item"><a class="page-link" onclick="selectBookReply('+ previous +');">Previous</a></li>';
		 				}
		 				for(let i = replyPi.startPage; i <= replyPi.endPage; i++){
		 					replyPiValue += '<li class="page-item"><a class="page-link" onclick="selectBookReply(' + i + ');">' + i + '</a></li>';
		 				}
		 				if(replyPi['currentPage'] == replyPi['endPage']){
		 					replyPiValue += '<li class="page-item disabled"><a class="page-link" href="#">Next</a></li>';
		 				} else{
		 					replyPiValue += '<li class="page-item"><a class="page-link" onclick="selectBookReply('+ next +');">Next</a></li>';
		 				}
        				$('#bookReply-area').html(replyValue);
        				$('#bookReply-pagination').html(replyPiValue);
    				}
    			},
    			error : () => {
    				console.log("댓글 통신 실패");
    			}
    		});
    	}
    	
    	$(function(){
    		selectBookReply();
    	})
    </script>
</head>
<body>
	
	<jsp:include page="../../common/header.jsp" />
	<br>
	<jsp:include page="../common/bookHeader.jsp" />
	<jsp:include page="../common/bookLeftBanner.jsp" />

    <div class="outer">
        <div>
            <button type="button" class="btn btn-secondary" onclick='location.href="book"'>목록</button>
            <span>${ b.bookCategory }</span>
            <c:if test="${ not empty sessionScope.loginUser }">
	            <span style="float: right;">내 서재에 담기</span>
	            <img class="bookmark" src="" style="height: 30px; width: 30px; float: right;" onclick="bookmark();">
       		</c:if>
        </div>
        <br>
        <div id="bookform-grid">
            <img src="${ b.bookImg }">
            <span style="font-size: 50px;">${ b.bookTitle }</span>
            <table class="table table-bordered">
                <tr>
                    <th>작가</th>
                    <td>${ b.bookWriter }</td>
                </tr>
                <tr>
                    <th>출판사</th>
                    <td>${ b.bookPublisher }</td>
                </tr>
                <tr>
                    <th>조회수</th>
                    <td>${ b.bookCount }</td>
                </tr>
                <tr>
                    <th>ISBN</th>
                    <td>${ b.ISBN13 }</td>
                </tr>
              </table>
        </div>
        <p>
            ${ b.bookContent }
        </p>

        <p><a href="${ b.bookLink }" target="_blank" rel="noopener noreferrer">알라딘 구매 링크</a></p>

        <hr>

        <div>
            <div>
                <span>한 줄 평</span>
                <span id="bookReply-count"></span>
            </div>
            <div id="bookReply-area">
                <p style="margin-bottom: 0px;"></p>
                <p style="margin-bottom: 0px;"></p>
                <p></p>
            </div>
            <ul class="pagination justify-content-center" id="bookReply-pagination">
            </ul>
            <c:choose>
            	<c:when test="${ empty sessionScope.loginUser }">
            		<input readonly type="text" placeholder="로그인 후 다양한 생각을 남겨주세요" style="height: 50px; width: 90%;">
            	</c:when>
            	<c:otherwise>
            		<input id="bookReplyContent" type="text" placeholder="하나의 도서에 하나의 한줄평만 작성할 수 있습니다" name="bookReplyContent" style="height: 50px; width: 90%;" maxlength="50">
            		<button type="submit" style="height: 50px; width: 9%;" onclick="insertReply();">등록</button>
	             	<p id="bookReplyKeyup">0/50</p>
	             	<script>
	             		
             			let inputReply = document.getElementById("bookReplyContent")
             			let str = "";
             			inputReply.oninput = e=>{
             				let content = e.target.value;
             				let contentLenght = content.length;
             				let maxByte = inputReply.getAttribute("maxlength");
							let length = 0;
							
             				for(let i = 0; i < contentLenght; i++){
             					if ((content < "0" || content > "9") && (content < "A" || content > "Z") && (content < "a" || content > "z")){
             						length = length + 3; // 숫자와 영어가 아닐경우 3바이트 계산
	                            }else{
	                            	length = length + 1;
	                            }
             				}
             				console.log(length);
             				
             				if (length <= maxByte) {
             					document.getElementById('bookReplyKeyup').innerText = length + "/50";
             					str = content;
             					console.log(str);
            					} else {
            						alert("바이트 제한을 초과했습니다.");
            					 	inputReply.value = str;
            					}
             			}
	             	</script>
            	</c:otherwise>
            </c:choose>
        </div>
    </div>
    <br>
</body>
</html>