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
    <title>도서목록</title>
    <style>
        /* 겉을 감싸는 div */
        .outer{
            margin: auto;
            width: 1200px;
            overflow:visible;
        }

        /* 검색 form태그 */
        .search-form{
            float: right;
        }

        /* 도서리스트 grid컨테이너와 요소들 */
        #book-container{
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(240px, max-content));
            grid-gap: 20px;
            justify-content: center;
            padding: initial;
        }
        #book-container div{
            list-style-type: none;
            border: 1px solid gray;
            padding: 5px;
            width: 240px;
            height: 450px;
            background-color: lightgray;
        }
        #book-container div>img{
            width: 100%;
            height: 210px;
        }
        #book-container div>p{
            margin-bottom: 5px;
        }
    </style>
    <c:if test="${ not empty selectBook }">
		<script>
			$(function(){
				$('#book-search option[value=${selectBook}]').attr('selected', true);
			})				
		</script>
	</c:if>
</head>
<body>

	<jsp:include page="../../common/header.jsp" />
	<jsp:include page="../common/bookHeader.jsp" />
	<jsp:include page="../common/bookLeftBanner.jsp" />

    <div class="outer">
        <form id="book-search" class="search-form" action="searchBook.bk" >
            <select name="selectBookOption">
                <option value="title">제목</option>
                <option value="writer">작가</option>
                <option value="category">카테고리</option>
            </select>
            <input type="text" name="searchBookValue" maxlength="50">
            <button type="submit">검색</button>
        </form>
        <br><br>
        
		
	        <div id="book-container">
	            <c:forEach items="${ bookList }" var="b">
	            	<form id="book" method="POST" action="">
	            		<div>
		            		<img src="${ b.bookImg }">
		            		<p class="book-title">${ b.bookTitle }</p>
		            		<p>${ b.bookWriter }</p>
		            		<p class="book-count">조회수 : ${ b.bookCount }</p>
			            	<input type="hidden" name="ISBN" value="${ b.ISBN13 }">
	            		</div>
	            	</form>
	            </c:forEach>
	        </div>
        
        <br>
        
        <c:if test="${ bookList.size() > pi.pageLimit }">
        	<ul class="pagination justify-content-center">
	        	<c:choose>
		       		<c:when test="${ pi.currentPage eq 1 }">
		             	<li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>
		       		</c:when>
		       		<c:otherwise>
		       		 	<li class="page-item"><a class="page-link page-previous" href="book?cPage=${ pi.currentPage - 1 }">Previous</a></li>
		       		</c:otherwise>
		       	</c:choose>
		           <c:forEach begin="${ pi.startPage }" end="${ pi.endPage }" var="p">
		           		<li class="page-item"><a id="${p}" class="page-link page-now" href="book?cPage=${p}">${p}</a></li>
		           </c:forEach>
	           	<c:choose>
		       		<c:when test="${ pi.currentPage eq pi.maxPage }">
		       			<li class="page-item disabled"><a class="page-link" href="#">Next</a></li>
		       		</c:when>
		       		<c:otherwise>
		       		 	<li class="page-item"><a class="page-link page-next" href="book?cPage=${ pi.currentPage + 1 }">Next</a></li>
		       		</c:otherwise>
	       		</c:choose>
       		</ul>
        	<c:if test="${ not empty selectBook }">
        		<script>
        			$(function(){
        				let selectBook = '${selectBook}';
        				let searchBook = '${searchBook}';
        				let attrNow = "searchbook.bk" + "?searchBook=" + searchBook + "&selectBook=" + selectBook + "&cPage="; 
        				
        				$.each($('.page-now'), function(index, item){
        					Now = attrNow + $(item).text();
        					$(item).attr("href", Now);
        				})
        				
        				let attrPrevious = attrNow + ${ pi.currentPage - 1 };
        				let attrNext = attrNow + ${ pi.currentPage + 1 };
        				
        				$('.page-previous').attr("href", attrPrevious);
        				$('.page-next').attr("href", attrNext);
        			})
        		</script>
        	</c:if>
        </c:if>
        
		<script>
			$(function(){
				$('#book-container div').click(function(){
					let ISBN13 = $(this).children('input[name=ISBN]').val();
					let count = $(this).children('.book-count').text().slice(5);
					$(this).parent().attr("action", "bookDetail.bk?ISBN13=" + ISBN13 + "&count=" + count);
					$(this).parent().submit();
				})
			})
		</script>
        
    </div>
</body>
</html>