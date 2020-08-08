# FinalProject - Your Tasks (Проект (приложение) - Твои задачи)

# Проект Your Tasks (Твои задачи)
## Общие сведения:
Your Tasks - простое и понятное приложение, в котором можно держать список своих задач, как в текстовом, так и в аудио варианте. Имеет поддержку 2 языков (EN и RU).  

***
## Что реализовано:  

Созданы два экрана (экран входа или регистрации, и главный экран приложения). В дизайне и разметке использованы элементы Material Design.  

При запуске приложения пользователь может либо зарегистрироваться, либо перейти в главный экран, введя уже существующие данные об аккаунте. Для регистрации(входа) необходимо указать почту и пароль (не менее 6 символов).  

При попадании в главный экран приложения, нас встречает Нижнее меню, которое отвечает за навигацию по функционалу приложения. По нажатии на пункты меню, содержимое экрана динамически меняется (реализация через Fragment) в зависимости от выбранного пункта меню.  

***
### Фрагмент TASKS (Задачи):  
Этот фрагмент установлен по умолчанию при попадании в главный экран приложения.   

Здесь пользователь может создавать задачу, нажав на кнопку «+» в нижнем правом углу (FloatingActionButton).   

Появится диалоговое окно, в котором необходимо ввести название, дату и время задачи.   

После положительного результата диалогового окна задача появится в списке (RecyclerView).   

Все задачи по умолчанию сортируются по дате из выполнения. То есть, задача, чей срок выполнения уже близок, будет находится выше остальных.  


Задачу можно отметить, как выполненную (поставив галочку в чекбоксе), удалить (свайп в любую сторону), редактировать (нажав на саму задачу появится диалоговое окно для редактирования).  
Так же, в случае удаления задачи, ее можно восстановить, нажав на появившуюся кнопку поворотной стрелочки (FloatingActionButton).  

Все удаляемые задачи хранятся в Стеке, как только он станет пуст, кнопка восстановления исчезнет.

***
### Фрагмент Profile (Профиль):  

Перейдя в профиль, пользователь может:
* Установить аватар (нажав на круглую форму вверху экрана)
* Добавить некоторые данные о себе (нажав на соответствующую кнопку внизу)
***
### Фрагмент Audio Notes (аудио заметки):  

Здесь пользователь может записать свою аудио заметку, нажав на кнопку микрофона в правом нижнем углу.   

Заметка так же появится в списке на экране (аналогия с текстовыми задачами). 

Все аудиофайлы сохраняются под названием в формате «ДАТА_ВРЕМЯ» и имеют расширение mp3.

Прослушать заметку можно по нажатию на кнопку зеленную PLAY слева от тайтла файла.

Свайпом можно удалить ненужную заметку.
### Последний элемент меню Exit (выход)   
отрабатывает функционал выхода их профиля и перекидывает пользователя на экран авторизации.
***
## Работа с данными:
Для работы с данными в приложении используются два варианта хранилищ.
1)	Для авторизации пользователя, хранения списка задач и данных об аккаунте используется FireBase (firestore, auth, storage).
2)	Для хранения аудио заметок используется внутреннее хранилище самого устройства пользователя.

