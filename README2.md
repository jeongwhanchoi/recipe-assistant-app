# Recipe Assistant

**10조 신현승, 최정환**

## 2차 과제 필수 개선 기능

- 아이디 관리(혹은 임의 아이디) - 꼭 외부 서버를 사용할 필요는 없음
- 다음의 기능을 활용하는 각각의 아이디어 제출
  - Service/Broadcast Receiver/ActivityForResult/Database/Notification/SharedReference/Handler
- App 종료 후 다시 시작 시 기존의 정보가 저장되어 있어야함

## 2차 개선 아이디어 스케치 내용

### 기능#1

![s1](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/s1.png)

#### 사용자 설정 저장

- SharedPreference를 사용하여 사용자 초기 설정에 대한 정보 저장
- Notification 설정 또는 UI 설정

### 기능#2

![s2](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/s2.png)

#### 타이머

- COOK TIME을 클릭하면 Timer가 시작

#### 알림

- Notification을 이용하여 특정 상황이 되면 Service가 알아서 알림을 발생
- 버튼을 특정 시간 후에 알림이 오게 하는 것

### 기능#3

![s3](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/s3.png)

#### 레시피 추가

- 1차 개선에서 이 기능을 구현하지 못하였기에 레시피 추가를 구현할 계획 
- Database로는 MySQL+PHP를 사용할 예정 
- ActivityForResult 사용

### 기능#4

![s4](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/s4.png)

#### 로그인

- AWS Cognito 사용하여 사용자 관리

- ~~인트로 화면 + 로그인 화면~~
- ~~인트로 화면에서는 Handler를 사용하여 로그인 화면으로 전환~~
- ~~로그인 화면에서는 Database를 사용하여 계정 관리~~



### 기능#5

#### 레시피 재료 쇼핑 리스트 개선

- SharedPreference 사용



### 기능별 사용 기술

| 사용 기술            | 기능        |
| ------------------ | ------------- |
| Service            | 기능#2        |
| Database           | 기능#3 |
| Notification       | 기능#2        |
| SharedPreference   | 기능#1, 기능#5 |
| Handler            | 기능#4, 기능#2 |
| ActivityForResult | 기능#3 |



## 현실적 제한 요소 고려사항

### 산업 표준

한국표준산업분류의 분류코드 63999 ‘그 외 기타 정보 서비스업’을 준수하고 있습니다. 

![standard](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/standard.png)

| 분류코드 | 63999                                          |
| -------- | ---------------------------------------------- |
| 분류명   | 그 외 기타 정보 서비스업                       |
| 설명     | 정보를 제공하는 기타 서비스 활동을 말한다      |
| 색인어   | 온라인게임 아이템 중개, 텍스트 음성변환 서비스 |



### 윤리성

- 진정한 실력으로 정정당당하게 선의의 경쟁을 하며, 경쟁사의 이익을 침해하거나 약점을 부당하게 이용하지 않는다.
- 앱 개발에 관련 법규 및 규정을 준수한다.
- 고객에게 정확한 정보만을 제공한다.



## 기능 별 설명

### 사용자 설정 저장

#### 레시피 카드 UI 변경

1. `fragment_setting.xml` 에 해당 checkbox 추가

   ```xml
   <CheckBoxPreference
               android:layout_width="match_parent"
               android:layout_height="wrap_content"
               android:defaultValue="true"
               android:key="pref_enable_column"
               android:summary="@string/pref_enable_summary"
               android:title="@string/pref_enable_title" />
   ```

2. `RecipeAdapter` 클래스의 `onCreateViewHolder()` 업데이트

   ```java
   if (Configurations.LIST_MENU_TYPE == Configurations.LIST_FULLWIDTH)
               v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_card, viewGroup, false);
           else if (Configurations.LIST_MENU_TYPE == Configurations.LIST_2COLUMNS)
               v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recipe_card_2column, viewGroup, false);
   ```

