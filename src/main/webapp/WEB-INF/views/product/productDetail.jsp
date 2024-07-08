<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 상세보기</title>
<!-- jQuery 라이브러리 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<link rel="stylesheet" href="resources/css/productStyle.css"/> 
<script type="text/javascript" src="resources/js/productScript.js"></script>
</head>
<body>
	<jsp:include page="../common/header.jsp" />
	<div class="product-content">
		<div id="product-top">
			<div id="main-pic">
				<img src="${p.mainImg }">
			</div>
			<div id="product-info">
			<form action="listOrderForm" method="get">
				<div id="productTitle">
					<div id="productName">
						<h1>${p.productName }</h1>
					</div>				
					<div id="like-star">
					<c:choose>
						<c:when test="${empty sessionScope.loginUser}">
							<div id="like"><img onclick="askLogin();" src="resources/images/heart-regular.svg"></div>
						</c:when>
						<c:when test="${like eq 'N' }">
						<div id="like"><img onclick="like(${p.productNo}, this);" src="resources/images/heart-regular.svg"></div>
						</c:when>
						<c:otherwise>
						<div id="like"><img onclick="like(${p.productNo}, this);" src="resources/images/heart-solid.svg"></div>
						</c:otherwise>
					</c:choose>
						<div id="star">
						<a href="#">${review.starRate }/5</a>
						 (${review.reviewNo }개) 
						</div>
					</div>
				</div>

				<div id="productDescription">
					<div id="brandstory">
					<h3 id="brand${brand.brandNo }">${brand.brandName }</h3>
					<p>${brand.brandIntro }</p>
					</div>
					
					<div id="briefInfo">${p.productInfo }</div>
					
					<div id="choice">

							<select name="optionNo" required>
								<option value="0">옵션을 선택하세요</option>
								<c:forEach items="${p.optionList }" var="option">
									<option value="${option.optionNo }">${option.optionName}</option>
								</c:forEach>
							</select> 
							
							<span id="price"></span>
							
							<br> 
							<input type="hidden" name="mainImg" value="${p.mainImg }">
							<input type="hidden" name="productNo" value="${p.productNo }">
							<input type="hidden" name="productName" value="${p.productName }">
							<input type="hidden" value="${sessionScope.loginUser.userNo }" name="userNo">
							<input type="number" value="1" min="1" max="10" required disabled name="qty">
							<span id="totalPrice"></span><br>
							
							<br>
						<c:choose>
						<c:when test="${!empty sessionScope.loginUser }">	
							<button type="button" onclick="addCart();">장바구니 추가</button>
							<button type="submit" onclick="return optionCheck();">구매하기</button>
						</c:when>
						<c:otherwise>
							<p>로그인 후 상품 주문이 가능합니다.</p>
						</c:otherwise>
						</c:choose>
					</div><!-- 옵션 선택 영역 끝 -->
				</div><!-- 제품 설명영역 끝 -->
			</form>
			</div>
		</div>

		<div id="picture-area">
			<div class="detail-info">
				<h3>상품 설명</h3>
				<p>${p.detailInfo }</p>
			</div>
			<hr>
				<c:if test="${empty images }">
				<h3 align ="center"> 상세 이미지가 없습니다. </h3>
				</c:if>
			<c:forEach items="${images }" var="img">
				<img src="${img}">
				<br>
			</c:forEach>
			<hr>
			<div class="extra-info">
				<h3>상품 정보</h3>
				<p>${p.extraInfo }</p>
			</div>
		</div>
		<hr>
		<div id="review-area">
			 <c:choose>
			 	<c:when test="${empty review.reviewNo}">
			 		<h2>상품 리뷰가 없습니다.</h2>
			 	</c:when>
			 	<c:otherwise>
			 		<h2 onclick="showReviews();">리뷰 보기(${review.reviewNo }개)</h2>
			 	</c:otherwise>			 	
			 </c:choose>
			 <div>
			 
			 <div id="pagination-container"></div>
			 </div>
		</div>

	</div>
	<script>
		let $reviews = $('#review-area>div');
		function showReviews(){
			if($reviews.css('display') == 'none'){
				$reviews.css('display', 'block');					
			}
			else{
				$reviews.css('display', 'none');
			}
		};
		$(()=>{
			$reviews.css('display', 'none');
			$.ajax({
				url:'product.review',
				data:{productNo:${p.productNo}},
				success: reviews => {
					let html = template(reviews);
					$reviews.html(html);
				}
			});
		})
	</script>
	
	<br><br><br>
	<br><br><br>
	<br><br><br>
	<br><br><br>
<jsp:include page="../common/footer.jsp"/>	
</body>
</html>