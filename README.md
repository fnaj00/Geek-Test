# GeekText – Book Browsing and Sorting Feature

Software Engineering 1 – Group 10

This service implements the book browsing functionality for the GeekText bookstore backend.

## Features

* Retrieve all books
* Sort books by rating
* Sort books by price
* Sort books by copies sold

## API Endpoint

GET /api/books

Optional query parameters:

* `?sort=rating`
* `?sort=price`
* `?sort=copiesSold`

Example:

GET /api/books?sort=rating
