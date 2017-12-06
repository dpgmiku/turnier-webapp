# Documentation of the REST Endpoints of the Turnier Webapplication

| Verb   | Resource URI with Request Params | Effect                                   |
| ------ | -------------------------------- | ---------------------------------------- |
|        |                                  | Operations for Role **nutzer**             |
| POST   | /nutzer/                         | Creates a new nutzer with given parameters in Body |
| DELETE | /nutzer/{nutzername}             | Deletes a nutzer with given nutzername as Parameter and verify the password given in Body |
| GET    | /bank/{nutzername}               | Returns the completed nutzer object with given nutzername as parameter. |
| PUT    | /bank/email/{nutzername}         | Updates the email of given nutzername if password given in body is verified.                     |
| PUT    | /bank/password/{nutzername}      | Uptades the password of given nuztername if password given in body is verified |
