# SoftTecoDemo

В приложении есть 2 экрана.
На 1м экране присутствуют:
- кнопка для сохранения logcat в файл (вверху срава)
- картинка (любая, например картошка, но область в которой находится картинка должна занимать 3/5 высоты экрана). Для картинки нужно сделать любую анимацию, и отображать кнопку для сохранения logcat только после окончания анимации. Анимация стартует при старте прилаги и работает до того момента пока не получен респонс от сервера.
- под картинкой горизонтально прокручиваемый двустрочный список. Каждый элемент списка - id поста. Под id в одну строчку (можно сокращённо) title поста. В каждый момент времени должно отображаться не более 6 элементов (2 ряда по 3 элемента).
- под прокручиваемым списком должен отображаться точечный индикатор, отображающий текущую позицию списка.
- при нажатии на любой элемент списка происходит переход на второй экран.
Список постов приложение должно получать вот отсюда: http://jsonplaceholder.typicode.com/posts


На 2м экране:
- в самом верху (в тулбаре) отображаетсяс строка "contact #X" где X - id контакта.
- в верхней левой части отображается id поста, на который было нажато на предыдущем экране.
- справа от id поста распологаются имя и никнейм юзера (2 строки)
- ниже id поста отображается емейл, веб сайт, телефон и город пользователя.
- по нажатию на емейл, веб сайт и телефон должны открыться соответствующие приложения на телефоне. 
При нажатии на город должна открыться Google Map с текущей координатой равной той, что у данного юзера в указано в профайле.
- в самом низу с правой стороны есть кнопка сохранения инфы о юзере в базу данных.
Детали пользователя приложение получает вот отсюда: http://jsonplaceholder.typicode.com/users/X , где X - целочисленный Id 
