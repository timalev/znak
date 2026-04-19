package io.cordova.test2_6720b;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Languages {

    // Сюда добавляем язык в первую очередь

    private String[] Languages = {
            "ru",
            "en",
            "de",
            "zh",
            "es",
            "ar"
    };

    private Map<String, String> hashMap = new HashMap<>();
    private Map<String, Integer> hashMap2 = new HashMap<>();

   // private String lang = GetLang(Locale.getDefault().getLanguage());
    private String lang = GetLang("ar");

    public String MenuFriends()
    {

        hashMap.put("ru","Поиск анкет");
        hashMap.put("en","Search partners");
        hashMap.put("de","Suche nach Profilen");
        hashMap.put("zh","搜索个人资料");
        hashMap.put("es","Buscar perfiles");
        hashMap.put("ar","البحث في الملفات الشخصية");

        return hashMap.get(lang);
    }
    public String MenuIndex()
    {
        hashMap.put("ru","Моя анкета");
        hashMap.put("en","My profile");
        hashMap.put("de","Mein Profil");
        hashMap.put("zh","我的个人资料");
        hashMap.put("es","Mi perfil");
        hashMap.put("ar","ملفي الشخصي");

        return hashMap.get(lang);
    }
    public String MenuSignout()
    {
        hashMap.put("ru","Выход");
        hashMap.put("en","Sign out");
        hashMap.put("de","Ausloggen");
        hashMap.put("zh","退出");
        hashMap.put("es","Cerrar sesión");
        hashMap.put("ar","تسجيل الخروج");

        return hashMap.get(lang);
    }
    public String MenuComplain()
    {
        hashMap.put("ru","Пожаловаться");
        hashMap.put("en","Complain");
        hashMap.put("de","Beschweren");
        hashMap.put("zh","抱怨");
        hashMap.put("es","Quejarse");
        hashMap.put("ar","اكتب شكوى");

        return hashMap.get(lang);
    }
    public String MenuMessages()
    {
        hashMap.put("ru","Мои сообщения");
        hashMap.put("en","My messages");
        hashMap.put("de","Meine Nachrichten");
        hashMap.put("zh","我的帖子");
        hashMap.put("es","Mis mensajes");
        hashMap.put("ar","رسائلي الخاصة");

        return hashMap.get(lang);
    }
    public String ProfileChangegeo()
    {
        hashMap.put("ru","Изменить локацию");
        hashMap.put("en","Change location");
        hashMap.put("de","Standort ändern");
        hashMap.put("zh","更改位置");
        hashMap.put("es","cambiar de ubicación");
        hashMap.put("ar","تغيير الموقع");

        return hashMap.get(lang);
    }
    public String MenuLikes()
    {
        hashMap.put("ru","Мои лайки");
        hashMap.put("en","My likes");
        hashMap.put("de","Meine Vorlieben");
        hashMap.put("zh","我喜歡");
        hashMap.put("es","Mis gustos");
        hashMap.put("ar","ما يعجبني");

        return hashMap.get(lang);
    }
    public String EnterString()
    {
        hashMap.put("ru","Войти");
        hashMap.put("en","Enter");
        hashMap.put("de","Eingeben");
        hashMap.put("zh","進入");
        hashMap.put("es","Ingresar");
        hashMap.put("ar","يدخل");

        return hashMap.get(lang);
    }
    public String RegistrationString()
    {
        hashMap.put("ru","Регистрация");
        hashMap.put("en","Registration");
        hashMap.put("de","Anmeldung");
        hashMap.put("zh","登記");
        hashMap.put("es","Registro");
        hashMap.put("ar","تسجيل");

        return hashMap.get(lang);
    }
    public String LoginPassword()
    {
        hashMap.put("ru","Пароль");
        hashMap.put("en","Password");
        hashMap.put("de","Passwort");
        hashMap.put("zh","密碼");
        hashMap.put("es","contraseña");
        hashMap.put("ar","كلمة المرور");

        return hashMap.get(lang);
    }
    public String LoginString()
    {
        hashMap.put("ru","Email");
        hashMap.put("en","E-Mail");
        hashMap.put("de","E-Mail");
        hashMap.put("zh","電子郵件");
        hashMap.put("es","correo electrónico");
        hashMap.put("ar","بريد إلكتروني");

        return hashMap.get(lang);
    }

    public String LocationExample()
    {
        hashMap.put("ru","(Например 'Смоленск, ул Ленина')");
        hashMap.put("en","(For example 'New York, Broadway')");
        hashMap.put("de","(Zum Beispiel 'Berlin, Unter den Linden')");
        hashMap.put("zh","(例如'上海南京路')");
        hashMap.put("es","(Por ejemplo, 'Madrid, Gran Vía')");
        hashMap.put("ar","(على سبيل المثال، 'دبي، شارع الشيخ محمد بن راشد')");

        return hashMap.get(lang);
    }
    public String SetupLocationBtn()
    {
        hashMap.put("ru","Установить");
        hashMap.put("en","Save");
        hashMap.put("de","Speichern");
        hashMap.put("zh","節省");
        hashMap.put("es","Ahorrar");
        hashMap.put("ar","يحفظ");

        return hashMap.get(lang);
    }


    public String LocationText()
    {
        hashMap.put("ru","Местоположение");
        hashMap.put("en","Location");
        hashMap.put("de","Standort");
        hashMap.put("zh","地點");
        hashMap.put("es","Ubicación");
        hashMap.put("ar","موقع");

        return hashMap.get(lang);
    }
    public String  ProfileDelank()
    {
        hashMap.put("ru","Удалить анкету");
        hashMap.put("en","Delete profile");
        hashMap.put("de","Profil löschen");
        hashMap.put("zh","刪除個人資料");
        hashMap.put("es","Eliminar perfil");
        hashMap.put("ar","حذف الملف الشخصي");

        return hashMap.get(lang);
    }
    public String MenuAbout()
    {
        hashMap.put("ru","О приложении");
        hashMap.put("en","About app");
        hashMap.put("de","Über App");
        hashMap.put("zh","关于申请");
        hashMap.put("es","Sobre la aplicación");
        hashMap.put("ar","حول التطبيق");

        return hashMap.get(lang);
    }
    public String TexteditHint()
    {
        hashMap.put("ru","Новое сообщение");
        hashMap.put("en","Compose a message");
        hashMap.put("de","eine Nachricht schreiben");
        hashMap.put("zh","新消息");
        hashMap.put("es","Nuevo mensaje");
        hashMap.put("ar","رسالة جديدة");

        return hashMap.get(lang);
    }
    public String ButtonSend()
    {
        hashMap.put("ru","Отправить");
        hashMap.put("en","Send");
        hashMap.put("de","Senden");
        hashMap.put("zh","发送");
        hashMap.put("es","Para enviar");
        hashMap.put("ar","لإرسال");

        return hashMap.get(lang);
    }
    public String ProfileCheckName()
    {
        hashMap.put("ru","имя");
        hashMap.put("en","name");
        hashMap.put("de","der name");
        hashMap.put("zh","这个名字");
        hashMap.put("es","primer nombre");
        hashMap.put("ar","الاسم");

        return hashMap.get(lang);
    }
    public String ProfileCheckPhoto()
    {
        hashMap.put("ru","фото");
        hashMap.put("en","photo");
        hashMap.put("de","foto");
        hashMap.put("zh","照片");
        hashMap.put("es","foto");
        hashMap.put("ar","صورة شخصية");

        return hashMap.get(lang);
    }
    public String ProfileCheckAge()
    {
        hashMap.put("ru","возраст (18+)");
        hashMap.put("en","age (18+)");
        hashMap.put("de","alter (18+)");
        hashMap.put("zh","年龄 (18+)");
        hashMap.put("es","la edad (18+)");
        hashMap.put("ar"," (18+)عمر");

        return hashMap.get(lang);
    }
    public String ProfileCheckSex()
    {
        hashMap.put("ru","пол");
        hashMap.put("en","sex");
        hashMap.put("de","geschlecht");
        hashMap.put("zh","性。");
        hashMap.put("es","sexo");
        hashMap.put("ar","الجنس");

        return hashMap.get(lang);
    }
    public String ProfileTakePicture()
    {
        hashMap.put("ru","Сделать фото");
        hashMap.put("en","Take a picture");
        hashMap.put("de","Mach ein Foto");
        hashMap.put("zh","拍一张照片");
        hashMap.put("es","Toma una foto");
        hashMap.put("ar","التقط صوره");

        return hashMap.get(lang);
    }
    public String ProfileSelectPhoto()
    {
        hashMap.put("ru","Выбрать фото");
        hashMap.put("en","Select photo");
        hashMap.put("de","Foto auswählen");
        hashMap.put("zh","选择一张照片");
        hashMap.put("es","Elige una foto");
        hashMap.put("ar","اختر صورة");

        return hashMap.get(lang);
    }
    public String ProfileCancelPhoto()
    {
        hashMap.put("ru","Отменить");
        hashMap.put("en","Cancel");
        hashMap.put("de","Stornieren");
        hashMap.put("zh","取消");
        hashMap.put("es","Cancelar");
        hashMap.put("ar","إلغاء");

        return hashMap.get(lang);
    }
    public String ProfileWarning()
    {
        hashMap.put("ru","Внимание!");
        hashMap.put("en","Warning!");
        hashMap.put("de","Achtung!");
        hashMap.put("zh","警告！");
        hashMap.put("es","Atencion!");
        hashMap.put("ar","تحذير!");

        return hashMap.get(lang);
    }
    public String ProfileRequired()
    {
        hashMap.put("ru","Нужно добавить: ");
        hashMap.put("en","Required to fill: ");
        hashMap.put("de","Erforderlich zum Ausfüllen: ");
        hashMap.put("zh","需要添加： ");
        hashMap.put("es","Necesidad de rellenar: ");
        hashMap.put("ar","تحتاج إلى إضافة: ");

        return hashMap.get(lang);
    }
    public String ProfileActive()
    {
        hashMap.put("ru","Активна");
        hashMap.put("en","Active");
        hashMap.put("de","Aktiv");
        hashMap.put("zh","活性");
        hashMap.put("es","Activo");
        hashMap.put("ar","نشط");

        return hashMap.get(lang);
    }
    public String ProfileTextviewName()
    {
        hashMap.put("ru","Имя");
        hashMap.put("en","Name");
        hashMap.put("de","der Name");
        hashMap.put("zh","名字");
        hashMap.put("es","Primer nombre");
        hashMap.put("ar","الاسم الأول");

        return hashMap.get(lang);
    }
    public String ProfileEditName()
    {
        hashMap.put("ru","Ваше имя");
        hashMap.put("en","Your name");
        hashMap.put("de","Ihr Name");
        hashMap.put("zh","你的名字");
        hashMap.put("es","Tu nombre");
        hashMap.put("ar","اسمك");

        return hashMap.get(lang);
    }

    public String ProfileTextviewAge()
    {
        hashMap.put("ru","Возраст");
        hashMap.put("en","Age");
        hashMap.put("de","Alter");
        hashMap.put("zh","年龄");
        hashMap.put("es","La edad");
        hashMap.put("ar","عمر");

        return hashMap.get(lang);
    }
    public String ProfileEditAge()
    {
        hashMap.put("ru","Ваш возраст");
        hashMap.put("en","Your age");
        hashMap.put("de","Dein Alter");
        hashMap.put("zh","你的年龄");
        hashMap.put("es","Tu edad");
        hashMap.put("ar","عمرك");


        return hashMap.get(lang);
    }
    public String ProfileTextviewSex()
    {
        hashMap.put("ru","Пол");
        hashMap.put("en","Sex");
        hashMap.put("de","Geschlecht");
        hashMap.put("zh","性性别");
        hashMap.put("es","Sexo");
        hashMap.put("ar","الجنس.");


        return hashMap.get(lang);
    }
    public String ProfileTextviewMale()
    {
        hashMap.put("ru","М");
        hashMap.put("en","M");
        hashMap.put("de","Mann");
        hashMap.put("zh","一个男人");
        hashMap.put("es","M");
        hashMap.put("ar","ذكر");

        return hashMap.get(lang);
    }
    public String ProfileTextviewFemale()
    {
        hashMap.put("ru","Ж");
        hashMap.put("en","F");
        hashMap.put("de","Frau");
        hashMap.put("zh","一个女人");
        hashMap.put("es","F");
        hashMap.put("ar","أنثى");

        return hashMap.get(lang);
    }
    public String ProfileAddPhoto()
    {
        hashMap.put("ru","Добавление фотографии");
        hashMap.put("en","Adding a photo");
        hashMap.put("de","Ein Foto hinzufügen");
        hashMap.put("zh","添加照片");
        hashMap.put("es","Añadiendo una foto");
        hashMap.put("ar","إضافة صورة");

        return hashMap.get(lang);
    }
    public String ProfileActtext()
    {
        hashMap.put("ru","Ваша анкета не активна и не доступна для поиска.");
        hashMap.put("en","Your profile is not active and is not searchable.");
        hashMap.put("de","Ihr Profil ist nicht aktiv und kann nicht durchsucht werden.");
        hashMap.put("zh","您的个人资料未激活且无法搜索。");
        hashMap.put("es","Tu perfil no está activo y no se puede buscar.");
        hashMap.put("ar","ملفك التعريفي غير نشط ولا يمكن البحث فيه.");

        return hashMap.get(lang);
    }
    public String MymessagesWas()
    {
        hashMap.put("ru","Был(а) ");
        hashMap.put("en","Was ");
        hashMap.put("de","War ");
        hashMap.put("zh","是 ");
        hashMap.put("es","Estaba ");
        hashMap.put("ar","كان ");

        return hashMap.get(lang);
    }
    public String MymessagesOnline()
    {
        hashMap.put("ru","Онлайн");
        hashMap.put("en","Online");
        hashMap.put("de","Online");
        hashMap.put("zh","现在在现场");
        hashMap.put("es","En linea");
        hashMap.put("ar","الآن في الموقع");

        return hashMap.get(lang);
    }
    public String MymessagesToday()
    {
        hashMap.put("ru","сегодня в");
        hashMap.put("en","today at");
        hashMap.put("de","heute um");
        hashMap.put("zh","今天在");
        hashMap.put("es","hoy en");
        hashMap.put("ar","اليوم في");

        return hashMap.get(lang);
    }
    public String MymessagesYestoday()
    {
        hashMap.put("ru","вчера в");
        hashMap.put("en","yesterday at");
        hashMap.put("de","gestern um");
        hashMap.put("zh","昨天在");
        hashMap.put("es","ayer en");
        hashMap.put("ar","أمس في");

        return hashMap.get(lang);
    }
    public String LoginBantext()
    {
        hashMap.put("ru","Ваша анкета на рассмотрении..");
        hashMap.put("en","Your profile is being verified ..");
        hashMap.put("de","Ihr Profil wird überprüft..");
        hashMap.put("zh","您的个人资料正在审核中..");
        hashMap.put("es","Su perfil está bajo revisión..");
        hashMap.put("ar","ملفك الشخصي قيد المراجعة ..");

        return hashMap.get(lang);
    }
    public String AboutTitle()
    {
        hashMap.put("ru","О приложении");
        hashMap.put("en","About app");
        hashMap.put("de","Über die Anwendung");
        hashMap.put("zh","关于申请");
        hashMap.put("es","Sobre la aplicación");
        hashMap.put("ar","حول التطبيق");

        return hashMap.get(lang);
    }
    public String AboutBody()
    {
        hashMap.put("ru","Текущая версия приложения: ");
        hashMap.put("en","Current app version: ");
        hashMap.put("de","Aktuelle version der Anwendung: ");
        hashMap.put("zh","当前版本的应用程序: ");
        hashMap.put("es","Versión actual de la aplicación: ");
        hashMap.put("ar","الإصدار الحالي من التطبيق: ");

        return hashMap.get(lang);
    }

    public String RassilkaLookphoto()
    {
        hashMap.put("ru","Смотреть фото");
        hashMap.put("en","See photo");
        hashMap.put("de","Siehe Foto");
        hashMap.put("zh","见照片");
        hashMap.put("es","Ver foto");
        hashMap.put("ar","عرض صورة الملف الشخصي");

        return hashMap.get(lang);
    }
    public String RassilkaNewprofile()
    {
        hashMap.put("ru","Новая анкета");
        hashMap.put("en","New profile");
        hashMap.put("de","Neues profil");
        hashMap.put("zh","新成员");
        hashMap.put("es","Nuevo perfil");
        hashMap.put("ar","ملف تعريف جديد");

        return hashMap.get(lang);
    }


    public String TitleProfile()
    {
        hashMap.put("ru","Моя анкета");
        hashMap.put("en","My profile");
        hashMap.put("de","Mein profil");
        hashMap.put("zh","我的细节");
        hashMap.put("es","Mi perfil");
        hashMap.put("ar","ملفي الشخصي");

        return hashMap.get(lang);
    }
    public String TitleMymessages()
    {
        hashMap.put("ru","Мои сообщения");
        hashMap.put("en","My messages");
        hashMap.put("de","Meine nachrichten");
        hashMap.put("zh","我的信");
        hashMap.put("es","Mis mensajes");
        hashMap.put("ar","رسائلي الخاصة");

        return hashMap.get(lang);
    }
    public String TitleSearching()
    {
        hashMap.put("ru","Поиск анкет");
        hashMap.put("en","Searching parthers");
        hashMap.put("de","Suche nach profilen");
        hashMap.put("zh","搜索个人资料");
        hashMap.put("es","Buscar perfiles");
        hashMap.put("ar","البحث في الملفات الشخصية");

        return hashMap.get(lang);
    }
    public String SearchText()
    {
        hashMap.put("ru","Идет поиск анкет в Вашем радиусе..");
        hashMap.put("en","Searching profiles in your radius..");
        hashMap.put("de","Suchen von Profilen in Ihrem Radius..");
        hashMap.put("zh","搜索您半徑範圍內的輪廓");
        hashMap.put("es","Buscar perfiles en tu radio..");
        hashMap.put("ar","البحث في ملفات التعريف الخاصة بك في دائرة نصف قطرها");

        return hashMap.get(lang);
    }

    public String NoMessages()
    {
        hashMap.put("ru","Нет сообщений");
        hashMap.put("en","No messages");
        hashMap.put("de","Keine nachrichten");
        hashMap.put("zh","没有信件");
        hashMap.put("es","No mensajes");
        hashMap.put("ar","لا توجد مشاركات");

        return hashMap.get(lang);
    }
    public String PhotoAdded()
    {
        hashMap.put("ru","Фото успешно добавлено!");
        hashMap.put("en","Photo successfully added!");
        hashMap.put("de","Foto erfolgreich hinzugefügt!");
        hashMap.put("zh","照片成功添加！");
        hashMap.put("es","Foto añadida con éxito!");
        hashMap.put("ar","تمت إضافة الصورة بنجاح!");



        return hashMap.get(lang);
    }
    public String NewMessage()
    {
        hashMap.put("ru","Вам новое сообщение!");
        hashMap.put("en","New message!");
        hashMap.put("de","Neue nachricht!");
        hashMap.put("zh","你收到了一封信！");
        hashMap.put("es","¡Ha llegado un nuevo mensaje!");
        hashMap.put("ar","وصلت رسالة جديدة!");

        return hashMap.get(lang);
    }
    public String NewLike()
    {
        hashMap.put("ru","Вас лайкнули!");
        hashMap.put("en","Like you!");
        hashMap.put("de","Wie du!!");
        hashMap.put("zh","像你一樣！");
        hashMap.put("es","Como tu");
        hashMap.put("ar","مثلك!");

        return hashMap.get(lang);
    }

    public String NoUsers()
    {
        hashMap.put("ru","Пока в вашем радиусе никого нет, попробуйте зайти в другой раз..");
        hashMap.put("en","No one is in your radius, try another time..");
        hashMap.put("de","Versuchen Sie es zu einem anderen Zeitpunkt, solange sich niemand in Ihrem Umkreis befindet..");
        hashMap.put("zh","没有人在附近，再试一次..");
        hashMap.put("es","Mientras que nadie está cerca de ti, prueba en otro momento..");
        hashMap.put("ar","نما لا يوجد أحد في دائرة نصف قطرها ، حاول مرة أخرى.");


        return hashMap.get(lang);
    }
    public String GoogleEnterLink()
    {
        hashMap.put("ru","\uD83D\uDD10 Войти через Google");
        hashMap.put("en","\uD83D\uDD10 Sign in with Google");
        hashMap.put("de","\uD83D\uDD10 Mit Google anmelden");
        hashMap.put("zh","\uD83D\uDD10 使用 Google 登入");
        hashMap.put("es","\uD83D\uDD10 Iniciar sesión con Google");
        hashMap.put("ar","سجّل الدخول باستخدام حساب جوجل\uD83D\uDD10 ");


        return hashMap.get(lang);
    }

    public String NoMoreUsers()
    {
        hashMap.put("ru","Больше анкет нет.");
        hashMap.put("en","No more profiles");
        hashMap.put("de","Keine Profile mehr");
        hashMap.put("zh","沒有更多個人資料");
        hashMap.put("es","No mas perfiles");
        hashMap.put("ar","لا مزيد من الملفات الشخصية");


        return hashMap.get(lang);
    }


    public Integer ImagesUploadPhoto()
    {
        hashMap2.put("ru",R.drawable.kamran2_ru);
        hashMap2.put("en",R.drawable.kamran2_en);
        hashMap2.put("de",R.drawable.kamran2_de);
        hashMap2.put("zh",R.drawable.kamran2_zh);
        hashMap2.put("es",R.drawable.kamran2_es);
        hashMap2.put("ar",R.drawable.kamran2_ar);

        return hashMap2.get(lang);
    }
    public Integer ImagesForward()
    {
        hashMap2.put("ru",R.drawable.forward_ru);
        hashMap2.put("en",R.drawable.forward_en);
        hashMap2.put("de",R.drawable.forward_de);
        hashMap2.put("zh",R.drawable.forward_zh);
        hashMap2.put("es",R.drawable.forward_es);
        hashMap2.put("ar",R.drawable.forward_ar);

        return hashMap2.get(lang);
    }

    public Integer SwipeNext()
    {
        hashMap2.put("ru",R.layout.tinder_swipe_next_msg_view_ru);
        hashMap2.put("en",R.layout.tinder_swipe_in_msg_view_en);
        hashMap2.put("de",R.layout.tinder_swipe_in_msg_view_de);
        hashMap2.put("zh",R.layout.tinder_swipe_in_msg_view_zh);
        hashMap2.put("es",R.layout.tinder_swipe_in_msg_view_es);
        hashMap2.put("fr",R.layout.tinder_swipe_in_msg_view_fr);
        hashMap2.put("ar",R.layout.tinder_swipe_in_msg_view_ar);

        return hashMap2.get(lang);
    }

    public Integer SwipeIn()
    {
        hashMap2.put("ru",R.layout.tinder_swipe_in_msg_view_ru);
        hashMap2.put("en",R.layout.tinder_swipe_in_msg_view_en);
        hashMap2.put("de",R.layout.tinder_swipe_in_msg_view_de);
        hashMap2.put("zh",R.layout.tinder_swipe_in_msg_view_zh);
        hashMap2.put("es",R.layout.tinder_swipe_in_msg_view_es);
        hashMap2.put("fr",R.layout.tinder_swipe_in_msg_view_fr);
        hashMap2.put("ar",R.layout.tinder_swipe_in_msg_view_ar);

        return hashMap2.get(lang);
    }
    public Integer SwipeOut()
    {
        hashMap2.put("ru",R.layout.tinder_swipe_out_msg_view_ru);
        hashMap2.put("en",R.layout.tinder_swipe_out_msg_view_en);
        hashMap2.put("de",R.layout.tinder_swipe_out_msg_view_de);
        hashMap2.put("zh",R.layout.tinder_swipe_out_msg_view_zh);
        hashMap2.put("es",R.layout.tinder_swipe_out_msg_view_es);
        hashMap2.put("fr",R.layout.tinder_swipe_out_msg_view_fr);
        hashMap2.put("ar",R.layout.tinder_swipe_out_msg_view_ar);

        return hashMap2.get(lang);
    }











    private String GetLang(String lang)
    {
        for (int i = 0; i < Languages.length; i++) {
            if (Languages[i].equals(lang))
            {
                return Languages[i];
            }
        }
        return "en";

    }
}
