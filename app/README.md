# Project Documentation

## Project Overview

This project is a mobile application designed to assist users in managing their exercise routines and tasks. The application is built using Kotlin and Java, and it leverages the Android framework for the user interface and SQLite for data persistence.

## User Manual

### Home Screen (`fragment_home.xml`)

The home screen allows users to input and save their exercise routines. It includes a spinner for selecting the type of exercise, an EditText for inputting the duration of the exercise, and a button for saving the exercise.

The corresponding Kotlin code (`HomeFragment.kt`) handles the user interactions, validates the input, and saves the exercise data to the SQLite database using the `DatabaseHelper` class.

### Dashboard Screen (`fragment_dashboard.xml`)

The dashboard screen displays a list of saved exercises in a RecyclerView. Each item in the list shows the name and duration of the exercise.

The corresponding Kotlin code (`DashboardFragment.kt`) retrieves the saved exercises from the SQLite database using the `DatabaseHelper` class and displays them in the RecyclerView using the `ExerciseAdapter` class.

### Notifications Screen (`fragment_notifications.xml`)

The notifications screen displays a list of tasks in a RecyclerView. Each item in the list shows the title of the task and a checkbox indicating whether the task is completed. There is also a FloatingActionButton for adding new tasks.

The corresponding Kotlin code (`NotificationsFragment.kt`) handles the user interactions, manages the tasks list, and displays the tasks in the RecyclerView using the `TaskAdapter` class.

### Timer Service (`TimerService.kt`)

The TimerService is a background service that runs a timer. It logs each tick of the timer and broadcasts an intent with the action "TIMER_UPDATE".

## Code Overview

The codebase is organized into several packages, each containing related classes. The `ui` package contains classes for the user interface, including fragments for each screen and adapters for the RecyclerViews. The `DatabaseHelper` class in the root package handles interactions with the SQLite database. The `TimerService` class runs a background timer and broadcasts updates.