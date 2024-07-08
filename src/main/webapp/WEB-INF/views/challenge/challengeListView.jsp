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
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.4/dist/jquery.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

<!-- overal -->
<style>
    #wrapper{
		border : 1px solid teal;
        width: 1200px;
        height: 1000px; 
       margin: auto;
        background-color: white;
    }

    #search-area{
    	border : 1px solid teal;
       width : 100%;
       height : 15%;
       display : flex;
       justify-content: space-around;
       align-items: center;
    }

    #content-area{

       border : 1px solid teal;
        height : 75%; 
        width : 100%;
    }
    
  	#pasing-area{
		display : flex;
		justify-content : center;
		align-items : center;
	}

</style>

  </head>
  <body>
 	<jsp:include page="../common/header.jsp" />
 	
    <div id="wrapper">
			
        <section id="search-area">

            <article id="search-condition">
          	 	 <form action="search.condition" method="GET">
					<select name="condition">
						<option value="title">제목</option>
						<option value="userId">글쓴이</option>
					</select>
	                <input type="text" name="keyword"  value="${ keyword }"/>
					<button id="search-keyword-btn" type="submit"><i class="fas fa-search fa-lg"></i></button>
				</form>
            </article>
            
            <c:if test="${ not empty condition }">
	            <script>
	            	$(function(){
	            	
	            		$('#search-condition option[value=${ condition }]').attr('selected', true);
	            	})
	            </script>
            </c:if>

		<c:if test="${ not empty sessionScope.loginUser }">
			<div><a href="enrollForm.ch">글쓰기</a></div>
		</c:if>
		
            <article id="search-status">
            	<form action="search.status" method="GET">
	            	<select name="status">
	            		<option value="coming">진행예정</option>
	            		<option value="progress">진행중</option>
	            		<option value="complete">종료</option>
	            	</select>
	            	<button id="search-status-btn" type="submit">
	            	<i class="fas fa-search fa-lg"></i>
	            	</button>
	            </form>
            </article>
        
        </section>
<!-- search -->
<style>
	#search-condition{
	}
	
	#search-keyword{
	}
	
	#search-keyword-btn{
		border: 0;
 	    background-color: transparent;
 	    border :  1px solid black;
	}
	
	
	#search-status{
	}
	

</style>

	<section id="content-area">
		
			<c:choose>
			
			
				<c:when test="${ not empty list }">
				<article id="content-list">
					<c:forEach var="c" items="${ list }">
				
						<div class="content-item">
						
							<a href="detail.ch?activityNo=${ c.activityNo }"><img src="${ c.changeName }"/></a>
							<h6>${ c.challengeTitle }</h6>
							
							<div>
								<span>${ c.viewCount }조회수 |</span>
								<span>${ c.successLimit }% 목표 |</span>
								<span>${ c.startDate }부터 시작</span>
							</div>
						</div>
						
					</c:forEach>
					</article>
				</c:when>
				
				<c:otherwise>
					<div id="no-item">
						<h1>게시글 없지롱</h1>
					</div>
				</c:otherwise>
			</c:choose>		
		
	</section>


	<!-- content  -->
	<style>
	#content-list{
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
	.content-item{
	    align-items:center;
	    justify-content:center; 

	}
	.content-item img{
		width : 400px;
		height : 250px;
	}
	</style>

	
	<section id="pasing-area"><!-- 검색시/정렬시/기본 3가지 CASE 페이징처리 -->
	
		<c:choose>
	
			<c:when test="${ not empty condition }">
			
					<c:choose>
							<c:when test="${ pi.currentPage eq 1 }">
								<article class="page-item disabled"><a class="page-link" href="#">Previous</a></article>
							</c:when>
							<c:otherwise>
								<article class="page-item"><a class="page-link" href="search.condition?currentPage=${ pi.currentPage - 1 }&condition=${ condition }&keyword=${ keyword }">Previous</a></article>
							</c:otherwise>
						</c:choose>
						
						
						<c:forEach var="p" begin="${ pi.startPage }" end="${ pi.endPage }">		
								<article class="page-item"><a class="page-link" href="search.condition?currentPage=${ p }&condition=${ condition }&keyword=${ keyword }">${ p }</a></article>
						</c:forEach>
						
						
						<c:choose>
							<c:when test="${ pi.currentPage eq pi.maxPage }">
								<article class="page-item disabled"><a class="page-link" href="#">Next</a></article>
							</c:when>
							<c:otherwise>
								<article class="page-item"><a class="page-link" href="search.condition?currentPage=${ pi.currentPage + 1 }&condition=${ condition }&keyword=${ keyword }">Next</a></article>
							</c:otherwise>
					</c:choose>
					
			</c:when>
			
			
			<c:when test="${ not empty status }">
			
					<c:choose>
							<c:when test="${ pi.currentPage eq 1 }">
								<article class="page-item disabled"><a class="page-link" href="#">Previous</a></article>
							</c:when>
							<c:otherwise>
								<article class="page-item"><a class="page-link" href="search.status?currentPage=${ pi.currentPage - 1 }&status=${ status }">Previous</a></article>
							</c:otherwise>
						</c:choose>
						
						
						<c:forEach var="p" begin="${ pi.startPage }" end="${ pi.endPage }">		
								<article class="page-item"><a class="page-link" href="search.status?currentPage=${ p }&status=${ status }">${ p }</a></article>
						</c:forEach>
						
						
						<c:choose>
							<c:when test="${ pi.currentPage eq pi.maxPage }">
								<article class="page-item disabled"><a class="page-link" href="#">Next</a></article>
							</c:when>
							<c:otherwise>
								<article class="page-item"><a class="page-link" href="search.status?currentPage=${ pi.currentPage + 1 }&status=${ status }">Next</a></article>
							</c:otherwise>
					</c:choose>
					
			</c:when>
			
			
		
			<c:otherwise>
			
					<c:choose>
							<c:when test="${ pi.currentPage eq 1 }">
								<article class="page-item disabled"><a class="page-link" href="#">Previous</a></article>
							</c:when>
							<c:otherwise>
								<article class="page-item"><a class="page-link" href="challenge?currentPage=${ pi.currentPage - 1 }">Previous</a></article>
							</c:otherwise>
						</c:choose>
						
					
						<c:forEach var="p" begin="${ pi.startPage }" end="${ pi.endPage }">		
								<article class="page-item"><a class="page-link" href="challenge?currentPage=${ p }">${ p }</a></article>
						</c:forEach>
						
						
						<c:choose>
							<c:when test="${ pi.currentPage eq pi.maxPage }">
								<article class="page-item disabled"><a class="page-link" href="#">Next</a></article>
							</c:when>
							<c:otherwise>
								<article class="page-item"><a class="page-link" href="challenge?currentPage=${ pi.currentPage + 1 }">Next</a></article>
							</c:otherwise>
					</c:choose>
					
			</c:otherwise>
			
		</c:choose>
	</section>




	</div>

</body>
</html>



