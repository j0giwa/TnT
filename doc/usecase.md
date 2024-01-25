# Projektvision: Plattform zur Ablage von kurzen Notizen und Weblinks

## Idee: 
Die Web-Anwendung erlaubt es dem Anwender, kurze Notizen, Links auf Webseiten oder Bilder in einer Datenbank zu speichern.
Zu allen Einträgen können Schlagwörter vergeben werden und es kann nach Schlagwörtern gesucht oder sortiert werden.
Außerdem können einzelne Einträge durch Generierung eindeutiger Links freigegeben werden.

## Die folgenden Use-Cases sind erforderlich:
+ UC-1: Neuanlage eines Benutzerkontos
+ UC-2: Einloggen und Ausloggen
+ UC-3: Ablage einer neuen Notiz / Links oder Bildes. Zu jeder Notiz können Schlagworte und Notizen gespeichert werden.
+ UC-4: Übersichtsdarstellung aller Notizen in einem Dashboard
+ UC-5: Filtern der angezeigten Notizen nach Schlagwort, Art und Zeitraum
+ UC-6: Die Notizen können vordefinierten Kategorien zugeordnet werden. Die Kategorien stellen eine weitere Auswahlmöglichkeit bei UC-5 dar.
+ UC-7: Generierung eines Freigabelinks (als nicht erratbare GUID) und Zugriff auf das Objekt über Aufruf des Links

# Vergleichbare Lösung: 
+ [PasteBin](https://pastebin.com)PasteBin

# Mindestumfang: 
Falls Sie das Projekt allein realisieren, sind die Anwendungsfälle UC-1, UC-2, UC-3 und UC-4 erforderlich.
Bei Bearbeitung als Zweier-Team sollte der komplette Funktionsumfang von UC-1 bis UC-7 (prototypisch) realisiert sein.
Es ist ausreichend, wenn die Anwendungsfälle grundsätzlich lauffähig sind; es muss keine Fehlerbehandlung aller möglichen Situationen erfolgen (Sie dürfen also davon ausgehen, dass der Benutzer wohlwollend ist und sinnvolle und vollständige Eingaben vornimmt).