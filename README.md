# URL Shortener

Shorten your URLs

## Getting Started

To run this project locally, ensure you have Docker installed on your machine.

### Running the whole Project

1. Clone this repository to your local machine.
2. Navigate to the project's root directory in a terminal.
3. Run the following command:

```bash
docker-compose up
```
4. When containers are up, go to `localhost:4200` in your browser to access the interface

### Running the project without Docker

Ensure you have a MongoDB running.

1. Clone this repository to your local machine.
2. [Run the backend](backend/README.md)
3. [Run the frontend](frontend/README.md)


### Stopping the Project

To stop the containers, use `docker-compose down` in the terminal. This stops and removes the containers, networks, and volumes created by `docker-compose up`.

## Tech Stack

### Backend

* Java 17
* Spring boot
* MongoDB
* Lombok
* Maven

### Frontend

* Angular 17
* TypeScript 5
* RxJS 7
* Tailwind CSS

### Deployment and run

* Docker


## Improvements
* **Security**: Use JWT for secure API calls.
* **Authenticated User**: For the user to retrieve saved links
* **Custom Alias**: Allow users to define custom short URLs for better personalization, instead of relying on automatically generated ones