3. `MyPreferenceFragment` 클래스의 `onCreate()` 업데이트

   ```java
   public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           addPreferencesFromResource(R.xml.fragment_settings);
   
           android.preference.Preference myPref = findPreference("pref_enable_column");
   
           myPref.setOnPreferenceChangeListener(new android.preference.Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(android.preference.Preference preference, Object o) {
                   if ((boolean) o) {
                       Configurations.LIST_MENU_TYPE = Configurations.LIST_2COLUMNS;
                   } else {
                       Configurations.LIST_MENU_TYPE = Configurations.LIST_FULLWIDTH;
                   }
                   return true;
               }
           });
       }
   ```

#### 캡처

![card1234](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/card1234.png)

### 로그인

#### AWS Mobile Hub + AWS Cognito

1. Mobile Hub Project 생성 및 App platform 설정
   - ![](https://docs.aws.amazon.com/aws-mobile/latest/developerguide/images/wizard-createproject-platform-android.png)

2. Cloud Config 파일 다운로드
   - ![](https://docs.aws.amazon.com/aws-mobile/latest/developerguide/images/wizard-createproject-backendsetup-android.png)

3. backend service config 파일을 App에 추가
   - ![](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/l5.png)

4. User Sign-in 설정

   -  [Mobile Hub console](https://console.aws.amazon.com/mobilehub) 에서 User Sign-in 설정 선택

   - Email and Password sign-in 선택

     ![](https://docs.aws.amazon.com/aws-mobile/latest/developerguide/images/add-aws-mobile-sdk-email-and-password.png)

   - User pool 생성

     ![](https://docs.aws.amazon.com/aws-mobile/latest/developerguide/images/add-aws-mobile-sdk-email-and-password-create.png)

   - AWS Cognito 사용을 위한 config 파일 다시 설정 후 App에 다시 추가

#### 구현을 위한 소스코드

1. backend config 파일을 app에 추가

2. `AndroidManifest.xml` 에 해당 permission 없으면 추가

   ```xml
   <uses-permission android:name="android.permission.INTERNET"/>
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
   ```

3. `app/build.gradle` 파일에 dependencies 추가 

   ```groovy
   	// AWS Mobile SDK for Android
       implementation 'com.amazonaws:aws-android-sdk-core:2.6.+'
       implementation 'com.amazonaws:aws-android-sdk-auth-core:2.6.+@aar'
       implementation 'com.amazonaws:aws-android-sdk-pinpoint:2.6.+'
   
       implementation 'com.android.support.constraint:constraint-layout:1.0.2'
       implementation 'com.android.support:multidex:1.0.1'
       implementation 'joda-time:joda-time:2.9.9'
       // AWS Mobile Authentication for Android
       implementation 'com.amazonaws:aws-android-sdk-auth-ui:2.6.+@aar'
       implementation 'com.amazonaws:aws-android-sdk-auth-userpools:2.6.+@aar'
       implementation 'com.amazonaws:aws-android-sdk-cognitoidentityprovider:2.6.+'
   ```

4. 로그인 화면을 위한 activity 추가

   ```xml
   <activity android:name=".AuthenticatorActivity">
       <intent-filter>
           <action android:name="android.intent.action.MAIN" />
           <category android:name="android.intent.category.LAUNCHER" />
       </intent-filter>
   </activity>
   ```

5.  `AWSMobileClient `를 호출하기 위한 `AuthenticatorActivity` 의 `onCreate` 함수 업데이트

   - `AuthenticatorActivity`
   - aws package의 클래스들 확인



https://docs.aws.amazon.com/aws-mobile/latest/developerguide/getting-started.html

https://github.com/jeongwhanchoi/recipe-assistant-app/commit/8642623e249765701d9c7cc089cb29bf59e85904

#### 로그인 화면 캡처

![l123](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/l123.png)

#### AWS Cognito 아이디 관리 화면

![l4](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/l4.png)



### 타이머

- 위의 공유버튼 옆의 타이머 버튼을 누르면 기존 타이머 앱을 이용할 수 있음

1. `AndroidManifest.xml` 에 permission 추가 

   ```xml
   <uses-permission android:name="com.android.alarm.permission.SET_ALARM" />
   ```

2. `Recipe` 클래스에 `startTimerActivity()`  추가

   ```java
   public void startTimerActivity(Activity activity, int length)
       {
           if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
           {
               try
               {
                   length = cook_time*60;
                   Intent intent = new Intent(AlarmClock.ACTION_SET_TIMER);
                   intent.putExtra(AlarmClock.EXTRA_LENGTH, length);
                   activity.startActivity(intent);
               }
               catch(android.content.ActivityNotFoundException e)
               {
                   // can't start activity
               }
           }
       }
   ```

#### 타이머 캡처

![t12](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/t12.png)



### 타이머+알림

- 타이머 이외에도 요리는 비교적 짧은 시간을 많이 요구하기 때문에 짧은 시간 알림을 추가
- 가운데 Cook Time 버튼을 누르면 ‘Cooking Start’ 라는 Toast메시지가 나오고 특정시간(예:30초)을 카운트
  - 특정 시간이 지나면 상단바에 ‘Time’s up’이라는 알림이 뜬다.
- 상단바의 알림을 누르면 카운트 시작 시 눌렀던 Cook Time 버튼 창으로 돌아간다.

1. MyService.java 클래서에서 Notification 선언 

   ```java
   public class MyService extends Service {
   
       NotificationManager Notifi_M;
       ServiceThread thread;
       Notification Notifi ;
   ```

2. ServiceHandler을 생성하고 역할 설정. 

   ```java
   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
       Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       myServiceHandler handler = new myServiceHandler();
       thread = new ServiceThread(handler);
       thread.start();
       return START_STICKY;
   }
   ```

3. 상단 알림 설정 

   ```java
   @Override
       public void handleMessage(android.os.Message msg) {
           Intent intent = new Intent(MyService.this, MainActivity.class);
           PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
   
           Notifi = new Notification.Builder(getApplicationContext())
                   .setContentTitle("Cooking Time")
                   .setContentText("Time's Up")
                   .setSmallIcon(R.drawable.ic_launcher)
                   .setTicker("Time's Up!!!")
                   .setContentIntent(pendingIntent)
                   .build();
   ```

4. 알림 세부사항 설정. 

   `Notifi.defaults = Notification.*DEFAULT_SOUND*;` - 소리추가.

   `Notifi.flags = Notification.*FLAG_ONLY_ALERT_ONCE*;` - 알림 소리를 한번만 내도록.

   `Notifi.flags = Notification.*FLAG_AUTO_CANCEL*;` - 확인하면 자동으로 알림이 제거 되도록.  `Toast.*makeText*(MyService.this, "Finish?", Toast.*LENGTH_LONG*).show();` - 토스트 띄우기. 

5. `ServiceThread.java` 에서 Handler 선언. 

   ```java
   public class ServiceThread extends Thread{
       Handler handler;
       boolean isRun = true;
       int sleepTime;
       MyService myService;
   
   
       public ServiceThread(Handler handler){
           this.handler = handler;
       }
   ```

6. Thread는 한 번 알림 후 끝나도록 `isOn`이라는 flag를 사용

   ```java
   public void run(){
           //반복적으로 수행할 작업을 한다.
           while(isRun){
               try{
                   sleepTime = 30000;
                   Thread.sleep(sleepTime); 
                   isRun=false;
               }catch (Exception e) {}
               handler.sendEmptyMessage(0); 
           }
       }
   ```

#### 캡처

![aaa-side](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/aaa-side.png)



### 레시피 추가

1. `AddRecipeFragment` 클래스 추가

2. `MainActivity` 의 `onCreate()` 에서 navigation drawerBuilder 업데이트

   ```java
   case NAV_ADD_RECIPE:
                                   changeFragment(new AddRecipeFragment());
   ```

   

3. `AddRecipeFragment` 클래스에서 `onCreateView()` 업데이트

   ```java
   imageView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (image != null) {
                       //delete
                       image = null;
                       imageView.setIcon("faw-picture-o");
                       imageView.setPaddingDp(30);
                   } else {
                       Intent intent = new Intent();
                       intent.setType("image/*");
                       intent.setAction(Intent.ACTION_GET_CONTENT);
                       startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                   }
               }
           });
   ```

4. `AddRecipeFragment` 클래스에서 `onActivityResult()` 업데이트

   ```java
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
           super.onActivityResult(requestCode, resultCode, data);
           switch (requestCode) {
               case PICK_IMAGE:
                   if (resultCode == RESULT_OK && null != data) {
                       selectedImage = data.getData();
                       Bitmap image = loadImageHelper.decodeSampledBitmapFromResource(selectedImage, 600, 300);
                       if (image != null) {
                           this.image = image;
                           imageView.setImageBitmap(image);
                           imageView.setPaddingDp(0);
                       }
                   }
                   break;
           }
       }
   ```

- `Recipe.php` 에서 `add()` 업데이트

  ```php
  public function add($f3, $id, $name, $directions, $ingredients, $category, $generalFields, $img_files, $accepted)
      {
          //add or edit to db
          if ($id>=0) {
              $this->load(array('id = ?',$id));
          }
          $this->name = $name;
          $this->directions = $directions;
          $this->ingredients = json_encode(array_slice($ingredients, 0, count($ingredients)-1, true));
          $this->category = $category;
          $this->accepted = $accepted;
  
          foreach ($generalFields as $key => $generalField) {
              $this->{$key} = $generalField;
          }
  
          if (count($img_files)>0) {
              $imageNames = array_keys($img_files);
              foreach ($imageNames as $key => $imageName) {
                  $imageNamebase[$key] = basename($imageName);
  
                  //resize image
                  $img = new \Image($imageName);
                  $img->resize( 600, 300, true, true );
                  $f3->write( $imageName, $img->dump() );
              }
              $this->image = json_encode(array_slice($imageNamebase, 0, count($imageNamebase), true));
          }
          $this->save();
      }
  ```



#### 레시피 추가 캡처

![add1](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/add1.png)

- phpMyAdmin 

![phpMyAdmin](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/phpMyAdmin.png)



---

### 재료 쇼핑 리스트 추가 기능 개선

SharedPreference 사용

1. `Save` 클래스

   ```java
   public static boolean saveArray(int[] array, String arrayName, Context mContext) {
       SharedPreferences prefs = mContext.getSharedPreferences(mContext.getString(R.string.save_preference_name), 0);
       SharedPreferences.Editor editor = prefs.edit();
       editor.putInt(arrayName + "_size", array.length);
       for (int i = 0; i < array.length; i++)
           editor.putInt(arrayName + "_" + i, array[i]);
       return editor.commit();
   }
   ```

2. `IngredientItem` 클래스에서 `addIngredient()` 업데이트

   ```java
   public static void addIngredient(Context context, String ingredientName, String RecipeName) {
           Save.addToArray(ingredientName, "shopping_list_" + RecipeName, context);
   
           String[] headers = loadArray("sl_headers", context);
           if (headers != null) {
               if (Arrays.asList(headers).contains("shopping_list_" + RecipeName)) {
                   return;
               }
           }
           Save.addToArray("shopping_list_" + RecipeName, "sl_headers", context);
       }
   ```

#### 캡처

![shopping_two](/Users/jeongwhanchoi/Study/3-1/Mobile App Programming/Project/Project_02/img/shopping_two.png)



---

## Reference

- cPanel MySQL Database(Using HTML to PHP to Database)
  - https://www.youtube.com/watch?v=rlsp7LGPSEc
  - https://www.youtube.com/watch?v=xx0YwP1O3Fs
- PHP와 MySQL의 연동 - 데이터를 HTML에 표현하기
  - https://opentutorials.org/course/62/5174
  - https://www.youtube.com/watch?v=AocaT966qaI
  - https://www.youtube.com/watch?v=ceiT46Lu8kE
- 서비스와 노티피케이션 통합 예제
  - http://twinw.tistory.com/50

https://github.com/jk2K/Android-Upload-Demo/blob/master/app/src/main/java/com/jk2k/helloworld/VolleyMultiPartRequest.java

https://github.com/DWorkS/VolleyPlus/blob/master/library/src/com/android/volley/request/MultiPartRequest.java

https://github.com/wasabeef/richeditor-android

- 국가 통계포털 
  - http://kosis.kr/index/index.do/ 