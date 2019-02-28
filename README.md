# Rest API
----
REST Service for web app and android app


## Usage

* `POST` **Login**

Log into the webserver to get a unique link to play the game
```
/web/api/login
```
**Headers**

>Content-Type: `application/json'`

**Body**

>{"username" : "`username`", "password" : "`password`"} 

----

* `GET` **Get game JSON object**

Use the unique link to get the JSON file
```
/web/api/play/UUID
```
----
* `POST` **Guess letter**

Guess a single letter
```
/web/api/play/UUID
```
**Headers**

>Content-Type: `application/x-www-form-urlencoded`

**Body**

>letter : `letter`

----
* `POST` **Reset game**

Reset the ongoing game
```
/web/api/play/UUID
```
**Headers**

>Content-Type: `application/x-www-form-urlencoded`

**Body**

>reset : `true`

----

* `GET` **Get Highscore JSON object**

Get the top 10 players scores
```
/web/api/highscore
```
