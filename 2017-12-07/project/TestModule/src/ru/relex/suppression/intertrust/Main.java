public static void main(String[] args) {
	//регистрация 7 контроллеров
	//регистрация 7 объектов
	
//общий	 вызов и проверка работаспособности+отображение статистики
	for(Controller item : Register.getController())
		item.start(args[0],args[1],Registrator.getChekers());
	
}