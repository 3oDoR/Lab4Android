# Лабараторная работа №4. RecyclerView.

# Цели
1.Ознакомиться с принципами работы adapter-based views.
2.Получить практические навки разработки адаптеров для view
# Задачи
В вашем распоряжении имеется библиотека, предоставляющая программный доступ к записям в формате bibtex. Библиотека имеет 2 режима работы: normal и strict. В strict mode работает искусственное ограничение: в памяти нельзя хранить более name.ank.lab4.BibConfig#maxValid=20 записей одновременно. При извлечении maxValid+1й записи 1я извелеченная запись становится невалидной (при доступе к полям кидаются исключения). Это ограничение позволит быстрее выявлять ошибки при работе с RecyclerView и адаптерами.
# Выполнение работы

## Задача 1. Знакомсотво с библиотекой (unit test)
В strict mode работает искусственное ограничение: в памяти нельзя хранить более name.ank.lab4.BibConfig#maxValid=20 записей одновременно. При извлечении maxValid+1й записи 1я извелеченная запись становится невалидной (при доступе к полям кидаются исключения).```java.lang.IllegalStateException: This object has already been invalidated. myOrder=1, latestOrder=21```.  
Суть этого теста(strictModeThrowsException) в том, что при включённом strict режиме при хранении более 20 записей бросается исключение. В тесте показано как при 21 записи бросается исключение. 
#### Листинг 1. strictModeThrowsException
```
  @Test
  public void strictModeThrowsException() throws IOException {
    BibDatabase database = openDatabase("/mixed.bib");
    BibConfig cfg = database.getCfg();
    cfg.strict = true;
    boolean flag = false;

    BibEntry first = database.getEntry(0);
    for (int i = 0; i < cfg.maxValid + 1; i++) {
      BibEntry unused = database.getEntry(0);
      try {
        assertNotNull("Should not throw any exception @" + i, first.getType());
      } catch (Exception exception) {
        flag = true;
      }
    }
    assertTrue(flag);
  }
```
В shuffle mode работает перемешка записей. Для выполнения теста был создан новый bib файл, который содержит 4 разных типа записей.
#### Листинг 2. shuffle.bib
```
@ARTICLE{MFP_62_ARTICLE_2019,
  AUTHOR = {Marzi, C. and Ferro, M. and Pirrelli, V.}
}
@INPROCEEDINGS{VLFS_102_INPROCEEDINGS_2008,
  AUTHOR = {Valenza, G. and Lanatà, A. and Ferro, M. and Scilingo, E.P.}
}
@TECHREPORT{PFC_47_TECHREPORT_2012,
  AUTHOR = {Pirrelli, V. and Ferro, M. and Calderone, B.}
}
@UNPUBLISHED{NTFMP_98_UNPUBLISHED_2020__submitted,
  AUTHOR = {Nadalini, A. and Taxitari, L. and Ferro, M. and Marzi, C. and Pirrelli, V.}
}
```
Я решил 20 раз попробовать прочитать файл из 4 записей с перемешиванием и если за 20 раз первой записью окажеться нужный мне тип UNPUBLISHED, то перемешивание сработало.
#### Листинг 3. shuffleFlag
```
    @Test
    public void shuffleFlag() throws IOException {

      BibConfig cfg = new BibConfig();
      BibDatabase database;
      cfg.shuffle = true;
      boolean flag = false;


      for (int it = 0; it < 20; it++) {
        database = openDatabase("/shuffle.bib");
        if (database.getEntry(0).getType() == Types.UNPUBLISHED) {
          flag = true;
        }
      }
      assertTrue(flag);
    }
}
```
Далее использовалась команда ./gradlew build после чего появились  результаты сборки по пути build/libs/biblib.jar.
![alt text]( https://github.com/3oDoR/Lab4Android/blob/main/png/1.png "./gradlew build")
#### Задача 2. Знакомство с RecyclerView.
Для решения задачи была добавлена библиотека biblib, добавлен файл articles.bib(res/raw/articles.bib).
В layout activity_main лобавлен RecyclerView.
#### Листинг 4. activity_main.xml
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    >

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>
```
bibilib_entry - layout для отображения записей содержащий пять textView с выбранным мною параметрамми для отображения.
#### Листинг 5. biblib_entry.xml
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:padding="16dp"
    android:background="#87CEEB">

    <TextView
        android:id="@+id/title"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="@android:color/white"
        android:textSize="20sp"
        tools:text="Title" />

    <TextView
        android:id="@+id/author"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:layout_marginBottom="10dp"
        android:textColor="@android:color/white"
        android:textSize="16sp"
        tools:text="Author" />

    <TextView
        android:id="@+id/JournalAndYear"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="@android:color/white"
        android:textSize="16sp"
        tools:text="Journal and year" />

    <TextView
        android:id="@+id/url"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="@android:color/white"
        android:textSize="16sp"
        tools:text="URL" />

    <TextView
        android:id="@+id/pages"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="@android:color/white"
        android:textSize="16sp"
        tools:text="Pages"
        android:layout_gravity="bottom|left"
         android:paddingBottom="50dp"/>

</LinearLayout>
```
Теперь добавим MainActivity и Adapter.

#### Листинг 6. Main_Activity.java
```
public class MainActivity extends AppCompatActivity {
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        InputStream articles = getResources().openRawResource(R.raw.articles);
        LinearLayoutManager viewManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(viewManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration( binding.recyclerView.getContext(), viewManager.getOrientation());
        binding.recyclerView.addItemDecoration(itemDecoration);
        binding.recyclerView.addItemDecoration(itemDecoration);
        try {
            adapter = new Adapter(articles);
        } catch (IOException e) {
            e.printStackTrace();
        }
        binding.recyclerView.setAdapter(adapter);
    }
}
```
#### Листинг 7. Adapter.java

```
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    BibDatabase database;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        TextView journalAndYear;
        TextView url;
        TextView pages;


        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            journalAndYear = view.findViewById(R.id.JournalAndYear);
            url = view.findViewById(R.id.url);
            pages = view.findViewById(R.id.pages);
        }
    }

    Adapter(InputStream articles) throws IOException {
        InputStreamReader reader = new InputStreamReader(articles);
        database = new BibDatabase(reader);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BiblibEntryBinding binding = BiblibEntryBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new ViewHolder(binding.getRoot());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        BibEntry entry = database.getEntry(position);
        viewHolder.title.setText(entry.getField(Keys.TITLE));
        viewHolder.author.setText("Authors : " + entry.getField(Keys.AUTHOR));
        viewHolder.journalAndYear.setText("Journal : " + entry.getField(Keys.JOURNAL) + " , " + entry.getField(Keys.YEAR));
        viewHolder.url.setText("URL : " + entry.getField(Keys.URL));
        viewHolder.pages.setText("Pages : " + entry.getField(Keys.PAGES));

    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
}
```
#### Резултат работы программы
![alt text]( https://github.com/3oDoR/Lab4Android/blob/main/png/2.png "Резултат работы программы")
#### Задача 3. Бесконечный список.

#### Листинг 7. Adapter.java

В методе onBindViewHolder() указываем не просто позицию, а берем остаток от деления на размер базы данных
В методе getItemCount() возвращаем Integer.MAX_VALUE
```
@SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        BibEntry entry = database.getEntry(position % database.size());

        viewHolder.title.setText(entry.getField(Keys.TITLE));
        viewHolder.author.setText("Authors : " + entry.getField(Keys.AUTHOR));
        viewHolder.journalAndYear.setText("Journal : " + entry.getField(Keys.JOURNAL) + " , " + entry.getField(Keys.YEAR));
        viewHolder.url.setText("URL : " + entry.getField(Keys.URL));
        viewHolder.pages.setText("Pages : " + entry.getField(Keys.PAGES));


    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }
```
#### Выводы
1 задача - 30 минут - довольно простое задание + готовые примеры тестов, которые показывают как использовать библиотеку. 
2 задача - 2 часа - в основном разбирался с классом “Adapter” т.к. “MainActivity” был готовый в примере и почти полностью подходил под нашу задачу. 
3 задача - 1 час - Исходный код из второй задачи   был модифицирован и теперь список выводимых записей стал бесконечным. 
