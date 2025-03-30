# README

## Overview
This module configures authentication, user management, menu management, and S3 image handling routes in a Ktor application. It provides endpoints for user registration, login, token refreshing, email verification, fetching user information, managing menus, and handling image uploads to AWS S3.

## Endpoints

### User Authentication Routes

#### `POST /user/create`
**Description**: Registers a new user using email and password.
- **Request Body**: 
  ```json
  {
    "email": "user@example.com",
    "password": "securepassword"
  }
  ```
- **Response**:
  - `200 OK`: "User was created"

#### `POST /user/login`
**Description**: Authenticates a user and returns access tokens.
- **Request Body**: 
  ```json
  {
    "email": "user@example.com",
    "password": "securepassword"
  }
  ```
- **Response**:
  - `200 OK`: JSON containing authentication tokens

#### `POST /user/token/refresh`
**Description**: Refreshes authentication tokens using a refresh token.
- **Request Body**: Refresh token as plain text
- **Response**:
  - `200 OK`: JSON containing new authentication tokens
  - `400 Bad Request`: "Refresh Token parameter is empty"

#### `POST /user/confirmation/resend`
**Description**: Resends the email confirmation code.
- **Request Body**: Email as plain text
- **Response**:
  - `200 OK`: "New code was sent"
  - `400 Bad Request`: "Email parameter is empty"

#### `POST /user/confirmation/verify`
**Description**: Verifies a user's email using a confirmation code.
- **Request Body**: 
  ```json
  {
    "email": "user@example.com",
    "confirmationCode": "123456"
  }
  ```
- **Response**:
  - `200 OK`: "Email was verified"
  - `400 Bad Request`: "Email or Verification Code parameter is empty"

#### `GET /user/info/{access_token}` (Authenticated Route)
**Description**: Fetches user information using an access token.
- **Request Body**:
  ```json
  {
    "token": "userAccessToken"
  }
  ```
- **Response**:
  - `200 OK`: JSON containing user details

### Menu Management Routes

#### `POST /table/object/menu`
**Description**: Creates a new menu for a user.
- **Request Body**:
  ```json
  {
    "userId": "user123"
  }
  ```
- **Response**:
  - `200 OK`: "Menu was created"
  - `400 Bad Request`: "User ID is incorrect"

#### `PUT /table/object/menu`
**Description**: Updates an existing menu.
- **Request Body**:
  ```json
  {
    "menuId": "menu123",
    "name": "Updated Menu Name"
  }
  ```
- **Response**:
  - `200 OK`: "Menu was updated"

#### `GET /table/object/menu/owner/{user_id}`
**Description**: Retrieves all menus owned by a user.
- **Response**:
  - `200 OK`: JSON list of menus
  - `400 Bad Request`: "The User ID is incorrect"

#### `GET /table/object/menu/{menu_id}`
**Description**: Retrieves details of a specific menu.
- **Response**:
  - `200 OK`: JSON containing menu details
  - `400 Bad Request`: "Menu ID parameter is empty"
  - `404 Not Found`: "The data was not found"

#### `DELETE /table/object/menu/{menu_id}`
**Description**: Deletes a specific menu.
- **Response**:
  - `200 OK`: "The menu was deleted"
  - `400 Bad Request`: "Menu ID parameter is empty"

### S3 Image Handling Routes

#### `POST /bucket/object/image/{image_id}`
**Description**: Uploads an image to AWS S3.
- **Request Body**: Multipart form-data with file
- **Response**:
  - `200 OK`: "File was uploaded"
  - `400 Bad Request`: "Incorrect Dish ID" or "File has not been attached"
  - `415 Unsupported Media Type`: "Type of file is unsupported"

#### `GET /bucket/object/image/{image_id}`
**Description**: Retrieves an image from AWS S3.
- **Response**:
  - `200 OK`: Image file bytes
  - `400 Bad Request`: "Parameters are null"
  - `404 Not Found`: "The file was not found"

#### `DELETE /bucket/object/image/{image_id}`
**Description**: Deletes an image from AWS S3.
- **Response**:
  - `200 OK`: "The file was deleted"
  - `400 Bad Request`: "Parameters are null"

#### `GET /bucket/object/image/check/{image_id}`
**Description**: Checks if an image exists in AWS S3.
- **Response**:
  - `200 OK`: Boolean (`true` if exists, `false` otherwise)
  - `400 Bad Request`: "Parameters are null"

## Authentication
The `GET /user/info/{access_token}` route requires Bearer authentication using AWS Cognito.

## Dependencies
- Ktor
- AWS Cognito SDK
- AWS S3 SDK
- AWS DynamoDB

## Setup
1. Configure AWS Cognito in your project.
2. Implement `CognitoUseCasesInterface` to handle authentication logic.
3. Implement `MenuMainUseCasesInterface` to handle menu management.
4. Implement `S3ImageObjectUseCases` to handle AWS S3 image storage.
5. Register these modules in your Ktor application.

## License
This project is licensed under the MIT License.

