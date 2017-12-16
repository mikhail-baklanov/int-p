Не совсем понял как продемонстрировать консольный запуск helloWorld.html на ТОМСАТа поэтому опишу по шагам
1.Выставляем глобальные переменные %CATALINA_HOME%, %JAVA_HOME%, %JRE_HOME%
2.В папку %CATALINE_HOME%\webapps добавляем папку myApp с нашим helloWorld.html
3.В %CATALINA_HOME%\bin запускаем startup.bat и переходим http://localhost:8080/MyApp
4. Насмотревшись на Hello World!!! запускаем в этой же папке shutdown.bat