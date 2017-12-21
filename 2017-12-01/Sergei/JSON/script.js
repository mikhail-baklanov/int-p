recordAboutSomebody = {
    name : "Vasylii",
    age : 20
};
var recordAboutSomebodyElse = {}

var jsonData = JSON.stringify(recordAboutSomebody);
recordAboutSomebodyElse = JSON.parse(jsonData);

console.log("Объект:");
console.log(jsonData);

console.log("Сравнение 2-х объектов:");
if (recordAboutSomebody == recordAboutSomebodyElse){
    console.log("Объекты одинаковы")
} else {
    console.log("Объекты разные")
}
