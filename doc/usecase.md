# Projektvision: Plattform zur Ablage von Todo-Listen

## Idee:
Die Web Anwendung Todolist erlaubt es Benutzern, Aufgaben hinzuzufügen bzw. zu löschen. Der Benutzer kann also seinen Tag planen und die Priorität zu jeder Aufgaben setzen. Nach 24h werden die nicht gemachten Aufgaben in den Überfälligen Bereich verschoben. Die Aufgaben können bearbeitet werden.
Außerdem erlaubt die Web-Anwendung dem Anwender, kurze Notizen, Links auf Webseiten oder Bilder in einer Datenbank zu speichern.
Zu allen Einträgen können Schlagwörter vergeben werden und es kann nach Schlagwörtern gesucht oder sortiert werden.
Weiterhin können einzelne Notizen durch Generierung eindeutiger Links freigegeben werden.

## Die folgenden Use-Cases sind erforderlich:
+ UC-0: Homepage (grobe Präsentation Webapp)
+ UC-1: Neuanlage eines Benutzerkontos :godmode:
+ UC-2: Einloggen und Ausloggen :godmode:
+ UC-3: Ablage eines neuen Tasks mit Priorität und Fälligkeitsdatum, erledigte Tasks können optional automatisch gelöscht werden
+ UC-4: Ablage einer neuen Notiz / Links oder Bildes. Zu jeder Notiz können Schlagworte gespeichert werden 
+ UC-5: Notizen und Aufgaben können gelöscht oder bearbeitet werden :godmode:
+ UC-6: Übersichtsdarstellung aller Notizen und todo-items (hohe Priorität und oder überfällig) in einem Dashboard
+ UC-7: Filtern der angezeigten Notizen/Todo's nach Schlagwort, Art und Zeitraum
+ UC-8: Die Notizen können vordefinierten Kategorien zugeordnet werden. Die Kategorien stellen eine weitere Auswahlmöglichkeit bei UC-5 dar.
+ UC-9: Generierung eines Freigabelinks (als nicht erratbare GUID) und Zugriff auf das Objekt über Aufruf des Links
+ UC-10: Notizen/Task können im Markdown format verfasst werden (optional) :godmode:
+ UC-11: Möglichkeit Timetracker zu den jeweiligen Aufgaben (optional)
+ UC-12: Falls kein Beschriftung von Notizen vorgegeben wurden, automatisch die erste Zeile als Überschrift übernehmen. (Optional) :godmode: (irgendwie)
+ UC-13: Möglichkeit Sprache zu ändern (Optional)
+ UC-14: Pomodorotimer (Optional)
