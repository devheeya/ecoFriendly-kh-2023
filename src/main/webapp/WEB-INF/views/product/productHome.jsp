<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>product Home</title>
<!-- jQuery 라이브러리 -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<!-- Alertify -->
<!-- JavaScript -->
<script src="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/alertify.min.js"></script>
<!-- CSS -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/alertify.min.css"/>
<!-- Default theme -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/default.min.css"/>
<!-- Semantic UI theme -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/semantic.min.css"/>
<link rel="stylesheet" href="resources/css/productStyle.css"/> 
<script type="text/javascript" src="resources/js/productScript.js"></script>

</head>
<body>
<jsp:include page="../common/header.jsp"/>
<br><br>
	<div class="outer">
		<br> <br>
		<div class="content">
			<div id="searching-area">
			<form action = "product" method="get">
			<c:choose>
				<c:when test="${not empty map.orderBy }">
					<input type="hidden" name = "orderBy" value="${map.orderBy}"> 
				</c:when>
				<c:otherwise>
					<input type="hidden" name = "orderBy" value=""> 
				</c:otherwise>
			</c:choose>
			
			<c:choose>
				<c:when test="${not empty map.keyword }">
				<input type="text" name = "keyword" placeholder="${map.keyword} " oninput="keywords();"> 
				</c:when>
				<c:otherwise>
				<input type="text" name = "keyword" placeholder="검색어를 입력하세요." oninput="keywords();"> 
				</c:otherwise>
			</c:choose>
				<button type="submit">
				<img src="resources/images/searhIcon.svg">
				</button>
				<ul id="keyword-list"></ul>	
			</form>
			
			</div>

			<div id="controll-area">
				<button id="F" class="btn-not" onclick="sort(this);">FOOD</button>
				<button id="G" class="btn-not" onclick="sort(this);">Eco Friendly Items</button>
				<button id="all" class="btn-not" onclick="sort(this);">ALL</button>
				<select>
					<option value="latest">최신순</option>
					<option value="view">조회순</option>
					<option value="like">좋아요순</option>
					<option value="priceDesc">가격 높은순</option>
					<option value="priceAsc">가격 낮은순</option>
				</select>
			</div>
			<script>
				$(()=>{
					
				})
			</script>
			<div id="product-area">
				<div id="products">
					<c:forEach var="p" items="${productList }">
						<div id="${p.productNo }" class="product">
									
							<c:choose>
							<c:when test="${empty loginUser }">
								<div class="product-info" 
								onclick="location.href='product.detail?userNo=0&productNo='+${p.productNo}">
							</c:when>
							<c:otherwise>		
								<div class="product-info" 
								onclick="location.href='product.detail?userNo='+${sessionScope.loginUser.userNo }+'&productNo='+${p.productNo}">
							</c:otherwise>
							</c:choose>
								<h1>${p.productName }</h1>
								<table class="table">
									<thead>
										<tr>
											<th>옵션명</th>
											<th>가격</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="option" items="${p.optionList }">
											<tr>
												<td>${option.optionName }</td>
												<td>${option.price }</td>
											</tr>
										</c:forEach>
										<tr>
										</tr>
									</tbody>
								</table>
							</div>
							
							<div class="like-cart">
									<c:choose>
									<c:when test="${empty sessionScope.loginUser }">
										<img onclick="askLogin();" src="resources/images/heart-regular.svg">
									</c:when>
									<c:otherwise>
										<img onclick="like(${p.productNo}, this);" src="resources/images/heart-regular.svg">
									</c:otherwise>
									</c:choose>
							</div>
							
						</div><!-- product class-->
						
							
						<script>
							$(()=> {
								$('#${p.productNo}').css({
									"background-image" : "url(${p.mainImg})",
									"background-size" : "cover",
									"opacity" : "0.9"
								}).hover(()=>{
									$('#${p.productNo} h1').css("display", "none");
									$('#${p.productNo} table').css('display', 'block');
								}, ()=>{
									$('#${p.productNo} table').css("display", "none");
									$('#${p.productNo} h1').css('display', 'block');
								});
							})
						</script>
					</c:forEach>
					
					</div>
				</div>
				
				<script>
					$(()=>{
						$('#controll-area>button[id="${map.category}"]')
						.removeClass("btn-not")
						.addClass("btn-selected");
						
						$.ajax({
							url : 'getLikes.pr',
							success: result =>{
								for(let i in result){
									$('.product').each((index, item)=>{
										if(result[i].productNo == item.id){
										$(item).find('.like-cart>img').attr('src', 'resources/images/heart-solid.svg');
											
										}
									})
								}
							}
						});
						
						$('#controll-area>select').change(e =>{
							location.href='product?category='+'${map.category}'+'&orderBy='+$(e.target).val();
						});						
					})
				</script>
				<c:if test = "${!empty loginUser}">
				</c:if>
				<c:if test="${not empty map.orderBy }">
					<script>
						$(()=>{
							$('#controll-area option[value="${map.orderBy}"]').attr('selected', true);
						})
					</script>
				</c:if>			
			<div class="paging-area">
				<ul>
				<c:choose>
					<c:when test="${pi.currentPage eq 1 }">
					<li><a disabled> &lt; </a></li>
					</c:when>
					<c:when test="${not empty keyword and not empty category }">
					<li><a href="product?keyword=${keyword }&category=${category }&cPage=${pi.currentPage-1 }"> &lt; </a></li>
					</c:when>
					<c:otherwise>
					<li><a href="product?cPage=${pi.currentPage-1 }"> &lt; </a></li>
					</c:otherwise>
				</c:choose>	
					
					<c:forEach begin="${pi.startPage }" end="${pi.endPage }" var="p">
					<c:choose>
						<c:when test="${pi.currentPage eq p }">
							<li><a disabled> ${p } </a></li>
						</c:when>
						<c:when test="${not empty keyword and not empty category }">
							<li><a href="product?keyword=${keyword }&category=${category}&cPage=${p}"> ${p } </a></li>
						</c:when>
						<c:otherwise>
							<li><a href="cPage=${p}"> ${p } </a></li>
						</c:otherwise>
                   </c:choose>
                    </c:forEach>
                    
                <c:choose>
					<c:when test="${pi.currentPage eq pi.maxPage }">
					<li><a disabled> &gt; </a></li>
					</c:when>
					<c:when test="${not empty keyword and not empty category }">
					<li><a href="product?keyword=${keyword }&category=${category }&cPage=${pi.currentPage-1 }"> &gt; </a></li>
					</c:when>
					<c:otherwise>
					<li><a href="product?cPage=${pi.currentPage+1 }"> &gt; </a></li>
					</c:otherwise>
				</c:choose>	    
                </ul>
			</div>
			</div>
			
		</div>
	</div>
<jsp:include page="../common/footer.jsp"/>
</body>
</html>