## Тестовое задание <br> для курса Neoflex Java Development

Приложение `"Калькулятор отпускных"` <br>
Микросервис на SpringBoot + Java 11 с одним API: <br>
`GET` "/calculate"

Параметры запроса:
* `salary` - средняя зарплата за 12 месяцев
* `vac` - количество дней отпуска
* `start` - дата начала отпуска (необязательный параметр)
* `end` - дата окончания отпуска (необязательный параметр)

Возвращаемое значение: сумма отпускных или ошибка **400**
