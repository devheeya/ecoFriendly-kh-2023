<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>독후감 게시판 목록</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
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

        /* 게시글 목록 테이블 */
        #report-table{
            table-layout: fixed;
        }
        #report-table td{
            white-space:nowrap; 
            text-overflow:ellipsis; 
            overflow:hidden;
        }
    </style>
</head>
<body>
	<jsp:include page="../../common/header.jsp" />
	<jsp:include page="../common/bookHeader.jsp" />
	<jsp:include page="../common/bookLeftBanner.jsp" />
    
    <div class="outer">
        <div>
          <h3 style="display: inline-block;">독후감 게시판</h3>
          <form class="search-form" action="reportSearch.bk" id="reportsearch-area">
              <select name="reportSearchOption">
                  <option value="title">제목</option>
                  <option value="content">내용</option>
                  <option value="writer">작성자</option>
              </select>
              <input type="text" name="reportSearchValue" id="reportSearchKeyword" maxlength="50">
              <button type="submit">검색</button>
          </form>
        </div>
        <hr>
        <c:if test="${ not empty sessionScope.loginUser }">
            <button type="button" onclick="location.href='reportEnroll.bk'">작성</button>
        </c:if>
        
        <br><br>
        <table class="table table-bordered" id="report-table">
          <thead>
            <tr>
              <th style="width: 10%;">게시판번호</th>
              <th style="width: 15%;">별점</th>
              <th style="width: 35%;">제목</th>
              <th style="width: 10%;">작성자</th>
              <th style="width: 10%;">작성일</th>
              <th style="width: 10%;">조회수</th>
            </tr>
          </thead>
          <tbody>
          	<c:choose>
          		<c:when test="${ empty list }">
          			<tr>
          				<td colspan="6" align="center">작성된 게시글이 없습니다.</td>
          			</tr>
          		</c:when>
          		<c:otherwise>
		          	<c:forEach items="${ list }" var="r" varStatus="i">
		          		<c:choose>
		          			<c:when test="${ r.bookReportSecret eq 1 }">
		        				<c:choose>
		        					<c:when test="${ r.userId eq loginUser.userId or 
		        									 loginUser.userStatus eq 'A' }">
		        						<tr>
								          <td class="rno"><c:out value="${ r.bookReportNo }" /></td>
								          <td>
								          	<c:forEach var="i" begin="1" end="${ r.bookReportStar }">
								          		★
								          	</c:forEach>
								          </td>
								          <td>[비밀글] <c:out value="${ r.bookReportTitle }" /></td>
								          <td><c:out value="${ r.userId }" /></td>
								          <td><c:out value="${ r.bookReportDate }" /></td>
								          <td><c:out value="${ r.bookReportCount }" /></td>
								        </tr>
		        					</c:when>
		        				</c:choose>
		          			</c:when>
		          			<c:otherwise>
		          				<tr>
						          <td class="rno"><c:out value="${ r.bookReportNo }" /></td>
						          <td>
						          <c:forEach var="i" begin="1" end="${ r.bookReportStar }">
					          		  ★
					          	  </c:forEach>
								  </td>
						          <td><c:out value="${ r.bookReportTitle }" /></td>
						          <td><c:out value="${ r.userId }" /></td>
						          <td><c:out value="${ r.bookReportDate }" /></td>
						          <td><c:out value="${ r.bookReportCount }" /></td>
						        </tr>
		          			</c:otherwise>
		          		</c:choose>
		          	</c:forEach>
	          	</c:otherwise>
          	</c:choose>
          </tbody>
        </table>
        
        <script>
        	$(function(){ // 테이블의 행을 클릭시 상세조회
       			$('#report-table > tbody > tr').click(function(){
       				if($('#report-table').find('td').length > 1){
	           			location.href='reportDetail.bk?rno='+$(this).children(('.rno')).text();
       				}
           		})
        	})
        </script>
        
        <c:if test="${ not empty condition }">
			<script>
				$(function(){
					$('#reportsearch-area option[value=${condition}]').attr('selected', true);
					$('#reportsearch-area input[name=reportsearch]').val('${keyword}');
				})				
			</script>
		</c:if>
        
       	<ul class="pagination justify-content-center">
	        	<c:choose>
		       		<c:when test="${ pi.currentPage eq 1 }">
		             	<li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>
		       		</c:when>
		       		<c:otherwise>
		       		 	<li class="page-item"><a class="page-link page-previous" href="bookReport?cPage=${ pi.currentPage - 1 }">Previous</a></li>
		       		</c:otherwise>
		       	</c:choose>
		           <c:forEach begin="${ pi.startPage }" end="${ pi.endPage }" var="p">
		           		<li class="page-item"><a id="${p}" class="page-link page-now" href="bookReport?cPage=${p}">${p}</a></li>
		           </c:forEach>
	           	<c:choose>
		       		<c:when test="${ pi.currentPage eq pi.maxPage }">
		       			<li class="page-item disabled"><a class="page-link" href="#">Next</a></li>
		       		</c:when>
		       		<c:otherwise>
		       		 	<li class="page-item"><a class="page-link page-next" href="bookReport?cPage=${ pi.currentPage + 1 }">Next</a></li>
		       		</c:otherwise>
	       		</c:choose>
       		</ul>
       		
      	<script>
       		/*
      		$(function(){
      			if(${ pi.currentPage } == 1){
      				$('.page-previous').attr("href", "#");
      				$('.li-previous').addClass('disabled');
      			}
      			if("${ pi.maxPage }"){
      				$('.page-next').attr("href", "#");
      				$('.li-next').addClass('disabled');
      			}
      			if(${pi.currentPage} == ${ pi.maxPage }){
      				$('.li-previous').after('<li class="page-item"><a class="page-link disabled" href="#">1</a></li>');
      			}
      		})*/
      	</script>
       	<c:if test="${ not empty reportsearch }">
       		<script>
       			$(function(){
       				let reportSearchOption = '${reportSearchOption}';
       				let reportSearchValue = '${reportSearchValue}';
       				let attrNow = "reportsearch.bk" + "?reportSearchOption=" + reportSearchOption + "&reportSearchValue=" + reportSearchValue + "&cPage="; 
       				
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
         
         
        <!-- 
        <c:choose>
			<c:when test="${ empty condition }">
				<ul class="pagination justify-content-center">
		        	<c:choose>
			       		<c:when test="${ pi.currentPage eq 1 }">
			             	<li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>
			       		</c:when>
			       		<c:otherwise>
			       		 	<li class="page-item"><a class="page-link" href="book?cPage=${ pi.currentPage - 1 }">Previous</a></li>
			       		</c:otherwise>
			       	</c:choose>
			           <c:forEach begin="${ pi.startPage }" end="${ pi.endPage }" var="p">
			           		<li class="page-item"><a class="page-link" href="book?cPage=${p}">${p}</a></li>
			           </c:forEach>
			           <c:choose>
			       		<c:when test="${ pi.currentPage eq pi.maxPage }">
			       			<li class="page-item disabled"><a class="page-link" href="#">Next</a></li>
			       		</c:when>
			       		<c:otherwise>
			       		 	<li class="page-item"><a class="page-link" href="book?cPage=${ pi.currentPage + 1 }">Next</a></li>
			       		</c:otherwise>
			       	</c:choose>
        		</ul>
			</c:when>
			<c:otherwise>
				<ul class="pagination justify-content-center">
		        	<c:choose>
			       		<c:when test="${ pi.currentPage eq 1 }">
			             	<li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>
			       		</c:when>
			       		<c:otherwise>
			       		 	<li class="page-item"><a class="page-link" href="searchbook.bk?cPage=${ pi.currentPage - 1 }&searchBook=${searchBook}&selectBook=${selectBook}">Previous</a></li>
			       		</c:otherwise>
			       	</c:choose>
			           <c:forEach begin="${ pi.startPage }" end="${ pi.endPage }" var="p">
			           		<li class="page-item"><a class="page-link" href="searchbook.bk?cPage=${p}&searchBook=${searchBook}&selectBook=${selectBook}">${p}</a></li>
			           </c:forEach>
			           <c:choose>
			       		<c:when test="${ pi.currentPage eq pi.maxPage }">
			       			<li class="page-item disabled"><a class="page-link" href="#">Next</a></li>
			       		</c:when>
			       		<c:otherwise>
			       		 	<li class="page-item"><a class="page-link" href="searchbook.bk?cPage=${ pi.currentPage + 1 }&searchBook=${searchBook}&selectBook=${selectBook}">Next</a></li>
			       		</c:otherwise>
			       	</c:choose>
	        	</ul>
			</c:otherwise>
		</c:choose> --> 
    </div>
</body>
</html>