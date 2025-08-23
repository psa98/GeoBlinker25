var url_prefix = "https://ibronevik.ru/taxi/c/tutor/api/v1/"; 
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

//Авторизация и получение токена юзером:
var req = new XMLHttpRequest(); 
req.open('POST', url_prefix + "auth", false);
req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
var post_obj = { 
 "login": user.login, 
 "password": user.password, 
 "type":  user.type
} 
req.send(encode_data(post_obj));
var auth_hash = JSON.parse(req.response).auth_hash;
req = new XMLHttpRequest();
req.open('POST', url_prefix + "token", false);
req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
var post_obj = {"auth_hash": auth_hash};
req.send(encode_data(post_obj));
user_token = JSON.parse(req.response).data.token;
user_u_hash = JSON.parse(req.response).data.u_hash;

//Создание платежа
req = new XMLHttpRequest(); 
req.open('POST', url_prefix + "payment/create", false);
req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
var post_obj = { 
 "token": user_token,
 "u_hash": user_token,
 "data": JSON.stringify({ 
	"sum":	100,//для примера
	"currency":	"RUB",
	"payment_service": 1,
	"subs_id": "идентификатор подписки юзера",//необязательно
	"payment_way": 2,
	"from":	"идентификатор юзера"//необязательно, доступно только админу
 },
 "appUrl": 'страница редиректа после оплаты или JSON.stringify({"succeeded":"страница для успешной оплаты","canceled":"страница для отменной оплаты"})'//необязательно
 )
}; 
req.send(encode_data(post_obj));
var p_id = JSON.parse(req.response).data.p_id								//идентификатор платежа
var confirmation_url = JSON.parse(req.response).data.confirmation_url		//ссылка на оплату

//Получение платежа
req = new XMLHttpRequest(); 
req.open('GET', url_prefix + "payment/get", false);
req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
var post_obj = { 
 "token": user_token,
 "u_hash": user_token,
 "p_id": "идентификатор платежа"
}; 
req.send(encode_data(post_obj));
var payment_obj = JSON.parse(req.response).data.payment[0];
/*
Ключи объекта платежа:
	"sum":						"сумма платежа",
	"percent":					"комиссия при оплате в процентах",
	"total_sum":				"сумма платежа с учетом комиссии",
	"currency":					"валюта платежа",
	"json":						"массив дополнительных параметров",
	"payment_status":			"статус платежа",
	"payment_service":			"платежный сервис",
	"from":						"идентификатор юзера, выполняющего оплату",	
	"to":						"идентификатор юзера, получающего оплату",
	"c_u_id":					"идентификатор юзера, создавшего платеж",
	"e_u_id":					"идентификатор юзера, изменившего платеж",
	"timestamp_edited":			"юникс время редактирования платежа",
	"timestamp_created":		"юникс время создания платежа",
	"subs_id":					"идентификатор подписки юзера",
	"payment_way":				"способ оплаты",
	"p_id_outer":				"идентификатор платежа в сервисе"
	
Значения payment_status:
id			название в апи			название в юкассе
1			Created					pending
3			Canceled				canceled
6			Succeeded				succeeded
*/

//Получение тарвифов для подписок
req = new XMLHttpRequest(); 
req.open('GET', url_prefix + "data", false); 
req.send(); 
var cities = JSON.parse(req.response).data.data.tariffs;