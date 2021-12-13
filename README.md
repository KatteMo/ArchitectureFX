# ArchitectureFX
Целью практической работы	является создание модели вычислительных систем (ВС) или ее компоненты на некотором уровне детализации, описывающей и имитирующей ее структуру и функциональность.

Каждый реальный объект ВС обладает огромной сложностью, определяемой множеством состояний, множеством внутренних и внешних связей, множеством анализируемых характеристик. Модель дает приближенное описание объекта с целью получения требуемых результатов с определенной точностью и достоверностью. Степень приближения модели к описываемому объекту может быть различной и зависит от требований задачи.

При необходимости исследования поведенческих характеристик ВС в процессе исследования выгодно использовать не сам объект, а его модель. Степень приближения модели к описываемому объекту может быть различной и зависит от требований задачи.

### Формализованная схема ВС
![image](https://user-images.githubusercontent.com/61520646/145860552-12f1b6b1-aab2-4cf0-bc36-7892e6e7769e.png)

### Краткое описание задачи 
1) ИБ – бесконечный источник
2) ИЗ1 - Пуассоновский закон распределения заявок
3) ПЗ2 - равномерный закон распределения времени обслуживания
4) Д1ОЗ1 – заполнение буферной памяти по кольцу.
5) Д1ОО3 – отказ по самой старой заявке в буфере
6) Д2П1 – приоритет прибора по номеру
7) Д2Б3 – выбор из буфера по кольцу
8) ОР1 – сводная таблица результатов (отражение результатов после сбора статистики)
9) ОД3 — временные диаграммы, текущее состояние (динамическое отражение результатов) .

В данном проекте был описан вариант работы ВС с определенными параметрами. Язык реализации - Java с использованием JavaFX для консольного вывода результатов. Реализованы автоматический и пошаговый режимы.

### Вывод результатов автоматического режима 
![image](https://user-images.githubusercontent.com/61520646/145860823-560ff64f-e56a-4f74-8f44-daa5940013a4.png)

### Вывод результатов пошагового режима
![image](https://user-images.githubusercontent.com/61520646/145860843-dc45a89c-7955-4ae4-b395-5c021fbe1ad0.png)