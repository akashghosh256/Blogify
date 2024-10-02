# Blogify (made with Spring Boot)
Developed a robust blog application using Java and Spring Boot, where users can register, log in, and perform CRUD (Create, Read, Update, Delete) operations on their blogs. Implemented user authentication and authorization features to ensure secure access to personal accounts. Designed the database structure using SQL for efficient storage and retrieval of blog data, including user profiles and blog content. Integrated RESTful APIs for seamless communication between the frontend and backend, allowing users to interact with the platform easily. Ensured high performance and scalability, optimizing database queries and system design.

## Screenshots

## Anyone can see the blogs with or without login/registering
![App Screenshot](https://github.com/akashghosh256/Blogify/blob/main/screenshots/forall.png)

## Login
![App Screenshot](https://github.com/akashghosh256/Blogify/blob/main/screenshots/login.png)

## Register
![App Screenshot](https://github.com/akashghosh256/Blogify/blob/main/screenshots/register.png)

## Create new blog
![App Screenshot](https://github.com/akashghosh256/Blogify/blob/main/screenshots/createnew.png)


## Edit blog
![App Screenshot](https://github.com/akashghosh256/Blogify/blob/main/screenshots/editblog.png)


## Add comments in blog posts
![App Screenshot](https://github.com/akashghosh256/Blogify/blob/main/screenshots/editComment.png)





## About the Project

Application made using:
- Java 17
- Spring Boot v3.1.2
- Spring Web
- Spring Data JPA
- Spring Security
- H2-in-memory-database
- Lombok
- Thymeleaf
- Maven
 

### Features

- Unregistered/anonymous blog users can view all posts and comments.
- Registered and logged-in users (authenticated users) can add new posts, view only their own posts, and edit or delete them (CRUD functionality).
- Users can write comments on posts by themselves or other users.
- Validation for creating new posts: the body must not be empty and the title must have a default length of 7 characters or more.
- Spring Security authentication and authorization rules ensure that users can only edit or delete their own posts.
- The front end is made using Thymeleaf templates.


Made an effort to write clean OOP code to the best of my understanding, focusing on separation of concerns and encapsulation of the internal workings of the class to hide details from the outside while providing a simple interface. The goal was to minimize the pain of adding new functionality.

## How to Set Up the Application

Open the terminal and use the `git clone` command to download the remote GitHub repository to your computer:

```bash
git clone https://github.com/akashghosh256/Blogify.git
```
It will create a new folder with the same name as the GitHub repository, "Blogify". All the project files and Git data will be cloned into it. After cloning is complete, change directories into that new folder:
```
cd Blogify
```

## How to use

To launch the application run this command (uses maven wrapper):
```
$ ./mvnw clean spring-boot:run
```
Or using your installed maven version:
```
$ mvn clean spring-boot:run
```
<b>For interacting with application one can use <i>a browser</i></b>.
By default, application uses Tomcat which listening on port: 8080,
means you can reach it if run on a local machine by hitting URL http://localhost:8080.

