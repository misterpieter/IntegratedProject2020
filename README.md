Integrated Project 2020


TO DO:
- CSV/XLS/JSON importeren / exporteren
- Lokaal inloggen
- Firebase synchronisatie (JONAS)
- lijst weergeven studenten: naam - snummer
- detail lijst van studenten: handtekening - locatie - datum - (suspecion level)
- data lokaal wegschrijven (HALIMA)
- opzoeken mogelijkheid tot machine learning voor handtekeningen

DATA:
Student:  (id:Long) - studentnr:String - name:String - list<Autograph>
Autograph: (id:Long) - image:BLOB (of BASE64 string?) - date: Date - suspicionLevel:Enm
suspicionLevel: notRelevant - low  - high







SAMENVATTING
voormalig “I was there” (app om een student in te loggen met handtekening)

2 DELEN

USER gedeelte
inbreng handtekening (enkel als de student in de lijst staat, opgesteld door de admin)
GEEN feedback van suspicion level; enkel handtekening zetten -> popup dat het is gelukt; de rest gebeurd achter de schermen


ADMIN gedeelte
- Mogelijkheid tot CRUD van studenten. lijst van student met relevante gegevens
	Enkel wanneer de student in de lijst staat, heeft deze de mogelijkheid om in te loggen met zijn handtekening
	Belangrijk dat de admin deze lijst makkelijk kan exporteren (via CSV of iets anders: XLXS/JSON?) 
- overzicht handtekeningen met naam, studentennr, datum en locatie (reverse address lookup)
- voorzien van zoek/filter mogelijkheden
- beschermd door pwd 
- Data wordt lokaal weggeschreven maar kan gesynchroniseerd worden in firebase via synchronisatie knop
- bonuspunten voor handtekening verificatie, liefst lokaal en onmiddellijk
	Dit gebeurd dan via een “suspicion level” (omdat het moeilijk te achterhalen is of een digitale handtekening EFFECTIEF niet door de juiste persoon is gezet)
