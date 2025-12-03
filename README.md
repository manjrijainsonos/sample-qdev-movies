# 🏴‍☠️ Pirate's Movie Treasure Chest - Spring Boot Demo Application

Ahoy matey! Welcome to the most swashbuckling movie catalog web application on the seven seas! Built with Spring Boot and featuring a pirate-themed search functionality that would make even Blackbeard proud.

## ⚓ Features

- **🎬 Movie Treasure Chest**: Browse 12 classic movies with detailed information
- **🔍 Search the Seven Seas**: Advanced search functionality with name, ID, and genre filters
- **📋 Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **⭐ Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **🌊 REST API**: JSON endpoints for external applications to access our treasure
- **📱 Responsive Design**: Mobile-first design that works on all devices
- **🎨 Pirate Theme**: Dark theme with pirate-inspired UI elements and language

## 🛠️ Technology Stack

- **Java 8**
- **Spring Boot 2.7.18**
- **Maven** for dependency management
- **Thymeleaf** for templating
- **Log4j 2** for logging
- **JUnit 5.8.2** for testing
- **JSON** for data parsing

## 🚀 Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **🏴‍☠️ Movie Treasure Chest**: http://localhost:8080/movies
- **🔍 Search Movies**: http://localhost:8080/movies?name=Prison&genre=Drama
- **📋 Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)
- **⚓ REST API Search**: http://localhost:8080/movies/search?name=Action&genre=Crime

## 🔍 Search Functionality

### HTML Search Form
The main movies page now includes a pirate-themed search form with the following features:
- **Movie Name**: Case-insensitive partial matching
- **Movie ID**: Exact ID matching for treasure hunters who know what they seek
- **Genre**: Dropdown selection with partial matching support
- **Clear Search**: Reset all filters and return to full treasure chest

### Search Examples
- Search by name: `http://localhost:8080/movies?name=Prison`
- Search by genre: `http://localhost:8080/movies?genre=Drama`
- Search by ID: `http://localhost:8080/movies?id=1`
- Combined search: `http://localhost:8080/movies?name=Family&genre=Crime`

## 🗺️ API Endpoints

### Get All Movies (with optional search)
```
GET /movies
```
Returns an HTML page displaying movies. Supports optional search parameters.

**Query Parameters:**
- `name` (optional): Movie name to search for (case-insensitive partial match)
- `id` (optional): Specific movie ID to find
- `genre` (optional): Genre to filter by (case-insensitive partial match)

**Examples:**
```
http://localhost:8080/movies
http://localhost:8080/movies?name=Prison
http://localhost:8080/movies?genre=Action
http://localhost:8080/movies?name=Family&genre=Crime
```

### Search Movies (REST API)
```
GET /movies/search
```
Returns JSON response with search results. Perfect for other applications to access our treasure!

**Query Parameters:**
- `name` (optional): Movie name to search for
- `id` (optional): Specific movie ID to find  
- `genre` (optional): Genre to filter by

**Response Format:**
```json
{
  "success": true,
  "movies": [...],
  "count": 2,
  "message": "Shiver me timbers! Found 2 movies in our treasure chest!"
}
```

**Examples:**
```bash
# Search by name
curl "http://localhost:8080/movies/search?name=Prison"

# Search by genre
curl "http://localhost:8080/movies/search?genre=Action"

# Search with multiple filters
curl "http://localhost:8080/movies/search?name=Family&genre=Crime"

# Invalid ID example
curl "http://localhost:8080/movies/search?id=-1"
# Returns: {"success": false, "message": "Arrr! That ID be as useless as a compass that points south, matey!"}
```

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

**Example:**
```
http://localhost:8080/movies/1/details
```

## 🏗️ Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── movies/
│   │       │   ├── MoviesApplication.java    # Main Spring Boot application
│   │       │   ├── MoviesController.java     # REST controller with search endpoints
│   │       │   ├── MovieService.java         # Business logic with search functionality
│   │       │   ├── Movie.java                # Movie data model
│   │       │   ├── Review.java               # Review data model
│   │       │   └── ReviewService.java        # Review business logic
│   │       └── utils/
│   │           ├── MovieIconUtils.java       # Movie icon utilities
│   │           └── MovieUtils.java           # Movie validation utilities
│   └── resources/
│       ├── templates/
│       │   ├── movies.html                   # Enhanced with search form
│       │   └── movie-details.html            # Movie details template
│       ├── static/css/                       # Styling files
│       ├── application.yml                   # Application configuration
│       ├── movies.json                       # Movie data treasure chest
│       ├── mock-reviews.json                 # Mock review data
│       └── log4j2.xml                        # Logging configuration
└── test/                                     # Comprehensive unit tests
    └── java/
        └── com/amazonaws/samples/qdevmovies/movies/
            ├── MovieServiceTest.java         # Service layer tests
            ├── MoviesControllerTest.java     # Controller tests with search
            └── MovieTest.java                # Model tests
```

## 🧪 Testing

Run the comprehensive test suite:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MovieServiceTest

# Run with coverage
mvn test jacoco:report
```

### Test Coverage
- **MovieService**: Search functionality, edge cases, performance tests
- **MoviesController**: HTML and REST endpoints, error handling
- **Integration Tests**: End-to-end search scenarios

## 🔧 Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

Check the logs for pirate-themed error messages:
```bash
tail -f logs/application.log
```

## 🎯 Error Handling

The application includes comprehensive error handling with pirate-themed messages:

- **Invalid ID**: "Arrr! That ID be as useless as a compass that points south, matey!"
- **No results**: "Arrr! No treasure found with those search criteria, but don't give up the hunt!"
- **Server errors**: "Arrr! Something went wrong while searchin' for treasure. Try again, matey!"

## 🤝 Contributing

This project welcomes contributions from fellow pirates! Feel free to:
- Add more movies to the treasure chest
- Enhance the pirate theme and UI/UX
- Improve search functionality (fuzzy matching, advanced filters)
- Add new features like favorites or recommendations
- Optimize performance for larger datasets

### Development Guidelines
- Follow pirate-themed naming conventions in comments and logs
- Maintain comprehensive test coverage (>80%)
- Use proper JavaDoc with pirate flair
- Follow Spring Boot best practices

## 📜 License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

*Arrr! May fair winds fill yer sails as ye navigate through this treasure chest of movies! 🏴‍☠️*
