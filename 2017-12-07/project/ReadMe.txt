﻿#########################                       #########################
                        suppressionCheckerProject
#########################                       #########################

# Если программа еще не собрана
    Перед первым запуском программы ее следует собрать с помощью Maven. 
    Для этого в корневой папке проекта необходимо вызвать окно терминала и выполнить команду mvn install, либо запустить build.bat.

# Запуск программы
    Программа поставляется в виде файла с расширением .jar
    Программа запускается из консоли, либо с помощью start.bat.

# Работа с программой
    Для запуска программы необходимо вызвать консоль, после указания имени файла программы следует указать в качестве аргументов:
    <> полное имя файла suppressions.xml,
    <> путь к требуемой директории.
    По окончании работы программа закрывается автоматически. 

# Результаты работы программы
    В процессе работы программа не выводит никакие сообщения в консоль кроме сообщений об ошибках в работе.
    Если работа программы завершилась успешно, никаких сообщений быть не должно.
    В результате работы программы создается папка "output", в которой в виде тестовых файлов лежат результаты работы программы.
    Некоторые результаты могут сохраняться в корневую папку, игнорируя папку "output".
    В самих файлах указываются следующие данные:
    <> имя автора модуля, выполнявшего проверку наличия файлов,
    <> время работы парсера xml файла,
    <> время работы парсера директории,
    <> время работы поиска удаленных файлов,
    <> список найденных удаленных файлов
    Помимо текстовых файлов в конце работы программа генерирует окно с таблицей, в которой отражены вышеперечисленные данные.
