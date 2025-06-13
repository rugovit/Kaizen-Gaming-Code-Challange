# Sports Events App

This Android application provides a seamless experience for browsing upcoming sports events, organized by sport type, with features like favoriting events and real-time countdown timers. Built with modern Android development practices, it uses **Jetpack Compose** for the UI, follows the **MVI (Model-View-Intent)** pattern for state management, and adopts a **clean architecture** to separate concerns across data, domain, and presentation layers. The app ensures robust error handling, smooth navigation, and offline functionality, making it both user-friendly and reliable.

---

## Purpose and Main Features
The app serves as an intuitive platform for sports enthusiasts, offering:
- **Event Listing**: Displays sports (e.g., Football, Basketball) with their associated events in a structured, scrollable list.
- **Favorite Events**: Allows users to mark events as favorites and filter the list to show only favorited events per sport.
- **Real-Time Countdown**: Provides live countdown timers for each event, showing the time remaining until the event starts.
- **Expand/Collapse Sections**: Enables users to expand or collapse sport sections for better visibility control.
- **Error Feedback**: Shows user-friendly error messages (e.g., "No internet connection") via snackbars when issues arise.
- **Offline Support**: Caches events locally, ensuring the app remains functional without an internet connection after the initial sync.

---

## Architecture Overview
The app adheres to a **clean architecture** pattern, dividing the codebase into three distinct layers:
- **Data Layer**: Handles data retrieval and storage from a remote API and local database.
- **Domain Layer**: Encapsulates the app’s business logic and defines core data models.
- **Presentation Layer**: Manages the UI and user interactions using Jetpack Compose and MVI.

This layered approach ensures separation of concerns, enhancing maintainability, testability, and scalability.

---

## Data Layer
The data layer is the foundation for data management, providing a single source of truth:
- **Repository**: Coordinates data operations:
  - **Network**: Uses **Retrofit** (`ApiService`) to fetch data from a mock API (`sports.json`).
  - **Local Storage**: Employs **Room** with entities (`SportEntity`, `EventEntity`) and a DAO (`SportDao`) for persistent storage and offline access.
- **Mappers**: Transforms network models into database entities and preserves user-marked favorite statuses during syncs.
- **Data Syncing**: Updates the local database by removing outdated sports and events not present in the latest API response.

---

## Domain Layer
The domain layer defines the app’s core logic and data structures:
- **Models**:
  - `SportDomainModel`: Contains a sport’s ID, name, and list of events.
  - `EventDomainModel`: Includes an event’s ID, sport ID, name, start time, and favorite status.
- **Use Cases**:
  - `GetSportWithEventsUseCase`: Streams sports and events in real-time using a `Flow`.
  - `SyncSportsDataUseCase`: Triggers data synchronization between the API and local database.
  - `TimeTickerUseCase`: Emits the current time every second for countdown updates.
  - `ToggleFavoriteUseCase`: Updates an event’s favorite status.
- **Mappers**: Converts database entities into domain models, maintaining layer independence.

---

## Presentation Layer
The presentation layer drives the user interface and state management:
- **UI with Jetpack Compose**:
  - `SportsScreen`: Shows a scrollable list of sports and events with expandable sections and a favorite filter.
  - `EventItem`: Renders event details, including a countdown timer and favorite toggle.
  - `LandingScreen`: A simple entry screen with a button to access the sports list.
- **MVI Pattern**:
  - `SportsViewModel`: Manages state (`UiState`) and processes user intents (`UiIntent`), such as loading data or toggling favorites.
  - `UiState`: Tracks the current state (e.g., loading, sports list, current time).
  - `UiEffect`: Manages side effects, like displaying error snackbars.
- **Real-Time Updates**: Countdown timers update every second, freezing during scrolling for performance.
- **Animations**: Smooth transitions for expanding/collapsing sections and filtering favorites.

---

## Error Handling
Errors are managed using a sealed class `AppError` with categories like:
- `NetworkError`: For connectivity issues.
- `ApiError`: For API-related failures.
- `DatabaseError`: For local storage problems.
- `UnknownError`: For unexpected issues.
Errors are logged with **Timber**, propagated using **Arrow’s `Either`**, and displayed to users via snackbars with clear, actionable messages.

---

## Navigation
The app uses **Jetpack Navigation Compose** for a streamlined experience:
- **Routes**:
  - `LANDING`: Shows the `LandingScreen` with a button to proceed to the sports list.
  - `SPORTS`: Displays the `SportsScreen` with sports and events.

---

## Additional Features
- **Offline Support**: Local caching allows event browsing without an internet connection.
- **Favorite Persistence**: Favorite statuses persist across data syncs.
- **Performance**: Countdown timers pause during scrolling to optimize recompositions.
- **Custom UI**: Reusable components like `SportsToolbar` and `FavoriteToggle` ensure consistency.

---

## Benefits of the Design
- **Modularity**: Clean architecture separates concerns, simplifying updates and debugging.
- **Testability**: Use cases and repository functions are isolated for easy unit testing.
- **Reactive UI**: Flows and MVI keep the UI responsive to data and state changes.
- **Reliability**: Centralized error handling ensures a consistent, user-friendly experience.

---

This application exemplifies a modern, well-engineered solution, balancing functionality with a polished user experience. Its architecture and design choices make it a strong foundation for future enhancements and scalability.
