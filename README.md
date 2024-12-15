# Submission Intermediete Android Training - Dicoding

## Introduction

This repository contains the source code for an Android application developed as part of the Dicoding **Intermediete Android Training** program. The project demonstrates the implementation of essential Android development concepts, providing a solid foundation for building more advanced applications.

## Key Features

- **Map Integration**: Displays a list of stories on a map correctly.
- **Paging 3 Integration**: Implements a list of stories using Paging 3 to ensure efficient data loading and smooth scrolling.
- **Unit Testing**: Includes unit tests to verify the application's core functionalities and maintain code reliability.
- **Robust Application**: Ensures the application runs without force-closing.
- **Search Functionality**: Utilizes `SearchView` to enable users to search for specific data within the application.
- **Tab Navigation**: Uses `TabLayout` and `ViewPager2` for smooth navigation between different application pages.
- **Network Requests**: Integrates LoopJ and Retrofit libraries for seamless API interactions and JSON response handling.
- **CRUD Database Management**: Leverages SQLite and Room libraries to perform Create, Read, Update, and Delete operations for data persistence.
- **Key-Value Data Storage**: Employs DataStore for storing and retrieving user preferences and settings.

## Prerequisites

Before running this project, ensure you have the following:

- Basic understanding of Android development concepts, including Activities, Fragments, and Layouts.
- Familiarity with Java or Kotlin programming languages.
- Android Studio installed with necessary development tools and libraries.

## Setting Up the Project

Clone this repository to your local machine:
   ```bash
   git clone https://github.com/esnanta/UserGithubAPI.git
   ```

## Project Structure

```plaintext
├── app
│   ├── src
│   │   ├── main
│   │   │   ├── java/com/esnanta/storyapp   # Application source code
│   │   │   ├── res                         # Layouts, drawables, and other resources
│   │   ├── test                            # Unit tests
│   │   ├── androidTest                     # Instrumentation tests
├── build.gradle                            # Project build configuration
```

## Key Technologies

### Map Integration
Displays stories on a map using the Google Maps API. Each story is pinned at its corresponding location, providing users with a clear visual representation of story data.

### Paging 3
Efficiently loads and paginates a large dataset of stories, ensuring a smooth scrolling experience and reduced memory usage.

### Unit Testing
Verifies the functionality of key components, ensuring the application meets its requirements. The tests improve the overall code reliability and help prevent regressions.

### SearchView
Enables users to perform data searches directly within the application. The search results are dynamically updated to reflect the query.

### TabLayout & ViewPager2
Implements a tab-based navigation system, allowing users to switch seamlessly between pages.

### LoopJ & Retrofit
Handles network requests to fetch and parse JSON responses from APIs. LoopJ is used for asynchronous tasks, while Retrofit simplifies API interactions.

### SQLite & Room
Manages persistent storage using SQLite. The Room library abstracts database interactions, providing a robust implementation for CRUD operations.

### DataStore
Stores user preferences and application settings in a key-value format, ensuring data persistence and quick retrieval.

## Further Exploration and Customization

Feel free to:

- Explore the codebase to understand the implementation details.
- Modify the project to extend functionality or adapt it to your needs.
- Refer to the documentation of the libraries used:
  - [LoopJ](https://loopj.com/)
  - [Retrofit](https://square.github.io/retrofit/)

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

## Conclusion

This project demonstrates the integration of essential Android development concepts, including map-based features, Paging 3, and robust data management practices. By implementing best practices and incorporating unit tests, it serves as a strong foundation for building reliable and scalable Android applications. Explore, learn, and enjoy!
