
# Dagger-Hilt

Thêm Dagger-Hilt vào project
## Documentation

[1. Thêm thư viện](#AddLib)

[2. Enable plugin](#Enable)

[3. Tạo lớp Application](#AddApplication)

[4. Chèn phụ thuộc](#AddDependencies)

[5. Module Hilt](#Module)

[6. Note](#Note)


<a name="AddLib"></a>
### 1. Thêm thư viện, bật sửa lỗi kapt trong `build.gradle` cấp Module

```kotlin
plugins {
    //...
    // Add this
    id 'kotlin-kapt'
    id 'com.google.dagger.hilt.android'
}

dependencies { 
    //...
    // Dagger-Hilt & kapt
    implementation 'com.google.dagger:hilt-android:2.45'
    kapt 'com.google.dagger:hilt-compiler:2.45'
}

// Add this
hilt {
    enableAggregatingTask = true
}

// Add this
kapt {
    correctErrorTypes = true
}
```
- Chú ý: Nếu có hiện lỗi `app:checkDebugAarMetadata` thì nâng sdk lên 
```kotlin
android {
    //...
    compileSdk 33 // up to this (32 -> 33)

    defaultConfig {
        //...
        targetSdk 33 // up to this (32 to 33)
        //...
    }
}
```
- Chú ý: Nếu có hiện lỗi `app:checkdebugduplicateclasses` thì giảm version core androix xuống để phù hợp với phiên bản kotlin
```kotlin
dependencies {
    // core 1.9.0 ứng với kotlin '1.7.10'
    implementation 'androidx.core:core-ktx:1.9.0'
    //...
}
```
<a name="Enable"></a>
### 2. Enable plugin Hilt trong `build.gradle` cấp Project
```kotlin
buildscript {
    dependencies {
        // Add this
        classpath 'com.google.dagger:hilt-android-gradle-plugin:2.45'
    }
}

plugins {
    //...
    // Apply hilt to project
    id 'com.google.dagger.hilt.android' version '2.44' apply false
}
```
- Chú ý: Nếu có lỗi thì thêm dòng bên dưới vào `gradle.properties`
```kotlin
//...
android.enableJetifier = true
```
<a name="AddApplication"></a>
### 3. Tiến hành tạo lớp `Application` có tên `MyApplication` vào dự án
- Bắt buộc phải có lớp `Application` đóng vai trò nơi chứa các thành phần phụ thuộc của ứng dụng
-  Tên lớp không bắt buộc giống nhưng phải kế thừa từ lớp `Application` hoặc từ 1 lớp đã kế thừa lớp `Application`
- Thêm annotation *`@HiltAndroidApp`* vào dòng trước tên lớp
```kotlin
@HiltAndroidApp
class MyApplication: Application()
```
- Trong `AndroidManifest.xml` cần thêm tên `Application` mới thêm vào
```kotlin
<manifest
    //...
    // Add package
    package="com.khtn.myapplication"
    >

    <application
        // Add application name
        android:name=".MyApplication"
        //...
        >

        //...
    </application>
</manifest>
```
<a name="AddDependencies"></a>
### 4. Chèn phụ thuộc
- *Hilt* hỗ trợ các lớp sau:
```
Application (bằng cách sử dụng @HiltAndroidApp)
ViewModel (bằng cách sử dụng @HiltViewModel)
Activity
Fragment
View
Service
BroadcastReceiver
```
- Thêm annotation vào lớp
```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {}
```
- Thường trong *ViewModel* sẽ có các phụ thuộc được thêm vào trong constructer, ta thêm nhãn *`@Inject`* trước đó cho *Hilt* biết để cung cấp bản sao các phụ thuộc bên trong cho lớp đó, `AuthRepo` cũng là 1 phụ thuộc và sẽ được thêm ở phần [5](#Module)
<a name="ViewModel"></a>
```kotlin
// Tại ViewModel
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepo,
): ViewModel() {}
```
- Các tham số của một hàm khởi tạo có chú thích của một lớp là các phần phụ thuộc của lớp đó. Trong ví dụ, `AuthViewModel` có `AuthRepo` là phần phụ thuộc. Do đó, *Hilt* cũng phải biết cách cung cấp các bản sao của `AuthRepo`
<a name="Module"></a>
### 5. Module Hilt
- Nơi mà chúng ta sẽ cung cấp lần lượt tất cả các dependencies, ví dụ bên trong `AuthRepo` sẽ cần dùng `FirebaseAuth`, mỗi lần dùng phải gọi cả một dòng dài, tại đây ta trả ra thẳng instance để sử dụng luôn
```kotlin
@InstallIn(SingletonComponent::class)
@Module
class FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}
```
- Annotation *`@Provides`* để cung cấp sự phụ thuộc của ứng dụng (hay gọi là chèn thực thể), sẽ trả instance từ đây chứ không cần lấy instance mỗi khi cần từ `FirebaseAuth` nữa
- Bên trong `RepoModule` ta cũng cần khai báo các dependencies Repo để tái sử dụng, tương tự như trên, khi ta gọi `AuthRepo` thì *Hilt* sẽ trả về `AuthRepoImp` (nơi đã có hành động cụ thể)
<a name="RepoModule"></a>
```kotlin
@InstallIn(ViewModelComponent::class)
@Module
object RepoModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        appPreferences: SharedPreferences,
    ): AuthRepo {
        return AuthRepoImp(auth, appPreferences)
    }
}
```
<a name="Note"></a>
### 6. Note
- Fragment sử dụng *Hilt* phải thêm annotation *`@AndroidEntryPoint`* ở chính fragment đó và các fragment cùng một navigation controller. Hơn hết ở activity chứa nó cũng phải cần đặt annotation này.
- *`@InstallIn(SingletonComponent::class)`* vòng đời sẽ kết thúc khi Application bị hủy.
- *`@InstallIn(ViewModelComponent::class)`* vòng đời kết thúc khi ViewModel bị hủy.
- *`@InstallIn(ActivityComponent::class)`* tương tự *`@InstallIn(FragmentComponent::class)`* đều kết thúc ở *`onDestroy()`* tương ứng.
- Một Hilt thường sẽ có 4 phần chính
    - *`@Module`* ở đây sẽ thông báo cho Hilt cách cung cấp các phiên bản của một số thực thể nhất định. Ví dụ như [RepoModule](#RepoModule) cung cấp thực thể `AuthRepo`, mỗi khi được gọi sẽ trả về lớp AuthRepoImp mà không cần phải truyền đối số đầu vào (`FirebaseAuth`, `SharedPreferences`) mỗi lần gọi.
    - Ta có thể cho *Hilt* biết cách cung cấp các bản sao của thực thể này bằng cách tạo một hàm bên trong *`Module`* *Hilt* và chú thích hàm đó bằng *`@Provides`*.
    - *`@Inject`* là một annotation được sử dụng để xác định sự phụ thuộc bên trong người dùng. Ví dụ trong [ViewModel](#ViewModel) ta xác định sự phụ thuộc bên trong hàm khởi tạo (constructor). Các tham số của một hàm khởi tạo có chú thích của một lớp là các phần phụ thuộc của lớp đó.
    - Ngoài ra còn có 1 phần là *`@Component`* hay gọi là thành phần giao diện, tại đây chứa các *Module* bên trong. Nhưng trong Dagger-*Hilt* không còn cần thiết phải khai báo nữa.
