var options = {
	"key": /*[[${key}]]*/,
	"amount": /*[[${tripOrder.amount*100}]]*/,
	"currency": "INR",
	"name": "BlueBus.com",
	"description": "Ticket Booking",
	"image": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRztzdOgtuHMLzwdKqtVJ0TP-xgb7sV-DSW9A&s",
	"order_id": /*[[${tripOrder.orderId}]]*/,
	"callback_url": "/confirm-order/[[${tripOrder.id}]]",
	"prefill": {
		"name": /*[[${customer.name}]]*/,
		"email": /*[[${customer.email}]]*/,
		"contact": /*[['+91'+${customer.mobile}]]*/
     },
	"notes": {
		"address": "Razorpay Corporate Office"
	},
	"theme": {
		"color": "#F5F7F8"
	}
};
var rzp1 = new Razorpay(options);
document.getElementById('rzp-button1').onclick = function(e) {
	rzp1.open();
	e.preventDefault();
}
