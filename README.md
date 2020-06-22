![Android CI](https://github.com/RivuChk/Jetpack-Compose-MVI-Demo/workflows/Android%20CI/badge.svg) [![Bitrise Build Status](https://app.bitrise.io/app/5390ecd9380aa236/status.svg?token=JSvXJ9uYLw1XL9riIPN1cQ)](https://app.bitrise.io/app/5390ecd9380aa236)
## Jetpack Compose + MVI
The goal of this project is to have a demo application using popular MVI Arch pattern in android, while using Jetpack Compose for UI. This project shows how less-complicated the whole codebase and testing becomes just by using Jetpack Compose.
**We will keep this project updated with latest releases of Jetpack Compose.**
![Here's a demo of the app](composable-demo.gif)

### Technologies / Frameworks Used
- Spek + JUnit5 for Unit Tests / Specification Testing
- Jetpack Compose for UI
- Koin for DI (might migrate to Hilt in later stage)
- RxJava 3 for Streams
- Room for DB
- Mockito for mocking
- LiveData for emiting states from ViewModel
- `AdapterList` from Compose for showing List
- Composable `ConstraintLayout`
- Composable `TopAppBar`
- Bitrise and Github Actions for CI