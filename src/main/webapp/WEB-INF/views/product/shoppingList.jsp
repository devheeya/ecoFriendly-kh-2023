<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주문내역 조회</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<style>
/*--공통 스타일 --*/
div {
	box-sizing: border-box;
	margin: 0;
	padding: 0;
}

.content {
	width: 1200px;
	height: 1000px;
	margin: auto;
}

.outer {
	height: 1100px;
}

.order, .order-info {
	display: flex;
}

.order {
	align-items: center;
	background: beige;
	border-radius: 5px;
	padding: 5px;
	margin: 10px;
}

.order-info {
	height: 200px;
	width: 60%;
	align-items: center;
}

.order-info>img {
	width: 200px;
	height: 100%;
	border-radius: 5px;
}

.order-info>div {
	padding-left: 10px;
	height: 100%;
	width: 100%;
}

h3 {
	padding: 0%;
	margin: 0;
}

.shipping, .order-status {
	width: 250px;
	height: 200px;
	item-align: center;
	text-align: center;
	font-size: 20px;
	justify-content: center;
	padding-top: 60px;
}

.order-status>button {
	background: orange;
	border: none;
	border-radius: 5px;
	padding: 5px 10px 5px 10px;
	font-size: 20px;
	color: white;
	margin: 20px 2px 20px 2px;
}
.paging-area{
	padding-top : 50px;
	margin:auto;
	clear:both;
}
.paging-area>ul{
	list-style : none;
	display:flex;
	padding-left:950px;
}
.paging-area li{
	width : 25px;
	height:25px;
	font-size : 19px;
	font-weight:bold;
	padding:0px;
	margin:10px;
}


</style>

</head>
<body>
	<jsp:include page="../common/header.jsp" />
	<br>
	<br>
	<br>
	<br>
	<div class="outer">
		<div class="content">
		<h1>주문내역 조회</h1>
		<jsp:include page="prodMenu.jsp"/>
			<c:if test="${not empty orders }">
				<c:forEach items="${orders }" var="o" varStatus="status">
					<div class="order">
						<div class="order-info">
							<!-- 상품 메인 이미지 -->
							<img src="${o.orderDetail[0].mainImg }">

							<!-- 상품 정보 -->
							<div>
								<a href="product.detail?productNo=${o.orderDetail[0].productNo }"><h2>${o.orderDetail[0].productName }(${o.itemQty}건)</h2></a>
								<div>
									<c:forEach items="${o.orderDetail}" var="cart">
										<a href="product.detail?productNo=${cart.productNo }">${cart.optionName }</a>
										<span>${cart.price }</span>*<span>${cart.qty }</span>/ 
									</c:forEach>
								</div>
								<c:choose>
								<c:when test="${o.totalPrice ge 40000 }">
									<span class="total-price">${o.totalPrice }</span>원 
								</c:when>
								<c:otherwise>
										<span class="total-price">${o.totalPrice +3000 }</span>원
								</c:otherwise>
								</c:choose>
							</div>
						</div>

						<!-- 배송비-->
						<div class="shipping">
							배송비<br>
							<c:choose>
								<c:when test="${o.totalPrice ge 40000 }">
								무료배송
								</c:when>
								<c:otherwise>
								3,000원
								</c:otherwise>
							</c:choose>

						</div>

						<!-- 주문상태, 후기작성 버튼-->
						<div class="order-status">
							<span>주문 완료</span> <br>
							<button type="button" class="btn btn-info btn-lg review-submit"
								data-toggle="modal" data-target="#writeReview" onclick="loadOptions('${o.orderNo}');">후기 작성</button>
						</div>
					</div>
					<!-- 주문 한 개 -->
				</c:forEach>
			</c:if>
			<div class="paging-area">
				<ul>
				<c:choose>
					<c:when test="${pi.currentPage eq 1 }">
					<li><a disabled> &lt; </a></li>
					</c:when>
					<c:otherwise>
					<li><a href="shoppingList?userNo=${sessionScope.loginUser.userNo}&cPage=${pi.currentPage-1 }"> &lt; </a></li>
					</c:otherwise>
				</c:choose>	
					
					<c:forEach begin="${pi.startPage }" end="${pi.endPage }" var="p">
						<li><a href="shoppingList?userNo=${sessionScope.loginUser.userNo}&cPage=${p}"> ${p } </a></li>
                    </c:forEach>
                    
                <c:choose>
					<c:when test="${pi.currentPage eq pi.maxPage }">
					<li><a disabled> &gt; </a></li>
					</c:when>
					<c:otherwise>
					<li><a href="shoppingList?userNo=${sessionScope.loginUser.userNo}&cPage=${pi.currentPage+1 }"> &gt; </a></li>
					</c:otherwise>
				</c:choose>	    
                </ul>
			</div>	
		</div>
		<script>
			function loadOptions(ono){
				$.ajax({
					url : 'check.review',
					type : 'get',
					data : {orderNo : ono},
					success : e=>{
						let $str = '';
						if(e.length==0){
							$('#writeReview input, textarea, select, button:submit').attr('disabled', true);
							$('#writeReview textarea').text('\n모든 상품에 리뷰를 등록하셨습니다.');
						}
						else{
							for(let i in e){
							$str += '<option value = "'+e[i].optionNo+'">'
								 +e[i].optionName+'</option>';
							}
							$('#writeReview input, textarea, select, button:submit').attr('disabled', false);
							$('#writeReview textarea').text('리뷰를 작성하세요');
							$('#writeReview select').html($str);
							$('#writeReview input[name=orderNo]').val(ono);	
						}
					}
				})
			}
		</script>
	</div>
	</div>	



<jsp:include page="../common/footer.jsp"/>
		<!-- Modal -->
			<div class="modal fade" id="writeReview">
				<div class="modal-dialog modal-lg">
					<div class="modal-content">

						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">&times;</button>
							<h4 class="modal-title">후기 작성하기</h4>
						</div>

						<div class="modal-body">
							<form action="insert.review" method="post" enctype="multipart/form-data">
								제품 사진 : <input type="file" name="upfile">
								<select name="optionNo" required>
								
								</select><br>
								<input type="text" name="reviewTitle" placeholder="제목을 입력하세요" required /> <br>
								<textarea name="reviewContent" cols="70" rows="8" required>리뷰 내용을 작성하세요.</textarea>
								별점 : <input type="number" name="starRate" min="0" max="5">
								<input type="hidden" name="orderNo"/>
								<button type="submit">제출</button>
						</div>
						</form>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Close</button>
						</div>

					</div>
				</div>
			</div>
</body>
</html>