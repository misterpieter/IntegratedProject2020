Integrated Project 2020


TO DO:

Paswoord weg bij student: PIETER
Signature risico JONAS
PAD LAYOUT aanpassen: PIETER
Bij importeren csv: automatisch lokaal opslaan: HALIMA en PIETER
Exporteren van de lijst in xls + csv: PIETER en HALIMA

BUG : 
detail Listview student heeft duplicaten
Obama crash oplossen: JONAS
Crash lat en Lon bij afwezigheid van wifi/mobile oplossen: JONAS







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
