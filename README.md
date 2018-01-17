Turnier WebApp 
=======================
Beispielprojekt nach Domain Driven Design mit Spring Data JPA,

                                                (C) Michal Kubacki, 2017-10-01, 2018-01-24

In diesem Projekt haben wir versucht alle Prinzipien der Domain Driven Design zu implementieren.  Anders als in v?llig ausgearbeiteten Beispielen aus dem Netz haben wir die vereinfacht implementiert.
Dieses Beispielprojekt haben wir als die Abschlusspr?sentation f?r die Veranstaltung Software Engineering II an der Beuth University of Applied Sciences Berlin entwickelt.

Dieses Projekt benutzt:

- JDK 8
- Maven 3
- Spring Boot
- Spring Data + JPA + Hibernate + Derby
- AspectJ Compile Time Weaving for main sources
- JUnit 4
- The Exception Handling and Reporting Framework MulTEx

Detailierte infos finden Sie im Datei 'pom.xml'.

Mit Hilfe vom Command `mvn clean test` in der Konsole werden alle notwendige Libraries geholt, das Projekt wird kompiliert, Ausnahmenachrichten werden gesammelt und Tests werden durchgef?hrt.

Danach k?nnen Sie das Maven Projekt in Ihren Java IDE importieren.
(Wir empfehlen Spring Tool Suite, denn AspectJ weaving ist notwendig f?r die Kompilierungsphase).

## Welche DDD Prinzipien sind implementiert?

