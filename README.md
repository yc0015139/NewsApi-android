# NewsApi-android

## Environment
```
AndroidStudio = Android Studio Giraffe | 2022.3.1 Patch 2 
kotlin = 1.8.10
compileSdk = 34
minSdk = 29
targetSdk = 34
jvmTarget = 17
```

## Test coverage
- Yep, 97% lines covered on data layer
<img src="https://github.com/yc0015139/NewsApi-android/blob/master/preview/newsApi-android-test-coverage.png" width="400"/>

  - Repository
  - DataSources
    - PagingSource
- ViewModel

## TechStack
- Kotlin
  - Coroutines   
    - Flow
      - StateFlow 
  - Kotlin DSL in build.gradle.kts
- Testing
  - Testing first!
    - Almost all classes in the data layer have been covered with tests
    - PagingSource, PagingData, Realm and more
  - Libs
    - Kotlin test
    - Kotlin coroutines test
    - Mockk
    - MockWebServer
      - Mock the server response from json
    - Paging test
- Dependency Injection
  - Hilt
- Database
  - Realm
    - Test with Realm
- Network
  - Retrofit
  - Kotlin Serialization
- MVVM architecture
- Single Activity Architecture
- XML
  - Fragment
  - View binding
  - RecyclerView
    - Paging  
  - SwipeRefreshLayout
- Image
  - Glide
- Jetpack Compose
  - (WIP)
  - Compose navigation
    - Can navigate to Compose or XML View

## Previews

### Xml
<img src="https://github.com/yc0015139/NewsApi-android/blob/master/preview/newsApiPreview(XML).gif" width="320"/>

### Compose
  - (WIP)
