<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 주문</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<style>
/*--공통 스타일 --*/
div {
	box-sizing: border-box;
	margin: 0;
	padding: 0;
	border: 1px solid transparent;
}

.outer {
	height: 1500px;
	background: rgb(245, 238, 220);
}
.orderer-info button{
	width : 100px;
	height : 30px;
	backgound:none;
}

#order-content {
	width: 800px;
	margin: auto;

}

#order-content>div {
	width: 100%;
	padding: 5px 20px 5px 20px;
}

.order-item {
	padding-left: 100px;
	padding-right: 150px;
	display: flex;
	width:100%;
	margin: 5px 0px 5px 0px;
}

.order-item>img {
	width: 210px;
	height: 180px;
	border-radius: 2px;
}

.order-item>div {
	width: 300px;
	height: 180px;
	padding-left:20px;
	font-size : 20px;
}

h2 {
	margin: 0;
	padding: 0;
	background:rgb(216, 207, 186);
}
#orderbtn{
	padding:10px 200px 10px 200px;
	background:orange;
	border : none;
	border-radius:10px;
	margin: 10px 150px 100px 150px;
	font-size:20px;
}
</style>
</head>
<body>
	<jsp:include page="../common/header.jsp" />
	<br>

	<div class="outer">
		<br> <br> <br>
		<div id="order-content">
			<form action="product.order" method="post">
				<div class="item-info">
					<h2>주문 상품 정보</h2>
						<c:if test="${not empty item }">
							<div class="order-item">
							<input type="hidden" name="items[0].optionNo" value="${item.optionNo }">
							<input type="hidden" name="items[0].qty" value="${order.qty }">
							<img src="${item.mainImg }">
							<div>
								<h5>${item.productName}</h5>
								<span>${item.optionName }</span>
								<span>${item.price *order.qty}</span>
							</div>
							</div>
						</c:if>
						<c:if test="${not empty items }">
							<c:forEach  items="${items.itemList}" var="i" varStatus="status">
								<div class="order-item">
									<input type="hidden" name="items[${status.index }].optionNo" value="${i.optionNo }">
									<input type="hidden" name="items[${status.index }].qty" value="${i.qty }">
									<img src="${i.mainImg }">
									<div>
										<h5>${i.productName} (${i.qty }개)</h5>
										<span>${i.optionName }</span>
										<span>${i.price *i.qty}원</span>
									</div>
								</div>
							</c:forEach>
						</c:if>
					</div>
				<div class="orderer-info">
					<h2>주문자 정보  </h2>
					<input type="hidden" name="userNo"
						value="${sessionScope.loginUser.userNo }"> <br> 이름 :
					<input type="text" readonly
						value="${sessionScope.loginUser.userName }"> <br> 연락처
					: <input type="text" readonly
						value="${sessionScope.loginUser.phone }"> <br> 이메일 :
					<input type="text" readonly
						value="${sessionScope.loginUser.email }"> <br><br><br>
				</div>
				<div class="shipping-info">
					<h2>배송 정보</h2>
					<input type="hidden" name="addressNo">
					<a href="#addressList" data-toggle="modal">배송지 선택</a>
					<div>
					<br/>
					</div>
				</div>
				<div class="order-summary">
					<h2>주문 요약</h2>
					<div>
						<c:choose>
						<c:when test="${numOfItem ne 1 }">
						주문 상품 : 
						<span id="itemName">
						${items.itemList[0].optionName }(외 ${numOfItem-1 }건)
						</span>
						<input type="hidden" name="itemName" value="${items.itemList[0].optionName }(외 ${numOfItem-1 }건)"/>
						</c:when>
						<c:otherwise>
						주문 상품 : 
						<span id="itemName">
						${items.itemList[0].optionName }(1건)
						</span>
						<input type="hidden" name="itemName" value="${items.itemList[0].optionName }(1건)"/>
						</c:otherwise>
						</c:choose>
					</div>
					<div>
					<c:choose>
					<c:when test="${shipping}">
						배송 : 무료배송 <br/>
						총 금액 : <span id="totalPrice">${totalPrice }</span>
						<input type="hidden" name="totalAmount" value="${totalPrice }"/>
					</c:when>
					<c:otherwise>
						배송 : 3,000원 <br/>
						총 금액 :<span id="totalPrice">${totalPrice+3000 }</span>
						<input type="hidden" name="totalAmount" value="${totalPrice+3000}"/>
					</c:otherwise>
					</c:choose>
					</div>
					<input type="hidden" name="quantity" value="${numOfItem }"/>
					<button type="submit" id="orderbtn">주문하기</button>
				</div>
			</form>
			<script>
				function letsgo(){
					$.ajax({
						url :'pay',
						data : {
							userNo : '${sessionScope.loginUser.userNo}',
							totalAmount : $('#totalPrice').text(),
							itemName : $('#itemName').text(),
							quantity : '${numOfItem}'
						},
						success : e => {
							location.href=e;
						},
						error : ()=> {
							console.log('개똥 api..');
						}
					})
				};
				$(()=>{

					$.ajax({
						url : 'addressList',
						success : e=>{
							e.map(a=>{
								$el = $('<div class="address"><table class="table table-borderless"></table></div>').attr('id', a.addressNo);
								let $ptr = $('<tr></tr>');
								let $atr = $('<tr></tr>');
								let $dtr = $('<tr></tr>');
								$ptr.append($('<td></td>').text('수령인')
										, $('<td colspan="2"></td>').text(a.receiver + '(연락처 : '+a.phone+')'));
								
								$atr.append($('<td></td>').text('주소')
										, $('<td colspan="2"></td>').text(a.address+' (' + a.post + ' )'));
								$dtr.append($('<td></td>').text('상세주소')
										, $('<td colspan="2"></td>').text(a.detailAddress));								
								$el.append($ptr, $atr, $dtr);
								$('#addressList .modal-body').append($el);
							})
						},
						error : () =>{
							console.log('ajax하기시러..배송지이이이이!');
						}
					});
				});
				jQuery(document).ready(()=>{
					$('#addressList .modal-body').on('click','.address', e=>{
						let $address = $(e.target).parents('.address');
						let $adrInfo = $address.clone(false);
						$('.shipping-info>div').html($adrInfo);
						$('input[name = "addressNo"]').val($address.attr('id'));
						$('.address').css('background', 'none');
						$address.css('background', 'beige')
					});
				});
			</script>			

      		</div>
		</div>
	</div>
<!-- The Modal -->
  <div class="modal fade" id="addressList">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
      
        <!-- Modal Header -->
        <div class="modal-header">
          <h4 class="modal-title">배송지 선택</h4>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>       
        <!-- Modal body -->
        <div class="modal-body">
        	<a href="#addForm" data-toggle="modal">배송지 추가</a>

        </div>        
        <!-- Modal footer -->
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
        </div>
        
      </div>
    </div>	
    </div>	
    <div class="modal fade" id="addForm">
	    <div class="modal-dialog modal-dialog-centered">
	      <div class="modal-content">
	      
	        <!-- Modal Header -->
	        <div class="modal-header">
	          <h4 class="modal-title">신규 배송지 추가</h4>
	          <button type="button" class="close" data-dismiss="modal">&times;</button>
	        </div>       
	        <!-- Modal body -->
	        <div class="modal-body">
	        	<form action="newAddress" method="post" class="form-group">
	        		<input type="text"/><br>
	        		<input type="text"/>
	        	</form>	
	        </div>        
	        <!-- Modal footer -->
	        <div class="modal-footer">
	          <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
	        </div>
	        
	</div>
</body>
</html>