- Modellierung der Domainschickt als einen Package, der nicht von anderen Paketen abh?ngig ist au?er standard Java SE packages wie `javax.persistence`. Das wurde nur f?r die JPA annotations benutzt.
- vermeidet [Anemic Domain Model](https://martinfowler.com/bliki/AnemicDomainModel.html) - alle relevante Gesch?ftslogikmethoden sind in der Klasse `TurnierService`.  
  Dies ben?tigt Eigenschaften der **Domain Object Dependency Injection** (DODI), was kann nur mit Hilfe von vollen AspectJ zum Kompilierzeit weaving implementiert werden. 
  Siehe [Â§11.8.1 Using AspectJ to dependency inject domain objects with Spring](http://docs.spring.io/spring/docs/4.3.x/spring-framework-reference/html/aop.html#aop-atconfigurable).
- Domainschicht referenziert notwendige Services nur als selbstgesteckte, minimale Interfaces (package `domain.imports`).
- implementiert notwendige Services in der Infrastrukturschicht(package `infrastructure`).
- verkn?pft notwendige Services und Ihre Implementierung mit Hilfe von Dependency Injection zusammen. 
- implementiert Interfaceschicht f?r den externen Zugriff zum App. Es wurde als REST service in package `rest_interface` implementiert. 

## Projektbeschreibung

- Turnier Webapplikation, womit man neue Turniere erstellen kann und automatisch, je nach Anzahl der Spieler, ein Turnierbaum (Bracket) erstellt wird.
- Die am Turnier teilnehmenden Spieler sollen online einsehen k?nnen, wo sie im Ranking stehen und beobachten k?nnen, gegen welche Spieler sie welche Resultate erzielt haben.
- Soll dazu dienen, die europ?ische ESportsCommunity zu f?rdern. Die (nord-) amerikanische und die asiatischen ESports-Szenen sind bereits sehr gro?

## Ausf?hren
- Du brauchst: Das Verteilte Versionsverwaltungssystem Git, Das Build-Werkzeug Maven, am besten Die IDE ?Spring Tool Suite“ (auf Eclipse aufbauend) und Google Postman zum Testen eines REST-Services.
- `mvn cleantest` in der Konsole ausf?hren: alle notwendige Libraries werden geholt, das Projekt wird kompiliert, Ausnahmenachrichten werden gesammelt und Tests werden durchgef?hrt.
- `mvn spring-boot:run` in der Konsole ausf?hren: Ausf?hrung der Produkt (einen REST-Webservice f?r das Turneir)
- Die drittletzte Meldung sollte lauten:
 `...TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)`
Die letzte Meldung sollte lauten:
 ...turnier_webapp.Application: Started Application in 10.177 seconds ...
Jetzt ist der Webservice empfangsbereit.
-Port Konflikt:  Ist der Port 8080 schon besetzt, dann Tomcat kann nicht starten. 
Abhilfe schafft die explizite Angabe eines eigenen
Ports oder von 0 f?r einen zuf?llig freien Port als Property-Definition auf der Kommandozeile, also z.B.
 `mvn spring-boot:run -Dport=0`
Dann lautet die drittletzte Meldung z.B.:
 `...TomcatEmbeddedServletContainer : Tomcat started on port(s): 50368 (http)`
Diese Port-Nummer m?ssen Sie sich merken.


## Andere Eigenschaften

- Das Klassendiagramm finden Sie in der Datei `src/doc/TurnierModel.pdf`. Editierbare Quelle hat die Erweiterung `.uxf` (gemacht mit Hilfe vom UMLet).
- Applicationschicht wurde aus Vereinfachungsgrunden nicht implementiert, aber die Interfaceschicht wurde transactional gemacht.
- Internationalisierbare, parameterisierbare Ausnahmenachrichten
- F?ngt jeder Ausnahmenachricht in die referenzierte Sprache direkt als JavaDoc Kommentar auf
- Tests werden in einem leeren in-memory Derby Datenbank durchgef?hrt.
- Test Coverage Report generiert bei [JaCoCo Maven plugin](http://www.eclemma.org/jacoco/trunk/doc/maven.html) in der Datei [target/site/jacoco-ut/index.html](file:target/site/jacoco-ut/index.html).

### Wo finde ich die Ausnahmenachrichten?
In der Datei `MessageText.properties`. Editierbare Originaldatei mit festen Nachrichten befinden sich im `src/main/resource`
Maven Phase 'compile' kopiert diese Datei ins `target/classes/`.
In der n?chster Phase `process-classes` Ausnahmenachrichten sind aus dem JavaDoc Kommentaren von allen Ausnahmen unter `src/main/java/` durch `ExceptionMessagesDoclet`, die f?r das `maven-javadoc-plugin` konfiguriert wurde, extrahiert.
Diese werden danach in der Nachrichentextdatei `target/classes/` hinzugef?gt.
Dieses Prozess ist aus dem m2e Lifecycle Mapping in der `pom.xml`-Datei ausgeschlossen.

### Wo finde ich alle Rest Endpoints?
In der Datei `src/doc/REST-API.md`. (auf Englisch). 

## Zukunftspl?ne

- Mehr detaillierte Ausnamhenachrichten
- Auth 2.0 Authentifizierung
- GUI

## Referenzen und Quellen
- [Herr Knabe Beispielprojekt] (https://github.com/ChristophKnabe/spring-ddd-bank/)
- [Detailiertes Beispieltext ?ber DDD](https://www.mirkosertic.de/blog/2013/04/domain-driven-design-example/)
- [DDD Beispielprojket](https://github.com/citerus/dddsample-core), from which are taken some inspirations
- [The Ports and Adapters Pattern](http://alistair.cockburn.us/Hexagonal+architecture)
- [Can DDD be Adequately Implemented Without DI and AOP?](https://www.infoq.com/news/2008/02/ddd-di-aop)
- [Spring Boot](https://spring.io/guides/gs/spring-boot/)
- [Spring Dependency Injection](http://projects.spring.io/spring-framework/)
- [Spring Data JPA](https://spring.io/guides/gs/accessing-data-jpa/)
- [Spring Data JPA Query Methods](http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods)
- This application implements my knowledge about layering and data access at a given point of time. Previous versions were in a 3-layer form in german under [Bank3Tier - Bankanwendung in 3 Schichten](http://public.beuth-hochschule.de/~knabe/java/bank3tier/).
