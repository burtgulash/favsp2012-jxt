#!/usr/bin/env python
# -*- coding:UTF-8

cas = 7 * 60 + 30
hodina = 45
prestavka = 10

print '<?xml version="1.0" encoding="UTF-8"?>'
print "<data>"

print "\t<dny>"
for i, den in enumerate(["Po", "Út", "St", "Čt", "Pá", "So", "Ne"]):
	print '\t\t<den cislo="%d">%s</den>' % (i + 1, den)
print "\t</dny>"

print "\t<casy>"
for i in range(14):
	print '\t\t<hodina cislo="%d">' % (i + 1)
	print '\t\t\t<zacatek>%d:%d:00</zacatek>' % (cas // 60, cas % 60)
	cas += hodina
	print '\t\t\t<konec>%d:%d:00</konec>' % (cas // 60, cas % 60)
	cas += prestavka
	print '\t\t</hodina>'
print "\t</casy>"

print "</data>"
