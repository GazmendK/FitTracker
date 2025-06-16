# FitTracker 🏋️‍♀️🍎📊

Welcome to **FitTracker**, your all-in-one companion for a healthier lifestyle! This Android application helps you track your fitness journey, monitor your nutritional intake, and manage your workout routines with ease.

## ✨ Features

FitTracker is packed with features to support your health and fitness goals:

### 🏠 Home Tab (Weight Tracking & Activity Overview)
* **Weight Progress Visualization**: See your weight trends over the last 90 days with an interactive line chart.
* **Steps Tracking (Placeholder)**: A bar chart is included for future integration of step tracking, providing a comprehensive activity overview.
* **Easy Weight Entry**: Quickly log your current weight using a convenient bottom sheet dialog.

### 🥗 Nutrition Tab (Daily Intake & Product Scanning)
* **Daily Nutrition Summary**: Get a clear overview of your calorie, carbohydrate, protein, and fat intake for the current day with progress indicators.
* **Customizable Nutrition Goals**: Set and adjust your daily maximums for calories, carbs, protein, and fat to align with your dietary needs.
* **Barcode Scanner Integration**: Easily scan product barcodes to retrieve detailed nutritional information from Open Food Facts and add it to your daily log.
* **Manual Nutrition Entry**: Option to manually input nutrition details if a barcode isn't available or for custom food items.
* **Product History**: View a list of products you've logged for the current day.

### 💪 Training Tab (Workout Planning & Performance Tracking)
* **Customizable Training Plans**: Create and manage multiple training plans, each with its own set of exercises and daily routines.
* **Dynamic Exercise Tracking Table**: For each training plan, input and track your performance (e.g., reps, sets, weight) in a flexible table format. Add new rows for days and new columns for exercises as needed.
* **Workout Timer**: Utilize an integrated timer to manage rest periods between sets or exercises, enhancing your workout efficiency.
* **Progress Graphs**: Visualize your performance over time for each exercise within a training plan, helping you see your progress and identify trends.

### ℹ️ About Us
* **Information Section**: Access details about the application, including licenses and credits.

## 🛠️ Technologies Used

* **Android SDK**: Core development platform.
* **Java**: Primary programming language.
* **Android Architecture Components**:
    * `Navigation Component`: For seamless navigation between fragments.
    * `ViewModel` & `LiveData`: For robust data management and UI updates.
* **MPAndroidChart**: Powerful charting library for beautiful and interactive graphs.
* **CameraX**: Android Jetpack library for camera integration, enabling barcode scanning.
* **ML Kit Barcode Scanning**: Google's ML Kit for efficient barcode detection and parsing.
* **OkHttp**: An efficient HTTP client for making network requests to the Open Food Facts API.
* **SQLite**: Local database for persistent storage of weight data, nutrition logs, training plans, and exercise details.
* **Material Design**: For a modern and intuitive user interface.


## 📁 Project Structure

The application is organized into several fragments, each responsible for a specific part of the functionality:

* `MainActivity.java`: The main activity hosting the navigation.
* `AboutUsFragment.java`: Displays information about the app.
* `FirstFragment/`: Contains logic for weight tracking and the home screen.
    * `WeightDatabase.java`: Manages weight data storage.
    * `GraphCardAdapter.java`: Adapter for displaying weight and step graphs.
* `SecondFragment/`: Handles nutrition tracking and barcode scanning.
    * `CameraFragment.java`: Manages camera access and barcode scanning.
    * `GetProductData.java`: Fetches product data from Open Food Facts API.
    * `NutritionDatabase.java`: Stores daily nutrition data.
    * `MaxNutritionsData.java`: Manages user-defined maximum nutrition goals.
    * `DailyOverview.java`: Orchestrates the display and interaction with daily nutrition metrics.
    * `ProductCardAdapter.java`: Adapter for displaying logged food products.
* `ThirdFragment/`: Manages training plans and exercise tracking.
    * `DatabaseCards.java`: Stores training plan metadata (name, exercise count, day count).
    * `MyDatabaseHelper.java`: Generic SQLite database helper for dynamic tables.
    * `DataRepository.java`: Handles data operations for exercise tables.
    * `CardAdapter.java`: Adapter for displaying training plan cards.
    * `TableFragment.java`: Displays and manages the interactive exercise table.
    * `GraphFragment.java`: Displays graphs for exercise performance.
    * `SharedViewModel.java`: Shares data between `TableFragment` and `GraphFragment`.
    * `TimerSheet.java`: Implements the workout timer functionality.
