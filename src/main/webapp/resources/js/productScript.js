function optionCheck(){
	if($('#choice select').val() == 0){
		alert('옵션을 먼저 선택 해주세요.');
		return false;
	}
};					
function addCart(){
	let $option = $('#choice select').val();
	if($option == 0){
		alert('상품 옵션을 선택해주세요.');
		return false;
	}
	else{
		$.ajax({
			url : 'add.cart',
			method : 'POST',
			data : {
				optionNo : $option,
				qty : $('#choice input[type=number]').val()
			},
			success : e =>{
				if(e == 'added'){
					alert('장바구니에 추가되었습니다.');
				}
				else if(e == 'failed'){
					alert('장바구니 추가에 실패했습니다. 다시 한 번 도전해주세요.');
				}
				else{
					alert('이미 장바구니에 존재하는 상품입니다.\n 추가일자 :'+e);
				}
			}							
		})							
	}						
};
					
function askLogin(){
	if(confirm('로그인이 필요한 기능입니다. 로그인 화면으로 이동하시겠습니까?')){
	location.href='login';
	}
};

function like(pno, th){
	$.ajax({
		url : 'product.like',
		data : {
			productNo : pno
		},
		success : e => {
			let value = '';
			value = e=='added'? 'resources/images/heart-solid.svg' : 
					(e=='removed'? 'resources/images/heart-regular.svg': '');
			if(value != '') $(th).attr('src', value);
		}
	})
};

function template(data){
	let value = '<table><tbody>';
	$.each(data, (index, item)=>{
			value += '<tr>'
					+ '<td rowspan="2"><img src="'+item.changeName+'"></td>'
					+ '<td><span class="fakebtn">'+item.option+'</span> <b>'+item.reviewTitle+'</b></td>'
					+'</tr>'
					+'<tr>'
					+ '<td>'+item.reviewContent+'</td>'
					+'</tr>';
	});
	value += '</tbody></table>';
	return value;
};
function keywords(){
	$.ajax({
		url : 'getKeywords',
		type : 'get',
		data : {keyword:$('input[name=keyword]').val()},
		success : e =>{
			let str = '';
			for(let i in e){
				str += '<li onmouseover="setKeyword(this);">'
					+ '<div class="keyword">'
					+ '<div>'+e[i] +'</div>'
					+ '<button onclick="removeKeyword(this);"> X </button>'
					+'</div>'
					+'</li>';
			}
			$('#keyword-list').html(str);
		},
		error : ()=>{
			console.log('망해따..');
		}
	});
};
function removeKeyword(btn){
	$(btn).parents('li').empty();
};
function setKeyword(li){
	$('input[name=keyword]').val($(li).children().children('div').text());				
};
function sort(btn){
	location.href='product?category='+btn.id;
};
$(()=>{		
	$('#choice select').change(()=>{
		$.ajax({
			url : 'getPrice',
			data : {optionNo : $('#choice select').val()},
			success : price =>{
				$('#price').text(price+'원');
				$('#totalPrice').text(price+'원');
				$('#choice input').attr('disabled', false);
			}
		})
	});
	$('#choice input').change(()=>{
		if($('#choice select').val() == 0){
			alert('옵션을 먼저 선택 해주세요.');
			$('#choice select').focus();
		}else{							
		let price = $('#choice span').text();
		let totalPrice =parseInt(price.replace(',', ''))*parseInt($('#choice input[type=number]').val());
		$('#totalPrice').text(totalPrice.toLocaleString()+'원');
		}							
	});

})
						