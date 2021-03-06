# Documentation of the REST Endpoints of the Turnier Webapp

| Verb   | Resource URI with Request Params | Effect                                   |
| ------ | -------------------------------- | ---------------------------------------- |
|        |                                  | Operations for Role **nutzer**             |
| POST   | /nutzer/                         | Create a nutzer from the passed nutzer resource |
| POST   | /nutzer/{nutzername}/turnier/    | Create a turnier from the passed turnier resource with a nutzer with given nutzername as organisator. |
| PUT    | /nutzer/turnier/{turniername}/{nutzername} | Add teilnehmer with given nutzername to turnier with given turniername |
| PUT    | /nutzer/turnier/start/{turniername}/{nutzername}/ | Start turnier with given turniername only if nutzername is the organisator of this turnier                     |
| PUT    | /nutzer/turnier/start/{turniername}/{position}/{erg1}/{erg2} | set the result for given turnierbracket |
| DELETE | /nutzer/turnier/{turniername}/{nutzername}/delete | Delete teilnehmer with given nutzername from turnier with given turniername |
| DELETE | /nutzer/{nutzername}             | Delete nutzer with given nutzername, password verify by Delete Nutzer Command resource |
| GET    | /nutzer/{nutzername}             | Returns Nutzer Object as JSON with given nutzername |
| GET    | /nutzer/                         | Returns all Nutzer Objects as JSON |
| GET    | /nutzer/turnier/                 | Returns all Turnier Objects as JSON |
| GET    | /nutzer/{nutzername}/turnier/    | Returns all Turnier Objects with nutzer with given nutzername as organisator |
| GET    | /nutzer/turnier/{turniername}    | Returns Turnier Object as JSON with given turniername |
| GET    | /nutzer/turnier/{turniername}/ergebnisse | Returns String with ranked players, the first place is on the top  |
| DELETE | /nutzer/{nutzername}/turnier/{turniername} | Delete turnier with given turniername only if a nutzer with given nutzername is the organisator of this turnier |
| PUT    | /nutzer/email/{nutzername}       | Change email of nutzer with given nutzername, password verify by Email Change Command |
| PUT    | /nutzer/password/{nutzername}    | Change password of nutzer with given nutzername, password verify by Password Change Command |
|        |                                  | Operations for Role **admin**             |
| POST   | /admin/                          | Create an admin from the passed admin resource |
| PUT    | /admin/{adminname}/nutzer/{nutzername} | Change properties of nutzer with given nutzername only if admin with given adminname exists by passing nutzer resource.  |
| PUT    | /admin/{adminname}/turnier/{turniername}  | Change properties of turnier with given turniername only if admin with given adminname exists by passing turnier resource.  |
|


