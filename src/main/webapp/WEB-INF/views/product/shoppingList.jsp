<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주문내역 조회</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

<style>
/*--공통 스타일 --*/
div {
	box-sizing: border-box;
	margin: 0;
	padding: 0;
	border : 1px solid orange;
}
.content {
	width: 1200px;
	height: 1000px;
	margin: auto;
}
.outer {
	height: 1100px;
}
.order, .order-info{
	display: flex;
}
.order{
	align-items: center;
	background:beige;	
	border-radius : 5px;
	padding:5px;
	margin:10px;
}
.order-info{
	height:200px;
	width:60%;
	align-items: center;	
}
.order-info>img{
	width : 200px;
	height : 100%;
	border-radius : 5px;
}
.order-info>div{
	padding-left:10px;
	height:100%;
	width:100%;
}
h3{
	padding: 0%;
	margin:0;
}
.shipping, .order-status{
	width: 250px;
	height:200px;
	item-align:center;
	text-align:center;
	font-size:20px;
	justify-content:center;
	padding-top:60px;
}
.order-status>button{
	background:orange;
	border : none;
	border-radius:5px;
	padding:5px 10px 5px 10px;
	font-size:20px;
	color:white;
	margin: 20px 2px 20px 2px;
}

</style>

</head>
<body>
<jsp:include page="../common/header.jsp"/>
<br>
<br>
<br>
<br>
<div class="outer">
	<h1>주문내역 조회</h1>
	<div class="content">
		<c:if test="${not empty orders }">
			<c:forEach items="${orders }" var="o" varStatus="status">
				<div class="order">
				<div class="order-info">
					<!-- 상품 메인 이미지 -->
					<img src="${o.orderDetail[0].mainImg }">
					
					<!-- 상품 정보 -->
					<div>
						<a href="#"><h2>${o.orderDetail[0].productName }(${o.itemQty}건)</h2></a>
						<div>
							<c:forEach items="${o.orderDetail}" var="cart">
							<a href="product.detail?productNo=${cart.productNo }">${cart.optionName }</a>
							<span>${cart.price }</span>*<span>${cart.qty }</span>/ 
							</c:forEach>
						</div>
					<c:choose>
					<c:when test="${o.totalPrice ge 40000 }">
						<span>${o.totalPrice }</span>
					</c:when>
					<c:otherwise>
						<span>${o.totalPrice +3000 }</span> 원
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
					<span>주문상태</span>
					<br>
					<button>후기 작성</button>
					<button>문의</button>
				</div>
		</div><!-- 주문 한 개 -->
			
			</c:forEach>
		</c:if>
	</div>
	
	
</div>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>


</body>
</html>