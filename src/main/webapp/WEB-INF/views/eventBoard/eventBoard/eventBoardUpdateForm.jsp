<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>Document</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <style>
	    .content {
	        background-color:rgb(247, 245, 245);
	        width:80%;
	        margin:auto;
	    }
	    .innerOuter {
	        border:1px solid palegreen;
	        width:80%;
	        margin:auto;
	        padding:5% 10%;
	        background-color:white;
	    }
	    
	    #updateForm>table {width:100%;}
	    #updateForm>table * {margin:5px;}
    </style>
</head>
<body>

	<jsp:include page="../../common/header.jsp" />
	
	<div class="content">
		<br><br>
		<div class="innerOuter">
			<h2>게시글 수정하기</h2>
			<br>
			
			<form id="updateForm" method="post" action="update.bo" enctype="multipart/form-data">
				<input type="hidden" name="bno" value="${ b.bno }" />
				<table align="center">
					<tr>
						<th><label for="title">제목</label></th>
						<td><input type="text" id="title" class="form-control" value="${ b.title }" name="title" required></td>
					</tr>
					<tr>
						<th><label for="writer">작성자</label></th>
						<th><input type="text" id="writer" class="form-control" value="${ b.writer }" name="writer" readonly></th>
					</tr>
					<tr>
						<th><label for="upfile">첨부파일</label></th>
						<td>
							<input type="file" id="upfile" class="form-control-file border" name="">
							
							<c:if test="${ not empty b.originName }">
								현재 업로드된 파일 :
								<a href="${ b.changeName }" download="${ b.originName }">${ b.originName }</a>
								<input type="hidden" value="${ b.originName }" name="originName" />
                            	<input type="hidden" value="${ b.changeName }" name="changeName" />
                            </c:if>
                            
                        </td>
                    </tr>
                    <tr>
                    	<th><label for="content">내용</label></th>
                    	<td><textarea id="content" class="form-control" rows="10" style="resize:none;" name="content" required>${ b.content }</textarea></td>
				</table>
				<br>
				
				<div align="center">
					<button type="submit" class="btn btn-primary">수정하기</button>
					<button type="reset" class="btn btn-danger">이전으로</button>
				</div>
			</form>
		</div>
		<br><br>
		
	</div>
	
	

</body>
</html>