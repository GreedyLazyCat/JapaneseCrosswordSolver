## Алгоритм решения японских кроссвордов
### Основной подход
Основной подход, который я использую - это перебор вариаций строк с отсечением вариантов, грубо нарушающих строение столбцов до перебираемой в данный момент строки, где-то это значительно ускоряет перебор, например в кроссворде crosses/cup.json без отсечений решение занимает сильно больше часа, с отсечениями - минута(сейчас уже 20 секунд примерно). Иными словами я использую метод ветвей и границ. Так же перед началом решения закрашиваются квадраты в столбцах, где это можно сделать однозначно: пересечения, однозначно закрашиваемые столбцы, в строках закрашиваются только однозначные варианты, все это так же может сократить количество вариантов для строк.
Примеры кроссвордов, на которых реально происходили ускорения - это кроссворды в папке crosses, конечно, получается, что прошу поверить на слово, но в коде в методе isColValid класса JCross есть место, помеченное комментарием, которое можно закомментировать. У кроссвородов crosses/tea_pot_fast.json и crosses/bug_really_slow.json есть реальные ощутимые ускорения в связи с этим местом.
### Случайная генерация
Лучше генерировать кроссворды размером не больше 14x14, иначе возникает переполнение при решении.
### Обоснование для заказчика
Решение японского кроссворда относится к классу NP трудных задач, что означает отсутствие доказано наиболее эффективного решения этой задачи. Количество вариантов решений значительно растет при увеличении размеров входных данных, однако небольшие задачи вполне решаемы, за более менее адекватное время. Кроссворды решаемые данным алгоритмом подойдут для начальной тренировки своих навыков решения кроссвордов, а программа сможет дать решение на такой кроссворд для проверки.