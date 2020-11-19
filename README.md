[![Bitrise Build Status](https://app.bitrise.io/app/5390ecd9380aa236/status.svg?token=JSvXJ9uYLw1XL9riIPN1cQ)](https://app.bitrise.io/app/5390ecd9380aa236) ![Android CI](https://github.com/RivuChk/Jetpack-Compose-MVI-Demo/workflows/Android%20CI/badge.svg)
# Jetpack Compose + MVI
The goal of this project is to have a demo application using popular MVI Arch pattern in android, while using Jetpack Compose for UI. This project shows how less-complicated the whole codebase and testing becomes just by using Jetpack Compose.
**We will keep this project updated with latest releases of Jetpack Compose.**
- Current Jetpack Compose Version - *alpha-07*

![Here's a demo of the app](composable-demo-2.gif)

## Technologies / Frameworks Used
- Spek + JUnit5 for Unit Tests / Specification Testing
- Jetpack Compose for UI
- Lottie for Splash Animation
- Dagger HILT for DI (Migrated from Koin in this commit: [21a9db64bee1359bd57cf99ba757467da63f10b2](https://github.com/RivuChk/Jetpack-Compose-MVI-Demo/commit/21a9db64bee1359bd57cf99ba757467da63f10b2))
- RxJava 3 for Streams
- Room for DB (With RxJava 3)
- Mockito for mocking
- LiveData for emitting states from ViewModel
- `LazyColumnFor` from Compose for showing List
- *Glide* for image loading with Composable `Image`
- Composable `ConstraintLayout`
- Composable `TopAppBar`
- Bitrise and Github Actions for CI