var test = {
			name: "Василий",
			age: 20
		};
var testStr = JSON.stringify(test);
var newTest = JSON.parse(testStr);
if (newTest==test) {
	document.write("Имя: " + newTest.name +  "<br /> Возраст: " + newTest.age + " лет"); 
}
else document.write("Отправляемое и получаемое значение не равны.");