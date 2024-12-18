# Implement URL Shortener like bit.ly

## Problem Statement
To build a URL shortener like bit.ly. The system should be able to take a long URL and convert it into a short URL. The system should also be able to take a short URL and redirect it to the original long URL.
The 2 functionalities:
* Shorten a long URL
* Get back the original long URL from the short URL

## Solution
* Shorten a long URL
  - This functionality will be used by the user to shorten a long URL
  - The request to shorten a url wil contain the following things:
    1. The long URL
    2. The user id of the user who is requesting to shorten the URL
  - The response to shorten a url will contain the following things:
    1. The short URL
    2. Timestamp when the short url will expire
  - The short URL should be unique
  - The short URL should be of length 6
  - The short URL should contain only alphanumeric characters
  - The short URL should be case-sensitive
  - We have 4 types of user plans and their limits are as follows:
    1. Free (shortened url expires in 1 day)
    2. Team (shortened url expires in 7 days)
    3. Business (shortened url expires in 30 days)
    4. Enterprise (shortened url expires in 365 days)
  - Depending on the user plan, the shortened url should expire after the specified number of days
* Get back the original long URL from the short URL
  - This functionality will be used by the user to get back the original long URL from the short URL
  - The request to get back the original long URL will contain the following things:
    1. The short URL
  - The response to get back the original long URL will contain the following things:
    1. The original long URL
  - The short URL should exist in the database, if not then return an exception
  - If the short URL has expired, then return an exception
  - If the short URL is valid, then we need to track how many times the short URL has been accessed in `url_access_log` table, by capturing the time of the access.
  - If a URL has been accessed 10 times throughout the day, there should be 10 entries in the `url_access_log` table for that URL for that day with the timestamp of the access.
Note - We will be using H2 database which is an in-memory SQL database. 

