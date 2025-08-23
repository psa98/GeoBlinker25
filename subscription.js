var url_prefix = "https://ibronevik.ru/taxi/c/0/api/v1/"; 
var user = {
 "login": "логин", 
 "password": "пароль", 
 "type": "e-mail" 
};
var user_token = "значение токена"; 
var user_u_hash = "значение хеша"; 
function encode_data(obj){ 
 var data = []; 
 for (var key in obj){ 
  data.push(encodeURIComponent(key)+"="+encodeURIComponent(obj[key]));  
 } 
 data = data.join("&"); 
 return data; 
}

//Оформления подписки
req = new XMLHttpRequest(); 
req.open('POST', url_prefix + "subscription/create", false);
req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
var post_obj = { 
 "token": user_token,
 "u_hash": user_token,
 "data": JSON.stringify({ 
	"u_id":	"идентификатор юзера",//только админу
	"tariff": "идентификатор тарифа data.tariffs",
	"start_date": "юникс время начала подписки",//если не указывать, проставляется время подтверждения оплаты
	"end_date": "юникс время окончания подписки",
	"subs_status": "идентификатор статуса подписки data.subscription_statuses",//только админу
	"auto_renew":	"0 или 1, автопродление"
 })
};
//Обязательно указывать tariff
req.send(encode_data(post_obj));
var subs_id = JSON.parse(req.response).data.p_id								//идентификатор подписки

//Получение данных о подписке
req = new XMLHttpRequest(); 
req.open('GET', url_prefix + "subscription/get", false);
req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
var post_obj = { 
 "token": user_token,
 "u_hash": user_token,
 "subs_id": "идентификатор подписок через запятуюи",//необязательно
 "u_id": "идентификатор юзеров через запятую"//необязательно
}; 
req.send(encode_data(post_obj));
var subscription_arr = JSON.parse(req.response).data.subscription;
/*
Ключи элемента массива подписок:
	"subs_id":					"идентификатор подписки",
	"u_id":						"идентификатор юзера",
	"tariff":					"идентификатор тарифа data.tariffs",
	"start_date":				"юникс время начала подписки",
	"end_date":					"юникс время окончания подписки",		
	cancellation_date			"юникс время отмены подписки",
	"subs_status":				"идентификатор статуса подписки data.subscription_statuses",
	"auto_renew":				"0 или 1, автопродление",
	"p_id":						"массив идентификаторов оплаты",
	"paid":						"true или false оплачена ли"
*/