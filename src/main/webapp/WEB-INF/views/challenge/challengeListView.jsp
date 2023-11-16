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
						<option value="content">내용</option>
						<option value="userId">글쓴이</option>
					</select>
	                <input type="text" name="keyword" placeholder="검색어를 입력하세요"/>
					<button id="search-keyword-btn" type="submit"><i class="fas fa-search fa-lg"></i></button>
				</form>
            </article>

			<div><a href="enrollForm.ch">글쓰기</a></div>
	
            <article id="search-status">
            	<form action="search.status">
	            	<select name="status">
	            		<option value="coming">진행예정</option>
	            		<option value="progress">진행중</option>
	            		<option value="complete">종료</option>
	            	</select>
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
		<article id="content-items">
			<c:forEach var="c" items="${ list }">
		
				<div class="content-item">
					<div>
						<span>${ c.userNo }</span>
						<span>${ c.viewCount }</span>
						<span>${ c.likeCount }</span>
					</div>
					<!--  <a href="/petopia/detail.bo?bno=${ board.boardNo }">
	                        <img src="/petopia/${ board.fileImg }" alt="">
	                       </a> 
	                -->
					<a href="detail.ch?challengeNo=${c.challengeNo }">
					<img src="${c.changeName }"/><!-- /resources/uploadFiles/2023110311274210000.pdf -->
					</a>
					<h6>${ c.challengeTitle }</h6>
					<div>
						<span>${ c.categoryNo }</span>
						<span>${ c.successLimit }</span>
						<span>${ c.startDate }</span>
					</div>
				</div>
			
			</c:forEach>
		</article>
	</section>


<!-- content  -->
<style>
	#content-items{
      	display : grid;
      	justify-content : center;
        grid-template-columns : 400px 400px;
        grid-row: auto auto;
        
      grid-column-gap: 100px;
  		grid-row-gap: 50px;
        
    
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


	<section id="pasing-area">
	
	
	
	</section>



	</div>

</body>
</html>



