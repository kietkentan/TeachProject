
# Multilingual & Multi-dimensional

Tùy chọn ngôn ngữ và đa kích thước trong ứng dụng
## Documentation

[1. Thêm resource](#AddResource)

[2. Lớp hỗ trợ](#AddClass)

[3. Thiết lập ngôn ngữ](#LoadLang)

[4. Listener](#Listener)

<a name="AddResource"></a>
### 1. Thêm file resource
- Thêm các [file kích thước](https://github.com/kietkentan/TeachProject/tree/multilple_version/app/src/main/res) vào dự án. Kích thước là các file `dimens.xml` trong value. Mặc định hệ thống sẽ lấy kích thước từ value. Nếu chiều ngang màn hình thay đổi thì sẽ lấy trong value tương ứng, ví dụ màn hình có chiều ngang 420dp thì sẽ lấy trong value-w420dp.
    - Có thể sao chép vào hoặc tạo thủ công
- Đặt ngôn ngữ mặc định của android là `vi` (tiếng Việt) trong [value\strings.xml](https://github.com/kietkentan/TeachProject/blob/multilple_version/app/src/main/res/values/strings.xml)
```xml
<resources xmlns:tools="http://schemas.android.com/tools"
    tools:locale="vi">
    //...
 </resources>
```
- Thêm một bản dịch ngôn ngữ vào android: 
    - Chuột phải vào `res` -> New -> Android Resource File
    - Ở phần Avilable qualifiers ta chọn thêm Locale, tìm kiếm ngôn ngữ tương ứng

![Mặc định](https://github.com/kietkentan/TeachProject/assets/55453955/e5d50d6d-5cf7-4c06-8eb6-eccddb1310bd)
![Kích thước + Ngôn ngữ thay đổi](https://github.com/kietkentan/TeachProject/assets/55453955/e92ea1c2-945b-404e-836c-25c66289866d)
![image](https://github.com/kietkentan/TeachProject/assets/55453955/d1079952-90b8-4385-a2a3-821a0a6079ef)

<a name="AddClass"></a>
### 2. Thêm các lớp hỗ trợ trong [helper](https://github.com/kietkentan/TeachProject/tree/multilple_version/app/src/main/java/com/khtn/teachproject/helper) và [utils](https://github.com/kietkentan/TeachProject/tree/multilple_version/app/src/main/java/com/khtn/teachproject/utils)
Các lớp này hỗ trợ lưu và lấy thông tin cài đặt ngôn ngữ trong SharedPreferences
<a name="LoadLang"></a>
### 3. Thiết lập ngôn ngữ
- Tải cài đặt hệ thống, lưu ý thứ tự đặt dòng lệnh rất quan trọng
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Order is important ;)
    loadLanguage()
    setContentView(R.layout.activity_main)
    //...
}

//...

private fun loadLanguage() {
    LocaleHelper.loadLanguageConfig(this@MainActivity)
}
```
<a name="Listener"></a>
### 4. Thiết lập hành động chuyển đổi ngôn ngữ
- Thứ tự cập nhật Ui cũng rất quan trọng, nếu không sẽ không chạy đúng
```kotlin
button.setOnClickListener {
    //...
    // Change Locale
    isEnglish = checkEnglish()
    LocaleHelper.setLocale(
        this@MainActivity,
        if (isEnglish) SupportLanguage.VIETNAM.lang
        else SupportLanguage.ENGLISH.lang
    )
    updateUi()
}

private fun checkEnglish(): Boolean {
    val str = LocaleHelper.getLanguage(this@MainActivity)
    if (str == "en")
        return true
    return false
}

private fun updateUi() {
    // Order is important ;)
    finish()
    startActivity(intent)
    overridePendingTransition(0, 0)
}
```
